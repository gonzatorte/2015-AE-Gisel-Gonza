package Map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface DistanceTable extends Serializable{
    public void removePlace(Place p);
    
    /*
    Esto se dedica a solo dejar las distancias que interesan, hay que correrlo
    Antes de ejecutar el AE, pero despues de aplicar los eventos
    */
    public void compact(List<Place> places);
    
    public void addPlace(Place p1, HashMap<Place, Double> distances);
    
    /*
    Si no lo encuentra aqui, puede ser que vaya a buscarlo a la web
    O a la BD de crawling (un SQLlite)
    */
    public Double getDistance(Place p1, Place p2);
    
    /*
    Ojo que este metodo no esta dando como nearest al mismo punto p1
    */
    public Place getNearest(Place p1, Set<Place> included_places, Set<Place> excluded_places);
}
