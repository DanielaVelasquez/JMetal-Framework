package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 *Factory to MTS algorithms builders, with configuration needed
 * for elm problem. 
 */
public class MTSFactory extends AbstractBuilderFactory
{
    private int POPULATION_MTS_LS1;
    public MTSFactory(AbstractParametersFactory parametersFactory)
    {
        super(parametersFactory);
    }
    
    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType,
            DoubleProblem problem) throws Exception 
    {
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        this.loadAlgorithmValues(name, evaluatorType);
        switch(name)
        {
            case "MTS_LS1":
                builder = this.getMTSLS1(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        }
        return builder;
    }
    
     private AlgorithmBuilder getMTSLS1(int evaluations, DoubleProblem problem)
     {
        return new MTS_LS1Builder(problem)
                        .setPopulationSize(POPULATION_MTS_LS1)
                        .setBonus1(10)
                        .setBonus2(1)
                        .setPenalizeValue(PENALIZE_VALUE)
                        .setMaxEvaluations(evaluations)
                        .setComparator(COMPARATOR);
     }

    @Override
    protected void loadAlgorithmValues(String name, AbstractELMEvaluator.EvaluatorType evaluatorType) throws Exception {
        POPULATION_MTS_LS1 =  (int) parametersFactory.getValue("POPULATION", evaluatorType, "MTS") ;
    }
    
}
