
package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class Factory extends AbstractCandidateFactory<List<Integer>>
{
    int dimension;
    public Factory(int dimension)
    {
        this.dimension = dimension;
    }

    @Override
    public List<Integer> generateRandomCandidate(Random rng)
    {
       List<Integer> candidato= new ArrayList<Integer>();
       for(int i=0;i<dimension;i++){
           int valor=(new Random().nextInt(2));
           candidato.add(valor);
       }
       return candidato;
    }
}
