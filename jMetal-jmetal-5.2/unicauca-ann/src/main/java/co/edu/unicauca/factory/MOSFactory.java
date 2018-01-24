package co.edu.unicauca.factory;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSLS1Tecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 *Factory to MOS algorithms builders, with configuration needed
 * for elm problem. 
 */
public class MOSFactory extends AbstractFactory
{
    
    private final static  MTSFactory mtsFactory = new MTSFactory();
    private final static SolisAndWetsFactory solisAndWetsFactory = new SolisAndWetsFactory();
    
    private final static int FE_MOS = 75;//200;
    private final static double E_MOS = 0.4;//0.4;
    
    
    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, DoubleProblem problem)
    {
        this.evaluatorType = evaluatorType;
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        switch (name)
        {
            case "MOS":
                builder = this.getMOS(evaluations, problem);
                break;
        } 
        return builder;
    }
    
    private AlgorithmBuilder getMOS(int evaluations, DoubleProblem problem)
    {
        MTSLS1Tecnique mtsls1_exec = new MTSLS1Tecnique((MTS_LS1Builder) mtsFactory.getAlgorithm("MTS_LS1", this.evaluatorType, problem));
        
        SolisAndWetsTecnique sw_exec1 = new SolisAndWetsTecnique((SolisAndWetsBuilder) solisAndWetsFactory.getAlgorithm("SolisAndWets", this.evaluatorType, problem));
        return new MOSBuilder(problem)
                            .addTechnique(mtsls1_exec)
                            .addTechnique(sw_exec1)
                            .setFE(FE_MOS)
                            .setE(E_MOS)
                            .setMaxEvaluations(evaluations)
                            .setComparator(COMPARATOR)
                            .setPenalizeValue(PENALIZE_VALUE);
    }
}
