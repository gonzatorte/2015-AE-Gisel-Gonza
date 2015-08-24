package scheduler.solution;

import Map.Mapa;
import Map.Place;
import java.util.LinkedList;
import java.util.TreeSet;
import scheduler.events.Event;
import scheduler.problem.MandaderoTaskQueue;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public class GreedySolver extends Solver {

    public GreedySolver() {
    }

    @Override
    public Schedule solve(ProblemInstance problem) {
        Schedule solution = new Schedule(problem.mapa);
        LinkedList<MandaderoTaskQueue> tasks_by_mandadero = solution.tasks_queues;
        TreeSet<Place> visited = new TreeSet<Place>();
        //ToDo: Agregar a la solucion aquellos lugares de origen que tambien
        //aparecen en la lista de pendientes
        for (Place origin_mandadero : problem.origin_mandaderos) {
            MandaderoTaskQueue task_by_mandadero = new MandaderoTaskQueue();
            if (problem.mapa.places.contains(origin_mandadero)){
                //Ojo que mas de un mandadero puede estar resolviendo el origen
                //si estos parten del mismo origen y es un punto requerido
                task_by_mandadero.add(origin_mandadero);
                visited.add(origin_mandadero);
            }
            tasks_by_mandadero.add(task_by_mandadero);
        }
        for (int i = 0 ; i < problem.origin_mandaderos.size() ; i++) {
            Place origin_mandadero = problem.origin_mandaderos.get(i);
            Place nearest = problem.mapa.distances.getNearest(origin_mandadero, visited);
            visited.add(nearest);
            MandaderoTaskQueue task_by_mandadero = tasks_by_mandadero.get(i);
            task_by_mandadero.add(nearest);
        }
        boolean all_visited = visited.size() >= problem.mapa.places.size();
        while (!all_visited){
            for (MandaderoTaskQueue task_by_mandadero : tasks_by_mandadero) {
                Place place_before = task_by_mandadero.get(task_by_mandadero.size()-1);
                Place nearest = problem.mapa.distances.getNearest(place_before, visited);
                if (nearest == null){
                    break;
                }
                visited.add(nearest);
                task_by_mandadero.add(nearest);
            }
            all_visited = visited.size() >= problem.mapa.places.size();
        }
        return solution;
    }

    @Override
    public void applyEvent(Event event) {
    }
}
