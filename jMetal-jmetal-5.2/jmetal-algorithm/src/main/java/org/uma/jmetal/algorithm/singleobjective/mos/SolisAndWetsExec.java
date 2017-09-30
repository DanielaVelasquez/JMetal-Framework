/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.algorithm.util.MOSTecniqueExec;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;


public class SolisAndWetsExec extends MOSTecniqueExec
{
    public SolisAndWetsExec(HashMap<String, Object> atributes) {
        super(atributes);
    }
    
    @Override
    public Solution evolve(int FE, Solution best, Problem p, Comparator c) {
        SolisAndWets saw;
        
        List<DoubleSolution> population = new ArrayList<DoubleSolution>();
        population.add((DoubleSolution)best);
        
        if(best != null)
        {
            saw = new SolisAndWetsBuilder((DoubleProblem) p, new ObjectiveComparator<DoubleSolution>(0,ObjectiveComparator.Ordering.ASCENDING))
                    .setNumEFOs(FE)
                    .build();
        }
        else
        {
            saw = new SolisAndWetsBuilder((DoubleProblem) p, new ObjectiveComparator<DoubleSolution>(0,ObjectiveComparator.Ordering.ASCENDING))
                    .setNumEFOs(FE)
                    .setInitialSolution(best)
                    .build();
        }
    }
}
