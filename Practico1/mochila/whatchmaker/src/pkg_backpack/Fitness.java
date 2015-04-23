package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class Fitness implements FitnessEvaluator<Genotype> {

    List<Integer> ganancias = new ArrayList<>();
    List<Integer> pesos = new ArrayList<>();
    Integer w;
    public Integer p;

    public static Pair<Integer> FitnessFun(Fenotype ind, List<Integer> ganancias, List<Integer> pesos, Integer w){
        int ganancia = 0;
        int peso = 0;
        for (int i = 0; i < pesos.size(); i++) {
            if (ind.get(i) == 1) {
                ganancia += ganancias.get(i);
                peso += pesos.get(i);
            }
        }
        if (peso <= w) {
            return new Pair<>(ganancia, peso);
        } else {
            ganancia = (ganancia - ((2 * ganancia * (peso - w)) / peso));
            return new Pair<>(ganancia, peso);
        }
    }
    
    @Override
    public double getFitness(Genotype ind,
            List<? extends Genotype> population) {
        Pair<Integer> res =  Fitness.FitnessFun(Coder.decode(ind), Backpack.ganancias, Backpack.pesos, Backpack.w);
        p = res.second;
        return res.first;
    }

    @Override
    public boolean isNatural() {
        return true;
    }
}
