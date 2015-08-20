package scheduler.problem;

import scheduler.events.EventSource;
import Map.Mapa;
import java.util.Arrays;
import java.util.List;
import scheduler.solution.AEEngine;

public class OfflineProblemInstance extends ProblemInstance {
    public List<EventSource> event_list;
    public int step = 0;
    public AEEngine engine;
    public Mapa mapa;

    public OfflineProblemInstance(Mapa mapa){
        super(mapa);
        EventSource[] events = new EventSource[]{
            new EventSource(),
            new EventSource()
        };
        this.event_list = Arrays.asList(events);
    }

    @Override
    public void applyEvent(Object event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Schedule solve() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
