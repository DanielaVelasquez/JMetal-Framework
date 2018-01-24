package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.MOSFactory;
import co.edu.unicauca.factory.MTSFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSLS1Tecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.problem.DoubleProblem;

public class MOSParameters extends AbstractMetaHeuristicParametersFinder
{
    private final static  MTSFactory mtsFactory = new MTSFactory();
    
    private int FE;
    private double E;
    
    private SolisAndWetsParameters solisAndWetsParameters;
    private SolisAndWetsBuilder solisAndWetsBuilder;

    public MOSParameters( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new MOSFactory();
        solisAndWetsParameters = new SolisAndWetsParameters();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       FE = (int) values[0][configuration[0]];
       E = values[1][configuration[1]];
       builder = factory.getAlgorithm("MOS", type, problem);
       solisAndWetsBuilder = (SolisAndWetsBuilder) solisAndWetsParameters.configureAlgorithm(configuration, 2, values, type, problem);
       SolisAndWetsTecnique sw_technique = new SolisAndWetsTecnique(solisAndWetsBuilder);
       MTSLS1Tecnique mtsls1_exec = new MTSLS1Tecnique((MTS_LS1Builder) mtsFactory.getAlgorithm("MTS_LS1", AbstractELMEvaluator.EvaluatorType.TT, problem));
       return ((MOSBuilder) builder)
                            .setE(E)
                            .setFE(FE)
                            .removeTechniques()
                            .addTechnique(sw_technique)
                            .addTechnique(mtsls1_exec)
                            .build();
    }   
}
