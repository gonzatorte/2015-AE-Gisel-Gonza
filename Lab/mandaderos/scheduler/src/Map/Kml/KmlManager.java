package Map.Kml;

import scheduler.events.Event;
import scheduler.problem.Schedule;

public class KmlManager {
    int color_depth = 0;
    int color_count = 0;
    int color_offset = 256;
    int mandaderos_count = 0;
    
    /*
    Agrega un timeSpan para todos los mandaderos involucrados
    */
    public void add_reschedule(Schedule sched, Event event){
        if (Math.pow(mandaderos_count, color_depth) < mandaderos_count){
            color_count -= color_offset;
        } else {
            color_offset = color_offset*2;
            color_count = color_offset;
            color_depth--;
        }
        mandaderos_count--;
    }
    
    /*
    Agrega un layer con el recorrido de un nuevo mandadero
    */
    public void add_mandadero(Schedule sched){
        if (mandaderos_count < Math.pow(mandaderos_count, color_depth)){
            color_count += color_offset;
        } else {
            color_offset = color_offset/2;
            color_count = color_offset;
            color_depth++;
        }
        mandaderos_count++;
    }
    
    /*
    Agrega un timeSpan para todos los mandaderos involucrados
    */
    public void remove_mandadero(Schedule sched){
        
    }
    
    public void write_kml(String sched_id){
        
    }
}
