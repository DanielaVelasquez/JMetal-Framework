  package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.IHDELSFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.ihdels.IHDELSBuilder;
import org.uma.jmetal.problem.DoubleProblem;

public class IHDELSParams extends ParamsFinder
{
    private int FE_DE;
    private int FE_LS;
    private double a;
    private double b;
    private int restart;
    private double threshold;

    public IHDELSParams( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new IHDELSFactory();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       FE_DE = (int) values[0][configuration[0]];
       FE_LS = (int) values[1][configuration[1]];
       a = values[2][configuration[2]];
       b = values[3][configuration[3]];
       restart = (int) values[4][configuration[4]];
       threshold = values[5][configuration[5]];
       builder = factory.getAlgorithm("IHDELS", type, problem);
       return ((IHDELSBuilder) builder)
                            .setFE_DE(FE_DE)
                            .setFE_LS(FE_LS)
                            .setSearchDomain(a, b)
                            .setReStart(restart)
                            .setThreshold(threshold)
                            .build();
    }   
}
