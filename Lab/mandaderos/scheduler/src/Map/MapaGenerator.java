package Map;

import Map.Api.DistanceWebCrawler;
import Map.Api.PlacesWebCrawler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import my_utils.DualMap;
import org.xml.sax.SAXException;

public final class MapaGenerator {
    static long seed;
    static String output_filename = null;
    public static final long earth_radious = 6371009; // En KM
    
    public static void merge(TreeMap<String, Place> dest, TreeMap<String, Place> news){
        dest.putAll(news);
    }

    public static TreeMap<String, Place> toMap(List<Place> lista){
        TreeMap<String, Place> tree = new TreeMap<String, Place>();
        for (Place e: lista){
            tree.put(e.place_id, e);
        }
        return tree;
    }

    public static List<Place> toList(TreeMap<String, Place> tree){
        return new LinkedList<Place>(tree.values());
    }
    
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
//        Mapa mapa = new Mapa(new Coordinate(-34.543393, -56.083478), new Coordinate(-34.583742, -56.090683));
        Mapa mapa = new Mapa(new Coordinate(51.503186, -0.126446), new Coordinate(51.523186, -0.146446));
        PlacesWebCrawler pcrawler = new PlacesWebCrawler();
        
        TreeMap<String, Place> all_places_tree = new TreeMap<String, Place>();
        
        Coordinate aux_coord = new Coordinate(
                Math.min(mapa.diag1.latit, mapa.diag2.latit), 
                Math.min(mapa.diag1.longit, mapa.diag2.longit));
        Coordinate max_coord = new Coordinate(
                Math.max(mapa.diag1.latit, mapa.diag2.latit), 
                Math.max(mapa.diag1.longit, mapa.diag2.longit));
        double delta_longitud = 0.018;// 1 grado es 111,11 km. 0.018 son 2 KM
        double delta_latitud = 0.018;
        double overlaping = 0.5;
        //Radious en metros
        int some_radious = 2000;
        while (aux_coord.longit < max_coord.longit){
            while (aux_coord.latit < max_coord.latit){
                pcrawler.location_x = aux_coord.latit;
                pcrawler.location_y = aux_coord.longit;
                pcrawler.radius = some_radious;
                List<Place> places = pcrawler.process_response();
                TreeMap<String, Place> places_tree = toMap(places);
                merge(all_places_tree, places_tree);
                aux_coord.latit = aux_coord.latit + delta_latitud;
            }
            aux_coord.longit = aux_coord.longit + delta_longitud;
        }
        
        List<Place> all_places = toList(all_places_tree);
        
        DistanceWebCrawler dcrawler = new DistanceWebCrawler();
        DualMap<Place, Place, Double> all_distances = new DualMap<Place, Place, Double>();

        all_places = all_places.subList(0, 3);

        int places_size = all_places.size();
        assert(places_size < 100);
        for (int i = 0 ; i < places_size ; i++){
            List<Place> subset = all_places.subList(i+1, all_places.size());
            dcrawler.destinos = subset;
            Place origen = all_places.get(i);
            dcrawler.origenes = new ArrayList<Place>();
            dcrawler.origenes.add(origen);
            DualMap<Place, Place, Double> distances = dcrawler.process_response();
            all_distances.put(origen, distances.get(origen));
        }
        mapa.distances = all_distances;
        mapa.places = all_places;
    }
    /*
    wget "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Vancouver+BC&mode=bicycling&language=fr-FR&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs" -O gapimatrix.json
    
    wget "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=museum&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs&mode=bicycling&language=es" -O gapiplaces.json
    wget "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=museum&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs" -O gapiplaces.json
    */
}
