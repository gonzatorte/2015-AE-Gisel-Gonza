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
    
    public static void main(String[] args) throws IOException {
        String input_filename = args[0];
        String output_filename = args[1];

        me = new Fitness(input_filename);

//        List<Pair<Integer>> l = evolve();
        List<Integer> l = evolve();

//        fenotipo = decode(genotipo);
        FileWriter fichero = new FileWriter(output_filename);
        PrintWriter pw = new PrintWriter(fichero);
        for (Integer i : l) {
            pw.print(i);
        }
        pw.println();
        pw.println((int) me.getFitness(l, null));
        pw.print(Fitness.p);
        fichero.close();
    }

    public static String read_file(String input_filename) throws IOException {
        FileInputStream fstream = new FileInputStream(input_filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String todo = br.readLine();
        return todo;
    }

    public static void write_file(String output_filename, String content) throws IOException {
        FileWriter fichero_test = new FileWriter(output_filename);
        PrintWriter pw_test = new PrintWriter(fichero_test);
        pw_test.print("Hola " + content + " Chau");
        fichero_test.close();
    }
    
//    public static List<Pair<Integer>> evolve() {
    public static List<Integer> evolve() {

        Factory factory = new Factory();
//        List<EvolutionaryOperator<List<Pair<Integer>>>> operators = 
        List<EvolutionaryOperator<List<Integer>>> operators = 
                new ArrayList<>(2);
        operators.add(new Mutation(mutColor.getNumberGenerator()));
//        operators.add(new ListCrossover<Pair<Integer>>());
        operators.add(new ListCrossover<Integer>());
//        EvolutionaryOperator<List<Pair<Integer>>> pipeline = new EvolutionPipeline<>(operators);
        EvolutionaryOperator<List<Integer>> pipeline = new EvolutionPipeline<>(operators);
//        EvolutionEngine<List<Integer>> engine = new GenerationalEvolutionEngine<List<Pair<Integer>>>(factory,
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

        @Override
        public void populationUpdate(PopulationData<? extends List<Integer>> data) {
            System.out.println("Fitness: " + data.getBestCandidateFitness() + data.getGenerationNumber());
        }
    }

    static ProbabilityParameterControl mutColor = new ProbabilityParameterControl(Probability.ZERO, CERO_CON_CERO_UNO, 3, new Probability(0.01d));
}
