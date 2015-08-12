package scheduler;

import scheduler.problem.Fenotype;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;


public class Coder {
    public static void NormalizeProblem(){
    }
    
    public static Fenotype decode(List<Integer> genotipo){
        Fenotype f = new Fenotype(genotipo.size());
        return f;
    }
    
    public static List<Integer> encode(Fenotype fenotipo){
        if (fenotipo.genotipo != null){
            return fenotipo.genotipo;
        }
        List<Integer> g = new ArrayList<Integer>(fenotipo.size());
        for (int i=0; i<fenotipo.size(); i++){
            g.add(0);
        }
        return g;
    }
}
