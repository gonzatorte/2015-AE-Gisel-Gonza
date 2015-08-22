package scheduler.problem;

import Map.Mapa;
import Map.Place;
import java.util.LinkedList;
import java.util.List;
import scheduler.events.Event;
import scheduler.solution.AESolver;

public class OnlineProblemInstance extends ProblemInstance {

    public OnlineProblemInstance(Mapa mapa) {
        super(mapa);
    }
    
    public OnlineProblemInstance(Mapa mapa, LinkedList<Place> origin_mandaderos) {
        super(mapa, origin_mandaderos);
    }

    @Override
    public void applyEvent(Event event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
