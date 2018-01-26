package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSLS1Tecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 *Factory to MOS algorithms builders, with configuration needed
 * for elm problem. 
 */
public class MOSFactory extends AbstractBuilderFactory
{
    
    private MTSFactory mtsFactory ;
    private SolisAndWetsFactory solisAndWetsFactory ;
    
    private int FE_MOS ;
    private double E_MOS ;

    public MOSFactory(AbstractParametersFactory parametersFactory) {
        super(parametersFactory);
        mtsFactory = new MTSFactory(parametersFactory);
        solisAndWetsFactory = new SolisAndWetsFactory(parametersFactory);
    }
    
    
    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, DoubleProblem problem) throws Exception
    {
        this.evaluatorType = evaluatorType;
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        this.loadAlgorithmValues(name, evaluatorType);
        switch (name)
        {
            case "MOS":
                builder = this.getMOS(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        } 
        return builder;
    }
    
    private AlgorithmBuilder getMOS(int evaluations, DoubleProblem problem) throws Exception
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

    @Override
    protected void loadAlgorithmValues(String name, AbstractELMEvaluator.EvaluatorType evaluatorType) throws Exception {
        
        FE_MOS  = (int) parametersFactory.getValue("FE", evaluatorType, "MOS") ;
        E_MOS  = parametersFactory.getValue("E", evaluatorType, "MOS") ;

    }
}
