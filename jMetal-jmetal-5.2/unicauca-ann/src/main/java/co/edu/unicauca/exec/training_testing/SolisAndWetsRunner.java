package co.edu.unicauca.exec.training_testing;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_G;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDE;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWets;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsBuilder;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearch;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class SolisAndWetsRunner 
{    
    public static void main(String[] args) throws Exception 
    {
        JMetalRandom rnd = JMetalRandom.getInstance();
        
        DoubleProblem problem;
        SolisAndWets algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;
        SolutionListEvaluator<DoubleSolution> evaluator ;
        String problemName ;
        String referenceParetoFront = "" ;
        Comparator<DoubleSolution> comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);;
        if (args.length == 1) {
          problemName = args[0];
        } else if (args.length == 2) {
          problemName = args[0] ;
          referenceParetoFront = args[1] ;
        } else {
          problemName = "co.edu.unicauca.problem.training_testing.Iris";
        }
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
       
        /*double result[] = new double[]{-0.8644811116014093,0.482980034535944,-1.0,-1.0,0.9693794184155992,-0.6966032329569359,0.29491871745966697,0.5174827338123161,-0.915489247034809,-0.7829404115403313,0.21532418070938986,0.6453706265910443,-0.9656326548227394,-1.0,-1.0,-0.8573846664773566,-1.0,-0.9743262421676628};
        
        
        
        TrainingTestingEvaluator tt = (TrainingTestingEvaluator) problem;
        tt.getInputWeightsBiasFrom(result);
        System.out.println("Accuracy training: "+tt.train());*/
        
        double cr = 0.5 ;
        double f = 0.5 ;
        crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin") ;

        selection = new DifferentialEvolutionSelection() ;
        
        
        for(int i = 0; i < 30;i++)
        {
            algorithm = new SolisAndWetsBuilder(problem, comparator)
                            .setNumEFOs(3000)
                            .setInitialSolution(null)
                            .setPenalizeValue(1)
                            .build();
            rnd.setSeed(i);
            System.out.println("------------------------------");
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;

              DoubleSolution solution = (DoubleSolution) algorithm.getResult();
              long computingTime = algorithmRunner.getComputingTime() ;

              System.out.println("Total execution time: " + computingTime + "ms");
              System.out.println("Objective "+(1-solution.getObjective(0)));
              AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
              System.out.println("Testing: "+p.test(solution));
              System.out.println("Total evaluations: "+p.total);;
        }
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
