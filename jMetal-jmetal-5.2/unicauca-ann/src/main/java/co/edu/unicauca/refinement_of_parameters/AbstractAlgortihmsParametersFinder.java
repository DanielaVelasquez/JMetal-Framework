package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.factory.algorithm.AbstractBuilderFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

public abstract class AbstractAlgortihmsParametersFinder 
{
   
    protected AbstractBuilderFactory factory;
    protected AlgorithmBuilder builder;
    
    public abstract AlgorithmBuilder configureAlgorithm(int configuration[], 
                                                        int index, 
                                                        double values[][],
                                                        AbstractELMEvaluator.EvaluatorType type,
                                                        DoubleProblem problem);
    public abstract int getNumberParameters();
}
