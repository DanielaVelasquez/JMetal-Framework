package org.uma.jmetal.algorithm.singleobjective.hill_climbing;

import java.util.Comparator;
import org.uma.jmetal.operator.Tweak;
import org.uma.jmetal.operator.impl.localsearch.BoundedUniformConvultion;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.FrobeniusComparator;


public class HillClimbingBuilder  implements AlgorithmBuilder<HillClimbing>
{

     /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    private int maxEvaluations;
    private Comparator comparator;
    private Tweak tweak;
    private Problem problem;
    private double penalize_value;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    
    
    public HillClimbingBuilder(Problem problem) 
    {
        this.problem = problem;
        this.maxEvaluations = 3000;
        this.comparator = new FrobeniusComparator(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
        this.tweak = new BoundedUniformConvultion(0.5, 0.3);
        this.penalize_value = 1;
    }

    @Override
    public HillClimbing build() {
        return new HillClimbing(maxEvaluations, comparator, tweak, problem, penalize_value);
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

    public Problem getProblem() {
        return problem;
    }

    public double getPenalize_value() {
        return penalize_value;
    }


    public HillClimbingBuilder setMaxEvaluations(int maxEvaluations) {
        if(maxEvaluations < 1)
            throw new JMetalException("maxEvaluations must be greater than 1");
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public HillClimbingBuilder setComparator(Comparator comparator) {
        if(comparator == null)
            throw new JMetalException("comparator can't be null");
        this.comparator = comparator;
        return this;
    }

    public HillClimbingBuilder setTweak(Tweak tweak) {
        if(tweak == null)
            throw new JMetalException("tweak can't be null");
        this.tweak = tweak;
        return this;
    }

    public HillClimbingBuilder setProblem(Problem problem) {
        if(problem == null)
            throw new JMetalException("tweak can't be null");
        this.problem = problem;
        return this;
    }

    public HillClimbingBuilder setPenalizeValue(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }
    
}
