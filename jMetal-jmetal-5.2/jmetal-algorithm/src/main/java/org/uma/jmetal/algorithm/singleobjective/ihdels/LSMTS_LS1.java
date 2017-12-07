package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 *Definition of mts-ls1 as a local search for IHDELS algorithm
 */
public class LSMTS_LS1 extends LocalSearch
{
    private final MTS_LS1 mts_ls1;

    public LSMTS_LS1(AlgorithmBuilder builder) {
        super(builder);
        this.mts_ls1 = (MTS_LS1) algorithm;
    }

    @Override
    public Solution evolve(Solution best, List population, Problem p, Comparator c, int FE)
    {
        double oldFitness = best.getObjective(0);
        this.mts_ls1.setComparator(c);
        this.mts_ls1.setProblem((DoubleProblem)p);
        this.mts_ls1.setEvaluations(0);
        this.mts_ls1.setMaxEvaluations(FE);
        this.mts_ls1.setBest((DoubleSolution)best);
        this.mts_ls1.setPopulation(population);
        this.mts_ls1.setPopulationSize(population.size());
        
        this.mts_ls1.run();
        Solution ans = this.mts_ls1.getResult();
        if(c.compare(ans, best) < 0)
            ans = best;
        double newFitness = ans.getObjective(0);
        if(oldFitness != 0)
            this.setRatio(Math.abs(oldFitness - newFitness)/oldFitness);
        else 
            this.setRatio(Math.abs(oldFitness - newFitness));
        population = mts_ls1.getPopulation();
        return ans;
    }

    @Override
    public void restart() 
    {
        
    }
    
}
