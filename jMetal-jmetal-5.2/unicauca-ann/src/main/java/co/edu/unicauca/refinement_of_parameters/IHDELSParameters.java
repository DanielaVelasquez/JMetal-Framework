package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.algorithm.IHDELSFactory;
import co.edu.unicauca.factory.algorithm.MTSFactory;
import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbingBuilder;
import org.uma.jmetal.algorithm.singleobjective.ihdels.IHDELSBuilder;
import org.uma.jmetal.algorithm.singleobjective.ihdels.LSHillClimbing;
import org.uma.jmetal.algorithm.singleobjective.ihdels.LSMTS_LS1;
import org.uma.jmetal.problem.DoubleProblem;

public class IHDELSParameters extends AbstractMetaHeuristicParametersFinder
{
    
    
    /**
     * Atributes
     */
    private MTSFactory mtsFactory;
    private int FE_DE;
    private int FE_LS;
    private double a;
    private double b;
    private int restart;
    private double threshold;
    
    private SaDEParameters SaDEParameters;
    private SaDEBuilder SaDEBuilder;
    
    private HillClimbingParameters hillClimbingParameters;
    private HillClimbingBuilder hillClimbingBuilder;
    
    public IHDELSParameters( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem,
               AbstractParametersFactory parametersFactory) throws Exception {
        super(algorithmId, connection, type, problem, parametersFactory);
        factory = new IHDELSFactory(parametersFactory);
        SaDEParameters = new SaDEParameters(parametersFactory);
        hillClimbingParameters = new HillClimbingParameters(parametersFactory);
        mtsFactory = new MTSFactory(parametersFactory);
        
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) throws Exception 
    {
       FE_DE = (int) values[0][configuration[0]];
       FE_LS = (int) values[1][configuration[1]];
       a = values[2][configuration[2]];
       b = values[3][configuration[3]];
       restart = (int) values[4][configuration[4]];
       threshold = values[5][configuration[5]];
       builder = factory.getAlgorithm("IHDELS", type, problem);
       
       SaDEBuilder = (SaDEBuilder) SaDEParameters.configureAlgorithm(configuration, 6, values, type, problem);
       hillClimbingBuilder = (HillClimbingBuilder) hillClimbingParameters.configureAlgorithm(configuration,6 + SaDEParameters.getNumberParameters(), values, type, problem);
       LocalSearch mtsls1 = new LSMTS_LS1(mtsFactory.getAlgorithm("MTS_LS1", AbstractELMEvaluator.EvaluatorType.TT, problem));
       LocalSearch hillClimbingLS = new LSHillClimbing(hillClimbingBuilder);
       return ((IHDELSBuilder) builder)
                            .setFE_DE(FE_DE)
                            .setFE_LS(FE_LS)
                            .setSearchDomain(a, b)
                            .setReStart(restart)
                            .setThreshold(threshold)
                            .setSaDEBuilder(SaDEBuilder)
                            .removeLocalSearches()
                            .addLocalSearch(mtsls1)
                            .addLocalSearch(hillClimbingLS)
                            .build();
    }   
}
