package co.edu.unicauca.factory;

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
public class IHDELSFactory extends AbstractFactory
{
    private final static DifferentialEvolutionFactory deFactory = new DifferentialEvolutionFactory();
    private final static MTSFactory mtsFactory = new MTSFactory();
    private final static HillClimbingFactory hcFactory = new HillClimbingFactory();
    
    private static final int FE_DE_IHDELS = 30;//90;
    private static final int FE_LS_IHDLES = 90;//30;//60;
    private static final int POPULATION_IHDELS= 10;
    private static final int RESTART_IHDELS = 7;//3;//7;
    private static final double A_IHDELS = -1;//-0.7;//-0.5;
    private static final double B_IHDELS = 1;//0.5;//1;//1999ii9koo;
    private static final double THRESHOLD_IHDELS = 0.001;//0.01;//0.001;

    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, DoubleProblem problem)
    {
        this.evaluatorType = evaluatorType;
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
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
    
    private AlgorithmBuilder getIHDELS(int evaluations, DoubleProblem problem)
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
}
