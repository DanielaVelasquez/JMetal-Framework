package org.uma.jmetal.algorithm.util;

import java.util.List;
import org.uma.jmetal.problem.Problem;

/**
 * Wraps an algorithm and define it is going to be execute
 * accordign to its definition
 * @author Daniela Velásquez Garzón
 * @author David Fernando Sotelo
 */
public interface MOSTecniqueExec 
{
    /**
     * Evolve algorithm with an initial population
     * @param FE maximun number of function evaluation for the algorithm
     * @param population population to evolve
     * @param p problem to execute with the algorithm
     * @return population optimized
     */
    public List evolve(int FE, List population, Problem p);
}
