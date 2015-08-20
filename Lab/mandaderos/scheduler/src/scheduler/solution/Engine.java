package scheduler.solution;

import java.util.List;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public abstract class Engine {
    protected ProblemInstance problem;
    public Engine(ProblemInstance problem){
        this.problem = problem;
    }
    public abstract Schedule solve();
    public void applyEvent(Object event){
        this.problem.applyEvent(event);
    }
}
