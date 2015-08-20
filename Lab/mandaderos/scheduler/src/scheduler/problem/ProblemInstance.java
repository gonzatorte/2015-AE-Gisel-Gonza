package scheduler.problem;

import Map.Mapa;
import java.util.List;
import scheduler.solution.Engine;

public abstract class ProblemInstance {
    protected Mapa mapa;
    
    public ProblemInstance(Mapa mapa){
        this.mapa = mapa;
    }
    
    public abstract void applyEvent(Object event);
    public abstract Schedule solve();
}
