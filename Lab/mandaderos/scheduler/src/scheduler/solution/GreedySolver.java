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

    public GreedySolver(Mapa mapa) {
        super(mapa);
    }

    @Override
    public Schedule solve(ProblemInstance problem) {
        Schedule solution = new Schedule(mapa);
        LinkedList<MandaderoTaskQueue> tasks_by_mandadero = solution.tasks_queues;
        TreeSet<Place> visited = new TreeSet<Place>();
        for (Place origin_mandadero : problem.origin_mandaderos) {
            Place nearest = mapa.distances.getNearest(origin_mandadero, visited);
            visited.add(nearest);
            MandaderoTaskQueue task_by_mandadero = new MandaderoTaskQueue();
            task_by_mandadero.add(nearest);
            tasks_by_mandadero.add(task_by_mandadero);
        }
        boolean all_visited = false;
        while (!all_visited){
            for (MandaderoTaskQueue task_by_mandadero : tasks_by_mandadero) {
                Place place_before = task_by_mandadero.get(task_by_mandadero.size()-1);
                Place nearest = mapa.distances.getNearest(place_before, visited);
                if (nearest == null){
                    all_visited = true;
                    break;
                }
                visited.add(nearest);
                task_by_mandadero.add(nearest);
            }
        }
        return solution;
    }

    @Override
    public void applyEvent(Event event) {
    }
}
