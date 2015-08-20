package scheduler.problem;

import Map.Mapa;
import java.util.List;
import scheduler.events.Event;
import scheduler.solution.AEEngine;

public class OnlineProblemInstance extends ProblemInstance {

    public OnlineProblemInstance(Mapa mapa) {
        super(mapa);
    }

    @Override
    public void applyEvent(Event event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
