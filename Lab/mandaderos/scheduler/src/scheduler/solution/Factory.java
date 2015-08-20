package scheduler.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class Factory extends AbstractCandidateFactory<Genotype> {
    private final List<Integer> elements;

    public Factory(List<Integer> elements){
        this.elements = elements;
    }

    public Genotype generateRandomCandidate(Random rng){
        Genotype candidate = new Genotype(elements);
        Collections.shuffle(candidate, rng);
        return candidate;
    }
}
