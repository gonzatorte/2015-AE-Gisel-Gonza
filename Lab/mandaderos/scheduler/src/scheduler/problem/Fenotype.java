package scheduler.problem;

import Map.Mapa;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import my_utils.ArrayUtil;

public class Fenotype extends ArrayList<Integer>{
    public Mapa currentMapa;
    public LinkedList<MandaderoTaskQueue> tasks_queues;
    public List<Integer> genotipo = null;
    
    public boolean factible(){
        return true;
    }
    
    public void normalize(){
        
    }
    
    @Override
    public String toString(){
        String S = ArrayUtil.ArrayToString(this.toArray());
        int id = System.identityHashCode(this);
        return "[" + id + "]" + S;
    }
}
