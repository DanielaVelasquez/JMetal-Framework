package org.uma.jmetal.algorithm.technique;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 * Wraps an algorithm and define it is going to be execute
 * accordign to its definition
 * @author Daniela Velásquez Garzón
 * @author David Fernando Sotelo
 */
public abstract class Technique 
{
    /**
     * Atributes needed in order to for the algorithm to run
     */
    protected  AlgorithmBuilder builder;
    /**
     * Offspring sub-population produced by the tecnique
     */
    protected List offspring_population;
    /**
     * Algorithm to execute
     */
    protected Algorithm algorithm;

    public Technique(AlgorithmBuilder builder) {
        this.builder = builder;
    }
    
    
    
    /**
     * Evolve algorithm with an initial population
     * @param FE maximun number of function evaluation for the algorithm
     * @param population population to evolve
     * @param p problem to execute with the algorithm
     * @param c comparator to use on population
     * @return population optimized
     */
    public abstract Solution evolve(int FE, Solution best, Problem p, Comparator c);

    public List getOffspringPopulation() {
        return offspring_population;
    }
    
    public int getOffspringPopulationSize()
    {
        return this.offspring_population.size();
    }
    
    /**
     * Average fitness of the offspring population
     * @return average with fitness of all solution produce with algorithm
     */
    public double calculateAverageFitnessOffspringPopulationSize()
    {
        double fitness = 0;
        int size = offspring_population.size();
        for(int i = 0; i<size;i++)
        {
            Solution s = (Solution) offspring_population.get(i);
            fitness += s.getObjective(0);
        }
        return fitness/size;
    }
}
