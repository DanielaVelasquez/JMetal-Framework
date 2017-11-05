package co.edu.unicauca.exec.training_testing;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.random_search.RandomSearch;
import org.uma.jmetal.algorithm.singleobjective.random_search.RandomSearchBuild;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class RandomSearchRunner 
{    
    private static int ITERATIONS = 1;
    public static void main(String[] args) throws Exception 
    {
        JMetalRandom rnd = JMetalRandom.getInstance();
        
        DoubleProblem problem;
        RandomSearch algorithm;
        String problemName ;
        String referenceParetoFront = "" ;
        Comparator<DoubleSolution> comparator;
        if (args.length == 1) {
          problemName = args[0];
        } else if (args.length == 2) {
          problemName = args[0] ;
          referenceParetoFront = args[1] ;
        } else {
          problemName = "co.edu.unicauca.problem.training_testing.Iris";
        }
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
        
        
        long initTime = System.currentTimeMillis();
        
        for(int i = 0; i < ITERATIONS;i++)
        {
            algorithm =  new  RandomSearchBuild(problem).build();
            rnd.setSeed(1);
            System.out.println("------------------------------");
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;

              DoubleSolution solution = (DoubleSolution) algorithm.getResult();
              long computingTime = algorithmRunner.getComputingTime() ;

              System.out.println("Total execution time: " + computingTime + "ms");
              double train = (1-solution.getObjective(0));
              System.out.println("Objective "+train);
              AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
              double test = p.test(solution);
              System.out.println("Testing: "+test);
              System.out.println("Total evaluations: "+p.total);;
             
        }
        
        System.out.println("Total time: "+(System.currentTimeMillis() - initTime));
        /*System.out.println("--------------------------------");
        print(algorithm.getPopulation());
        /*double a = ((TrainingTestingEvaluator)problem).test(solution);
        
        System.out.println("Accuracy "+a);

        /*printFinalSolutionSet(population);
        if (!referenceParetoFront.equals("")) {
          printQualityIndicators(population, referenceParetoFront) ;
        }*/
    }
    private static void print(List<DoubleSolution> p)
    {
        for(DoubleSolution i: p)
        {
            System.out.println(""+i.getObjective(0));
        }
    }
    
}
