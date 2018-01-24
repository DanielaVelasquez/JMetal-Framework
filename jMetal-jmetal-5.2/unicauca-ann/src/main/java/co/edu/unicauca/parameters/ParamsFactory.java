package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.JMetalException;

public class ParamsFactory
{
    private static MOSParams mos ;

    private static IHDELSParameters ihdels;
    
    public static Algorithm getAlgorithm(int algorithmId,
                  DataBaseConnection connection,AbstractELMEvaluator.EvaluatorType type,
                  DoubleProblem problem, String nameAlgorithm, String configuration) throws Exception
    {
        switch(nameAlgorithm)
        {
            case "MOS":
                mos = new MOSParams(algorithmId, connection, type, problem);
                return mos.getAlgorithm(configuration);
            case "IHDELS":
                ihdels = new IHDELSParameters(algorithmId, connection, type, problem);
                return ihdels.getAlgorithm(configuration);
            default:
                 throw new JMetalException("Algorithm "+nameAlgorithm+" not exists");
        }
        
    }
}
