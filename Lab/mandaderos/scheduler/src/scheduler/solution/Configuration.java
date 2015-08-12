package scheduler.solution;

import org.uncommons.maths.random.Probability;

public class Configuration {
    // TODO: Sacar los parametros de un archivo de config para la configuracion
    // parametrica
    private static final Probability PROB_001 = new Probability(0.001d);
    private static final Probability PROB_75 = new Probability(0.75);
    private static final double crowding_delta = 0d;
    private static final int nro_islands = 1;
    private static final double migration_ratio = 0d;
    private static final int tournament_size = 2;
    private static final double elitism_proportion = 0.1;
}
