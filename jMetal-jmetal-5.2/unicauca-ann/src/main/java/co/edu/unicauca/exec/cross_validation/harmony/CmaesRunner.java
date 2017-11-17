package co.edu.unicauca.exec.cross_validation.harmony;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import co.edu.unicauca.problem.training_testing.TrainingTestingEvaluator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.CovarianceMatrixAdaptationEvolutionStrategy;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

/**
 *
 * @author fietp
 */
public class CmaesRunner {

    static int DEFAULT_EFOS = 3000;

    public static void main(String[] args) throws Exception {
        ejecutar(args);

    }

    public static void ejecutar(String[] args) throws IOException {

        System.out.println(" HS Is run");
        List problemsNames = new ArrayList();
        // problemsNames.add("co.edu.unicauca.problem.cross_validation.AutoMpg");
       // problemsNames.add("co.edu.unicauca.problem.training_testing.SPECTF");
        problemsNames.add("co.edu.unicauca.problem.training_testing.Iris");

        //Revisar con detalle
        Algorithm<DoubleSolution> algorithm = null;
        DoubleProblem problem = null;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem((String) problemsNames.get(0));

        algorithm = new CovarianceMatrixAdaptationEvolutionStrategy.Builder(problem, Integer.parseInt(10 + ""), Double.parseDouble(0.2 + ""), DEFAULT_EFOS).build();
        JMetalRandom.getInstance().setRandomGenerator(new JavaRandomGenerator());
        JMetalRandom.getInstance().setSeed(0);
        ((AbstractELMEvaluator) problem).setType_solution(0);
        double test = 0.0;
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();
        DoubleSolution solution = (DoubleSolution) algorithm.getResult();

        System.out.println("Best " + solution.getObjective(0));
        test = ((TrainingTestingEvaluator) problem).test(solution);
        System.out.println("Test " + test);

    }

}
