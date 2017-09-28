package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearch;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.algorithm.util.MOSTecniqueExec;
import org.uma.jmetal.problem.Problem;


public class MTSExec extends MOSTecniqueExec
{
    public MTSExec(HashMap<String, Object> atributes) {
        super(atributes);
    }
    

    @Override
    public List evolve(int FE, List population, Problem p, Comparator c) {
        //TO-DO ¿ Construirlo siempre o construirlo  antes? el problema de anes
        // es que no se tiene el problema
        MultipleTrajectorySearch mts = new MultipleTrajectorySearchBuilder((DoubleProblem) p)
                                       .setFE(FE)
                                       .setDefalultPopulation(population)
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
        mts.run();
        this.offspring_population = mts.getPopulation(); 
        return   offspring_population;
    }
}
