package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.Comparator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

public class SolisAndWetsBuilder 
{
    private DoubleProblem problem;
    private Comparator<DoubleSolution> comparator;
    private int sizeNeighborhood;
    private double rho;
    private int numCycles;
    
    public SolisAndWetsBuilder(DoubleProblem problem, Comparator c)
    {
        this.problem = problem;
        this.comparator = c;
        this.sizeNeighborhood = 20;
        this.rho = 0.5;
        this.numCycles = 5;
    }
    
    public SolisAndWetsBuilder setPopulationSizeNeighborhoodSize(int populationSizeNeighborhood)
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
    
    public SolisAndWetsBuilder setNumCycles(int numCycles)
    {
        if(numCycles < 0)
        {
            throw new JMetalException("Cycles is negative: " + numCycles);
        }
        
        this.numCycles = numCycles;
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
    
    public SolisAndWets build()
    {
        return new SolisAndWets(problem, comparator, numCycles, rho, sizeNeighborhood);
    }
}