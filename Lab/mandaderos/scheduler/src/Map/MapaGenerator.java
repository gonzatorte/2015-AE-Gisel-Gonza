package Map;

import java.util.Map;
import Map.Api.DistanceWebCrawler;
import Map.Api.PlacesWebCrawler;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import my_utils.DualMap;
import org.xml.sax.SAXException;

public final class MapaGenerator {
    static long seed;
    static String output_filename = null;
    
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        PlacesWebCrawler pcrawler = new PlacesWebCrawler();
        pcrawler.location_x = 51.503186;
        pcrawler.location_y = -0.126446;
        pcrawler.radius = 5000;
        List<Place> places = pcrawler.process_response();
        
        DistanceWebCrawler dcrawler = new DistanceWebCrawler(places);
        DualMap<Place, Place, Double> distances = dcrawler.process_response();
    }
    /*
    wget "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Vancouver+BC&mode=bicycling&language=fr-FR&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs" -O gapimatrix.json
    
    wget "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=museum&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs&mode=bicycling&language=es" -O gapiplaces.json
    wget "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=museum&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs" -O gapiplaces.json
    */
}
