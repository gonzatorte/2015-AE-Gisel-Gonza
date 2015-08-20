package scheduler.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

public class Cross extends AbstractCrossover<Genotype>{
    public Cross(){
        this(Probability.ONE);
    }

    public Cross(Probability crossoverProbability){
        super(2, // Requires exactly two cross-over points.
              crossoverProbability);
    }

    public Cross(NumberGenerator<Probability> crossoverProbabilityVariable)
    {
        super(new ConstantGenerator<Integer>(2), // Requires exactly two cross-over points.
              crossoverProbabilityVariable);
    }

    @Override
    protected List<Genotype> mate(Genotype parent1,
                                 Genotype parent2,
                                 int numberOfCrossoverPoints,
                                 Random rng)
    {
        assert numberOfCrossoverPoints == 2 : "Expected number of cross-over points to be 2.";

        if (parent1.size() != parent2.size())
        {
            throw new IllegalArgumentException("Cannot perform cross-over with different length parents.");
        }

        Genotype offspring1 = new Genotype(parent1); // Use a random-access list for performance.
        Genotype offspring2 = new Genotype(parent2);

        int point1 = rng.nextInt(parent1.size());
        int point2 = rng.nextInt(parent1.size());

        int length = point2 - point1;
        if (length < 0)
        {
            length += parent1.size();
        }

        Map<Integer, Integer> mapping1 = new HashMap<Integer, Integer>(length * 2); // Big enough map to avoid re-hashing.
        Map<Integer, Integer> mapping2 = new HashMap<Integer, Integer>(length * 2);
        for (int i = 0; i < length; i++)
        {
            int index = (i + point1) % parent1.size();
            Integer item1 = offspring1.get(index);
            Integer item2 = offspring2.get(index);
            offspring1.set(index, item2);
            offspring2.set(index, item1);
            mapping1.put(item1, item2);
            mapping2.put(item2, item1);
        }

        checkUnmappedElements(offspring1, mapping2, point1, point2);
        checkUnmappedElements(offspring2, mapping1, point1, point2);

        List<Genotype> result = new ArrayList<Genotype>(2);
        result.add(offspring1);
        result.add(offspring2);
        return result;
    }

    private void checkUnmappedElements(Genotype offspring,
                                       Map<Integer, Integer> mapping,
                                       int mappingStart,
                                       int mappingEnd)
    {
        for (int i = 0; i < offspring.size(); i++)
        {
            if (!isInsideMappedRegion(i, mappingStart, mappingEnd))
            {
                Integer mapped = offspring.get(i);
                while (mapping.containsKey(mapped))
                {
                    mapped = mapping.get(mapped);
                }
                offspring.set(i, mapped);
            }
        }
    }

    private boolean isInsideMappedRegion(int position,
                                         int startPoint,
                                         int endPoint)
    {
        boolean enclosed = (position < endPoint && position >= startPoint);
        boolean wrapAround = (startPoint > endPoint && (position >= startPoint || position < endPoint)); 
        return enclosed || wrapAround;
    }
}
