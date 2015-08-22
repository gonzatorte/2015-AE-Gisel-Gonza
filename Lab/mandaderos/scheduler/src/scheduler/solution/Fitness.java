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
    
    public Fitness(Coder coder){
        this.coder = coder;
    }
    
    public void setProblem(ProblemInstance problem){
        this.problem = problem;
    }
    
    public double getFitness(Genotype ind, List<? extends Genotype> population) {
        Schedule s = this.coder.decode(ind);
        double total_sum = 0;
        for (MandaderoTaskQueue q : s.tasks_queues){
            Place previous_place = q.get(0);
            double mandadero_sum = 0;
            for (int i=1; i<q.size(); i++){
                Place next_place = q.get(i);
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
