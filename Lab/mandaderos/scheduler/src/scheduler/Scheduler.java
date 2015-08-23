package scheduler;

import Map.Kml.KmlManager;
import Map.Mapa;
import Map.MapaGenerator;
import java.io.*;
import scheduler.events.Event;
import scheduler.events.EventSource;
import scheduler.problem.Schedule;
import scheduler.problem.OfflineProblemInstance;
import scheduler.problem.ProblemInstance;
import scheduler.solution.AESolver;
import scheduler.solution.GreedySolver;
import scheduler.solution.Solver;

class ParamGetter{
    public String[] args;
    public ParamGetter(String[] args){
        this.args = args;
    }
    
    public long get_seed(){
        long seed;
        if (args.length > 3){
            seed = Long.parseLong(args[2]);
        } else {
            seed = System.currentTimeMillis();
            System.out.println("SEED USADO :" + seed);
        }
        return seed;
    }
    
    public String get_method(){
        if (args.length > 2){
            return args[2];
        } else {
            return "Greedy";
        }
    }
}

public final class Scheduler {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        do_it(args);
        test_case(args);
    }
    
    public static void do_it(String[] args) throws IOException, ClassNotFoundException{
        ParamGetter param_getter = new ParamGetter(args);
        long seed = param_getter.get_seed();
        String solution_method = param_getter.get_method();
        
        String nombre_mapa = "ejemplo";
        File f = new File("mapas/" + nombre_mapa + ".jbin");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        Mapa mapa = (Mapa)ois.readObject();
        ProblemInstance problem = new OfflineProblemInstance(mapa);
        EventSource e_source = new EventSource();
        
        Solver solver;
        if ("AE".equals(solution_method)){
            solver = new AESolver(seed, problem.mapa);
        } else if ("Greedy".equals(solution_method)){
            solver = new GreedySolver(problem.mapa);
        } else {
            throw new Error("Solo Greedy o AE");
        }
        KmlManager kml_manager = new KmlManager();
        
        Event event = e_source.getNextEvent();
        while (event != null){
            problem.applyEvent(event);
            solver.applyEvent(event);
            Schedule mandaderos_schedule = solver.solve(problem);
            kml_manager.apply_reschedule(problem, mandaderos_schedule, event);
            event = e_source.getNextEvent();
        }
        kml_manager.write_kml("solucion_1.kml");
    }
    
    public static void test_case(String[] args){
        ProblemInstance pp = OfflineProblemInstance.test_case();
        Solver solver = new GreedySolver(pp.mapa);
        solver.solve(pp);
    }
}