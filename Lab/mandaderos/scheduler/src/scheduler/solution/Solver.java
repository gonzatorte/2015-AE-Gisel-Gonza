package scheduler.solution;

import Map.Mapa;
import java.util.List;
import scheduler.events.Event;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public abstract class Solver {
    protected Mapa mapa;
    public Solver(Mapa mapa){
        this.mapa = mapa;
    }
    public abstract Schedule solve(ProblemInstance problem);
    public abstract void applyEvent(Event event);
}
