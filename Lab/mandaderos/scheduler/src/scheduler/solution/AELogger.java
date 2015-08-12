package scheduler.solution;

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
import scheduler.Solver;
import scheduler.problem.Fenotype;

public class AELogger implements EvolutionObserver<List<Integer>> {
    PrintWriter writer = null;
    AELogger(String filename){
        try {
            FileWriter fichero = new FileWriter(filename + ".stats");
            writer = new PrintWriter(fichero);
        } catch (IOException ex) {
            Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    AELogger(){}

    public void populationUpdate(PopulationData<? extends List<Integer>> data) {
        Fenotype f = null;
        if (data.getGenerationNumber() % 100 == 0){
            List<Integer> g = data.getBestCandidate();
            f = Coder.decode(g);
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
