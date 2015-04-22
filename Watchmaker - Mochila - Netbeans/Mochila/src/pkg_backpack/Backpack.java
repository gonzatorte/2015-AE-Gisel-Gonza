package pkg_backpack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.ListCrossover;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.swing.ProbabilityParameterControl;

public final class Backpack {

    private static final Probability CERO_CON_CERO_UNO = new Probability(0.1d);

    static Fitness me;

    public static void main(String[] args) {
        me = new Fitness(args[0]);
        List<Integer> l = evolve();
        try {
            FileWriter fichero = new FileWriter("Resultado.out");
            PrintWriter pw = new PrintWriter(fichero);
            for (Integer i : l) {
                pw.print(i);
            }
            pw.println();
            pw.println((int) me.getFitness(l, null));
            pw.print(Fitness.p);
            fichero.close();
        } catch (Exception e) {
        }
    }

    public static List<Integer> evolve() {
        Factory factory = new Factory();
        List<EvolutionaryOperator<List<Integer>>> operators = new ArrayList<EvolutionaryOperator<List<Integer>>>(2);
        operators.add(new Mutation(mutColor.getNumberGenerator()));
        operators.add(new ListCrossover());
        EvolutionaryOperator<List<Integer>> pipeline = new EvolutionPipeline<List<Integer>>(operators);
        EvolutionEngine<List<Integer>> engine = new GenerationalEvolutionEngine<List<Integer>>(factory,
                pipeline,
                me,
                new RouletteWheelSelection(),
                new Random());

        engine.addEvolutionObserver(new EvolutionLogger());
        return engine.evolve(6, // 100 individuals in the population.
                1, // 5% elitism.
                new GenerationCount(300));
    }

    /**
     * Trivial evolution observer for displaying information at the end of each
     * generation.
     */
    private static class EvolutionLogger implements EvolutionObserver<List<Integer>> {

        public void populationUpdate(PopulationData<? extends List<Integer>> data) {
            System.out.println("Fitness: " + data.getBestCandidateFitness() + data.getGenerationNumber());
        }
    }

    static ProbabilityParameterControl mutColor = new ProbabilityParameterControl(Probability.ZERO, CERO_CON_CERO_UNO, 3, new Probability(0.01d));
}
