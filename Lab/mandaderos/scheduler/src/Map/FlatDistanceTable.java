package Map;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FlatDistanceTable extends HashMap<Place,HashMap<Place,Double>> implements DistanceTable {

    public void removePlace(Place p) {
        this.remove(p);
        Collection<HashMap<Place, Double>> values = this.values();
        for (HashMap<Place, Double> ps : values){
            ps.remove(p);
        }
    }

    public void compact(List<Place> places) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addPlace(Place p1, HashMap<Place, Double> distances) {
        this.put(p1, distances);
        this.get(p1).put(p1, 0.0);
        Set<Entry<Place, Double>> p_ds = distances.entrySet();
        for (Entry<Place, Double> pd : p_ds){
            this.get(pd.getKey()).put(p1, pd.getValue());
        }
    }

    /*
    Si no lo encuentra aqui, puede ser que vaya a buscarlo a la web
    O a la BD de crawling (un SQLlite)
    */
    public Double getDistance(Place p1, Place p2) {
        return this.get(p1).get(p2);
    }

    public Place getNearest(Place p1, Set<Place> included_places, Set<Place> excluded_places) {
        //ToDo: Si usara un diccionario TreeMap en vez de hashMap tendria
        // el minimo en tiempo 1
        Set<Entry<Place, Double>> pds = this.get(p1).entrySet();
        Double min_distance = null;
        Place nearest = null;
        for (Entry<Place, Double> pd : pds){
            Place it_place = pd.getKey();
            if ((!included_places.contains(it_place)) || (excluded_places.contains(it_place))){
                continue;
            }
            if ((min_distance == null) || (pd.getValue() < min_distance)){
                min_distance = pd.getValue();
                nearest = it_place;
            }
        }
        return nearest;
    }
}
