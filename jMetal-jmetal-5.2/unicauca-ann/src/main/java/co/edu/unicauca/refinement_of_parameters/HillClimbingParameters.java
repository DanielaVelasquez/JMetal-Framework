package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.factory.algorithm.HillClimbingFactory;
import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbingBuilder;
import org.uma.jmetal.operator.impl.localsearch.BoundedUniformConvultion;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

public class HillClimbingParameters extends AbstractAlgortihmsParametersFinder
{
    private double probability;
    private double radius;

    public HillClimbingParameters(AbstractParametersFactory parametersFactory)
    {
        super(parametersFactory);
        factory = new HillClimbingFactory(parametersFactory);
    }

    @Override
    public AlgorithmBuilder configureAlgorithm(int configuration[], 
                                               int index, 
                                               double values[][],
                                               AbstractELMEvaluator.EvaluatorType type,
                                               DoubleProblem problem) throws Exception 
    {
       probability =  values[index][configuration[index]];
       radius =  values[index + 1][configuration[index + 1]];
       builder = factory.getAlgorithm("HillClimbing", type, problem);
       return ((HillClimbingBuilder) builder)
                            .setTweak(new BoundedUniformConvultion(probability, radius));
    }   

    @Override
    public int getNumberParameters() 
    {
        return 2;
    }
}
