package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.technique.Technique;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

public class MOSBuilder implements AlgorithmBuilder
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
       /**
     * Algorithms to execute inside a MOS algorithm
     * wrapped in its own executer
     */
    protected List<Technique> techniques;
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
    protected int FE;
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
        this.E = 0.05;
        this.penalize_value = 1;
        this.techniques = new ArrayList<>();
    }
    
    public MOS build()
    {
        if(!techniques.isEmpty() || techniques.size() < 2)
        {
            return new MOS(techniques, problem, maxEvaluations, FE, comparator, E, penalize_value);
        }
        throw new JMetalException("Should be at least 2 tecniques");
    }

    public List<Technique> getTechniques() {
        return techniques;
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

    public MOSBuilder setMaxEvaluations(int maxEvaluations) {
        if(maxEvaluations < 1)
            throw new JMetalException("maxEvaluations must be greater than 1");
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public MOSBuilder setFE(int FE) {
        if(FE <= 0)
            throw new JMetalException("Function evalutions is 0 or less: "+FE);
        this.FE = FE;
        return this;
    }

    public MOSBuilder setE(double E) {
        this.E = E;
        return this;
    }

    public MOSBuilder setPenalizeValue(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }
    
    public MOSBuilder addTechnique(Technique t)
    {
        if(t == null)
            throw new JMetalException("Tecnique is null");
        techniques.add(t);
        return this;
    }
    
    public MOSBuilder removeTechniques()
    {
        techniques.clear();
        return this;
    }
}
