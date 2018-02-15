package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.algorithm.technique.Technique;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class MTSLS1Tecnique extends Technique
{

    public MTSLS1Tecnique(MTS_LS1Builder builder) {
        super(builder);
    }

    

    @Override
    public Solution evolve(int FE, Solution best, Problem p, Comparator c) {

        
        if(best !=null)
        {
            List<DoubleSolution> population = new ArrayList<DoubleSolution>();
            population.add((DoubleSolution)best);
            algorithm = ((MTS_LS1Builder)builder)
                            .setMaxEvaluations(FE)
                            .setDefaultPopulation(population)
                            .setComparator(c)
                            .build();
        }
        else
        {
            algorithm = ((MTS_LS1Builder)builder)
                            .setMaxEvaluations(FE)
                            .setDefaultPopulation(null)
                           .setComparator(c)
                            .build();
        }
        
        
        
        algorithm.run();
        this.offspring_population = ((MTS_LS1)algorithm).getOffspringPopulation();
        return  (Solution) algorithm.getResult();
    }
}
