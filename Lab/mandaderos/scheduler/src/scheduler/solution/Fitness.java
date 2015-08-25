package scheduler.solution;

import scheduler.solution.framework.Genotype;
import Map.Place;
import java.util.List;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import scheduler.Coder;
import scheduler.problem.MandaderoTaskQueue;
import scheduler.problem.ProblemInstance;
import scheduler.problem.Schedule;

public class Fitness implements FitnessEvaluator<Genotype> {
    public Coder coder;
    public ProblemInstance problem;
    
    public Fitness(ProblemInstance problem, Coder coder){
        this.coder = coder;
        this.problem = problem;
    }
    
    public double getFitness(Genotype ind, List<? extends Genotype> population) {
        Schedule s = this.coder.decode(ind);
        double total_sum = 0;
        int mandadero_count = 0;
        for (MandaderoTaskQueue q : s.tasks_queues){
            double mandadero_sum = 0;
            Place previous_place = this.problem.origin_mandaderos.get(mandadero_count);
            mandadero_count++;
            for (Place next_place : q) {
                mandadero_sum += this.problem.mapa.distances.getDistance(previous_place, next_place);
                previous_place = next_place;
            }
            total_sum += mandadero_sum;
        }
        double total_avg = total_sum / s.tasks_queues.size();
        return total_avg;
    }

    public boolean isNatural() {
        return true;
    }
}
