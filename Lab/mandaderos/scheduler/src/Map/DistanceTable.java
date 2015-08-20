package Map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
}
