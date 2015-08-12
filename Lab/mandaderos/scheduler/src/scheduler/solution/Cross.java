package scheduler.solution;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.ListOrderCrossover;

public class Cross extends ListOrderCrossover<Integer>{
    public Cross(Probability crossoverProbability){
        super(crossoverProbability);
    }
}
