package co.edu.unicauca.factory;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DEUnicaucaBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 *Factory to differential evolution algorithms builders, with configuration needed
 * for elm problem. 
 */
public class DifferentialEvolutionFactory extends AbstractFactory
{
    /**
     * Default configuration for DE
     */
    private final static double CR_DE = 0.7;
    private final static double F_DE = 0.5;
    private final static int POPULATION_DE = 10;
    /**
     * Default configuration SaDE
     */
    private final static double CR1_SADE = 0.6;
    private final static double F1_SADE = 0.5;
    private final static double CR2_SADE = 0.5;
    private final static double F2_SADE = 0.7;
    private final static int POPULATION_SaDE = 10;
    /**
     * Default configuration SaNSDE
     */
    private final static double CR1_SANSDE = 0.3;
    private final static double F1_SANSDE = 0.6;
    private final static double CR2_SANSDE = 0.7;
    private final static double F2_SANSDE = 0.3;
    private final static int POPULATION_SANSDE = 10;
    
    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType,
                                  DoubleProblem problem) 
    {
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        switch (name)
        {
            case "DEUnicauca":
                builder = this.getDEUnicauca(evaluations, problem);
                break;
            case "SaDE":
                builder = this.getSaDE(evaluations, problem);
                break;
            case "SaNSDE":
                builder = this.getSaNSDE(evaluations, problem);
                break;
            case "DECC_G":
                builder = this.getDECCG(evaluations, problem, evaluatorType);
                break;
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        }
        return builder;
    }
    
    private AlgorithmBuilder getDEUnicauca(int evaluations, DoubleProblem problem)
    {
        return new DEUnicaucaBuilder(problem)
                        .setPopulationSize(POPULATION_DE)
                        .setCrossover(new DifferentialEvolutionCrossover(CR_DE, F_DE, "rand/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .setMaxEvaluations(evaluations)
                        .setPenalizeValue(PENALIZE_VALUE)
                        .setComparator(COMPARATOR);
    }
    
    private AlgorithmBuilder getSaDE(int evaluations, DoubleProblem problem)
    {
        return new SaDEBuilder(problem)
                            .setPopulationSize(POPULATION_SaDE)
                            .setCrossoverOperator(new DifferentialEvolutionCrossover(CR1_SADE, F1_SADE, "rand/1/bin"))
                            .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SADE, F2_SADE, "current-to-best/1/bin"))
                            .setSelectionOperator(new DifferentialEvolutionSelection())
                            .setMaxEvaluations(evaluations)
                            .setPenalizeValue(PENALIZE_VALUE)
                            .setComparator(COMPARATOR);
    }
    
    private AlgorithmBuilder getSaNSDE(int evaluations, DoubleProblem problem)
    {
        return new SaNSDEBuilder(problem)
                        .setPopulationSize(POPULATION_SANSDE)
                        .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                        .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .setMaxEvaluations(evaluations)
                        .setPenalizeValue(PENALIZE_VALUE)
                        .setComparator(COMPARATOR);
    }
    
    private AlgorithmBuilder getDECCG(int evaluations, DoubleProblem problem,
            AbstractELMEvaluator.EvaluatorType evaluatorType)
    {
        DECC_GBuilder builder =  new DECC_GBuilder(problem)
                                     .setDEBuilder(new DEUnicaucaBuilder(problem)
                                                  .setCrossover(new DifferentialEvolutionCrossover(CR_DE, F_DE, "rand/1/bin"))
                                                  .setSelection(new DifferentialEvolutionSelection()))
                                    .setSaNSDEBuilder(new SaNSDEBuilder(problem)
                                                    .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                                                    .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin")))
                                    .setMaxEvaluations(evaluations)
                                    .setPenalizeValue(PENALIZE_VALUE)
                                    .setComparator(COMPARATOR);
        
        if(evaluatorType == AbstractELMEvaluator.EvaluatorType.CV)
        {
            builder.setPopulationSize(10)
                   .setSubcomponets(6)
                   .setFEs(30)
                   .setwFes(40);
        }
        else
        {
            builder.setPopulationSize(10)
                   .setSubcomponets(10)
                   .setFEs(70)
                   .setwFes(100);
        }
        
        return builder;
    }
    
    
}
