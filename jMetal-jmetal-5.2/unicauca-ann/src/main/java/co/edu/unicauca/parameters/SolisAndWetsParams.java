package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.SolisAndWetsFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.problem.DoubleProblem;

public class SolisAndWetsParams extends AbstractMetaHeuristicParametersFinder
{
    private int sizeNeighborhood;
    private double rho;

    public SolisAndWetsParams( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new SolisAndWetsFactory();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       rho =  values[0][configuration[0]];
       sizeNeighborhood = (int) values[1][configuration[1]];
       builder = factory.getAlgorithm("SolisAndWets", type, problem);
       return ((SolisAndWetsBuilder) builder)
                            .setRho(rho)
                            .setSizeNeighborhood(sizeNeighborhood)
                            .build();
    }   
}
