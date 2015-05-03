package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class Fitness implements FitnessEvaluator<Genotype> {

    List<Integer> ganancias = new ArrayList<Integer>();
    List<Integer> pesos = new ArrayList<Integer>();
    
    public static Pair<Integer> FitnessFun(Fenotype ind){
        return FitnessFun(ind, null);
    }
    public static Pair<Integer> FitnessFun(Fenotype ind,
            List<? extends Genotype> popu){
        if (ind.factible()) {
            return new Pair<Integer>(ind.GananciaTotal(), ind.PesoTotal());
        } else {
//            int peso = ind.PesoTotal();
//            int ganancia = ind.GananciaTotal();
//            ganancia = (ganancia - ((2 * ganancia * (peso - Backpack.w)) / peso));
//            return new Pair<Integer>(ganancia, peso);
            System.out.println("ERROR: " + Backpack.seed);
            System.exit(-1);
            return new Pair<Integer>(0, ind.PesoTotal());
        }
    }
    
    public double getFitness(Genotype ind,
            List<? extends Genotype> population) {
        Pair<Integer> res =  Fitness.FitnessFun(Coder.decode(ind), population);
        return res.first;
    }

    public boolean isNatural() {
        return true;
    }
}
