package pkg_backpack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uncommons.maths.number.AdjustableNumberGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

public final class Backpack {

    private static final Probability PROB_001 = new Probability(0.01d);
    private static final Probability PROB_75 = new Probability(0.75);

    static Fitness me;
    static List<Integer> ganancias = new ArrayList<Integer>();
    static List<Integer> pesos = new ArrayList<Integer>();
    static Integer w;
    static long seed;
    static String output_filename = null;

    static NumberGenerator<Probability> mutGen = new AdjustableNumberGenerator<Probability>(PROB_001);
    
    public static void main(String[] args) throws IOException {
        String input_filename = args[0];
        output_filename = args[1];
        
        if (args.length > 2){
            seed = Long.parseLong(args[2]);
        } else {
            seed = System.currentTimeMillis();
        }
        System.out.println("SEED USADO :" + seed);
        
        read_file(input_filename);
        Coder.SortProblem();
        Genotype l = evolve();

        write_file(output_filename, Coder.decode(l));
    }

    public static void read_file(String input_filename) throws IOException {
        FileInputStream fstream = new FileInputStream(input_filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String todo = br.readLine();
        int n = Integer.parseInt(todo);
        for (int i = 0; i < n; i++) {
            todo = br.readLine();
            String[] arrS = todo.split(" ");
            ganancias.add(Integer.parseInt(arrS[0]) - 1, Integer.parseInt(arrS[1]));
            pesos.add(Integer.parseInt(arrS[0]) - 1, Integer.parseInt(arrS[2]));
        }
        todo = br.readLine();
        w = Integer.parseInt(todo);
    }

    public static void write_file(String output_filename, Fenotype l) throws IOException {
        FileWriter fichero = new FileWriter(output_filename);
        PrintWriter pw = new PrintWriter(fichero);
        for (Integer i : l) {
            pw.print(i);
        }
        pw.println();
        pw.println((int) Fitness.FitnessFun(l).first);
        pw.print(me.p);
        fichero.close();
    }
    
    public static Genotype evolve() {
        me = new Fitness();
        Random rnd = new Random(seed);
        Factory factory = new Factory(pesos.size(), rnd);
        List<EvolutionaryOperator<Genotype>> operators = 
                new ArrayList<EvolutionaryOperator<Genotype>>(2);
        operators.add(new Mutation(mutGen, pesos.size()));
        operators.add(new Cross(PROB_75));
        EvolutionaryOperator<Genotype> pipeline =
                new EvolutionPipeline<Genotype>(operators);
        EvolutionEngine<Genotype> engine =
                new GenerationalEvolutionEngine<Genotype>(
                    factory,
                    pipeline,
                    me,
                    new RouletteWheelSelection(),
                    rnd
                );

//        engine.addEvolutionObserver(new EvolutionLogger(Backpack.output_filename));
        engine.addEvolutionObserver(new EvolutionLogger());

        List<Integer> res = engine.evolve(99, // 100 individuals in the population.
                1, // 5% elitism.
                new GenerationCount(10000));
        return new Genotype(res);
    }

    private static class EvolutionLogger implements EvolutionObserver<Genotype> {
        PrintWriter writer = null;        
        EvolutionLogger(String filename){
            try {
                FileWriter fichero = new FileWriter(filename + ".stats");
                writer = new PrintWriter(fichero);
            } catch (IOException ex) {
                Logger.getLogger(Backpack.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
        }
        
        EvolutionLogger(){}

        public void populationUpdate(PopulationData<? extends Genotype> data) {
            Genotype g = data.getBestCandidate();
            Fenotype f = Coder.decode(g);
            
            System.out.println();
            Pair<Integer> p = Fitness.FitnessFun(f);
            System.out.println("Fitness:" + p.first + " Peso:" + p.second + 
                    " Generacion:" + data.getGenerationNumber());
            for (Integer g1 : f) {
                System.out.print(g1 + ",");
            }
            System.out.println();
            
            if (writer != null)
                if (data.getGenerationNumber() % 100 == 0){
                    writer.print(data.getGenerationNumber() + "," + p.first + ";");
                    writer.flush();
                }
        }
    }
}
