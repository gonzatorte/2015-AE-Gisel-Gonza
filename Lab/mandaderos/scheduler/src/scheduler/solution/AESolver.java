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
    public Random rng;

    public AESolver(long seed, ProblemInstance problem) {
        this.coder = new Coder(problem);
        this.logger = new AELogger(this.coder);
        this.fitness = new Fitness(problem, this.coder);
        rng = new Random(seed);
        Population pop = new Population(100, rng);
        this.engine = new EvolutionEngine(pop, this.fitness, rng);
        engine.addEvolutionObserver(this.logger);
    }
    
    @Override
    public void applyEvent(Event event) {
        if ("addMandadero".equals(event.tipo)){
            engine.current_population.addMandadero();
        } else if ("removeMandadero".equals(event.tipo)){
            engine.current_population.removeMandadero();
        } else if ("addPedido".equals(event.tipo)){
            String place_id = (String) event.data[0];
            Integer place_id_int = this.coder.addPlace(place_id);
            engine.current_population.addPlace(place_id_int);
        } else if ("addPlace".equals(event.tipo)){
        } else if ("resolvePlace".equals(event.tipo)){
            String place_id = (String) event.data[0];
            Integer place_id_int = this.coder.removePlace(place_id);
            engine.current_population.removePlace(place_id_int);
        } else {
            throw new Error("No hay este tipo de evento");
        }
    }
    
    public Genotype evolve(ProblemInstance problem) {
        Genotype res = engine.evolve(0, this.end_condition);
        return res;
    }

    @Override
    public Schedule solve(ProblemInstance problem) {
        Genotype genotipo = this.evolve(problem);
        return this.coder.decode(genotipo);
    }
}
