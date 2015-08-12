package scheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_utils.Pair;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import static scheduler.Solver.seed;
import scheduler.problem.Configuration;
import scheduler.problem.Fenotype;
import scheduler.solution.Cross;
import scheduler.solution.Factory;
import scheduler.solution.Mutation;

public class AESingleThreadEngine {
    public long seed;
    public AESingleThreadEngine(){};
    public List<Integer> evolve() {
        Fitness me = new Fitness();
        Random rnd = new Random(this.seed);
        List<Integer> elems = new ArrayList<Integer>();
        for (int i=0 ; i<Configuration.N ; i++){
            elems.add(i);
        }
        for (int i=0 ; i<Configuration.M - 1 ; i++){
            elems.add(0);
        }
        Factory factory = new Factory(elems);
        List<EvolutionaryOperator<List<Integer>>> operators = 
                new ArrayList<EvolutionaryOperator<List<Integer>>>(2);
        operators.add(
                new Mutation()
        );
        operators.add(new Cross(new Probability(0.75d)));
        EvolutionaryOperator<List<Integer>> pipeline =
                new EvolutionPipeline<List<Integer>>(operators);
        GenerationalEvolutionEngine<List<Integer>> engine =
                new GenerationalEvolutionEngine<List<Integer>>(
                    factory,
                    pipeline,
                    me,
                    new RouletteWheelSelection(),
                    rnd
                );
        engine.setSingleThreaded(true);

        engine.addEvolutionObserver(new AELogger());

        List<Integer> res = engine.evolve(100,
                0,
                new GenerationCount(10000));
        return new ArrayList<Integer>(res);
    }
}
