package org.uma.jmetal.algorithm.singleobjective.lbfgsb;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class LBFGSB implements Algorithm<DoubleSolution>
{

    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Problem to solve
     */
    protected DoubleProblem problem ;
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    /**
     * Solution to evolve on
     */
    private DoubleSolution x;
    /**
     * Maximun number of function evaluations
     */
    private int FE;
    /**
     * Number of evaluations made
     */
    private int evaluations;
     /**
     * Individiuals produced on algorithm
     */
    protected List<DoubleSolution> offspring_population;
    /**
     * Value to set to solutions when evaluations are over
     */
    protected double penalize_value;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DoubleSolution getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return "L-BFGS-B";
    }

    @Override
    public String getDescription() {
        return "L-BFGS-B is a limited memory algorithm for solving large nonlinear "+
               "optimization problems subject to simple bounds on the variables";
    }
    
        /**
     * Evaluates an individual
     * @param solution 
     */
    protected void evaluate(DoubleSolution solution)
    {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(solution);
            this.offspring_population.add((DoubleSolution)solution.copy());
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
        return evaluations >= FE;
    }
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    protected DoubleSolution getBest(DoubleSolution s1, DoubleSolution s2)
    {
        int comparison = comparator.compare(s1, s2);
        if(comparison == 0)
        {
            try
            {
                double b_s1 = (double) s1.getAttribute("B");
                double b_s2 = (double) s2.getAttribute("B");
                if(b_s1 <= b_s2)
                    return s1;
                else
                    return s2;
            }
            catch(Exception e)
            {
                return s1;
            }
        }
        else if(comparison < 1)
            return s1;
        else
            return s2;
    }
    /**
     * Sets an individual function value to a penalization value given by 
     * the user
     * @param solution solution to penalize with a bad function value
     */
    protected void penalize(DoubleSolution solution){
        solution.setObjective(0, this.penalize_value);
    }
}
