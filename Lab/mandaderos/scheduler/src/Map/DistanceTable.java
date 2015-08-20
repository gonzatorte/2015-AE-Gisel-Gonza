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
}
