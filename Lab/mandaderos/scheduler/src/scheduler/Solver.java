package scheduler;

import scheduler.problem.Fenotype;
import scheduler.solution.Factory;
import scheduler.solution.Cross;
import scheduler.solution.Mutation;
import my_utils.Pair;
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
import scheduler.problem.Configuration;

public final class Solver {
    static long seed;
    static String output_filename = null;
    
    public static void main(String[] args) throws IOException {
        String input_filename = args[0];
        output_filename = args[1];
        
        if (args.length > 2){
            seed = Long.parseLong(args[2]);
        } else {
            seed = System.currentTimeMillis();
            System.out.println("SEED USADO :" + seed);
        }
        AESingleThreadEngine engine = new AESingleThreadEngine();
        List<Integer> l = engine.evolve();
        System.out.print(l);
    }
}
