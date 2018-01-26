
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
    
    private double PROBABILITY ; 
    private double RADIUS;

    public HillClimbingFactory(AbstractParametersFactory parametersFactory) {
        super(parametersFactory);
    }
    
    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, 
            DoubleProblem problem) throws Exception
    {
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        this.loadAlgorithmValues(name, evaluatorType);
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
                            .setTweak(new BoundedUniformConvultion(PROBABILITY, RADIUS))
                            .setMaxEvaluations(evaluations)
                            .setComparator(COMPARATOR)
                            .setPenalizeValue(PENALIZE_VALUE);
    }

    @Override
    protected void loadAlgorithmValues(String name, AbstractELMEvaluator.EvaluatorType evaluatorType) throws Exception 
    {
        
        PROBABILITY  = parametersFactory.getValue("PROBABILITY", evaluatorType, "HillClimbing") ; 
        RADIUS = parametersFactory.getValue("RADIUS", evaluatorType, "HillClimbing") ;

    }
    
}
