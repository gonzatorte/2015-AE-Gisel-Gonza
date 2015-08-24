package scheduler.solution.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.AbstractEvolutionEngine;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionUtils;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import scheduler.solution.Fitness;

public class EvolutionEngine
//implements org.uncommons.watchmaker.framework.EvolutionEngine<Genotype> 
//extends AbstractEvolutionEngine<Genotype>
{
    private final Set<EvolutionObserver<? super Genotype>> observers = 
            new CopyOnWriteArraySet<EvolutionObserver<? super Genotype>>();
    private final EvolutionaryOperator<Genotype> evolutionScheme;
    private final Random rng;
    private FitnessEvaluator<? super Genotype> fitnessEvaluator;
    private SelectionStrategy<? super Genotype> selectionStrategy;
    public Population current_population;
    private List<TerminationCondition> satisfiedTerminationConditions;
    
    public void setFitness(FitnessEvaluator<? super Genotype> fitnessEvaluator){
        this.fitnessEvaluator = fitnessEvaluator;
    }

    public EvolutionEngine(Population pop, Fitness fitnessEvaluator, Random rng){
        List<EvolutionaryOperator<Genotype>> operators = 
        new ArrayList<EvolutionaryOperator<Genotype>>(2);
        operators.add(new Mutation());
        operators.add(new Cross(new Probability(0.75d)));
        this.evolutionScheme = new EvolutionPipeline<Genotype>(operators);
        this.rng = rng;
        this.fitnessEvaluator = fitnessEvaluator;
        this.selectionStrategy = new RouletteWheelSelection();
        this.current_population = pop;
    }

    protected List<EvaluatedCandidate<Genotype>> nextEvolutionStep(List<EvaluatedCandidate<Genotype>> evaluatedPopulation,
                                                            int eliteCount,
                                                            Random rng)
    {
        List<Genotype> population = new ArrayList<Genotype>(evaluatedPopulation.size());

        // First perform any elitist selection.
        List<Genotype> elite = new ArrayList<Genotype>(eliteCount);
        Iterator<EvaluatedCandidate<Genotype>> iterator = evaluatedPopulation.iterator();
        while (elite.size() < eliteCount)
        {
            elite.add(iterator.next().getCandidate());
        }
        // Then select candidates that will be operated on to create the evolved
        // portion of the next generation.
        population.addAll(selectionStrategy.select(evaluatedPopulation,
                                                   fitnessEvaluator.isNatural(),
                                                   evaluatedPopulation.size() - eliteCount,
                                                   rng));
        // Then evolve the population.
        population = evolutionScheme.apply(population, rng);
        // When the evolution is finished, add the elite to the population.
        population.addAll(elite);
        return evaluatePopulation(population);
    }

    protected List<EvaluatedCandidate<Genotype>> evaluatePopulation(List<Genotype> population)
    {
        List<EvaluatedCandidate<Genotype>> evaluatedPopulation = new ArrayList<EvaluatedCandidate<Genotype>>(population.size());
        for (Genotype candidate : population)
        {
            evaluatedPopulation.add(new EvaluatedCandidate<Genotype>(candidate, fitnessEvaluator.getFitness(candidate, population)));
        }
        return evaluatedPopulation;
    }
    
    public Genotype evolve(int eliteCount,
                    TerminationCondition... conditions)
    {
        return evolvePopulation(current_population.size(),
                      eliteCount,
                      conditions).get(0).getCandidate();
    }
    
    public List<EvaluatedCandidate<Genotype>> evolvePopulation(int populationSize,
                                                        int eliteCount,
                                                        TerminationCondition... conditions)
    {
        if (eliteCount < 0 || eliteCount >= populationSize)
        {
            throw new IllegalArgumentException("Elite count must be non-negative and less than population size.");
        }
        if (conditions.length == 0)
        {
            throw new IllegalArgumentException("At least one TerminationCondition must be specified.");
        }

        satisfiedTerminationConditions = null;
        int currentGenerationIndex = 0;
        long startTime = System.currentTimeMillis();

        List<Genotype> population = this.current_population;

        // Calculate the fitness scores for each member of the initial population.
        List<EvaluatedCandidate<Genotype>> evaluatedPopulation = evaluatePopulation(population);
        EvolutionUtils.sortEvaluatedPopulation(evaluatedPopulation, fitnessEvaluator.isNatural());
        PopulationData<Genotype> data = EvolutionUtils.getPopulationData(evaluatedPopulation,
                                                                  fitnessEvaluator.isNatural(),
                                                                  eliteCount,
                                                                  currentGenerationIndex,
                                                                  startTime);
        // Notify observers of the state of the population.
        notifyPopulationChange(data);

        List<TerminationCondition> satisfiedConditions = EvolutionUtils.shouldContinue(data, conditions);
        while (satisfiedConditions == null)
        {
            ++currentGenerationIndex;
            evaluatedPopulation = nextEvolutionStep(evaluatedPopulation, eliteCount, rng);
            EvolutionUtils.sortEvaluatedPopulation(evaluatedPopulation, fitnessEvaluator.isNatural());
            data = EvolutionUtils.getPopulationData(evaluatedPopulation,
                                                    fitnessEvaluator.isNatural(),
                                                    eliteCount,
                                                    currentGenerationIndex,
                                                    startTime);
            // Notify observers of the state of the population.
            notifyPopulationChange(data);
            satisfiedConditions = EvolutionUtils.shouldContinue(data, conditions);
        }
        this.satisfiedTerminationConditions = satisfiedConditions;
        return evaluatedPopulation;
    }

    private void notifyPopulationChange(PopulationData<Genotype> data)
    {
        for (EvolutionObserver<? super Genotype> observer : observers)
        {
            observer.populationUpdate(data);
        }
    }
    
    public List<TerminationCondition> getSatisfiedTerminationConditions()
    {
        if (satisfiedTerminationConditions == null)
        {
            throw new IllegalStateException("EvolutionEngine has not terminated.");
        }
        else
        {
            return Collections.unmodifiableList(satisfiedTerminationConditions);
        }
    }

    public void addEvolutionObserver(EvolutionObserver<? super Genotype> observer)
    {
        observers.add(observer);
    }


    public void removeEvolutionObserver(EvolutionObserver<? super Genotype> observer)
    {
        observers.remove(observer);
    }
}
