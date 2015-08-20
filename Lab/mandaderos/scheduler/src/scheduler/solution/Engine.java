package scheduler.solution;

import java.util.List;
import scheduler.events.Event;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public abstract class Engine {
    protected ProblemInstance problem;
    public Engine(ProblemInstance problem){
        this.problem = problem;
    }
    public abstract Schedule solve();
    public void applyEvent(Event event){
        this.problem.applyEvent(event);
    }
}
