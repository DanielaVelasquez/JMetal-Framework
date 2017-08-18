/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.exec.training_testing;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author danielavelasquezgarzon
 */
public abstract class TrainingTestingExperiment {
  private static final int RUNS = 30  ;

  public TrainingTestingExperiment(String problemName) throws IOException {
    
    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
    
    JMetalRandom rnd = JMetalRandom.getInstance();
        
    
    List<Algorithm> algorithmList = configureAlgorithmList(problem);
    AlgorithmRunner algorithmRunner;
    
    File file = new File(problemName);
    BufferedWriter bw = new BufferedWriter(new FileWriter(problemName));
    
    for(Algorithm algorithm: algorithmList)
    {
        for(int seed = 0; seed < RUNS; seed++)
        {
            rnd.setSeed(seed);
            
            algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

            long computingTime = algorithmRunner.getComputingTime() ;
            DoubleSolution solution = (DoubleSolution) algorithm.getResult();
            AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
            //Algoritmo - Tiempo - Problema - Train - Test - seed
            String line = algorithm.getName() + " - " + computingTime + " - "+problemName +" - "+(1-solution.getObjective(0)) + " - " + p.test(solution) +" - "+seed+"\n";
            System.out.println(algorithm.getName() + " - " + computingTime + " - "+problemName +" - "+(1-solution.getObjective(0)) + " - " + p.test(solution) +" - "+seed);
            bw.write(line);
            
        }
        
    }
    bw.close();
    
  }

 
  static List<Algorithm> configureAlgorithmList(DoubleProblem problem) 
  {
    List<Algorithm> algorithms = new ArrayList<>();
    
    Algorithm algorithm = new MemeticEDBuilder(problem)
                        .setMaxEvaluations(3000)
                        .setPopulationSize(10)
                        .build();
                                  
                
        
    //algorithms.add(algorithm);
    
      
     algorithm = new DECC_GBuilder(problem)
                        .setCycles(3)
                        .setPopulationSize(10)
                        .setSubcomponets(10)
                        .setFEs(6)
                        .setwFes(9)
                        .build();


    algorithms.add(algorithm);
      

    
      

      

    return algorithms;
  }
    
}
