package org.uma.jmetal.algorithm.util;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

public abstract class LocalSearch <S extends Solution<?>>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Probability to execute local search
     */
    protected double probability;
    /**
     * Local search improvement
     */
    protected double improvement;
    
    protected int FE;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
     
    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
    
    public abstract S evolve( S best, List<S> population,Problem p, Comparator c);
}
