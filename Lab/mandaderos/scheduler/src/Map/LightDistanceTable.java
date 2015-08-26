package Map;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LightDistanceTable extends HashMap<Place,HashMap<Place,Double>> implements DistanceTable {
    public void removePlace(Place p){
        //Remover la row y de todas las demas filas que vienen despues y
        //tienen el place
        this.remove(p);
        for (HashMap<Place,Double> distances : this.values()){
            distances.remove(p);
        }
    }
    
    /*
    Esto se dedica a solo dejar las distancias que interesan, hay que correrlo
    Antes de ejecutar el AE, pero despues de aplicar los eventos
    */
    public void compact(List<Place> places){
//        this.remove(p);
//        for (HashMap<Place,Double> distances : this.values()){
//            distances.remove(p);
//        }
    }
    
    public void addPlace(Place p1, HashMap<Place, Double> distances){
        Set<Entry<Place, Double>> d_ps = distances.entrySet();
        HashMap<Place, Double> new_row = new HashMap<Place, Double>();
        new_row.put(p1, 0.0);
        for (Entry<Place, Double> d_p : d_ps){
            Place p2 = d_p.getKey();
            int cmp = p1.compareTo(p2);
            if (cmp > 0){
                if (this.get(p2) == null){
                    this.put(p2, new HashMap<Place, Double>());
                }
                this.get(p2).put(p1, d_p.getValue());
            } else if (cmp < 0) {
                new_row.put(p2, d_p.getValue());
            } else {
                throw new Error("El lugar ya existe");
            }
        }
        this.put(p1, new_row);
    }
    
    public Double getDistance(Place p1, Place p2){
        int cmp = p1.compareTo(p2);
        if (cmp < 0){
            return this.get(p1).get(p2);
        } else if (cmp >= 0) {
            return this.get(p2).get(p1);
        } else {
            return 0.0;
        }
    }
    
    public Place getNearest(Place p1, Set<Place> included_places, Set<Place> excluded_places){
        Double min_distance = null;
        Place nearest = null;
        for (Place p : this.keySet()){
            if ((!included_places.contains(p)) || (excluded_places.contains(p))){
                continue;
            }
            Double dist = this.getDistance(p1, p);
            if ((min_distance == null) || (dist < min_distance)){
                min_distance = dist;
                nearest = p;
            }
        }
        return nearest;
    }
}
