package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.HillClimbingFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbingBuilder;
import org.uma.jmetal.operator.impl.localsearch.BoundedUniformConvultion;
import org.uma.jmetal.problem.DoubleProblem;

public class HillClimbingParams extends ParamsFinder
{
    private double mean;
    private double standarDeviation;

    public HillClimbingParams( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new HillClimbingFactory();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       mean =  values[0][configuration[0]];
       standarDeviation =  values[1][configuration[1]];
       builder = factory.getAlgorithm("HillClimbing", type, problem);
       return ((HillClimbingBuilder) builder)
                            .setTweak(new BoundedUniformConvultion(mean, standarDeviation))
                            .build();
    }   
}
