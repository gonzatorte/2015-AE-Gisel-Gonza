package scheduler.solution;

import Map.Mapa;
import scheduler.solution.framework.AELogger;
import scheduler.solution.framework.Genotype;
import Map.Place;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.TerminationCondition;
import scheduler.Coder;
import scheduler.events.Event;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;
import scheduler.solution.framework.EvolutionEngine;
import scheduler.solution.framework.Population;

public class AESolver extends Solver {
    protected Fitness fitness;
    protected AELogger logger;
    protected TerminationCondition end_condition = new StabilizedOrInterruptedCondition();
    protected Coder coder;
    protected EvolutionEngine engine;

    public AESolver(long seed, Mapa mapa) {
        super(mapa);
        this.coder = new Coder(mapa);
        this.logger = new AELogger(this.coder);
        this.fitness = new Fitness(this.coder);
        Population pop = new Population(100);
        Random rng = new Random(seed);
        pop.RandomizePopultion(rng);
        this.engine = new EvolutionEngine(pop, this.fitness, rng);
        engine.setSingleThreaded(true);
        engine.addEvolutionObserver(this.logger);
    }
    
    @Override
    public void applyEvent(Event event) {
        if ("addMandadero".equals(event.tipo)){
            engine.current_population.addMandadero();
        } else if ("removeMandadero".equals(event.tipo)){
            engine.current_population.removeMandadero();
        } else if ("addPlace".equals(event.tipo)){
            String place_id = event.data;
            Integer place_id_int = this.coder.addPlace(place_id);
            engine.current_population.addPlace(place_id_int);
        } else if ("removePlace".equals(event.tipo)){
            String place_id = event.data;
            Integer place_id_int = this.coder.removePlace(place_id);
            engine.current_population.removePlace(place_id_int);
        } else {
            throw new Error("No hay este tipo de evento");
        }
    }
    
    public Genotype evolve(ProblemInstance problem) {
        List<Integer> elems = new ArrayList<Integer>();
        for (Place p : problem.mapa.places){
            Integer place_id_int = this.coder.addPlace(p.place_id);
            elems.add(place_id_int);
        }
        fitness.setProblem(problem);
        engine.setFitness(fitness);
        engine.current_population.addMandadero(problem.origin_mandaderos.size());
        engine.current_population.addPlace(elems);
        
        Genotype res = engine.evolve(0, this.end_condition);
        return res;
    }

    @Override
    public Schedule solve(ProblemInstance problem) {
        Genotype genotipo = this.evolve(problem);
        return this.coder.decode(genotipo);
    }
}
