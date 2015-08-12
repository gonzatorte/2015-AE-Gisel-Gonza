package scheduler.problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfflineProblemInstance extends ProblemInstance {
    public List<Event> event_list;
    public int step = 0;

    public OfflineProblemInstance(){
        Event[] events = new Event[]{
            new Event(),
            new Event()
        };
        this.event_list = Arrays.asList(events);
    }
    
    @Override
    public List<Event> getNextEvents() {
        Event ev = this.event_list.remove(step);
        this.step++;
        ArrayList<Event> res = new ArrayList<Event>();
        res.add(ev);
        return res;
    }
}
