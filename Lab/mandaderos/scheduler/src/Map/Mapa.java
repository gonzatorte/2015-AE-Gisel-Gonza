package Map;

import Map.Api.DistanceWebCrawler;
import com.almworks.sqlite4java.SQLiteException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Mapa implements Serializable{
    public LightDistanceTable distances = new LightDistanceTable();
    public LinkedList<Place> places = new LinkedList<Place>();
    double h_latit;
    double l_latit;
    double h_longit;
    double l_longit;
    
    public Mapa(Coordinate diag1, Coordinate diag2){
        l_latit = Math.min(diag1.latit, diag2.latit);
        l_longit = Math.min(diag1.longit, diag2.longit);
        h_latit = Math.max(diag1.latit, diag2.latit);
        h_longit = Math.max(diag1.longit, diag2.longit);
    }
    
    public Mapa subMapa(Coordinate diag1, Coordinate diag2){
        Mapa nuevoMapa = new Mapa(diag1, diag2);
        LinkedList<Place> nuevoPlaces = new LinkedList<Place>();
        LightDistanceTable nuevaDistancias = (LightDistanceTable) this.distances.clone();
        for (Place place : places){
            if ((l_latit < place.coord.latit) && (place.coord.latit < h_latit) &&
                (l_longit < place.coord.longit) && (place.coord.longit < h_longit)){
                nuevoPlaces.add(place);
            } else {
                nuevaDistancias.removePlace(place);
            }
        }
        nuevoMapa.places = nuevoPlaces;
        nuevoMapa.distances = nuevaDistancias;
        return nuevoMapa;
    }
    
    public void addPlace(Place place){
        DistanceWebCrawler dcrawler;
        HashMap<Place, Double> new_distances;
        try {
            dcrawler = new DistanceWebCrawler();
            new_distances = dcrawler.crawl(place, places);
        } catch (IOException ex) {
            Logger.getLogger(Mapa.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("Error al agregar place");
        } catch (SAXException ex) {
            Logger.getLogger(Mapa.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("Error al agregar place");
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Mapa.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("Error al agregar place");
        } catch (SQLiteException ex) {
            Logger.getLogger(Mapa.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("Error al agregar place");
        }
        distances.addPlace(place, new_distances);
        //ToDo: Add si no existe ya
        places.add(place);
    }
    
    public Place findPlaceById(String place_id){
        Place finded = null;
        for (Place p : this.places){
            if (p.place_id.equals(place_id)){
                finded = p;
                break;
            }
        }
        return finded;
    }
    
    public List<Place> findPlaceByCoords(Coordinate diag1, Coordinate diag2){
        double l_latit_find = Math.min(diag1.latit, diag2.latit);
        double l_longit_find = Math.min(diag1.longit, diag2.longit);
        double h_latit_find = Math.max(diag1.latit, diag2.latit);
        double h_longit_find = Math.max(diag1.longit, diag2.longit);
        LinkedList<Place> result = new LinkedList<Place>();
        for (Place p : this.places){
            if ((l_latit_find < p.coord.latit) && (p.coord.latit < h_latit_find) && 
                    (l_longit_find < p.coord.longit) && (p.coord.longit < h_longit_find)){
                result.add(p);
            }
        }
        return result;
    }
}
