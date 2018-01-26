package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.ihdels.IHDELSBuilder;
import org.uma.jmetal.algorithm.singleobjective.ihdels.LSHillClimbing;
import org.uma.jmetal.algorithm.singleobjective.ihdels.LSMTS_LS1;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 *Factory to IHDELS algorithms builders, with configuration needed
 * for elm problem. 
 */
public class IHDELSFactory extends AbstractBuilderFactory
{
    private DifferentialEvolutionFactory deFactory ;
    private MTSFactory mtsFactory ;
    private HillClimbingFactory hcFactory ;
    
    private int FE_DE_IHDELS ;
    private int FE_LS_IHDLES ;
    private int POPULATION_IHDELS;
    private int RESTART_IHDELS ;
    private double A_IHDELS ;
    private double B_IHDELS ;
    private double THRESHOLD_IHDELS ;

    public IHDELSFactory(AbstractParametersFactory parametersFactory) {
        super(parametersFactory);
        deFactory = new DifferentialEvolutionFactory(parametersFactory);
        mtsFactory = new MTSFactory(parametersFactory);
        hcFactory = new HillClimbingFactory(parametersFactory);
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
            case "IHDELS":
                builder = this.getIHDELS(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        } 
        return builder;
    }
    
    private AlgorithmBuilder getIHDELS(int evaluations, DoubleProblem problem) throws Exception
    {
        LocalSearch hillClimbing = new LSHillClimbing(hcFactory.getAlgorithm("HillClimbing", this.evaluatorType, problem));
        LocalSearch mtsls1 = new LSMTS_LS1(mtsFactory.getAlgorithm("MTS_LS1", this.evaluatorType, problem));
        SaDEBuilder sadeBuilder = (SaDEBuilder) deFactory.getAlgorithm("SaDE", this.evaluatorType, problem);
        
        return new IHDELSBuilder(problem)
                    .addLocalSearch(mtsls1)
                    .addLocalSearch(hillClimbing)
                    .setComparator(COMPARATOR)
                    .setMaxEvaluations(evaluations)
                    .setFE_DE(FE_DE_IHDELS)
                    .setFE_LS(FE_LS_IHDLES)
                    .setPopulation_size(POPULATION_IHDELS)
                    .setReStart(RESTART_IHDELS)
                    .setSearchDomain(A_IHDELS, B_IHDELS)
                    .setThreshold(THRESHOLD_IHDELS)
                    .setSaDEBuilder(sadeBuilder);
    }

    @Override
    protected void loadAlgorithmValues(String name, AbstractELMEvaluator.EvaluatorType evaluatorType) throws Exception
    {
        
        FE_DE_IHDELS  = (int) parametersFactory.getValue("FE_DE", evaluatorType, "IHDELS") ;
        FE_LS_IHDLES  = (int) parametersFactory.getValue("FE_LS", evaluatorType, "IHDELS") ;
        POPULATION_IHDELS = (int) parametersFactory.getValue("POPULATION", evaluatorType, "IHDELS") ;
        RESTART_IHDELS  = (int) parametersFactory.getValue("RESTART", evaluatorType, "IHDELS") ;
        A_IHDELS  = parametersFactory.getValue("A", evaluatorType, "IHDELS") ;
        B_IHDELS  = parametersFactory.getValue("B", evaluatorType, "IHDELS") ;
        THRESHOLD_IHDELS  = parametersFactory.getValue("THRESHOLD", evaluatorType, "IHDELS") ;

    }
}
