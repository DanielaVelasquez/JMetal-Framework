package org.uma.jmetal.algorithm.singleobjective.hill_climbing;

import java.util.Comparator;
import org.uma.jmetal.operator.Tweak;
import org.uma.jmetal.operator.impl.localsearch.BoundedUniformConvultion;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.FrobeniusComparator;


public class SteepestAscentHillClimbingBuilder  implements AlgorithmBuilder<SteepestAscentHillClimbing>
{

     /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    private int maxEvaluations;
    private Comparator comparator;
    private Tweak tweak;
    private int n;
    private Problem problem;
    private double penalize_value;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    
    
    public SteepestAscentHillClimbingBuilder(Problem problem) 
    {
        this.problem = problem;
        this.maxEvaluations = 3000;
        this.comparator = new FrobeniusComparator(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
        this.tweak = new BoundedUniformConvultion(0.5, 0.3);
        this.penalize_value = 1;
        this.n = 10;
    }

    @Override
    public SteepestAscentHillClimbing build() {
        return new SteepestAscentHillClimbing(maxEvaluations, comparator, tweak, n, problem, penalize_value);
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public Tweak getTweak() {
        return tweak;
    }

    public int getN() {
        return n;
    }

    public Problem getProblem() {
        return problem;
    }

    public double getPenalize_value() {
        return penalize_value;
    }


    public SteepestAscentHillClimbingBuilder setMaxEvaluations(int maxEvaluations) {
        if(maxEvaluations < 1)
            throw new JMetalException("maxEvaluations must be greater than 1");
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public SteepestAscentHillClimbingBuilder setComparator(Comparator comparator) {
        if(comparator == null)
            throw new JMetalException("comparator can't be null");
        this.comparator = comparator;
        return this;
    }

    public SteepestAscentHillClimbingBuilder setTweak(Tweak tweak) {
        if(tweak == null)
            throw new JMetalException("tweak can't be null");
        this.tweak = tweak;
        return this;
    }

    public SteepestAscentHillClimbingBuilder setN(int n) {
        if(n <=0)
            throw new JMetalException("n is zero or less "+n);
        this.n = n;
        return this;
    }

    public SteepestAscentHillClimbingBuilder setProblem(Problem problem) {
        if(problem == null)
            throw new JMetalException("tweak can't be null");
        this.problem = problem;
        return this;
    }

    public SteepestAscentHillClimbingBuilder setPenalize_value(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }
    
}
