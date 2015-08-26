package scheduler.solution;

import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;

public class StabilizedOrInterruptedCondition implements TerminationCondition {
    public StabilizedOrInterruptedCondition(){
    }

    public boolean shouldTerminate(PopulationData<?> populationData){
        //ToDo: Consulta la lista de eventos para saber si debe hacer algo o 
        // Si el sistema ya no tiene mucha mejora mas...
//        double bestCandidateFitness = populationData.getBestCandidateFitness();
        double stdDev = populationData.getFitnessStandardDeviation();
        double best = populationData.getBestCandidateFitness();
        double mean = populationData.getMeanFitness();
        double stuckness = (stdDev!=0)?(Math.abs((best-mean)/stdDev)):(stdDev);
        return (stuckness < 1.5);
    }
}
