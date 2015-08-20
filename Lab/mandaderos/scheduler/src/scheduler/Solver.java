package scheduler;

import Map.Mapa;
import scheduler.solution.AESingleThreadEngine;
import java.io.*;
import java.util.List;
import scheduler.problem.EventSource;
import scheduler.problem.Schedule;
import scheduler.problem.OfflineProblemInstance;
import scheduler.problem.ProblemInstance;
import scheduler.solution.AEEngine;

class ParamGetter{
    public String[] args;
    public ParamGetter(String[] args){
        this.args = args;
    }
    
    public long get_seed(){
        long seed;
        if (args.length > 2){
            seed = Long.parseLong(args[2]);
        } else {
            seed = System.currentTimeMillis();
            System.out.println("SEED USADO :" + seed);
        }
        return seed;
    }
}

public final class Solver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ParamGetter param_getter = new ParamGetter(args);
        long seed = param_getter.get_seed();
        
        String nombre_mapa = "ejemplo";
        File f = new File("mapas/" + nombre_mapa + ".jbin");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        Mapa mapa = (Mapa)ois.readObject();
        ProblemInstance problem = new OfflineProblemInstance(mapa);
        EventSource e_source = new EventSource();
        
        AEEngine engine = new AESingleThreadEngine(seed, problem);
        
        List<Object> events;
        events = e_source.getNextEvents();
        while (events != null){
            for (Object ev : events){
                engine.applyEvent(ev);
            }
            Schedule mandaderos_schedule = engine.solve();
            events = e_source.getNextEvents();
        }
    }
}
