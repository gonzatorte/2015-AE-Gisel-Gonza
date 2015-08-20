package scheduler.problem;

import Map.Mapa;
import java.util.List;
import scheduler.events.Event;
import scheduler.solution.Engine;

public abstract class ProblemInstance {
    public Mapa mapa;
    public int count_mandaderos = 0;
    
    public ProblemInstance(Mapa mapa){
        this.mapa = mapa;
    }
    
    public abstract void applyEvent(Event event);
}
