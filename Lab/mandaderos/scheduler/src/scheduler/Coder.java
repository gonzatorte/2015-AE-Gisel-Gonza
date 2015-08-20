package scheduler;

import Map.Place;
import scheduler.problem.Schedule;
import java.util.List;
import java.util.Map;
import scheduler.problem.ProblemInstance;
import scheduler.solution.Genotype;


public class Coder {
    public Map<String, Integer> place_to_id_map;
    public Map<Integer, String> id_to_place_map;
    
    public Coder(ProblemInstance problem){
        
    }
    
    public void addPlace(String place_id){
        //ToDo: Actualiza los mapeos
    }

    public void removePlace(String place_id){
        //ToDo: Actualiza los mapeos
    }
    
    public Schedule decode(Genotype genotipo){
        Schedule f = new Schedule();
        return f;
    }
    
    public Genotype encode(Schedule fenotipo){
        return null;
    }
}
