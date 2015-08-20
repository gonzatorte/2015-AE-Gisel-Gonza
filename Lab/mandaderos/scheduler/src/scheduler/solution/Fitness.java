package scheduler.solution;

import scheduler.problem.Schedule;
import my_utils.Pair;
import java.util.ArrayList;
import java.util.List;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class Fitness implements FitnessEvaluator<List<Integer>> {

    public double getFitness(List<Integer> ind,
            List<? extends List<Integer>> population) {
        return 0;
    }

    public boolean isNatural() {
        return true;
    }
}
