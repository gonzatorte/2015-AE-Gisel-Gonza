package scheduler;

import Map.Mapa;
import Map.Place;
import scheduler.problem.Schedule;
import java.util.List;
import java.util.Map;
import scheduler.problem.MandaderoTaskQueue;
import scheduler.problem.ProblemInstance;
import scheduler.solution.framework.Genotype;


public class Coder {
    public Map<String, Integer> place_to_id_map;
    public Map<Integer, String> id_to_place_map;
    public Mapa mapa;
    private int id_generator_counter = 1;
    
    public Coder(Mapa mapa){
        this.mapa = mapa;
    }
    
    public Integer addPlace(String place_id){
        place_to_id_map.put(place_id, id_generator_counter);
        id_to_place_map.put(id_generator_counter, place_id);
        Integer last_id = id_generator_counter;
        id_generator_counter++;
        return last_id;
    }

    public Integer removePlace(String place_id){
        Integer place_index = place_to_id_map.remove(place_id);
        //ToDo: Re acomodar todos los demas para que no queden espacios vacios
        // en la numeracion, puede no ser obligatorio igual
//        id_generator_counter--;
        String place_id_2 = id_to_place_map.remove(place_index);
//        if (place_id_2 != place_id) throw Error("Tienen que ser iguales");
        return place_index;
    }
    
    public Schedule decode(Genotype genotipo){
        Schedule f = new Schedule(this.mapa);
        MandaderoTaskQueue m_queue = new MandaderoTaskQueue();
        for (Integer gen : genotipo){
            if (gen == 0){
                f.tasks_queues.add(m_queue);
                m_queue = new MandaderoTaskQueue();
            } else {
                Place p = mapa.findPlaceById(id_to_place_map.get(gen));
                m_queue.add(p);
            }
        }
        f.tasks_queues.add(m_queue);
        return f;
    }
    
    public Genotype encode(Schedule fenotipo){
        return null;
    }
}
