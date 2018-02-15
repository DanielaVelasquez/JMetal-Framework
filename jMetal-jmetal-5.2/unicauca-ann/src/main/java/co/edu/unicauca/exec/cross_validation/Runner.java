package co.edu.unicauca.exec.cross_validation;

import co.edu.unicauca.factory.algorithm.AlgorithmFactory;
import co.edu.unicauca.factory.parameters.FileParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Runner 
{    
    private static final int EXECUTIONS = 1;
    public static void main(String[] args) throws Exception 
    {
        JMetalRandom rnd = JMetalRandom.getInstance();
        
        DoubleProblem problem;
        Algorithm algorithm;
        String problemName ;
        String referenceParetoFront = "" ;
        if (args.length == 1) {
          problemName = args[0];
        } else if (args.length == 2) {
          problemName = args[0] ;
          referenceParetoFront = args[1] ;
        } else {
          problemName = "co.edu.unicauca.problem.cross_validation.Iris";
        }
        
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        
        for(int i = 0; i < EXECUTIONS;i++)
        {
            AlgorithmFactory factory = new AlgorithmFactory(new FileParametersFactory("src/resources-parameters", "parametersClasification"));
            algorithm = factory.getAlgorithm("IHDELS", AbstractELMEvaluator.EvaluatorType.CV, problem);
            rnd.setSeed(i+1);            
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            DoubleSolution solution = (DoubleSolution) algorithm.getResult();
            long computingTime = algorithmRunner.getComputingTime();
            AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
            double train = (solution.getObjective(0));
            double test = p.test(solution);
            System.out.printf("Training: %6.2f   Testing: %6.2f   Computing Time: %d", train, test, computingTime);
        }
    } 
}