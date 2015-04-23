package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class Fitness implements FitnessEvaluator<Genotype> {

    List<Integer> ganancias = new ArrayList<>();
    List<Integer> pesos = new ArrayList<>();
    Integer w;
    public Integer p;

    public Fitness(List<Integer> ganancias, List<Integer> pesos, Integer w) {
        assert(ganancias.size() == pesos.size());
        this.ganancias = ganancias;
        this.pesos = pesos;
        this.ganancias = ganancias;
        this.w = w;
    }

    public double FitnessFun(Fenotype ind){
        int ganancia = 0;
        int peso = 0;
        for (int i = 0; i < pesos.size(); i++) {
            if (ind.get(i) == 1) {
                ganancia += ganancias.get(i);
                peso += pesos.get(i);
            }
        }
        p = peso;
        if (peso <= w) {
            return ganancia;
        } else {
            return (ganancia - ((2 * ganancia * (peso - w)) / peso));
        }
    }
    
    @Override
    public double getFitness(Genotype ind,
            List<? extends Genotype> population) {
        return this.FitnessFun(Coder.decode(ind));
    }

    @Override
    public boolean isNatural() {
        return true;
    }
}
