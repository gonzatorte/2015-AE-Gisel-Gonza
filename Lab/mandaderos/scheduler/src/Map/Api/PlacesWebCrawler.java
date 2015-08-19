package Map.Api;

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
import java.util.Locale;

public class PlacesWebCrawler extends WebCrawler {
    
    public PlacesWebCrawler(){}

    public double location_x;
    public double location_y;
    public int radius;

    public URL create_call() throws MalformedURLException {
        String base_url = "maps.googleapis.com/maps/api/place/radarsearch/xml";
        String coordinate_format = "%f,%f";
        String location = String.format(Locale.ENGLISH, coordinate_format, location_x, location_y);
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

    public List<Place> process_response() throws SAXException, IOException, ParserConfigurationException {
        BufferedInputStream web_stream = this.call();
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
