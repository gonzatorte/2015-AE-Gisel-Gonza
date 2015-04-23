
package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class Factory extends AbstractCandidateFactory<List<Integer>>
{
    public Factory()
    {
    }

    @Override
    public List<Integer> generateRandomCandidate(Random rng)
    {
       List<Integer> candidato= new ArrayList<Integer>();
       for(int i=0;i<Fitness.n;i++){
           
           int valor=(new Random().nextInt(2));
           candidato.add(valor);
       }
       return candidato;
    }
}
