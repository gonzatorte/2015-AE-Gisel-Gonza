package scheduler.events;

import Map.Coordinate;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventSource {
    int step = 0;
    ArrayList<Event> event_list = new ArrayList<Event>();

    private static final Pattern p_addMandadero = Pattern.compile("addMandadero<(.+?)>(?:<(.+?,.+?)>)?");
    private static final Pattern p_removeMandadero = Pattern.compile("removeMandadero<(.+?)>");
    private static final Pattern p_addPlace = Pattern.compile("addPlace<(.+?)>(?:<(.+?,.+?)>)?");
    private static final Pattern p_resolvePlace = Pattern.compile("resolvePlace<(.+?)><(.+?)>");
    
    public EventSource(File filepath) throws FileNotFoundException, IOException{
        FileInputStream fis = new FileInputStream(filepath);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        String line;
        Pattern p_time = Pattern.compile("time");
        while ((line = br.readLine()) != null) {
            {
                Matcher m = p_time.matcher(line);
                if(m.matches()){
                    event_list.add(null);
                    continue;
                }
            }
            {
                Matcher m = p_addMandadero.matcher(line);
                if(m.matches()){
                    String place_id = m.group(1);
                    String origin_coords = m.group(2);
                    if (origin_coords != null){
                        Coordinate origin = Coordinate.fromString(origin_coords);
                        Object[] data = new Object[]{place_id, origin};
                        event_list.add(new Event("addMandadero", data));
                    } else {
                        Object[] data = new Object[]{place_id};
                        event_list.add(new Event("addMandadero", data));
                    }
                    continue;
                }
            }
            {
                Matcher m = p_removeMandadero.matcher(line);
                if(m.matches()){
                    String place_id = m.group(1);
                    event_list.add(new Event("removeMandadero", place_id));
                    continue;
                }
            }
            {
                Matcher m = p_addPlace.matcher(line);
                if(m.matches()){
                    String place_id = m.group(1);
                    String place_coords = m.group(2);
                    if (place_coords != null){
                        Coordinate origin = Coordinate.fromString(place_coords);
                        Object[] data = new Object[]{place_id, origin};
                        event_list.add(new Event("addPlace", data));
                    } else {
                        Object[] data = new Object[]{place_id};
                        event_list.add(new Event("addPlace", data));
                    }
                    continue;
                }
            }
            {
                Matcher m = p_resolvePlace.matcher(line);
                if(m.matches()){
                    String place_id = m.group(1);
                    String origin_id = m.group(2);
                    Object[] data = new Object[]{place_id, origin_id};
                    event_list.add(new Event("resolvePlace", data));
                    continue;
                }
            }
        }
    }
    
    public EventSource(){
        load_test_data();
    }
    
    public Event getNextEvent(){
        if (step < this.event_list.size()){
            Event ev = this.event_list.remove(step++);
            ev.time = step++;
            return ev;
        } else {
            return null;
        }
    }
    
//    public List<Event> getNextEvents() {
//        Event ev = this.event_list.remove(step++);
//        ev.time = step++;
//        return ev;
//    }
    
    private void load_test_data(){
        event_list.add(new Event("addMandadero", "id1"));
        event_list.add(new Event("addMandadero", "id2"));
        event_list.add(new Event("addMandadero", "id1"));
    }
    
    public static void main(String[] args) throws IOException{
        test_case_1();
    }
    
    public static EventSource test_case_1() throws IOException{
        return new EventSource(new File("./instances/events/test.evn"));
    }
}
