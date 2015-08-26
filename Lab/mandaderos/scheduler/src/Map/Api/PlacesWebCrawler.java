package Map.Api;

import Map.Coordinate;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import Map.Place;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.TreeMap;
import my_utils.MapUtils;

public class PlacesWebCrawler {
    
    private SQLiteStatement consulta_get_lugar;
    private SQLiteStatement consulta_get_borders;
    private SQLiteStatement consulta_insert_lugar;
    private SQLiteConnection db_con;
    
    public PlacesWebCrawler() throws SQLiteException{
        SQLiteConnection sqLiteConnection = new SQLiteConnection(new File("./instances/places.db"));
        sqLiteConnection.open(false);
        this.db_con = sqLiteConnection;
        this.db_con.exec(
"CREATE TABLE IF NOT EXISTS \"Places\" (\n" +
"    \"place_id\" TEXT NOT NULL,\n" +
"    \"latitud\" REAL NOT NULL,\n" +
"    \"longitud\" REAL NOT NULL,\n" +
"    PRIMARY KEY (\"place_id\")\n" +
");"
        );
        this.consulta_get_borders = sqLiteConnection.prepare(
"SELECT min(\"latitud\"), max(\"latitud\"), min(\"longitud\"), max(\"longitud\") "+
"FROM \"Places\" WHERE ? > \"longitud\" AND \"longitud\" > ? AND "+
"? > \"latitud\" AND \"latitud\" > ?"
);
        this.consulta_get_lugar = sqLiteConnection.prepare(
"SELECT \"place_id\", \"latitud\", \"longitud\" "+
"FROM \"Places\" WHERE ? > \"longitud\" AND \"longitud\" > ? AND "+
"? > \"latitud\" AND \"latitud\" > ?"
);
        this.consulta_insert_lugar = sqLiteConnection.prepare(
"INSERT INTO \"Places\" (\"place_id\", \"latitud\", \"longitud\") VALUES (?,?,?)"
);
    }

    public List<Place> crawl(double h_latit, double l_latit, double h_longit, double l_longit) 
            throws MalformedURLException, UnsupportedEncodingException, IOException, ParserConfigurationException, SAXException, SQLiteException {
        this.consulta_get_lugar.bind(1, h_longit);
        this.consulta_get_lugar.bind(2, l_longit);
        this.consulta_get_lugar.bind(3, h_latit);
        this.consulta_get_lugar.bind(4, l_latit);
        this.consulta_get_lugar.step();
        while(this.consulta_get_lugar.hasRow()){
            String place_id = this.consulta_get_lugar.columnString(0);
            double latit = this.consulta_get_lugar.columnDouble(1);
            double longit = this.consulta_get_lugar.columnDouble(2);
        }
        TreeMap<String, Place> all_places_tree = new TreeMap<String, Place>();
        
        Coordinate aux_coord = new Coordinate(l_latit, l_longit);
        Coordinate max_coord = new Coordinate(h_latit, h_longit);
        double delta_longitud = 0.018;// 1 grado es 111,11 km. 0.018 son 2 KM
        double delta_latitud = 0.018;
        double overlaping = 0.5;
        //Radious en metros
        int some_radious = 2000;
        boolean stop_iter = false;
        while ((aux_coord.longit < max_coord.longit) && (!stop_iter)){
            while ((aux_coord.latit < max_coord.latit) && (!stop_iter)){
                List<Place> places = this.single_crawl(aux_coord, some_radious);
                TreeMap<String, Place> places_tree = MapUtils.toMap(places);
                MapUtils.merge(all_places_tree, places_tree);
                aux_coord.latit = aux_coord.latit + delta_latitud;
                if (all_places_tree.size() > 10){
                    stop_iter = true;
                }
            }
            aux_coord.longit = aux_coord.longit + delta_longitud;
        }
        List<Place> all_places = MapUtils.toList(all_places_tree);
        
        for (Place p : all_places){
            this.consulta_insert_lugar.bind(1, p.place_id);
            this.consulta_insert_lugar.bind(2, p.coord.latit);
            this.consulta_insert_lugar.bind(3, p.coord.longit);
            this.consulta_insert_lugar.step();
        }
        return all_places;
    }

    public List<Place> single_crawl(Coordinate origin, int radius) throws MalformedURLException, IOException, SAXException, ParserConfigurationException {
        URL url_api = this.create_call(origin, radius);
        BufferedInputStream b_web_stream = this.call(url_api);
        List<Place> places = this.process_response(b_web_stream);
        return places;
    }
    
    private BufferedInputStream call(URL url_api) throws MalformedURLException, UnsupportedEncodingException, IOException{
        InputStream web_stream = url_api.openStream();
        BufferedInputStream buff_web_stream = new BufferedInputStream(web_stream);
        return buff_web_stream;
    }
    
    private URL create_call(Coordinate origin, int radius) throws MalformedURLException {
        String base_url = "maps.googleapis.com/maps/api/place/radarsearch/xml";
        String coordinate_format = "%f,%f";
        String location = String.format(Locale.ENGLISH, coordinate_format, origin.latit, origin.longit);
        location = "location=" + location;
        String radius_format = "radius=%d";
        String radius_str = String.format(Locale.ENGLISH, radius_format, radius);
        String filter = "keyword=pizza";
//        String filter = "types=" + String.join("|", g_types);
        String options = "mode=bicycling&language=es";
        String apikey = "key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs";
        String api_format = "https://%s?%s&%s&%s&%s&%s";
        String strurl = String.format(api_format, base_url, location, radius_str, filter, apikey, options);
        URL url_api = new URL(strurl);
        return url_api;
    }

    private List<Place> process_response(BufferedInputStream web_stream) throws SAXException, IOException, ParserConfigurationException {
        InputSource web_source = new InputSource(web_stream);
        DocumentBuilderFactory dom_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dom_factory.newDocumentBuilder();
        Document document = builder.parse(web_source);
        NodeList nodeList = document.getElementsByTagName("result");
        List<Place> places;
        places = new ArrayList<Place>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            String latit_str = ((Element) node).getElementsByTagName("lat")
                        .item(0).getChildNodes().item(0).getNodeValue();
            
            String longit_str = ((Element) node).getElementsByTagName("lng")
                        .item(0).getChildNodes().item(0).getNodeValue();

            String place_id = ((Element) node).getElementsByTagName("place_id")
                        .item(0).getChildNodes().item(0).getNodeValue();

            double latit = Double.parseDouble(latit_str);
            double longit = Double.parseDouble(longit_str);

            places.add(new Place(place_id, latit, longit));
        }
        return places;
    }

    public static void main () throws SAXException, IOException, ParserConfigurationException, SQLiteException{
        PlacesWebCrawler pcrawler = new PlacesWebCrawler();
        
        List<Place> places_1 = pcrawler.single_crawl(new Coordinate(51.503186, -0.126446), 2000);
        TreeMap<String, Place> places_tree_1 = MapUtils.toMap(places_1);

        List<Place> places_2 = pcrawler.single_crawl(new Coordinate(51.503186, -0.126446), 2000);
        TreeMap<String, Place> places_tree_2 = MapUtils.toMap(places_2);
        
        MapUtils.merge(places_tree_1, places_tree_2);
        
        assert(places_tree_1 == places_tree_2);
    }
    
    public static final String [] more_g_types = {
        "administrative_area_level_1",
        "administrative_area_level_2",
        "administrative_area_level_3",
        "administrative_area_level_4",
        "administrative_area_level_5",
        "colloquial_area",
        "country",
        "floor",
        "geocode",
        "intersection",
        "locality",
        "natural_feature",
        "neighborhood",
        "political",
        "point_of_interest",
        "post_box",
        "postal_code",
        "postal_code_prefix",
        "postal_code_suffix",
        "postal_town",
        "premise",
        "room",
        "route",
        "street_address",
        "street_number",
        "sublocality",
        "sublocality_level_4",
        "sublocality_level_5",
        "sublocality_level_3",
        "sublocality_level_2",
        "sublocality_level_1",
        "subpremise",
        "transit_station"
    };
    
    public static final String [] g_types = {
        "accounting",
        "airport",
        "amusement_park",
        "aquarium",
        "art_gallery",
        "atm",
        "bakery",
        "bank",
        "bar",
        "beauty_salon",
        "bicycle_store",
        "book_store",
        "bowling_alley",
        "bus_station",
        "cafe",
        "campground",
        "car_dealer",
        "car_rental",
        "car_repair",
        "car_wash",
        "casino",
        "cemetery",
        "church",
        "city_hall",
        "clothing_store",
        "convenience_store",
        "courthouse",
        "dentist",
        "department_store",
        "doctor",
        "electrician",
        "electronics_store",
        "embassy",
        "establishment",
        "finance",
        "fire_station",
        "florist",
        "food",
        "funeral_home",
        "furniture_store",
        "gas_station",
        "general_contractor",
        "grocery_or_supermarket",
        "gym",
        "hair_care",
        "hardware_store",
        "health",
        "hindu_temple",
        "home_goods_store",
        "hospital",
        "insurance_agency",
        "jewelry_store",
        "laundry",
        "lawyer",
        "library",
        "liquor_store",
        "local_government_office",
        "locksmith",
        "lodging",
        "meal_delivery",
        "meal_takeaway",
        "mosque",
        "movie_rental",
        "movie_theater",
        "moving_company",
        "museum",
        "night_club",
        "painter",
        "park",
        "parking",
        "pet_store",
        "pharmacy",
        "physiotherapist",
        "place_of_worship",
        "plumber",
        "police",
        "post_office",
        "real_estate_agency",
        "restaurant",
        "roofing_contractor",
        "rv_park",
        "school",
        "shoe_store",
        "shopping_mall",
        "spa",
        "stadium",
        "storage",
        "store",
        "subway_station",
        "synagogue",
        "taxi_stand",
        "train_station",
        "travel_agency",
        "university",
        "veterinary_care",
        "zoo"
    };
}
