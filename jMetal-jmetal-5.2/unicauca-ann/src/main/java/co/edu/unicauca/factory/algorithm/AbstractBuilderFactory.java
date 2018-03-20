package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.Comparator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.comparator.FrobeniusComparator;

/**
 * Abstract factory to create builder algorithms, with appropiate configuration
 * for elm problems. 
 */
public abstract class AbstractBuilderFactory 
{
    /**
     * Default comparator
     */
    protected final static Comparator<DoubleSolution> COMPARATOR =
            new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
    /**
     * Value to penalize an individual
     */
    protected final static double PENALIZE_VALUE = 0;
    /**
     * Number of evaluations for trainig testing problems
     */
    protected final static int EVALUATIONS_TT = 3000;
    /**
     * Number of evaluations for cross validation problems
     */
    protected final static int EVALUATIONS_CV = 300;
    
    protected AbstractELMEvaluator.EvaluatorType evaluatorType;
    
    protected final AbstractParametersFactory parametersFactory;
    
    public AbstractBuilderFactory(AbstractParametersFactory parametersFactory)
    {
        this.parametersFactory = parametersFactory;
    }
    public abstract AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType,
                                           DoubleProblem problem) throws Exception;
    
    protected abstract void loadAlgorithmValues(String name, 
                            AbstractELMEvaluator.EvaluatorType evaluatorType) throws Exception;
}
