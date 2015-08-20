package scheduler.solution;

import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import scheduler.Coder;
import scheduler.events.Event;
import scheduler.problem.Schedule;
import scheduler.problem.ProblemInstance;

public abstract class AEEngine extends Engine {
    protected Fitness fitness;
    protected Mutation mutation;
    protected Cross cross;
    protected Factory factory;
    protected SelectionStrategy selection_estrategy;
    protected Random rnd;
    protected AELogger logger;
    protected TerminationCondition end_condition = new StabilizedOrInterruptedCondition();
    protected Coder coder;

    public AEEngine(ProblemInstance problem) {
        super(problem);
        this.coder = new Coder(problem);
        this.logger = new AELogger(this.coder);
    }
    
    @Override
    public void applyEvent(Event event) {
        super.applyEvent(event);
        //ToDo: Hay que cambiar la factory
        if ("addMandadero".equals(event.tipo)){
            List<Genotype> population = get_population();
            //Tengo que agregar un 0 en algun lugar
            for (Genotype individuo : population){
                individuo.add(this.problem.count_mandaderos);
            }
        } else if ("removeMandadero".equals(event.tipo)){
            List<Genotype> population = get_population();
            //Tengo que eliminar un 0
            for (Genotype individuo : population){
                individuo.remove(this.problem.count_mandaderos);
            }
        } else if ("addPlace".equals(event.tipo)){
            String place_id = event.data;
            this.coder.addPlace(place_id);
            Integer place_id_int = this.coder.place_to_id_map.get(place_id);
            List<Genotype> population = get_population();
            for (Genotype individuo : population){
                individuo.add(place_id_int);
            }
        } else if ("removePlace".equals(event.tipo)){
            String place_id = event.data;
            Integer place_id_int = this.coder.place_to_id_map.get(place_id);
            List<Genotype> population = get_population();
            for (Genotype individuo : population){
                individuo.remove(place_id_int);
            }
            this.coder.removePlace(place_id);
            //Remover todos los mandaderos que estaban alli... y ponerlos en otro lugar
            // No considere eso del punto de origen
        } else {
            throw new Error("No hay este tipo de evento");
        }
    }
    
    public abstract Genotype evolve();

    @Override
    public Schedule solve() {
        Genotype genotipo = this.evolve();
        return this.coder.decode(genotipo);
    }
    
    private List<Genotype> get_population(){
        return null;
    }
}
