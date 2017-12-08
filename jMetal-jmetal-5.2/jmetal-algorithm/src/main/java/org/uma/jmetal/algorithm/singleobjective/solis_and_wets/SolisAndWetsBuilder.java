package org.uma.jmetal.algorithm.singleobjective.solis_and_wets;

import java.util.Comparator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

public class SolisAndWetsBuilder implements AlgorithmBuilder<SolisAndWets>
{
    private DoubleProblem problem;
    private Comparator<DoubleSolution> comparator;
    private int sizeNeighborhood;
    private double rho;
    private int maxEvaluations;
    private DoubleSolution initialSolution;
    private double penalize_value;
    
    public SolisAndWetsBuilder(DoubleProblem problem)
    {
        this.problem = problem;
        comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
        this.sizeNeighborhood = 12;
        this.rho = 0.5;
        this.maxEvaluations = 3000;    
        this.penalize_value = 1;
    }
    
    public SolisAndWetsBuilder setSizeNeighborhood(int populationSizeNeighborhood)
    {
        if(populationSizeNeighborhood < 0)
        {
            throw new JMetalException("Neighborhood size is negative: " + populationSizeNeighborhood);
        }
        else if(populationSizeNeighborhood % 2 != 0)
        {
            throw new JMetalException("Neighborhood size should be even: " + populationSizeNeighborhood);
        }
        
        this.sizeNeighborhood = populationSizeNeighborhood;
        return this;
    }
    
    public SolisAndWetsBuilder setMaxEvaluations(int maxEvaluations)
    {
        if(maxEvaluations <= 0)
        {
            throw new JMetalException("maxEvaluations is negative or zero: " + maxEvaluations);
        }
        
        this.maxEvaluations = maxEvaluations;
        return this;
    }
    
    public SolisAndWetsBuilder setInitialSolution(DoubleSolution initialSolution)
    {
        this.initialSolution = initialSolution;        
        return this;
    }
    
    public SolisAndWetsBuilder setComparator(Comparator c)
    {
        this.comparator = c;
        return this;
    }
    
    public SolisAndWetsBuilder setRho(double rho)
    {
        this.rho = rho;
        return this;
    }
    
    public SolisAndWetsBuilder setPenalizeValue(double penalize_value)
    {
        this.penalize_value = penalize_value;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public int getSizeNeighborhood() {
        return sizeNeighborhood;
    }

    public double getRho() {
        return rho;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public DoubleSolution getInitialSolution() {
        return initialSolution;
    }

    public double getPenalize_value() {
        return penalize_value;
    }
    
    
    
    @Override
    public SolisAndWets build()
    {
        return new SolisAndWets(problem, comparator, maxEvaluations, rho, sizeNeighborhood, initialSolution, penalize_value);
    }

}