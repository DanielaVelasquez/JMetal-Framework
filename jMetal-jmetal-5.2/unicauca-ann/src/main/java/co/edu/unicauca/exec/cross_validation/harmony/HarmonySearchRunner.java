package co.edu.unicauca.exec.cross_validation.harmony;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import co.edu.unicauca.problem.cross_validation.AbstractCrossValidationEvaluator;
import co.edu.unicauca.problem.training_testing.TrainingTestingEvaluator;
import co.edu.unicauca.util.sumary.SumaryFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.harmonySearch.*;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 *
 */
public class HarmonySearchRunner {
    
    private static final int DEFAULT_NUMBER_OF_EXPERIMENTS = 35;//IS OK

    public static void main(String[] args) throws Exception {
        ejecutar(args);
        
    }
    
    public static void ejecutar(String[] args) throws IOException {
        
        System.out.println(" HS Is run");
        List problemsNames = new ArrayList();
        // problemsNames.add("co.edu.unicauca.problem.cross_validation.AutoMpg");
        //  problemsNames.add("co.edu.unicauca.problem.cross_validation.Hill");

      //  problemsNames.add("co.edu.unicauca.problem.cross_validation.Iris");
        //  problemsNames.add("co.edu.unicauca.problem.cross_validation.Dermatology");
         problemsNames.add("co.edu.unicauca.problem.training_testing.Iris");
        //problemsNames.add("co.edu.unicauca.problem.training_testing.Banknote");
        /* problemsNames.add("co.edu.unicauca.problem.cross_validation.Blood");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Cardiotocography");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Car");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Chart");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Connectionist");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Contraceptive");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Dermatology");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Diabetes");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Ecoli");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Fertility");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Glass");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Haberman");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Hayes");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Hill");
    
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Indian");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Ionosphere");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Leaf");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Letter");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Libras");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Optdigits");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Seeds");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.SPECTF");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Shuttle");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Vertebral2C");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Vertebral3C");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Pen");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.QSARBiodegradation");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Wdbc");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Yeast");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Wine");
         problemsNames.add("co.edu.unicauca.problem.cross_validation.Zoo");
         */

 /*Aux to Experiments results*/
        List accurracyList = new ArrayList();
        List executionTimeList = new ArrayList();
        List<DoubleSolution> solutionList = new ArrayList<>();
        
        Algorithm<DoubleSolution> algorithm = null;
        SolutionListEvaluator<DoubleSolution> evaluator;
        evaluator = new SequentialSolutionListEvaluator<>();//Cambiar para Usar el Evaluador del getProblem().Evaluador();
        DoubleProblem problem = null;
        //problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemsNames.get(0));

        for (int j = 0; j < problemsNames.size(); j++) {
            for (int i = 0; i < DEFAULT_NUMBER_OF_EXPERIMENTS; i++) {
                JMetalRandom randomGenerator = JMetalRandom.getInstance();
                randomGenerator.setSeed(9);
                problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemsNames.get(j));
               // ((AbstractCrossValidationEvaluator) problem).setType_solution(1);
                               ((AbstractELMEvaluator) problem).setType_solution(1);

                algorithm = new HSBuilder((DoubleProblem) problem)
                        .setHMS(10)
                        .setHMCR(Double.parseDouble(0.85 + ""))
                        .setPAR(Double.parseDouble(0.35 + ""))
                        .setBW(Double.parseDouble(0.03 + ""))
                        .setMaxEvaluations(300)
                        //.setSolutionListEvaluator(evaluator)
                        .build();
                
                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                        .execute();
                
                DoubleSolution solution = algorithm.getResult();
                
                System.out.println("Best Train " + solution.getObjective(0));
                long computingTime = algorithmRunner.getComputingTime();
                
                System.out.println("Total execution time: " + computingTime + "ms");
                double a = ((TrainingTestingEvaluator) problem).test(solution);

               // double a = ((AbstractCrossValidationEvaluator) problem).test(solution);
                executionTimeList.add(computingTime);
                accurracyList.add((Object) a);
                solutionList.add(solution);
                System.out.println(" Accuracy " + a);
                
                System.gc();
            }

            //   SumaryFile.SumaryHS(algorithm, null, (TrainingTestingEvaluator) problem, solutionList, accurracyList, executionTimeList, problem, null);//           
            SumaryFile.SumaryHS(algorithm, null, (AbstractELMEvaluator) problem, solutionList, accurracyList, executionTimeList, problem, null);
            evaluator.shutdown();
            /*Clear for next problem*/
            accurracyList.clear();
            executionTimeList.clear();
            solutionList.clear();
        }
    }
    
}
