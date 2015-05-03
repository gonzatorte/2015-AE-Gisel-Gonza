package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

public class Cross extends AbstractCrossover<Genotype>
{
    public Cross(Probability crossoverProbability)
    {
        super(1, crossoverProbability);
    }

    public Cross()
    {
        super(1);
    }
    
    public Cross(NumberGenerator<Integer> crossoverPointsVariable)
    {
        super(crossoverPointsVariable);
    }

    public Cross(NumberGenerator<Integer> crossoverPointsVariable,
                         NumberGenerator<Probability> crossoverProbabilityVariable)
    {
        super(crossoverPointsVariable, crossoverProbabilityVariable);
    }
    
    @Override
    protected List<Genotype> mate(Genotype parent1,
                                 Genotype parent2,
                                 int numberOfCrossoverPoints,
                                 Random rng)
    {
        List<Genotype> result = new ArrayList<Genotype>(2);
        assert(parent1.size() == parent2.size());
        if (parent1.size() > 1)
        {
            int crossoverIndex = (1 + rng.nextInt(parent1.size() - 1));
            Genotype offspring1 = parent1.cross(parent2, crossoverIndex);
            offspring1.correct(rng);
            result.add(offspring1);

            Genotype offspring2 = parent2.cross(parent1, crossoverIndex);
            offspring2.correct(rng);
            result.add(offspring2);
        }
        return result;
    }
}
