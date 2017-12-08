package org.uma.jmetal.algorithm.singleobjective.random_search;

import java.util.Comparator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

public class RandomSearchBuilder <S extends Solution<?>,P extends Problem<S>> implements AlgorithmBuilder
{
     /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Problem to solve
     */
    private P problem ;
    /**
     * Determines how a solution should be order
     */
    private Comparator<S> comparator;
     /**
     * Maximun number of function evaluations
     */
    private int maxEvaluations;
    /**
     * Value to set to solutions when evaluations are over
     */
    protected double penalize_value;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    public RandomSearchBuilder(P problem)
    {
        this.problem  = problem;
        this.maxEvaluations = 3000;
        this.penalize_value = 1;
        this.comparator  = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
    }

    public P getProblem() {
        return problem;
    }

    public RandomSearchBuilder setProblem(P problem) {
        if(problem == null)
            throw new JMetalException("Problem can`t be null");
        this.problem = problem;
        return this;
    }

    public Comparator<S> getComparator() {
        return comparator;
    }

    public RandomSearchBuilder setComparator(Comparator<S> comparator) {
        if(comparator == null)
            throw new JMetalException("Comparator can`t be null");
        this.comparator = comparator;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public RandomSearchBuilder setMaxEvaluations(int maxEvaluations) {
        if(maxEvaluations <= 0)
            throw new JMetalException("maxEvaluations must be a positive number greater than 0");
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public double getPenalizeValue() {
        return penalize_value;
    }

    public RandomSearchBuilder setPenalizeValue(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }
    
    public RandomSearch build()
    {
        return new RandomSearch<S,P>(problem, comparator, maxEvaluations, penalize_value);
    }
    
    
}
