package scheduler.solution.framework;

import scheduler.solution.framework.Genotype;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_utils.Pair;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import scheduler.Coder;
import scheduler.Scheduler;
import scheduler.problem.Schedule;

public class AELogger implements EvolutionObserver<Genotype> {
    PrintWriter writer;
    Coder coder;
    
    public AELogger(Coder coder){
        this.coder = coder;
    }
    
    public AELogger(Coder coder, String filename){
        this(coder);
        try {
            FileWriter fichero = new FileWriter(filename + ".stats");
            writer = new PrintWriter(fichero);
        } catch (IOException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    public void populationUpdate(PopulationData<? extends Genotype> data) {
        Schedule f = null;
        if (data.getGenerationNumber() % 100 == 0){
            Genotype g = data.getBestCandidate();
            f = this.coder.decode(g);
            System.out.println(f);
        }
        if (writer != null){
            if (data.getGenerationNumber() % 100 == 0){
//                writer.print(data.getGenerationNumber() + "," + p + ";");
                writer.flush();
            }
        }
    }
}
