package co.edu.unicauca.exec.cross_validation.harmony;

import co.edu.unicauca.problem.cross_validation.AbstractCrossValidationEvaluator;
import co.edu.unicauca.util.sumary.SumaryFile;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.harmonySearch.IHSBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 */
public class ImprovedHSRunner {

    private static final int DEFAULT_NUMBER_OF_EXPERIMENTS = 5;

    public static void main(String[] args) throws Exception {

        List problemsNames = new ArrayList();
        problemsNames.add("co.edu.unicauca.problem.cross_validation.Iris");
        //  problemsNames.add("co.edu.unicauca.problem.cross_validation.vowel");

        for (int j = 0; j < problemsNames.size(); j++) {

            /*Aux to Experiments results*/
            List accurracyList = new ArrayList();
            List executionTimeList = new ArrayList();
            List<DoubleSolution> solutionList = new ArrayList<>();

            DoubleProblem problem;
            problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemsNames.get(0));
            Algorithm<DoubleSolution> algorithm = null;
            SolutionListEvaluator<DoubleSolution> evaluator;
            evaluator = new SequentialSolutionListEvaluator<>();

            for (int i = 0; i < DEFAULT_NUMBER_OF_EXPERIMENTS; i++) {

                /**
                 * Set a Seed for the experiment
                 */
                JMetalRandom randomGenerator = JMetalRandom.getInstance();
                randomGenerator.setSeed(i);

                algorithm = new IHSBuilder((DoubleProblem) problem)
                        .setHMCR(0.9)
                        .setHMS(10)
                        .setPARMIN(0.01)//Reset this par, try set before firts execution
                        .setPARMAX(0.5)/*There are a problem, in real experimemnts the value of PAR is set To 0.666 this is a better form that set a  
                         variable values in IHS and GHS*/
                        .setBWMIN(0.01)
                        .setBWMAX(0.2)
                        .setMaxEvaluations(600)
                        .setSolutionListEvaluator(evaluator)
                        .build();

                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                        .execute();

                DoubleSolution solution = algorithm.getResult();

                System.out.println("Best Train " + solution.getObjective(0));
                long computingTime = algorithmRunner.getComputingTime();

                List<DoubleSolution> population = new ArrayList<>(1);
                population.add(solution);

                System.out.println("Total execution time: " + computingTime + "ms");
                double a = ((AbstractCrossValidationEvaluator) problem).test(solution);
                executionTimeList.add(computingTime);
                accurracyList.add((Object) a);
                solutionList.add(solution);
                System.out.println(" Accuracy " + a);

                System.gc();
            }

            SumaryFile.SumaryHS(algorithm, null, (AbstractCrossValidationEvaluator) problem, solutionList, accurracyList, executionTimeList, problem,null);
            evaluator.shutdown();
            /*Clear for next problem*/
            accurracyList.clear();
            executionTimeList.clear();
            solutionList.clear();

        }
    }

}
