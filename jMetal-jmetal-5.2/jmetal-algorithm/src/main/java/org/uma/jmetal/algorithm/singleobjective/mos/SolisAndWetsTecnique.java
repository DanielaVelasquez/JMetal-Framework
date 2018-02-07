package org.uma.jmetal.algorithm.singleobjective.mos;

import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWets;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import java.util.Comparator;
import org.uma.jmetal.algorithm.technique.Technique;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class SolisAndWetsTecnique extends Technique
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
