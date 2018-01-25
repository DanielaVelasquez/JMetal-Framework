package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.algorithm.MOSFactory;
import co.edu.unicauca.factory.algorithm.MTSFactory;
import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
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
    private MTSFactory mtsFactory ;
    
    private int FE;
    private double E;
    
    private SolisAndWetsParameters solisAndWetsParameters;
    private SolisAndWetsBuilder solisAndWetsBuilder;

    public MOSParameters( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem,
               AbstractParametersFactory parametersFactory) throws Exception {
        super(algorithmId, connection, type, problem, parametersFactory);
        factory = new MOSFactory(parametersFactory);
        solisAndWetsParameters = new SolisAndWetsParameters(parametersFactory);
        mtsFactory = new  MTSFactory(parametersFactory);
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
