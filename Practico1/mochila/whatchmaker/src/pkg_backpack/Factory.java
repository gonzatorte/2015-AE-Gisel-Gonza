package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class Factory extends AbstractCandidateFactory<Genotype>
{
    int dimension;
    Random rnd;
    public Factory(int dimension, Random rnd)
    {
        this.dimension = dimension;
        this.rnd = rnd;
    }

    public Genotype generateRandomCandidate(Random rng)
    {
       List<Integer> candidato = new ArrayList<Integer>();
       for(int i=0;i<dimension;i++){
           int valor=(this.rnd.nextInt(2));
           candidato.add(valor);
       }
       Genotype g = new Genotype(candidato);
       g.correct(rng);
       return g;
    }
}
