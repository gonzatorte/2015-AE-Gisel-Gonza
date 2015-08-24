package scheduler.solution;

import Map.Mapa;
import java.util.List;
import scheduler.events.Event;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public abstract class Solver {
    public abstract Schedule solve(ProblemInstance problem);
    public abstract void applyEvent(Event event);
}
