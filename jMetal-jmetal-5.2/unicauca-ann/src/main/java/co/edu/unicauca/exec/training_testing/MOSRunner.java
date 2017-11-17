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
        
        problemName = "co.edu.unicauca.problem.cross_validation.Banknote";
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        
        
                
        MTSTecnique mts_exec = new MTSTecnique(new MultipleTrajectorySearchBuilder(problem)
                        .setLocalSearchTest(3)
                        .setLocalSearch(75)
                        .setNumberOfForeground(5)
                        .setPopulationSize(5)
                        .setLocalSearchBest(100)
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
                        .setMaxEvaluations(600)
                        .build(); 
            rnd.setSeed(10);
            System.out.println("-------------------------------------------------");
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;

            DoubleSolution solution = (DoubleSolution) algorithm.getResult();
            long computingTime = algorithmRunner.getComputingTime() ;

            System.out.println("Total execution time: " + computingTime + "ms");
            System.out.println("Objective "+(1-solution.getObjective(0)));
            AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
            System.out.println("Testing: "+p.test(solution));
        }
    }
}
