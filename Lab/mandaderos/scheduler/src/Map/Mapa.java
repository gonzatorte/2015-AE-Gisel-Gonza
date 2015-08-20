package Map;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Mapa implements Serializable{
    public DistanceTable distances;
    public List<Place> places;
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
        DistanceTable nuevaDistancias = (DistanceTable) this.distances.clone();
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
    
    public void Serialize(){
    }
}
