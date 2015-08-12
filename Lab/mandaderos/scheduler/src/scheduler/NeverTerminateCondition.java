package scheduler;

import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;

public class NeverTerminateCondition implements TerminationCondition
{
    public NeverTerminateCondition(){}

    /**
     * {@inheritDoc}
     */
    public boolean shouldTerminate(PopulationData<?> populationData)
    {
        return false;
    }
}
