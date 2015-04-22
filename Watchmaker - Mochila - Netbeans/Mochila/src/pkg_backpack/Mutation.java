package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

class Mutation implements EvolutionaryOperator<List<Integer>> {

    private final NumberGenerator<Probability> mutationProbability;

    /**
     * @param mutationProbability A {@link NumberGenerator} that controls the
     * probability that a polygon's points will be mutated.
     */
    protected Mutation(NumberGenerator<Probability> mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    protected NumberGenerator<Probability> getMutationProbability() {
        return mutationProbability;
    }

    @Override
    public List<List<Integer>> apply(List<List<Integer>> population, Random rng) {
        List<List<Integer>> newPop = new ArrayList<List<Integer>>(population.size());
        for (List<Integer> individual : population) {
            List<Integer> newInd = mutateIndividual(individual, rng);
            newPop.add(newInd.equals(individual)
                    ? individual
                    : new ArrayList<Integer>(newInd));
        }
        return newPop;
    }

    protected List<Integer> mutateIndividual(List<Integer> individual, Random rng) {
        if (true) {
            List<Integer> newInd = new ArrayList<Integer>(individual);
            for (int i = 0; i < Fitness.n; i++) {
                if (getMutationProbability().nextValue().nextEvent(rng)) {
                    if (newInd.get(i) == 0) {
                        newInd.set(i, 1);
                    } else {
                        newInd.set(i, 0);
                    }
                }
            }
            return newInd;
        } else // Nothing changed.
        {
            return individual;
        }
    }
}
