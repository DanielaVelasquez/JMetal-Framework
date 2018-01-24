package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.DifferentialEvolutionFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DEUnicaucaBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.DoubleProblem;

public class DEUnicaucaParams extends AbstractMetaHeuristicParametersFinder
{
    private double cr ;
    private double f ;

    public DEUnicaucaParams( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new DifferentialEvolutionFactory();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       cr = values[0][configuration[0]];
       f = values[1][configuration[1]];
       
       
       builder = factory.getAlgorithm("DEUnicauca", type, problem);
       return ((DEUnicaucaBuilder) builder)
                            .setCrossover(new DifferentialEvolutionCrossover(cr, f, "rand/1/bin"))
                            .build();
    }   
}
