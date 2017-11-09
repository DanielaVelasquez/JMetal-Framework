package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.Comparator;
import java.util.HashMap;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

public class SolisAndWetsBuilder 
{
    private DoubleProblem problem;
    private Comparator<DoubleSolution> comparator;
    private int sizeNeighborhood;
    private double rho;
    private int numEFOs;
    private DoubleSolution initialSolution;
    private double penalize_value;
    
    public SolisAndWetsBuilder(DoubleProblem problem, Comparator c)
    {
        this.problem = problem;
        this.comparator = c;
        this.sizeNeighborhood = 12;
        this.rho = 0.5;
        this.numEFOs = 3000;    
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
    
    public SolisAndWetsBuilder setNumEFOs(int numEFOs)
    {
        if(numEFOs < 0)
        {
            throw new JMetalException("Evaluations is negative: " + numEFOs);
        }
        
        this.numEFOs = numEFOs;
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

    public int getNumEFOs() {
        return numEFOs;
    }

    public DoubleSolution getInitialSolution() {
        return initialSolution;
    }

    public double getPenalize_value() {
        return penalize_value;
    }
    
    
    
    public SolisAndWets build()
    {
        return new SolisAndWets(problem, comparator, numEFOs, rho, sizeNeighborhood, initialSolution, penalize_value);
    }
    public HashMap<String, Object> getConfiguration()
    {
        HashMap<String, Object> atributes = new HashMap<>();
        atributes.put("sizeNeighborhood", sizeNeighborhood);
        atributes.put("rho", rho);
        return atributes;
    }
}