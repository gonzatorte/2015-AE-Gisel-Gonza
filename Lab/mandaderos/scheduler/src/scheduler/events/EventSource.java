package scheduler.events;

import java.util.ArrayList;
import java.util.List;

public class EventSource {
    int step = 0;
    ArrayList<Event> event_list;
    
    public EventSource(){
        event_list = new ArrayList<Event>();
        event_list.add(new Event("addMandadero"));
        event_list.add(new Event("addMandadero"));
        event_list.add(new Event("addMandadero"));
    }
    
    public Event getNextEvent(){
        Event ev = this.event_list.remove(step++);
        ev.time = step++;
        return ev;
    }
    
//    public List<Event> getNextEvents() {
//        Event ev = this.event_list.remove(step++);
//        ev.time = step++;
//        return ev;
//    }
}
