package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.util.MOSTecniqueExec;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

public class MOSBuilder 
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
       /**
     * Algorithms to execute inside a MOS algorithm
     * wrapped in its own executer
     */
    protected List<MOSTecniqueExec> tecniques;
    /**
     * Algorithm's problem
     */
    protected DoubleProblem problem ;
    
    /**
     * Algorithm's comparator
     */
    private Comparator<DoubleSolution> comparator ;
    
     /**
     * Maximun number of generations
     */
    protected int maxEvaluations;
    /**
     * Function evaluation for every cycle
     */
    protected double FE;
    /**
     * Reduction factor
     */
    protected double E;
    /**
     * Value to penalize a solution in case evaluations are over
     */
    protected double penalize_value;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    public MOSBuilder(DoubleProblem problem)
    {
        this.problem = problem;
        this.comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
        this.maxEvaluations = 3000;
        this.FE = 100;
        //CUAL VALORPOR DEFECTO???
        this.E = 0.05;
        this.penalize_value = 1;
        this.tecniques = new ArrayList<>();
    }
    
    public MOSHRH build()
    {
        if(!tecniques.isEmpty() || tecniques.size() < 2)
        {
            return new MOSHRH(tecniques, problem, maxEvaluations, FE, comparator, E, penalize_value);
        }
        throw new JMetalException("Tecniques should be at least two");
    }

    public List<MOSTecniqueExec> getTecniques() {
        return tecniques;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public double getFE() {
        return FE;
    }

    public double getE() {
        return E;
    }

    public double getPenalize_value() {
        return penalize_value;
    }

    public MOSBuilder setProblem(DoubleProblem problem) {
        if(problem == null)
            throw new JMetalException("Problem can't be null");
        this.problem = problem;
        return this;
    }

    public MOSBuilder setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
        return this;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        if(maxEvaluations < 1)
            throw new JMetalException("Problem can't be null");
        this.maxEvaluations = maxEvaluations;
    }

    public void setFE(double FE) {
        this.FE = FE;
    }

    public void setE(double E) {
        this.E = E;
    }

    public void setPenalize_value(double penalize_value) {
        this.penalize_value = penalize_value;
    }
    
    
}
