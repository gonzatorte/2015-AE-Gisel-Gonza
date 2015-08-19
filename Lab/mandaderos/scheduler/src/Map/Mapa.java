package Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import my_utils.DualMap;

public class Mapa implements Serializable{
    public DualMap<Place, Place, Double> distances;
    public List<Place> places;
    Coordinate diag1;
    Coordinate diag2;
    
    public Mapa(Coordinate diag1, Coordinate diag2){
        this.diag1 = diag1;
        this.diag2 = diag2;
    }
    
    public void Serialize(){
    }
}
