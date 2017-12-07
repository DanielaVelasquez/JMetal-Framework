package org.uma.jmetal.algorithm.local_search;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

public abstract class LocalSearch <S extends Solution<?>>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/

    /**
     * Ratio of improvement
     */
    protected double ratio;
    /**
     * Algorithm builder which creates the algorithm with values on its
     * variables
     */
    protected AlgorithmBuilder builder;
    /**
     * Algorithm to execute
     */
    protected Algorithm algorithm;

    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Creates a local search with its algorithm builder
     * @param builder to create tha algorithm of the local search
     */
    public LocalSearch(AlgorithmBuilder builder) {
        this.builder = builder;
        this.algorithm = builder.build();
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
    
    public double getRatio( ) {
        return ratio;
    }

    public abstract S evolve(S best, List<S> population, Problem p, Comparator c, int FE);
    
    public abstract void restart();
}
