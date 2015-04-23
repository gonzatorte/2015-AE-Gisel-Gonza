package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import java.util.Collections;

public class Cross extends AbstractCrossover<Genotype>
{
    public Cross(Probability crossoverProbability)
    {
        super(1, crossoverProbability);
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

    public void correct(Genotype g1, Genotype g2, Random rng){
//        if (!Coder.decode(offspring1).factible()){
//            int crosspoint = (1 + rng.nextInt(g1.size() - 1));
//            g1.cross(g2, crosspoint);
//        }
        while (!Coder.decode(g1).factible()){
            for(int i=g1.size()-1;i>=0;i--){
                if (g1.get(i) == 1){
                    g1.set(i, 0);
                    break;
                }
            }
        }
    }
    
    @Override
    protected List<Genotype> mate(Genotype parent1,
                                 Genotype parent2,
                                 int numberOfCrossoverPoints,
                                 Random rng)
    {
        Genotype offspring1 = new Genotype(parent1); // Use a random-access list for performance.
        Genotype offspring2 = new Genotype(parent2);
        assert(parent1.size() == parent2.size());
        if (parent1.size() > 1) // Don't perform cross-over if there aren't at least 2 elements in each list.
        {
            int crossoverIndex = (1 + rng.nextInt(parent1.size() - 1));
            offspring1 = offspring1.cross(offspring2, crossoverIndex);
            offspring2 = offspring2.cross(offspring1, crossoverIndex);
            correct(offspring1, offspring2, rng);
            correct(offspring2, offspring1, rng);
        }
        List<Genotype> result = new ArrayList<Genotype>(2);
        result.add(offspring1);
        result.add(offspring2);
        return result;
    }
}
