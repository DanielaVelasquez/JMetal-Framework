package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearch;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.algorithm.tecnique.Tecnique;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class MTSTecnique extends Tecnique
{

    public MTSTecnique(MultipleTrajectorySearchBuilder builder) {
        super(builder);
    }

    

    @Override
    public Solution evolve(int FE, Solution best, Problem p, Comparator c) {

        
        if(best !=null)
        {
            List<DoubleSolution> population = new ArrayList<DoubleSolution>();
            population.add((DoubleSolution)best);
            algorithm = ((MultipleTrajectorySearchBuilder)builder)
                            .setMaxEvaluations(FE)
                            .setDefaultPopulation(population)
                            .build();
        }
        else
        {
            algorithm = ((MultipleTrajectorySearchBuilder)builder)
                            .setMaxEvaluations(FE)
                            .setDefaultPopulation(null)
                            .build();
        }
        
        
        
        algorithm.run();
        this.offspring_population = ((MultipleTrajectorySearch)algorithm).getOffspringPopulation();
        return  (Solution) algorithm.getResult();
    }
}
