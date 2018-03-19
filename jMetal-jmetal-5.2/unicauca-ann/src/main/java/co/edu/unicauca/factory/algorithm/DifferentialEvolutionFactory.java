package co.edu.unicauca.factory.algorithm;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.Comparator;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DEUnicaucaBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

/**
 * Factory to differential evolution algorithms builders, with configuration
 * needed for elm problem.
 */
public class DifferentialEvolutionFactory extends AbstractBuilderFactory {

    /**
     * Default configuration for DE
     */
    private double CR_DE;
    private double F_DE;
    private int POPULATION_DE;
    /**
     * Default configuration SaDE
     */
    private double CR1_SADE;
    private double F1_SADE;
    private double CR2_SADE;
    private double F2_SADE;
    private int POPULATION_SaDE;
    /**
     * Default configuration SaNSDE
     */
    private double CR1_SANSDE;
    private double F1_SANSDE;
    private double CR2_SANSDE;
    private double F2_SANSDE;
    private int POPULATION_SANSDE;
    /**
     * Default configuration for MemeticDE
     */
    private double CR_MEMETIC_DE;
    private double F_MEMETIC_DE;
    private int POPULATION_MEMETIC;
    private final static Comparator<DoubleSolution> COMPARATOR_MEMETIC = new ObjectiveComparator<>(0, ObjectiveComparator.Ordering.DESCENDING);

    private int DECCG_POPULATION;

    public DifferentialEvolutionFactory(AbstractParametersFactory parametersFactory) {
        super(parametersFactory);

    }

    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType,
            DoubleProblem problem) throws Exception {
        this.evaluatorType = evaluatorType;
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT ? EVALUATIONS_TT : EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        this.loadAlgorithmValues(name, evaluatorType);
        switch (name) {
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
                builder = this.getDECCG(evaluations, problem);
                break;
            case "MemeticED":
                builder = this.getMemeticED(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm " + name + " not exists");
        }
        return builder;
    }

    private AlgorithmBuilder getDEUnicauca(int evaluations, DoubleProblem problem) {
        return new DEUnicaucaBuilder(problem)
                .setPopulationSize(POPULATION_DE)
                .setCrossover(new DifferentialEvolutionCrossover(CR_DE, F_DE, "rand/1/bin"))
                .setSelection(new DifferentialEvolutionSelection())
                .setMaxEvaluations(evaluations)
                .setPenalizeValue(PENALIZE_VALUE)
                .setComparator(COMPARATOR);
    }

    private AlgorithmBuilder getSaDE(int evaluations, DoubleProblem problem) {
        return new SaDEBuilder(problem)
                .setPopulationSize(POPULATION_SaDE)
                .setCrossoverOperator(new DifferentialEvolutionCrossover(CR1_SADE, F1_SADE, "rand/1/bin"))
                .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SADE, F2_SADE, "current-to-best/1/bin"))
                .setSelectionOperator(new DifferentialEvolutionSelection())
                .setMaxEvaluations(evaluations)
                .setPenalizeValue(PENALIZE_VALUE)
                .setComparator(COMPARATOR);
    }

    private AlgorithmBuilder getSaNSDE(int evaluations, DoubleProblem problem) {
        return new SaNSDEBuilder(problem)
                .setPopulationSize(POPULATION_SANSDE)
                .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin"))
                .setSelection(new DifferentialEvolutionSelection())
                .setMaxEvaluations(evaluations)
                .setPenalizeValue(PENALIZE_VALUE)
                .setComparator(COMPARATOR);
    }

    private AlgorithmBuilder getDECCG(int evaluations, DoubleProblem problem) {
        DECC_GBuilder builder = new DECC_GBuilder(problem)
                .setDEBuilder(new DEUnicaucaBuilder(problem)
                        .setCrossover(new DifferentialEvolutionCrossover(CR_DE, F_DE, "rand/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection()))
                .setSaNSDEBuilder(new SaNSDEBuilder(problem)
                        .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                        .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin")))
                .setMaxEvaluations(evaluations)
                .setPenalizeValue(PENALIZE_VALUE)
                .setComparator(COMPARATOR);

        if (evaluatorType == AbstractELMEvaluator.EvaluatorType.CV) {
            builder.setPopulationSize(DECCG_POPULATION)
                    .setSubcomponets(6)
                    .setFEs(30)
                    .setwFes(40);
        } else {
            builder.setPopulationSize(DECCG_POPULATION)
                    .setSubcomponets(10)
                    .setFEs(70)
                    .setwFes(100);
        }

        return builder;
    }

    private AlgorithmBuilder getMemeticED(int evaluations, DoubleProblem problem) {
        return new MemeticEDBuilder(problem)
                .setPopulationSize(POPULATION_MEMETIC)
                .setCrossover(new DifferentialEvolutionCrossover(CR_MEMETIC_DE, F_MEMETIC_DE, "current-to-best/1/bin"))
                .setSelection(new DifferentialEvolutionSelection())
                .setMaxEvaluations(evaluations - 1)
                .setComparator(COMPARATOR_MEMETIC);
    }

    @Override
    protected void loadAlgorithmValues(String name,
            AbstractELMEvaluator.EvaluatorType evaluatorType) throws Exception {

        CR_DE = parametersFactory.getValue("CR", evaluatorType, "DEUnicauca");
        F_DE = parametersFactory.getValue("F", evaluatorType, "DEUnicauca");
        POPULATION_DE = (int) parametersFactory.getValue("POPULATION", evaluatorType, "DEUnicauca");

        CR1_SADE = parametersFactory.getValue("CR1", evaluatorType, "SaDE");
        F1_SADE = parametersFactory.getValue("F1", evaluatorType, "SaDE");
        CR2_SADE = parametersFactory.getValue("CR2", evaluatorType, "SaDE");
        F2_SADE = parametersFactory.getValue("F2", evaluatorType, "SaDE");
        POPULATION_SaDE = (int) parametersFactory.getValue("POPULATION", evaluatorType, "SaDE");

        CR1_SANSDE = parametersFactory.getValue("CR1", evaluatorType, "SaNSDE");
        F1_SANSDE = parametersFactory.getValue("F1", evaluatorType, "SaNSDE");
        CR2_SANSDE = parametersFactory.getValue("CR2", evaluatorType, "SaNSDE");
        F2_SANSDE = parametersFactory.getValue("F2", evaluatorType, "SaNSDE");
        POPULATION_SANSDE = (int) parametersFactory.getValue("POPULATION", evaluatorType, "SaNSDE");

        CR_MEMETIC_DE = parametersFactory.getValue("CR", evaluatorType, "MemeticED");
        F_MEMETIC_DE = parametersFactory.getValue("F", evaluatorType, "MemeticED");
        POPULATION_MEMETIC = (int) parametersFactory.getValue("POPULATION", evaluatorType, "MemeticED");

        DECCG_POPULATION = (int) parametersFactory.getValue("POPULATION", evaluatorType, "DECC_G");
    }

}
