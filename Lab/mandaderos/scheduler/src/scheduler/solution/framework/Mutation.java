package scheduler.solution.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class Mutation implements EvolutionaryOperator<Genotype>{
    private final NumberGenerator<Integer> mutationCountVariable;
    private final NumberGenerator<Integer> mutationAmountVariable;

    public Mutation(){
        this(1, 1);
    }

    public Mutation(int mutationCount, int mutationAmount){
        this(new ConstantGenerator<Integer>(mutationCount),
             new ConstantGenerator<Integer>(mutationAmount));
    }

    public Mutation(NumberGenerator<Integer> mutationCount,
                             NumberGenerator<Integer> mutationAmount){
        this.mutationCountVariable = mutationCount;
        this.mutationAmountVariable = mutationAmount;
    }


    public List<Genotype> apply(List<Genotype> selectedCandidates, Random rng){
        List<Genotype> result = new ArrayList<Genotype>(selectedCandidates.size());
        for (Genotype candidate : selectedCandidates)
        {
            Genotype newCandidate = new Genotype(candidate);
            int mutationCount = Math.abs(mutationCountVariable.nextValue());
            for (int i = 0; i < mutationCount; i++)
            {
                int fromIndex = rng.nextInt(newCandidate.size());
                int mutationAmount = mutationAmountVariable.nextValue();
                int toIndex = (fromIndex + mutationAmount) % newCandidate.size();
                if (toIndex < 0)
                {
                    toIndex += newCandidate.size();
                }
                // Swap the randomly selected element with the one that is the
                // specified displacement distance away.
                Collections.swap(newCandidate, fromIndex, toIndex);
            }
            result.add(newCandidate);
        }
        return result;
    }
}
