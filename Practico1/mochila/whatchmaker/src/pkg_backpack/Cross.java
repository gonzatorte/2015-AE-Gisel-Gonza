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
        int max = Math.min(parent1.size(), parent2.size());
        if (max > 1) // Don't perform cross-over if there aren't at least 2 elements in each list.
        {
            int crossoverIndex = (1 + rng.nextInt(max - 1));
            offspring1 = offspring1.cross(offspring2, crossoverIndex);
            offspring2 = offspring2.cross(offspring1, crossoverIndex);
        }
        List<Genotype> result = new ArrayList<Genotype>(2);
        result.add(offspring1);
        result.add(offspring2);
        return result;
    }
}
