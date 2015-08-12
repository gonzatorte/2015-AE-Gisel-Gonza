package scheduler.solution;

import java.util.List;
import org.uncommons.watchmaker.framework.factories.ListPermutationFactory;

public class Factory extends ListPermutationFactory<Integer>{
    public Factory(List<Integer> elements) {
        super(elements);
    }
}
