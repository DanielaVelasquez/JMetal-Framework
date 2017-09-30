package org.uma.jmetal.algorithm.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

/**
 * Wraps an algorithm and define it is going to be execute
 * accordign to its definition
 * @author Daniela Velásquez Garzón
 * @author David Fernando Sotelo
 */
public abstract class MOSTecniqueExec 
{
    /**
     * Atributes needed in order to for the algorithm to run
     */
    protected  HashMap<String, Object> atributes;
    /**
     * Offspring sub-population produced by the tecnique
     */
    protected List offspring_population;
    /**
     * Output populationof the tecnique
     */
    protected List output_population;

    public MOSTecniqueExec(HashMap<String, Object> atributes) {
        this.atributes = atributes;
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

    public List getOutputPopulation() {
        return output_population;
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
