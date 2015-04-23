package pkg_backpack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class Fitness implements FitnessEvaluator<List<Integer>> {

    static List<Integer> ganancias = new ArrayList<>();
    static List<Integer> pesos = new ArrayList<>();
    public static Integer n;
    public static Integer p;
    public static Integer w;

    public Fitness(String textIn) {

        // Open the file
        FileInputStream fstream = null;
        try {

            fstream = new FileInputStream(textIn);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            //Read File Line By Line
            String todo = br.readLine();
            n = Integer.parseInt(todo);

            for (int i = 0; i < n; i++) {
                todo = br.readLine();
                String[] arrS = todo.split(" ");
                ganancias.add(Integer.parseInt(arrS[0]) - 1, Integer.parseInt(arrS[1]));
                pesos.add(Integer.parseInt(arrS[0]) - 1, Integer.parseInt(arrS[2]));

            }
            todo = br.readLine();
            w = Integer.parseInt(todo);
            //Close the input stream
            br.close();
        } catch (Exception ex) {
            Logger.getLogger(Fitness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public double getFitness(List<Integer> ind,
            List<? extends List<Integer>> population) {
        int ganancia = 0;
        int peso = 0;
        for (int i = 0; i < n; i++) {
            if (ind.get(i) == 1) {
                ganancia += ganancias.get(i);
                peso += pesos.get(i);
            }

        }
        p = peso;
        if (peso <= w) {
            return ganancia;
        } else {
            return (ganancia - ((2 * ganancia * (peso - w)) / peso));
        }

    }

    @Override
    public boolean isNatural() {
        return true;
    }
}
