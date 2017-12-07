package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.tecnique.Tecnique;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class SolisAndWetsTecnique extends Tecnique
{

    public SolisAndWetsTecnique(SolisAndWetsBuilder builder) {
        super(builder);
    }

    

    
    @Override
    public Solution evolve(int FE, Solution best, Problem p, Comparator c) {
        
        algorithm = ((SolisAndWetsBuilder) builder)
                        .setMaxEvaluations(FE)
                        .setComparator(c)
                        .setInitialSolution((DoubleSolution) best)
                        .build();
        
        algorithm.run();
        offspring_population = ((SolisAndWets)algorithm).getPopulation();
        return (Solution) algorithm.getResult();
    }
}
