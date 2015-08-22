package Map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DistanceTable extends HashMap<Place,HashMap<Place,Double>> implements Serializable {
    public void removePlace(Place p){
        this.remove(p);
        for (HashMap<Place,Double> distances : this.values()){
            distances.remove(p);
        }
    }
    
    public void addPlace(Place p, HashMap<Place, Double> distances){
        this.put(p, distances);
    }
    
    public Double getDistance(Place p1, Place p2){
        int cmp = p1.compareTo(p2);
        if (cmp > 0){
            return this.get(p1).get(p2);
        } else if (cmp < 0) {
            return this.get(p2).get(p1);
        } else {
            return 0.0;
        }
    }
    
    public Place getNearest(Place p1, Set<Place> excluded_places){
        HashMap<Place,Double> distancesOfThat = this.get(p1);
        Set<Entry<Place, Double>> entrySet = distancesOfThat.entrySet();
        Double min_distance = null;
        Place res_place = null;
        for (Entry<Place, Double> p_d : entrySet){
            if (excluded_places.contains(p_d.getKey())){
                continue;
            }
            if ( (min_distance == null) || (p_d.getValue() < min_distance) ){
                min_distance = p_d.getValue();
                res_place = p_d.getKey();
            }
        }
        return res_place;
    }
}
