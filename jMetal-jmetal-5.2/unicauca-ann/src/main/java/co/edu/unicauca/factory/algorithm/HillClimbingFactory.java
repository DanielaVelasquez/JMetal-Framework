
package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbingBuilder;
import org.uma.jmetal.operator.impl.localsearch.BoundedUniformConvultion;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 *Factory to hill climbing algorithms builders, with configuration needed
 * for elm problem. 
 */
public class HillClimbingFactory extends AbstractBuilderFactory
{
    
    private static final double  MEAN_HC = 0.45; //0.5;
    private static final double STANDAR_DEVIATION_HC = 0.4;//0.2;

    public HillClimbingFactory(AbstractParametersFactory parametersFactory) {
        super(parametersFactory);
    }
    
    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, 
            DoubleProblem problem)
    {
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        switch (name)
        {
            case "HillClimbing":
                builder = this.getHillClimbing(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        }
        return builder;
    }
    
    private AlgorithmBuilder getHillClimbing(int evaluations, DoubleProblem problem)
    {
        return new HillClimbingBuilder(problem)
                            .setTweak(new BoundedUniformConvultion(MEAN_HC, STANDAR_DEVIATION_HC))
                            .setMaxEvaluations(evaluations)
                            .setComparator(COMPARATOR)
                            .setPenalizeValue(PENALIZE_VALUE);
    }
    
}
