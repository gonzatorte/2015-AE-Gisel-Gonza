package scheduler;

import Map.Coordinate;
import Map.Kml.KmlManager;
import Map.Mapa;
import Map.MapaGenerator;
import com.almworks.sqlite4java.SQLiteException;
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import scheduler.events.Event;
import scheduler.events.EventSource;
import scheduler.problem.Schedule;
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
    public static void main(String[] args) throws IOException, ClassNotFoundException, SAXException, ParserConfigurationException, UnsupportedEncodingException, SQLiteException {
//        do_it(args);
//        test_case_1();
//        test_kml();
        test_case_2();
        test_case_3();
    }
    
    public static void do_it(String[] args) throws IOException, ClassNotFoundException{
        ParamGetter param_getter = new ParamGetter(args);
        long seed = param_getter.get_seed();
        String solution_method = param_getter.get_method();
        
        String nombre_mapa = "ejemplo";
        File f = new File("mapas/" + nombre_mapa + ".jbin");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        Mapa mapa = (Mapa)ois.readObject();
        ProblemInstance problem = new ProblemInstance(mapa);
        EventSource e_source = new EventSource();
        
        Solver solver;
        if ("AE".equals(solution_method)){
            solver = new AESolver(seed, problem);
        } else if ("Greedy".equals(solution_method)){
            solver = new GreedySolver();
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
    
    public static Schedule test_case_1(){
        ProblemInstance pp = ProblemInstance.test_case();
        Solver solver = new GreedySolver();
        Schedule solution = solver.solve(pp);
        KmlManager kml = new KmlManager();
        Event e = new Event(null);
        e.time = 0;
        kml.apply_reschedule(pp, solution,e);
        kml.write_kml("Solution.kml");
        return solution;
    }
    
    public static Schedule test_case_2() throws IOException{
        EventSource e_source = new EventSource(new File("./instances/events/test_1.evn"));
        Mapa mapa = new Mapa(new Coordinate(-30.0, -55.0), new Coordinate(-28.0, -52.0));
        ProblemInstance problem = new ProblemInstance(mapa);
        Solver solver = new AESolver(17, problem);
        Event event = e_source.getNextEvent();
        Schedule solution = null;
        KmlManager kml = new KmlManager();
        while (event != null){
            while (!"time".equals(event.tipo)){
                problem.applyEvent(event);
                solver.applyEvent(event);
                event = e_source.getNextEvent();
            }
            solution = solver.solve(problem);
            kml.apply_reschedule(problem, solution, event);
            event = e_source.getNextEvent();
        }
        kml.write_kml("Solution_AE.kml");
        return solution;
    }

    public static Schedule test_case_3() throws IOException{
        EventSource e_source = new EventSource(new File("./instances/events/test_1.evn"));
        Mapa mapa = new Mapa(new Coordinate(-30.0, -55.0), new Coordinate(-28.0, -52.0));
        ProblemInstance problem = new ProblemInstance(mapa);
        Solver solver = new GreedySolver();
        Event event = e_source.getNextEvent();
        Schedule solution = null;
        KmlManager kml = new KmlManager();
        while (event != null){
            while (!"time".equals(event.tipo)){
                problem.applyEvent(event);
                solver.applyEvent(event);
                event = e_source.getNextEvent();
            }
            solution = solver.solve(problem);
            kml.apply_reschedule(problem, solution, event);
            event = e_source.getNextEvent();
        }
        kml.write_kml("Solution_greedy.kml");
        return solution;
    }
    
    public static Schedule test_kml() throws SAXException, IOException, ParserConfigurationException, UnsupportedEncodingException, SQLiteException{
        ProblemInstance pp = ProblemInstance.test_kml("C:\\Users\\Gisel\\Desktop\\Ejemplo.kml");
        Solver solver = new GreedySolver();
        Schedule solution = solver.solve(pp);
        KmlManager kml = new KmlManager();
        Event e = new Event(null);
        e.time = 0;
        kml.apply_reschedule(pp, solution,e);
        kml.write_kml("Solution.kml");
        return solution;
    }
}
