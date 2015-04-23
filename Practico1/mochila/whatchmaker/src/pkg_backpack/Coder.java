package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;


public class Coder {
    public static Map<Integer, Integer> Genotype_2_Fenotype_map = new TreeMap<Integer, Integer>();
    public static Map<Integer, Integer> Fenotype_2_Genotype_map = new TreeMap<Integer, Integer>();
    public static int MaxInd(List<Integer> lista){
        int val = lista.get(0);
        int ind = 0;
        for (int i=0; i<lista.size(); i++){
            if (lista.get(i) > val){
                val = lista.get(i);
                ind = i;
            }
        }
//        lista.set(ind, Integer.MAX_VALUE);
        lista.set(ind, 0);
        return ind;
    }
    public static void SortProblem(){
        List<Integer> aux = new ArrayList<Integer>(Backpack.pesos);
        List<Integer> aux2 = new ArrayList<Integer>(Backpack.pesos);
        Collections.sort(aux);
        for (int i=0; i<aux.size();i++) {
            Integer m = MaxInd(aux2);
            Genotype_2_Fenotype_map.put(i, m);
            Fenotype_2_Genotype_map.put(m, i);
        }
    }
    public static Fenotype decode(Genotype genotipo)
    {
        Fenotype f = new Fenotype(genotipo.size());
        for (int i=0; i<genotipo.size(); i++){
            f.add(0);
        }
        for (int i=0; i<genotipo.size(); i++){
            f.set(Genotype_2_Fenotype_map.get(i), genotipo.get(i));
        }
        return f;
//        return new Fenotype((ArrayList<Integer>)genotipo);
    }

    public static Genotype encode(Fenotype fenotipo)
    {
        Genotype g = new Genotype(fenotipo.size());
        for (int i=0; i<fenotipo.size(); i++){
            g.add(0);
        }
        for (int i=0; i<fenotipo.size(); i++){
            g.set(Fenotype_2_Genotype_map.get(i), fenotipo.get(i));
        }
        return g;
//        return new Genotype((ArrayList<Integer>)fenotipo);
    }
}
