package scheduler.events;

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
    ArrayList<Event> event_list;

    public EventSource(File filepath) throws FileNotFoundException, IOException{
        FileInputStream fis = new FileInputStream(filepath);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        String line;
        Pattern p_time = Pattern.compile("time");
        Pattern p_addMandadero = Pattern.compile("addMandadero <.+>");
        Pattern p_removeMandadero = Pattern.compile("removeMandadero <.+>");
        Pattern p_addPlace = Pattern.compile("addPlace <.+>");
        Pattern p_removePlace = Pattern.compile("removePlace <.+>");
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
                    String place_id = m.group(0);
                    event_list.add(new Event("addMandadero", place_id));
                    continue;
                }
            }
            {
                Matcher m = p_removeMandadero.matcher(line);
                if(m.matches()){
                    String place_id = m.group(0);
                    event_list.add(new Event("removeMandadero", place_id));
                    continue;
                }
            }
            {
                Matcher m = p_addPlace.matcher(line);
                if(m.matches()){
                    String place_id = m.group(0);
                    event_list.add(new Event("addPlace", place_id));
                    continue;
                }
            }
            {
                Matcher m = p_removePlace.matcher(line);
                if(m.matches()){
                    String place_id = m.group(0);
                    event_list.add(new Event("removePlace", place_id));
                    continue;
                }
            }
        }
    }
    
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
