package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

//class Mutation implements EvolutionaryOperator<List<Pair<Integer>>> {
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
//    public List<List<Pair<Integer>>> apply(List<List<Pair<Integer>>> population, Random rng) {
    public List<List<Integer>> apply(List<List<Integer>> population, Random rng) {

//        List<List<Pair<Integer>>> newPop = new ArrayList<>(population.size());
        List<List<Integer>> newPop = new ArrayList<>(population.size());

//        for (List<Pair<Integer>> individual : population) {
        for (List<Integer> individual : population) {

//            List<Pair<Integer>> newInd = mutateIndividual(individual, rng);
            List<Integer> newInd = mutateIndividual(individual, rng);

            newPop.add(newInd.equals(individual)
                    ? individual
                    : new ArrayList<>(newInd));
        }
        return newPop;
    }

//    protected List<Pair<Integer>> mutateIndividual(List<Pair<Integer>> individual, Random rng) {
    protected List<Integer> mutateIndividual(List<Integer> individual, Random rng) {

        if (true) {
//            List<Pair<Integer>> newInd = new ArrayList<>(individual);
            List<Integer> newInd = new ArrayList<>(individual);

            for (int i = 0; i < Fitness.n; i++) {
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
