package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class Fitness implements FitnessEvaluator<Genotype> {

    List<Integer> ganancias = new ArrayList<Integer>();
    List<Integer> pesos = new ArrayList<Integer>();
    Integer w;
    public Integer p;

    public static Integer PesoTotal(Fenotype ind){
        int peso = 0;
        for (int i = 0; i < Backpack.pesos.size(); i++) {
            if (ind.get(i) == 1) {
                peso += Backpack.pesos.get(i);
            }
        }
        return peso;
    }

    public static Integer GananciaTotal(Fenotype ind){
        int peso = 0;
        for (int i = 0; i < Backpack.ganancias.size(); i++) {
            if (ind.get(i) == 1) {
                peso += Backpack.ganancias.get(i);
            }
        }
        return peso;
    }
    
    public static Pair<Integer> FitnessFun(Fenotype ind){
        int ganancia = GananciaTotal(ind);
        int peso = PesoTotal(ind);
        
        if (peso <= Backpack.w) {
            return new Pair<Integer>(ganancia, peso);
        } else {
//            ganancia = (ganancia - ((2 * ganancia * (peso - Backpack.w)) / peso));
//            return new Pair<Integer>(ganancia, peso);
            return new Pair<Integer>(0, peso);
        }
    }
    
    public double getFitness(Genotype ind,
            List<? extends Genotype> population) {
        Pair<Integer> res =  Fitness.FitnessFun(Coder.decode(ind));
        p = res.second;
        return res.first;
    }

    public boolean isNatural() {
        return true;
    }
}
