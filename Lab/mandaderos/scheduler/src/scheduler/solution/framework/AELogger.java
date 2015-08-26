package scheduler.solution.framework;

import scheduler.solution.framework.Genotype;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import scheduler.Coder;
import scheduler.Scheduler;
import scheduler.problem.Schedule;

public class AELogger implements EvolutionObserver<Genotype> {
    PrintWriter writer;
    Coder coder;
    
    public AELogger(Coder coder){
        this(coder, "./instances/exec");
    }
    
    public AELogger(Coder coder, String filename){
        this.coder = coder;
        try {
            FileWriter fichero = new FileWriter(filename + ".stats");
            writer = new PrintWriter(fichero);
        } catch (IOException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    public void populationUpdate(PopulationData<? extends Genotype> data) {
        if (data.getGenerationNumber() % 100 == 0){
            Genotype g = data.getBestCandidate();
            Schedule f = this.coder.decode(g);
            System.out.println(f.tasks_queues);
            if (writer != null){
                writer.print(data.getGenerationNumber() + "," + f.tasks_queues + ";");
                writer.flush();
            }
        }
    }
}
