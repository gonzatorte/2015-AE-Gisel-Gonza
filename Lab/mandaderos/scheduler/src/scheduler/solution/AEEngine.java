package scheduler.solution;

import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import scheduler.Coder;
import scheduler.events.Event;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public abstract class AEEngine extends Engine {
    protected Fitness fitness;
    protected Mutation mutation;
    protected Cross cross;
    protected Factory factory;
    protected SelectionStrategy selection_estrategy;
    protected Random rnd;
    protected AELogger logger = new AELogger();
    protected TerminationCondition end_condition = new StabilizedOrInterruptedCondition();

    public AEEngine(ProblemInstance problem) {
        super(problem);
    }
    
    @Override
    public void applyEvent(Event event) {
        super.applyEvent(event);
    }
    
    public abstract List<Integer> evolve();

    @Override
    public Schedule solve() {
        List<Integer> genotipo = this.evolve();
        return Coder.decode(genotipo);
    }
}
