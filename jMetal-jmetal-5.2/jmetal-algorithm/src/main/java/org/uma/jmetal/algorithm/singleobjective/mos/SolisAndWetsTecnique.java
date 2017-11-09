package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.algorithm.util.Tecnique;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;


public class SolisAndWetsTecnique extends Tecnique
{

    
    public SolisAndWetsTecnique(HashMap<String, Object> atributes) {
        super(atributes);
    }
    
    @Override
    public Solution evolve(int FE, Solution best, Problem p, Comparator c) {
        
        
        List<DoubleSolution> population = new ArrayList<DoubleSolution>();
        population.add((DoubleSolution)best);
        
        if(best == null)
        {
            algorithm = new SolisAndWetsBuilder((DoubleProblem) p, c)
                    .setRho((double)this.atributes.get("rho"))
                    .setSizeNeighborhood((int) this.atributes.get("sizeNeighborhood"))
                    .setNumEFOs(FE)
                    .setInitialSolution(null)
                    .setPenalizeValue(1)
                    .build();
        }
        else
        {
            algorithm = new SolisAndWetsBuilder((DoubleProblem) p, c)
                    .setRho((double)this.atributes.get("rho"))
                    .setSizeNeighborhood((int) this.atributes.get("sizeNeighborhood"))
                    .setNumEFOs(FE)
                    .setInitialSolution((DoubleSolution) best)
                    .setPenalizeValue(1)
                    .build();
        }
        algorithm.run();
        offspring_population = ((SolisAndWets)algorithm).getPopulation();
        return (Solution) algorithm.getResult();
    }
}
