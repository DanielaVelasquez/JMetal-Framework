package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 * Creates algorithms as needed to run elm problems
 */
public class AlgorithmFactory 
{
    private final static DifferentialEvolutionFactory de = new DifferentialEvolutionFactory();
    private final static HillClimbingFactory hc = new HillClimbingFactory();
    private final static IHDELSFactory ihdels = new IHDELSFactory();
    private final static MOSFactory mos = new MOSFactory();
    private final static MTSFactory mts = new MTSFactory();
    private final static SolisAndWetsFactory sw = new SolisAndWetsFactory();
    private final static RandomFactory random = new RandomFactory();
    
    public static Algorithm getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType,
                                  DoubleProblem problem)
    {
        AlgorithmBuilder builder = null;
        switch(name)
        {
            case "DECC_G":
                return de.getAlgorithm(name, evaluatorType, problem).build();
            case "DEUnicauca":
                return de.getAlgorithm(name, evaluatorType, problem).build();
            case "SaNSDE":
                return de.getAlgorithm(name, evaluatorType, problem).build();
            case "MemeticED":
                return de.getAlgorithm(name, evaluatorType, problem).build();
            case "MOS":
                return mos.getAlgorithm(name, evaluatorType, problem).build();
            case "MTS_LS1":
                return mts.getAlgorithm(name, evaluatorType, problem).build();
            case "SolisAndWets":
                return sw.getAlgorithm(name, evaluatorType, problem).build();
            case "IHDELS":
                return ihdels.getAlgorithm(name, evaluatorType, problem).build();
            case "SaDE":
                return de.getAlgorithm(name, evaluatorType, problem).build();
            case "HillClimbing":
                return hc.getAlgorithm(name, evaluatorType, problem).build();
            case "Random":
                return random.getAlgorithm(name, evaluatorType, problem).build();
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        }
    }
}
