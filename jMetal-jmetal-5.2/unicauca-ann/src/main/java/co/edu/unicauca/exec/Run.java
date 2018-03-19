package co.edu.unicauca.exec;

import co.edu.unicauca.factory.algorithm.AlgorithmFactory;
import co.edu.unicauca.factory.parameters.FileParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Run {

    private static int numberOfExecutions = 0;
    private static String typeProblem = "";
    private static String datasetName = "";
    private static String problemName = "";
    private static String algorithmName = "";
    private static String parametersFile = "";
    private static boolean isClasification;
    private static AbstractELMEvaluator.EvaluatorType evaluator = null;
    private static DoubleProblem problem;
    private static Algorithm algorithm;

    public static void main(String[] args) throws Exception {
        JMetalRandom rnd = JMetalRandom.getInstance();

        readConfigurationExecution();

        for (int i = 0; i < numberOfExecutions; i++) {
            AlgorithmFactory factory = new AlgorithmFactory(new FileParametersFactory("src/resources-parameters", parametersFile));
            algorithm = factory.getAlgorithm(algorithmName, evaluator, problem);
            rnd.setSeed(i + 1);
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            DoubleSolution solution = (DoubleSolution) algorithm.getResult();
            long computingTime = algorithmRunner.getComputingTime();
            AbstractELMEvaluator p = (AbstractELMEvaluator) problem;
            double train = (solution.getObjective(0));
            double test = p.test(solution);
            System.out.printf(" %d) Training: %6.2f   Testing: %6.2f   Computing Time: %d \n", i + 1, train, test, computingTime);
        }
        System.out.println("");
    }

    private static void readConfigurationExecution() {
        numberOfExecutions = readNumberOfExecutions();
        typeProblem = readTypeOfProblem();
        evaluator = getEvaluator(typeProblem);
        readDataSet();
        getTypeOfProblem();
        getAlgorithm();
        System.out.println("----------------------------------------------------------");
        System.out.println("Algorithm " + algorithmName);
        if (isClasification) {
            System.out.println("Data set " + datasetName + ", a clasification problem");
        } else {
            System.out.println("Data set " + datasetName + ", a regresion problem");
        }
        System.out.println("Validation " + typeProblem);
        System.out.println("Number of executions " + numberOfExecutions);
        System.out.println("----------------------------------------------------------");
    }

    private static void getAlgorithm() {
        algorithmName = "";
        boolean valid = false;
        do {
            try {
                AlgorithmFactory factory = new AlgorithmFactory(new FileParametersFactory("src/resources-parameters", parametersFile));
                algorithmName = readString("Which algorithm you want to use?");
                algorithm = factory.getAlgorithm(algorithmName, evaluator, problem);
                valid = true;
            } catch (Exception ex) {
                System.out.println("The algorithm " + algorithmName + " does not exist");
            }
        } while (!valid);
    }

    private static void getTypeOfProblem() {
        List<String> regresionDataSet = new ArrayList();
        regresionDataSet.add("AutoMpg");
        regresionDataSet.add("AutoPrice");
        regresionDataSet.add("BodyFat");
        regresionDataSet.add("Cpu");
        regresionDataSet.add("Housing");
        regresionDataSet.add("Sensory");
        regresionDataSet.add("Servo");
        regresionDataSet.add("Veteran");

        isClasification = true;
        if (regresionDataSet.contains(datasetName)) {
            parametersFile = "parametersRegresion";
            isClasification = false;
        } else {
            parametersFile = "parametersClasification";
        }

    }

    private static void readDataSet() {

        boolean valid = false;
        do {
            try {
                datasetName = readString("What is the name of the dataset?");
                datasetName = ucFirst(datasetName);
                problemName = "co.edu.unicauca.problem." + typeProblem + "." + datasetName;
                problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);
                valid = true;
            } catch (Exception e) {
                System.out.println("Dataset " + datasetName + " is not a valid name. Look the user manual to check for a valid dataset's name");
            }

        } while (!valid);
    }

    private static AbstractELMEvaluator.EvaluatorType getEvaluator(String typeProblem) {
        if (typeProblem.equalsIgnoreCase("cross_validation")) {
            return AbstractELMEvaluator.EvaluatorType.CV;
        } else {
            return AbstractELMEvaluator.EvaluatorType.TT;
        }
    }

    private static String readTypeOfProblem() {
        String type = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            type = readString("What type of evaluation you want to use?\n"
                    + "- Use CV for cross-validation\n"
                    + "- Use TT for training testing");
            if (!type.equalsIgnoreCase("TT") && !type.equalsIgnoreCase("CV")) {
                System.out.println("There are only two types of evaluation");
            }
        } while (!type.equalsIgnoreCase("TT") && !type.equalsIgnoreCase("CV"));

        if (type.equalsIgnoreCase("CV")) {
            type = "cross_validation";
        } else {
            type = "training_testing";
        }
        return type;
    }

    private static String readString(String message) {
        String value = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println(message);
            try {
                value = br.readLine();
                if (value.length() == 0) {
                    System.out.println("The string is empty");
                }
            } catch (Exception ex) {

            }
        } while (value.length() == 0);
        return value;
    }

    private static int readNumberOfExecutions() {
        int number = -1;
        do {
            number = readNumber("How many times you wish to execute the algorithm?");
            if (number <= 0) {
                System.out.println("The number of executions must be a positive value greater than 0");
            }
        } while (number <= 0);
        return number;
    }

    private static int readNumber(String message) {
        int number = -1;
        boolean isNumber = false;
        String value = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println(message);
            try {
                value = br.readLine();
                number = Integer.parseInt(value);
                isNumber = true;
            } catch (Exception ex) {
                System.out.println("Value: " + value + " must be a number");
            }

        } while (!isNumber);
        return number;
    }

    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
}
