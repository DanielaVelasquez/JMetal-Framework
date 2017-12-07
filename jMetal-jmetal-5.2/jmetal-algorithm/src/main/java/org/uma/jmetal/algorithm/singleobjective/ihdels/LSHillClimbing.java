package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbing;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 * Definition of hillclimbing as a local search for IHDELS algorithm
 */
public class LSHillClimbing extends LocalSearch
{

    private final HillClimbing hillClimbing;
    
    public LSHillClimbing(AlgorithmBuilder builder) {
        super(builder);
        this.hillClimbing = (HillClimbing) this.algorithm;
    }

    @Override
    public Solution evolve(Solution best, List population, Problem p, Comparator c, int FE)
    {
        double oldFitness = best.getObjective(0);
        
        this.hillClimbing.setProblem(p);
        this.hillClimbing.setComparator(c);
        int size = population.size();
        int FE_individual = FE / size;
        int missing_evaluations = FE % size;
        
        Solution current_best = best.copy();
        
        for(int i = 0; i < size; i++)
        {
            if(missing_evaluations!= 0 && (size - i + 1) == missing_evaluations)
                FE_individual++;
            this.hillClimbing.setEvaluations(0);
            this.hillClimbing.setMaxEvaluations(FE_individual);
            this.hillClimbing.setBest((Solution) population.get(i));
            this.hillClimbing.run();
            Solution ans = this.hillClimbing.getResult();
            population.set(i, ans);
            if(c.compare(ans, current_best) < 0)
                current_best = ans;
        }
        double newFitness = current_best.getObjective(0);
        if(oldFitness != 0)
            this.setRatio(Math.abs(oldFitness - newFitness)/oldFitness);
        else 
            this.setRatio(Math.abs(oldFitness - newFitness));
        return current_best;
    }

    @Override
    public void restart() 
    {
        
    }
    
}
