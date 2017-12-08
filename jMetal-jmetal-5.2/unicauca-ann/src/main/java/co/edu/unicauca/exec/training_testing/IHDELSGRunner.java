package co.edu.unicauca.exec.training_testing;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbingBuilder;
import org.uma.jmetal.algorithm.singleobjective.ihdels.IHDELS;
import org.uma.jmetal.algorithm.singleobjective.ihdels.IHDELSBuilder;
import org.uma.jmetal.algorithm.singleobjective.ihdels.LSHillClimbing;
import org.uma.jmetal.algorithm.singleobjective.ihdels.LSMTS_LS1;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.FrobeniusComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class IHDELSGRunner 
{    
    public static void main(String[] args) throws Exception 
    {
        JMetalRandom rnd = JMetalRandom.getInstance();
        
        DoubleProblem problem;
        IHDELS algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;
        SolutionListEvaluator<DoubleSolution> evaluator ;
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
       
        /*double result[] = new double[]{-0.8644811116014093,0.482980034535944,-1.0,-1.0,0.9693794184155992,-0.6966032329569359,0.29491871745966697,0.5174827338123161,-0.915489247034809,-0.7829404115403313,0.21532418070938986,0.6453706265910443,-0.9656326548227394,-1.0,-1.0,-0.8573846664773566,-1.0,-0.9743262421676628};
        
        
        
        TrainingTestingEvaluator tt = (TrainingTestingEvaluator) problem;
        tt.getInputWeightsBiasFrom(result);
        System.out.println("Accuracy training: "+tt.train());*/
        
        double train = -1.0;
        double test = -1.0;
        
        long initTime = System.currentTimeMillis();
        SaDEBuilder builder = new SaDEBuilder(problem);
        
        for(int i = 0; i < 30;i++)
        {
            HillClimbingBuilder hcBuilder = new HillClimbingBuilder(problem);
            LocalSearch hillClimbing = new LSHillClimbing(hcBuilder);
            
            MTS_LS1Builder mtsls1Builder = new MTS_LS1Builder(problem);
            LocalSearch mtsls1 = new LSMTS_LS1(mtsls1Builder);
            algorithm = new IHDELSBuilder(problem)
                    .addLocalSearch(mtsls1)
                    .addLocalSearch(hillClimbing)
                    .setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
                    .setMaxEvaluations(3000)
                    .setFE_DE(100)
                    .setFE_LS(100)
                    .setPopulation_size(10)
                    .setReStart(3)
                    .setSearchDomain(-0.005, 0.005)
                    .setThreshold(0.01)
                    .setSaDEBuilder(builder)
                    .build();
            rnd.setSeed(i);
            System.out.println("------------------------------");
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;

              DoubleSolution solution = (DoubleSolution) algorithm.getResult();
              long computingTime = algorithmRunner.getComputingTime() ;

             // System.out.println("Total execution time: " + computingTime + "ms");
              AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
              
              System.out.println("evaluaciones: "+p.total);
              train = (1-solution.getObjective(0));
              //System.out.println("Objective "+train);
              
              test = p.test(solution);
              
              System.out.println("Testing: "+test);
             
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
