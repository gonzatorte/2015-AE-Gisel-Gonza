package scheduler.solution;

import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;

public class StabilizedOrInterruptedCondition implements TerminationCondition {
    public StabilizedOrInterruptedCondition(){}

    public boolean shouldTerminate(PopulationData<?> populationData){
        //ToDo: Consulta la lista de eventos para saber si debe hacer algo o 
        // Si el sistema ya no tiene mucha mejora mas...
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
