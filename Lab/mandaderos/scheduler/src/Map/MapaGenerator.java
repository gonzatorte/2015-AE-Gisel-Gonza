package Map;

import Map.Api.DistanceWebCrawler;
import Map.Api.PlacesWebCrawler;
import SerializableTest.main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import my_utils.MapUtils;
import org.xml.sax.SAXException;

public final class MapaGenerator {
    static long seed;
    static String output_filename = null;
    public static final long earth_radious = 6371009; // En KM
    
    public static void main(String[] args) {
        try {
            generate_map();
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Mapa test_data(){
       Mapa mapa = new Mapa(new Coordinate(-29.0, 9.0), new Coordinate(-39.0, 19.0));
       List<Place> places = new LinkedList<Place>();
       LightDistanceTable dd = new LightDistanceTable();
       
       Place p1 = new Place("p_1", -30.0, 10.0);
       places.add(p1);
       HashMap<Place, Double> dp1 = new HashMap<Place, Double>();
       dd.addPlace(p1, dp1);
       
       Place p2 = new Place("p_2", -30.1, 10.1);
       places.add(p2);
       HashMap<Place, Double> dp2 = new HashMap<Place, Double>();
       dp2.put(p1, 20.0);
       dd.addPlace(p2, dp2);
       
       Place p3 = new Place("p_3", -30.2, 10.2);
       places.add(p3);
       HashMap<Place, Double> dp3 = new HashMap<Place, Double>();
       dp3.put(p1, 30.0);
       dp3.put(p2, 40.0);
       dd.addPlace(p3, dp3);
       
       mapa.places = places;
       mapa.distances = dd;
       return mapa;
    }
    
    public static void generate_map() throws IOException, SAXException, ParserConfigurationException {
//        Mapa mapa = new Mapa(new Coordinate(-34.543393, -56.083478), new Coordinate(-34.583742, -56.090683));
        Mapa mapa = new Mapa(new Coordinate(51.503186, -0.126446), new Coordinate(51.523186, -0.146446));
        PlacesWebCrawler pcrawler = new PlacesWebCrawler();
        
        TreeMap<String, Place> all_places_tree = new TreeMap<String, Place>();
        
        Coordinate aux_coord = new Coordinate(mapa.l_latit, mapa.l_longit);
        Coordinate max_coord = new Coordinate(mapa.h_latit, mapa.h_longit);
        double delta_longitud = 0.018;// 1 grado es 111,11 km. 0.018 son 2 KM
        double delta_latitud = 0.018;
        double overlaping = 0.5;
        //Radious en metros
        int some_radious = 2000;
        boolean stop_iter = false;
        while ((aux_coord.longit < max_coord.longit) && (!stop_iter)){
            while ((aux_coord.latit < max_coord.latit) && (!stop_iter)){
                pcrawler.latit = aux_coord.latit;
                pcrawler.longit = aux_coord.longit;
                pcrawler.radius = some_radious;
                List<Place> places = pcrawler.process_response();
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
        
        DistanceWebCrawler dcrawler = new DistanceWebCrawler();
        LightDistanceTable all_distances = new LightDistanceTable();

        all_places = new LinkedList<Place>(all_places.subList(0, 40));

        int places_size = all_places.size();
        //ToDo: El assert no anda a menos que los encendamos...
        assert(places_size < 100);
        for (int i = 0 ; i < places_size ; i++){
            List<Place> subset = all_places.subList(i+1, all_places.size());
            dcrawler.destinos = subset;
            Place origen = all_places.get(i);
            dcrawler.origenes = new ArrayList<Place>();
            dcrawler.origenes.add(origen);
            LightDistanceTable distances = dcrawler.process_response();
            all_distances.addPlace(origen, distances.get(origen));
        }
        mapa.distances = all_distances;
        mapa.places = all_places;
        String file_name = "ejemplo";
        File f = new File("mapas/" + 
//                "mapa_" + mapa.h_latit + "_" + mapa.h_longit + "_" + mapa.l_latit + "_" + mapa.l_longit + ".jbin");
                file_name + ".jbin");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(mapa);
        oos.close();
    }
    /*
    wget "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Vancouver+BC&mode=bicycling&language=fr-FR&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs" -O gapimatrix.json
    wget "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=museum&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs&mode=bicycling&language=es" -O gapiplaces.json
    wget "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=museum&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs" -O gapiplaces.json
    */
}
