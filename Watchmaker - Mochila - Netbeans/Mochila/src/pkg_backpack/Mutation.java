//=============================================================================
// Copyright 2006-2010 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package pkg_backpack;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

/**
 * Base class for mutation operators that modify the points of polygons in an
 * image.
 * @author Daniel Dyer
 */
class Mutation implements EvolutionaryOperator<List<Integer>>
{
    private final NumberGenerator<Probability> mutationProbability;


    /**
     * @param mutationProbability A {@link NumberGenerator} that controls the probability
     * that a polygon's points will be mutated.
     * @param canvasSize The size of the canvas.  Used to constrain the positions of the points.
     */
    protected Mutation(NumberGenerator<Probability> mutationProbability)
    {
        this.mutationProbability = mutationProbability;
    }


    protected NumberGenerator<Probability> getMutationProbability()
    {
        return mutationProbability;
    }


    public List<List<Integer>> apply(List<List<Integer>> population, Random rng)
    {
        List<List<Integer>> newPop = new ArrayList<List<Integer>>(population.size());
        for (List<Integer> individual : population)
        {
            List<Integer> newInd = mutateIndividual(individual, rng);
            newPop.add(newInd.equals(individual)
                            ? individual
                            : new ArrayList<Integer>(newInd));
        }
        return newPop;
    }
    
    protected List<Integer> mutateIndividual(List<Integer> individual, Random rng)
    {
        if (true)
        {
            List<Integer> newInd = new ArrayList<Integer>(individual);
            for(int i=0; i<Fitness.n; i++){
                if (getMutationProbability().nextValue().nextEvent(rng)){
                    if (newInd.get(i)==0){
                        newInd.set(i, 1);
                    }
                    else{
                        newInd.set(i, 0);
                    }
                }
            }
            return newInd;
        }
        else // Nothing changed.
        {
            return individual;
        }
    }
}
