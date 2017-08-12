/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.exec.cross_validation;

import co.edu.unicauca.problem.cross_validation.Iris;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmRunner;

/**
 *
 * @author danielavelasquezgarzon
 */
public class IrisExperiment {
    private static final int RUNS = 30;

  public static void main(String[] args) throws IOException {
    

    List<DoubleProblem> problemList = new ArrayList<>();
    problemList.add(new Iris());
    
    List<Algorithm> algorithmList = configureAlgorithmList(problemList);
    AlgorithmRunner algorithmRunner;

    for(int i = 0; i < RUNS; i++)
    {
        for(Algorithm algorithm: algorithmList)
        {
            algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;
        }
    }

    
  }

 
  static List<Algorithm> configureAlgorithmList(List<DoubleProblem> problemList) 
  {
    List<Algorithm> algorithms = new ArrayList<>();
    
    
    
      for (int i = 0; i < problemList.size(); i++) 
      {
        Algorithm algorithm = new DECC_GBuilder((DoubleProblem) problemList.get(i))
                                                        .setCycles(2)
                                                        .setPopulationSize(20)
                                                        .setSubcomponets(5)
                                                        .setFEs(60)
                                                        .setwFes(60)
                                                        .build();
                
        
        algorithms.add(algorithm);
      }

      for (int i = 0; i < problemList.size(); i++) 
      {
        Algorithm algorithm = new MemeticEDBuilder((DoubleProblem) problemList.get(i))
                              .setMaxEvaluations(9950)
                              .build();
                                  
                
        
        algorithms.add(algorithm);
      }

      

    return algorithms;
  }
    
}
