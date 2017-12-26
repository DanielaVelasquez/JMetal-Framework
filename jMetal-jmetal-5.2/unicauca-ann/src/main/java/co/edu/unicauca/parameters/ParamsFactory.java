package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.JMetalException;

public class ParamsFactory
{
    private static MOSParams mos ;
    private static SolisAndWetsParams solisAndWets;
    private static SaNSDEParams sansde;
    private static SaDEParams sade;
    private static DEUnicaucaParams de;
    private static HillClimbingParams hc;
    private static IHDELSParams ihdels;
    
    public static Algorithm getAlgorithm(int algorithmId,
                  DataBaseConnection connection,AbstractELMEvaluator.EvaluatorType type,
                  DoubleProblem problem, String nameAlgorithm, String configuration) throws Exception
    {
        switch(nameAlgorithm)
        {
            case "MOS":
                mos = new MOSParams(algorithmId, connection, type, problem);
                return mos.getAlgorithm(configuration);
            case "SolisAndWets":
                solisAndWets = new SolisAndWetsParams(algorithmId, connection, type, problem);
                return solisAndWets.getAlgorithm(configuration);
            case "SaNSDE":
                sansde = new SaNSDEParams(algorithmId, connection, type, problem);
                return sansde.getAlgorithm(configuration);
            case "SaDE":
                sade = new SaDEParams(algorithmId, connection, type, problem);
                return sade.getAlgorithm(configuration);
            case "DEUnicauca":
                de = new DEUnicaucaParams(algorithmId, connection, type, problem);
                return de.getAlgorithm(configuration);
            case "HillClimbing":
                hc = new HillClimbingParams(algorithmId, connection, type, problem);
                return hc.getAlgorithm(configuration);
            case "IHDELS":
                ihdels = new IHDELSParams(algorithmId, connection, type, problem);
                return ihdels.getAlgorithm(configuration);
            default:
                 throw new JMetalException("Algorithm "+nameAlgorithm+" not exists");
        }
        
    }
}
