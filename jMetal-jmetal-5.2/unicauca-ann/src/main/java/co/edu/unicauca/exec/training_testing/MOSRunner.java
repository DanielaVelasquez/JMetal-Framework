package co.edu.unicauca.exec.training_testing;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.HashMap;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSHRH;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSExec;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsExec;
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
        
        problemName = "co.edu.unicauca.problem.training_testing.Iris";
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        
        HashMap<String, Object> mts_atributes = new MultipleTrajectorySearchBuilder(problem)
                                                .getConfiguration();
                
        MTSExec mts_exec = new MTSExec(mts_atributes);
        SolisAndWetsExec sw_exec = new SolisAndWetsExec(null);
        
        for(int i = 0; i < EXECUTIONS;i++)
        {
            algorithm =  new   MOSBuilder(problem)
                            .addTecnique(mts_exec)
                            .addTecnique(sw_exec)
                            .setFE(300)
                            .setMaxEvaluations(3000)
                            .build();
            rnd.setSeed(i);
            System.out.println("-------------------------------------------------");
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;

            DoubleSolution solution = (DoubleSolution) algorithm.getResult();
            long computingTime = algorithmRunner.getComputingTime() ;

            System.out.println("Total execution time: " + computingTime + "ms");
            System.out.println("Objective "+(1-solution.getObjective(0)));
            AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
            System.out.println("Testing: "+p.test(solution));
            System.out.println("Total evaluations: "+p.total);
        }
    }
}
