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
        Pattern p = Pattern.compile("time");
        while ((line = br.readLine()) != null) {
            Matcher m = p.matcher(line);
            boolean b = m.matches();
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
