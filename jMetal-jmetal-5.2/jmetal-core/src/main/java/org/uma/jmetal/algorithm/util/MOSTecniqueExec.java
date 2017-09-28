package org.uma.jmetal.algorithm.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.problem.Problem;

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
    public abstract List evolve(int FE, List population, Problem p, Comparator c);

    public List getOffspringPopulation() {
        return offspring_population;
    }
    
    public int getOffspringPopulationSize()
    {
        return this.offspring_population.size();
    }
}
