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

    public List<Genotype> apply(List<Genotype> population, Random rng) {
        for (Genotype individual : population) {
            mutateIndividual(individual, rng);
        }
        return population;
    }

    protected Genotype mutateIndividual(Genotype individual, Random rng) {
        for (int i = 0; i < this.dimension; i++) {
            if (getMutationProbability().nextValue().nextEvent(rng)) {
                Integer p = (individual.get(i) + 1)%2;
                individual.set(i, p);
            }
        }
        individual.correct(rng);
        return individual;
    }
}
