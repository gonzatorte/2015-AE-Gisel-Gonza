package scheduler.problem;

import Map.Mapa;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import my_utils.ArrayUtil;

public class Schedule
//extends LinkedList<MandaderoTaskQueue>
{
    public Mapa currentMapa;
    public LinkedList<MandaderoTaskQueue> tasks_queues;
    
    public void normalize(){
        Collections.sort(tasks_queues);
    }
}
