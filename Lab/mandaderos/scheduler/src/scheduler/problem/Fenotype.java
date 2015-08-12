package scheduler.problem;

import Map.Mapa;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import my_utils.ArrayUtil;

public class Fenotype extends ArrayList<Integer>{
    public Mapa currentMapa;
    public List<Integer> tasks_queue = new ArrayList<Integer>();
    
    public List<Integer> genotipo = null;
    public Fenotype(int g){
        super(g);
    }
    
    public boolean factible(){
        return true;
    }
    
    @Override
    public String toString(){
        String S = ArrayUtil.ArrayToString(this.toArray());
        int id = System.identityHashCode(this);
        return "[" + id + "]" + S;
    }
}
