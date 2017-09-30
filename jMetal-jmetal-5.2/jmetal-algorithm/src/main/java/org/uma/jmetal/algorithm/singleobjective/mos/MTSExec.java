package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearch;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.algorithm.util.MOSTecniqueExec;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class MTSExec extends MOSTecniqueExec
{
    public MTSExec(HashMap<String, Object> atributes) {
        super(atributes);
    }
    

    @Override
    public Solution evolve(int FE, Solution best, Problem p, Comparator c) {

        MultipleTrajectorySearch mts;
        if(best !=null)
        {
            List<DoubleSolution> population = new ArrayList<DoubleSolution>();
            population.add((DoubleSolution)best);
            mts = new MultipleTrajectorySearchBuilder((DoubleProblem) p)
                                           .setFE(FE)
                                           .setDefaultPopulation(population)
                                           .setPopulationSize((int)this.atributes.get("population_size"))
                                           .setLocalSearchTest((int)this.atributes.get("local_search_test"))
                                           .setLocalSearch((int)this.atributes.get("local_search"))
                                           .setLocalSearchBest((int)this.atributes.get("local_search_best"))
                                           .setNumberOfForeground((int)this.atributes.get("number_of_foreground"))  
                                           .setBonus1((double) this.atributes.get("bonus_1"))
                                           .setBonus2((double) this.atributes.get("bonus_2"))
                                           .setBoundsA((double) this.atributes.get("lower_bound_a"), (double) this.atributes.get("upper_bound_a"))
                                           .setBoundsB((double) this.atributes.get("lower_bound_b"), (double) this.atributes.get("upper_bound_b"))
                                           .setBoundsC((double) this.atributes.get("lower_bound_c"), (double) this.atributes.get("upper_bound_c"))
                                           .build();
        }
        else
        {
            mts = new MultipleTrajectorySearchBuilder((DoubleProblem) p)
                                           .setFE(FE)
                                           .setPopulationSize((int)this.atributes.get("population_size"))
                                           .setLocalSearchTest((int)this.atributes.get("local_search_test"))
                                           .setLocalSearch((int)this.atributes.get("local_search"))
                                           .setLocalSearchBest((int)this.atributes.get("local_search_best"))
                                           .setNumberOfForeground((int)this.atributes.get("number_of_foreground"))  
                                           .setBonus1((double) this.atributes.get("bonus_1"))
                                           .setBonus2((double) this.atributes.get("bonus_2"))
                                           .setBoundsA((double) this.atributes.get("lower_bound_a"), (double) this.atributes.get("upper_bound_a"))
                                           .setBoundsB((double) this.atributes.get("lower_bound_b"), (double) this.atributes.get("upper_bound_b"))
                                           .setBoundsC((double) this.atributes.get("lower_bound_c"), (double) this.atributes.get("upper_bound_c"))
                                           .build();
        }
        
        
        
        mts.run();
        this.offspring_population = mts.getOffspringPopulation();
        return  mts.getResult();
    }
}
