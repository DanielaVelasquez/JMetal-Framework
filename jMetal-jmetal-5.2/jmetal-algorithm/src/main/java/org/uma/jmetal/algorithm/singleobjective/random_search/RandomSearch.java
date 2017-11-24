package org.uma.jmetal.algorithm.singleobjective.random_search;


import java.util.Comparator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class RandomSearch <S extends Solution<?>,P extends Problem<S>>  implements Algorithm<S>
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
     * Best individula found
     */
    private S best;
    /**
     * Maximun number of function evaluations
     */
    private int maxEvaluations;
    /**
     * Number of evaluations made
     */
    private int evaluations;
    /**
     * Value to set to solutions when evaluations are over
     */
    protected double penalize_value;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public RandomSearch(P problem, Comparator<S> comparator, int maxEvaluations, double penalize_value)
    {
        this.problem = problem;
        this.comparator = comparator;
        this.maxEvaluations = maxEvaluations;
        this.penalize_value = penalize_value;
    }

    /**
     * Evaluates an individual
     * @param solution 
     */
    protected void evaluate(S solution) {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(solution);
            this.updateProgress();
        }
        else
        {
            this.penalize(solution);
        }
    }
        /**
     * Increments the number of generations evaluated
     */
    protected void updateProgress() {
        evaluations += 1;
    }
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }
    /**
     * Sets an individual function value to a penalization value given by 
     * the user
     * @param solution solution to penalize with a bad function value
     */
    protected void penalize(S solution){
        solution.setObjective(0, this.penalize_value);
    }
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    private S getBest(S s1, S s2)
    {
        int comparison = comparator.compare(s1, s2);
        if(comparison <= 0)
            return s1;
        else
            return s2;
    }
    
    @Override
    public void run()
    {
        this.best = this.problem.createSolution();
        this.evaluate(best);
        while(!isStoppingConditionReached())
        {
            S next = this.problem.createSolution();
            this.evaluate(next);
            this.best = getBest(best, next);
        }
    }

    @Override
    public S getResult() {
        return best;
    }

    @Override
    public String getName() {
        return "Random Search";
    }

    @Override
    public String getDescription() {
        return "Random individuals are created in every iteration, finally the best individual found is selected";
    }
    
}