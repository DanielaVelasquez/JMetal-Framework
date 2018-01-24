package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.MOSFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.problem.DoubleProblem;

public class MOSParams extends AbstractMetaHeuristicParametersFinder
{
    private int FE;
    private double E;

    public MOSParams( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new MOSFactory();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       FE = (int) values[0][configuration[0]];
       E = values[1][configuration[1]];
       builder = factory.getAlgorithm("MOS", type, problem);
       return ((MOSBuilder) builder)
                            .setE(E)
                            .setFE(FE)
                            .build();
    }   
}
