package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.JMetalException;

public class ParametersFactory
{
    private static MOSParameters mos ;

    private static IHDELSParameters ihdels;
    
    private static DECC_GParameters decc_g;
    
    public static Algorithm getAlgorithm(int algorithmId,
                  DataBaseConnection connection,AbstractELMEvaluator.EvaluatorType type,
                  DoubleProblem problem, String nameAlgorithm, String configuration) throws Exception
    {
        switch(nameAlgorithm)
        {
            case "MOS":
                mos = new MOSParameters(algorithmId, connection, type, problem);
                return mos.getAlgorithm(configuration);
            case "IHDELS":
                ihdels = new IHDELSParameters(algorithmId, connection, type, problem);
                return ihdels.getAlgorithm(configuration);
            case "DECC_G":
                decc_g = new DECC_GParameters(algorithmId, connection, type, problem);
                return decc_g.getAlgorithm(configuration);
            default:
                 throw new JMetalException("Algorithm "+nameAlgorithm+" not exists");
        }
        
    }
}
