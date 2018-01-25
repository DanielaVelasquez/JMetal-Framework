package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 *Factory to Solis and Wets algorithms builders, with configuration needed
 * for elm problem. 
 */
public class SolisAndWetsFactory extends AbstractBuilderFactory
{
    private final static double RHO = 0.5;//0.7;
    private final static int NEIGHBORHOOD = 4; //8;

    public SolisAndWetsFactory(AbstractParametersFactory parametersFactory) {
        super(parametersFactory);
    }

    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, 
            DoubleProblem problem)
    {
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        switch(name)
        {
            case "SolisAndWets":
                builder = this.getSolisAndWets(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        }
        return builder;
    }
    
    private AlgorithmBuilder getSolisAndWets(int evaluations, DoubleProblem problem)
    {
        return new  SolisAndWetsBuilder(problem)
                        .setRho(RHO)
                        .setSizeNeighborhood(NEIGHBORHOOD)
                        .setPenalizeValue(PENALIZE_VALUE)
                        .setMaxEvaluations(evaluations)
                        .setComparator(COMPARATOR);
    }
    
}
