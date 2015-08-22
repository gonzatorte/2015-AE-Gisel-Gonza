package scheduler.events;

import java.util.ArrayList;
import java.util.List;

public class EventSource {
    int step = 0;
    ArrayList<Event> event_list;
    
    public EventSource(){
        load_test_data();
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
    
    private void load_test_data(){
        event_list = new ArrayList<Event>();
        event_list.add(new Event("addMandadero", "id1"));
        event_list.add(new Event("addMandadero", "id2"));
        event_list.add(new Event("addMandadero", "id1"));
    }
}
