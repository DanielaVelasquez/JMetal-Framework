package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.DifferentialEvolutionFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.DoubleProblem;

public class SaDEParams extends ParamsFinder
{
    private double cr1 ;
    private double f1 ;
    private double cr2 ;
    private double f2 ;

    public SaDEParams( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new DifferentialEvolutionFactory();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       cr1 = values[0][configuration[0]];
       cr2 = values[1][configuration[1]];
       f1 = values[2][configuration[2]];
       f2 = values[3][configuration[3]];
       
       builder = factory.getAlgorithm("SaDE", type, problem);
       return ((SaDEBuilder) builder)
                            .setCrossoverOperator(new DifferentialEvolutionCrossover(cr1, f1, "rand/1/bin"))
                            .setCrossoverOperator2(new DifferentialEvolutionCrossover(cr2, f2, "current-to-best/1/bin"))
                            .build();
    }   
}
