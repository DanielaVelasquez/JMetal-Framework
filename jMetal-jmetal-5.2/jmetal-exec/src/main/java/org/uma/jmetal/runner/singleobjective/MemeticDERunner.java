package org.uma.jmetal.runner.singleobjective;

import co.edu.unicauca.problem.cross_validation.CrossValidationEvaluator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.LocalOptimizer.OptSimulatedAnnealing;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 */
public class MemeticDERunner {

    public static void main(String[] args) throws Exception {

        JMetalRandom randomGenerator = JMetalRandom.getInstance();
        randomGenerator.setSeed(1);

        /**
         * Revizar El nuemero de Efos -->PAra le optimizador local Falta
         * implementar el operador de Crossover "DE/current-to-pbes"
         */
        DoubleProblem problem;
        Algorithm<DoubleSolution> algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;
        OptSimulatedAnnealing localSearch;
        SolutionListEvaluator<DoubleSolution> evaluator;

        String problemName = "co.edu.unicauca.problem.cross_validation.Iris";
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

        evaluator = new SequentialSolutionListEvaluator<>();

        crossover = new DifferentialEvolutionCrossover(0.5, 0.5, "current-to-best/1");
        //crossover.setRandom(randomGenerator);//Evitaria el Problema del patron singleton

        selection = new DifferentialEvolutionSelection();
        // selection.setRandom(randomGenerator);//Evitar Patron singleton

        Comparator<DoubleSolution> comparator;
        comparator = new ObjectiveComparator<>(0);
        localSearch = new OptSimulatedAnnealing(10, null, comparator, problem);
        localSearch.setRandom(randomGenerator);

        algorithm = new MemeticEDBuilder((DoubleProblem) problem)
                .setCrossover(crossover)
                .setSelection(selection)
                .setSolutionListEvaluator(evaluator)
                .setMaxEvaluations(600)
                .setPopulationSize(50)
                .setLocalSearch(localSearch)
                .setRandom(randomGenerator)
                .build();

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();//Esto es u hilo similar a lo ya implementado en HS-ELM Verificar la ejecucion en paralelo

        DoubleSolution solution = algorithm.getResult();

        long computingTime = algorithmRunner.getComputingTime();

        List<DoubleSolution > population = new ArrayList<>(1);

        population.add(solution);

        new SolutionListOutput(population)
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
                .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
                .print();

        JMetalLogger.logger.info(
                "Total execution time: " + computingTime + "ms");
        JMetalLogger.logger.info(
                "Objectives values have been written to file FUN.tsv");
        JMetalLogger.logger.info(
                "Variables values have been written to file VAR.tsv");
        evaluator.shutdown();

        System.out.println("Total execution time: " + computingTime + "ms");
        double a = ((CrossValidationEvaluator) problem).test(solution);

        System.out.println("Accuracy " + a);

    }

}
