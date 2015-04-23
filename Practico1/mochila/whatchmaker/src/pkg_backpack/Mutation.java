package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

class Mutation implements EvolutionaryOperator<Genotype> {

    private final NumberGenerator<Probability> mutationProbability;
    private final int dimension;

    /**
     * @param mutationProbability A {@link NumberGenerator} that controls the
     * probability that a polygon's points will be mutated.
     */
    protected Mutation(NumberGenerator<Probability> mutationProbability, int dimension) {
        this.mutationProbability = mutationProbability;
        this.dimension = dimension;
    }

    protected NumberGenerator<Probability> getMutationProbability() {
        return mutationProbability;
    }

    @Override
    public List<Genotype> apply(List<Genotype> population, Random rng) {

        List<Genotype> newPop = new ArrayList<>(population.size());

        for (Genotype individual : population) {

            Genotype newInd = mutateIndividual(new Genotype(individual), rng);

            newPop.add(newInd.equals(individual)
                    ? individual
                    : new Genotype(newInd));
        }
        return newPop;
    }

    protected Genotype mutateIndividual(Genotype individual, Random rng) {

        if (true) {
            Genotype newInd = new Genotype(individual);

            for (int i = 0; i < this.dimension; i++) {
                if (getMutationProbability().nextValue().nextEvent(rng)) {
//                    Integer ff = (newInd.get(i).first + 1)%2;
//                    Integer ss = (newInd.get(i).second + 1)%2;
//                    Pair<Integer> p = new Pair<>(ff,ss);
                    Integer p = (newInd.get(i) + 1)%2;
                    newInd.set(i, p);
                }
            }
            return newInd;
        } else // Nothing changed.
        {
            return individual;
        }
    }
}
