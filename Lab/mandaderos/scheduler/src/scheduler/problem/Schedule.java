package scheduler.problem;

import Map.Mapa;
import Map.Place;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import my_utils.ArrayUtil;

public class Schedule
//extends LinkedList<MandaderoTaskQueue>
{
    public Mapa currentMapa;
    public LinkedList<MandaderoTaskQueue> tasks_queues;
    
    public Schedule(Mapa mapa){
        this.currentMapa = mapa;
        tasks_queues = new LinkedList<MandaderoTaskQueue>();
    }
    
    public void normalize(){
        //ToDo: Esta normalizacion ahora debe cambiarse...
        //Ahora el sorter debe considerar mas iguales a los que tienen el mismo
        //origen
        //Y no debe cambiarlos de lugar, a menos que tambien cambie de lugar
        //el arreglo de origenes (de otro modo, sort a ambos mediante criterios
        //consistentes)
        
        //El task_queues tambien se deberia mover
        //Me estan quedando muy separados los origenes y el resto
        // del recorrido, . Eso esta bien?
        Collections.sort(tasks_queues);
    }
    
    public double get_average_cost(){
        double total_sum = 0;
        for (MandaderoTaskQueue q : this.tasks_queues){
            Place previous_place = q.get(0);
            double mandadero_sum = 0;
            for (int i=1; i<q.size(); i++){
                Place next_place = q.get(i);
                mandadero_sum += this.currentMapa.distances.getDistance(previous_place, next_place);
                previous_place = next_place;
            }
            total_sum += mandadero_sum;
        }
        double total_avg = total_sum / this.tasks_queues.size();
        return total_avg;
    }
}
