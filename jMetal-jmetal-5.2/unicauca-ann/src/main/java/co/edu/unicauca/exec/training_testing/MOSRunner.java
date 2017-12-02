package co.edu.unicauca.exec.training_testing;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.HashMap;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSHRH;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSLS1Tecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSTecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.FitnessComparator;
import org.uma.jmetal.util.comparator.FrobeniusComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class MOSRunner 
{    
    private static int EXECUTIONS = 1;
    public static void main(String[] args) throws Exception 
    {
        JMetalRandom rnd = JMetalRandom.getInstance();
        
        DoubleProblem problem;
        MOSHRH algorithm;
        String problemName ;
        
        problemName = "co.edu.unicauca.problem.training_testing.Banknote";
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        
        
                
        MTSLS1Tecnique mts_exec = new MTSLS1Tecnique(new MTS_LS1Builder(problem)
                        .setPopulationSize(5)
                        .setBonus1(10)
                        .setBonus2(1));
                    
        SolisAndWetsTecnique sw_exec = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem)
                        .setRho(0.5)
                        .setSizeNeighborhood(12));
        
        for(int i = 0; i < EXECUTIONS;i++)
        {
            algorithm = new MOSBuilder(problem)
                        .addTecnique(mts_exec)
                        .addTecnique(sw_exec)
                        .setFE(75)
                        .setE(0.15)
                        .setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
                        .setMaxEvaluations(3000)
                        .build(); 
            rnd.setSeed(1);
            System.out.println("-------------------------------------------------");
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;

            DoubleSolution solution = (DoubleSolution) algorithm.getResult();
            long computingTime = algorithmRunner.getComputingTime() ;

            System.out.println("Total execution time: " + computingTime + "ms");
            System.out.println("Objective "+(solution.getObjective(0)));
            AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
            System.out.println("Testing: "+p.test(solution));
        }
    }
}
