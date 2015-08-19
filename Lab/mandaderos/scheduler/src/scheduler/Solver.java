package scheduler;

import scheduler.solution.AESingleThreadEngine;
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
import scheduler.problem.Event;
import scheduler.problem.OfflineProblemInstance;
import scheduler.problem.ProblemInstance;

class ParamGetter{
    public String[] args;
    public ParamGetter(String[] args){
        this.args = args;
    }
    
    public long get_seed(){
        long seed;
        if (args.length > 2){
            seed = Long.parseLong(args[2]);
        } else {
            seed = System.currentTimeMillis();
            System.out.println("SEED USADO :" + seed);
        }
        return seed;
    }
}

public final class Solver {
    public static void main(String[] args) throws IOException {
        ParamGetter param_getter = new ParamGetter(args);
        long seed = param_getter.get_seed();
        AESingleThreadEngine engine = new AESingleThreadEngine(seed);
        ProblemInstance problem = new OfflineProblemInstance();
        List<Event> events;
        events = problem.getNextEvents();
        while (events != null){
            for (Event ev : events){
                problem.applyEvent(ev);
                //ToDo: Aplicar a todos los operadores y cosas del Engine Tb
            }
            List<Integer> l = engine.evolve();
            events = problem.getNextEvents();
        }
    }
}
