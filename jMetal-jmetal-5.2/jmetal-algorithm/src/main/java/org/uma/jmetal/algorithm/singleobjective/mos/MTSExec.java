package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearch;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.algorithm.util.MOSTecniqueExec;
import org.uma.jmetal.problem.Problem;


public class MTSExec implements MOSTecniqueExec
{
    @Override
    public List evolve(int FE, List population, Problem p) {
        MultipleTrajectorySearch mts = new MultipleTrajectorySearchBuilder((DoubleProblem) p)
                                       .setFE(FE)
                                       .setDefalultPopulation(population)
                                       .build();
        return mts.getPopulation();          
    }
}
