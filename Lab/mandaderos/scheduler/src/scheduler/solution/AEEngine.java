package scheduler.solution;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_utils.Pair;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import scheduler.problem.Configuration;
import scheduler.problem.Fenotype;
import scheduler.solution.Cross;
import scheduler.solution.Factory;
import scheduler.solution.Mutation;

public class AEEngine {
    protected Fitness fitness;
    protected Mutation mutation;
    protected Cross cross;
    protected Factory factory;
    protected SelectionStrategy selection_estrategy;
    protected Random rnd;
    protected AELogger logger = new AELogger();
    protected TerminationCondition end_condition = new StabilizedOrInterruptedCondition();
}
