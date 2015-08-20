package scheduler.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public class AESingleThreadEngine extends AEEngine {
    public long seed;
    
    public AESingleThreadEngine(long seed, ProblemInstance problem){
        super(problem);
        this.fitness = new Fitness();
        this.cross = new Cross(new Probability(0.75d));
        this.selection_estrategy = new RouletteWheelSelection();
        
        List<Integer> elems = new ArrayList<Integer>();
        for (int i=0 ; i<scheduler.problem.Configuration.N ; i++){
            elems.add(i);
        }
        for (int i=0 ; i<scheduler.problem.Configuration.M - 1 ; i++){
            elems.add(0);
        }
        this.factory = new Factory(elems);
        this.seed = seed;
        this.rnd = new Random(this.seed);
    };
    
    public List<Integer> evolve() {
        List<EvolutionaryOperator<List<Integer>>> operators = 
                new ArrayList<EvolutionaryOperator<List<Integer>>>(2);
        operators.add(
                this.mutation
        );
        operators.add(this.cross);
        EvolutionaryOperator<List<Integer>> pipeline =
                new EvolutionPipeline<List<Integer>>(operators);
        GenerationalEvolutionEngine<List<Integer>> engine =
                new GenerationalEvolutionEngine<List<Integer>>(
                    this.factory,
                    pipeline,
                    this.fitness,
                    this.selection_estrategy,
                    rnd
                );
        engine.setSingleThreaded(true);

        engine.addEvolutionObserver(this.logger);

        List<Integer> res = engine.evolve(100, 0, this.end_condition);
        return new ArrayList<Integer>(res);
    }
}