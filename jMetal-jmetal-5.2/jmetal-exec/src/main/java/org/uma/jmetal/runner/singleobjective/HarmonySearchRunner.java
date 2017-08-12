package org.uma.jmetal.runner.singleobjective;

import co.edu.unicauca.problem.cross_validation.AbstractCrossValidationEvaluator;
import co.edu.unicauca.util.sumary.SumaryFile;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.harmonySearch.HarmonySearchBuilder;
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

    /**
     * Pruebas de Ejecucion en Paralelo
     */
    private static final int DEFAULT_NUMBER_OF_EXPERIMENTS = 2;

    public static void main(String[] args) throws Exception {

        List problemsNames = new ArrayList();
       /* problemsNames.add("co.edu.unicauca.problem.cross_validation.Iris");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.Cardiotocography");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.SPECTF");*/
        problemsNames.add("co.edu.unicauca.problem.cross_validation.Banknote");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.blood");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.car");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.chart");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.connectionist");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.contraceptive");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.dermatology");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.diabetes");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.ecoli");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.fertility");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.glass");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.haberman");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.indian");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.ionosphere");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.leaf");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.libras");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.optdigits");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.seeds");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.shuttle");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.vertebral2C");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.vertebral3C");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.wdbc");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.yeast");
        problemsNames.add("co.edu.unicauca.problem.cross_validation.zoo");


        /*Aux to Experiments results*/
        List accurracyList = new ArrayList();
        List executionTimeList = new ArrayList();
        List<DoubleSolution> solutionList = new ArrayList<>();

        Algorithm<DoubleSolution> algorithm = null;
        SolutionListEvaluator<DoubleSolution> evaluator;
        evaluator = new SequentialSolutionListEvaluator<>();//Cambiar para Usar el Evaluador del getProblem().Evaluador();
        DoubleProblem problem = null;
        for (int i = 0; i < DEFAULT_NUMBER_OF_EXPERIMENTS; i++) {
            JMetalRandom randomGenerator = JMetalRandom.getInstance();
            randomGenerator.setSeed(i);

            problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemsNames.get(0));

            algorithm = new HarmonySearchBuilder((DoubleProblem) problem)
                    .setBW(0.04)
                    .setHMCR(0.9)
                    .setHMS(10)
                    .setPAR(0.4)
                    .setMaxEvaluations(600)
                    .setRandomGenerator(randomGenerator)
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

        SumaryFile.SumaryHS(algorithm, null, (AbstractCrossValidationEvaluator) problem, solutionList, accurracyList, executionTimeList, problem);
        evaluator.shutdown();
        /*Clear for next problem*/
        accurracyList.clear();
        executionTimeList.clear();
        solutionList.clear();
    }

}
