package org.uma.jmetal.runner.singleobjective.ann.tt;

import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.gde3.GDE3Builder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.singleobjective.ann.tt.TrainingTestingEvaluator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;


public class IrisRunner extends AbstractAlgorithmRunner
{
    
    
    public static void main(String[] args) throws Exception {
        int i = 0 ;
        DoubleProblem problem;
        Algorithm<List<DoubleSolution>> algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;

        String problemName ;
        String referenceParetoFront = "" ;
        if (args.length == 1) {
          problemName = args[0];
        } else if (args.length == 2) {
          problemName = args[0] ;
          referenceParetoFront = args[1] ;
        } else {
          problemName = "org.uma.jmetal.problem.singleobjective.ann.tt.Iris";
        }

        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        
        double cr = 0.5 ;
        double f = 0.5 ;
        crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin") ;

        selection = new DifferentialEvolutionSelection() ;

        algorithm = new GDE3Builder(problem)
          .setCrossover(crossover)
          .setSelection(selection)
          .setMaxEvaluations(25000)
          .setPopulationSize(100)
          .build() ;

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
          .execute() ;

        List<DoubleSolution> population = algorithm.getResult() ;
        long computingTime = algorithmRunner.getComputingTime() ;

        System.out.println("Total execution time: " + computingTime + "ms");
        double a = ((TrainingTestingEvaluator)problem).train();
        
        System.out.println("Accuracy"+a);

        printFinalSolutionSet(population);
        if (!referenceParetoFront.equals("")) {
          printQualityIndicators(population, referenceParetoFront) ;
        }
    }
    
}
