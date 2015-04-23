package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

public class Cross extends AbstractCrossover<Genotype>
{
    public Cross()
    {
        this(1);
    }

    public Cross(int crossoverPoints)
    {
        super(crossoverPoints);
    }

    public Cross(int crossoverPoints, Probability crossoverProbability)
    {
        super(crossoverPoints, crossoverProbability);
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
        Genotype offspring1 = new Genotype(parent1); // Use a random-access list for performance.
        Genotype offspring2 = new Genotype(parent2);
        // Apply as many cross-overs as required.
        for (int i = 0; i < numberOfCrossoverPoints; i++)
        {
            // Cross-over index is always greater than zero and less than
            // the length of the parent so that we always pick a point that
            // will result in a meaningful cross-over.
            int max = Math.min(parent1.size(), parent2.size());
            if (max > 1) // Don't perform cross-over if there aren't at least 2 elements in each list.
            {
                int crossoverIndex = (1 + rng.nextInt(max - 1));
                for (int j = 0; j < crossoverIndex; j++)
                {
                    Integer temp = offspring1.get(j);
                    offspring1.set(j, offspring2.get(j));
                    offspring2.set(j, temp);
                }
            }
        }
        List<Genotype> result = new ArrayList<Genotype>(2);
        result.add(offspring1);
        result.add(offspring2);
        return result;
    }
}