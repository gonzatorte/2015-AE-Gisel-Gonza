package scheduler.problem;

import java.util.List;

public abstract class ProblemInstance {
    public abstract List<Event> getNextEvents();
}
