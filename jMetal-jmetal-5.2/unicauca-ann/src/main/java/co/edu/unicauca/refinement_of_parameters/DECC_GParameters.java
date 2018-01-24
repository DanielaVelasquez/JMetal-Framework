package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.algorithm.DifferentialEvolutionFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DEUnicaucaBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.problem.DoubleProblem;

public class DECC_GParameters extends AbstractMetaHeuristicParametersFinder
{
    /**
     * Atributes
     */
   private DEUnicaucaParameters DEParameters;
   private DEUnicaucaBuilder DEBuilder;
   
   private SaNSDEParameters sansdeParameters;
   private SaNSDEBuilder sansdeBuilder;
    
    
    public DECC_GParameters( int algorithmId, 
               DataBaseConnection connection, AbstractELMEvaluator.EvaluatorType type,
               DoubleProblem problem) throws Exception {
        super(algorithmId, connection, type, problem);
        factory = new DifferentialEvolutionFactory();
        DEParameters = new DEUnicaucaParameters();
        sansdeParameters = new SaNSDEParameters();
    }

    @Override
    protected Algorithm configureAlgorithm(int[] configuration) 
    {
       builder = factory.getAlgorithm("DECC_G", type, problem);
       DEBuilder = (DEUnicaucaBuilder) DEParameters.configureAlgorithm(configuration, 0, values, type, problem);
       sansdeBuilder = (SaNSDEBuilder) sansdeParameters.configureAlgorithm(configuration, DEParameters.getNumberParameters(), values, type, problem);
       return ((DECC_GBuilder) builder)
                            .setDEBuilder(DEBuilder)
                            .setSaNSDEBuilder(sansdeBuilder)
                            .build();
    }   
}
