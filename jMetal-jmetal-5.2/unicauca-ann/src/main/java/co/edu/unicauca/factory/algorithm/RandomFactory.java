package co.edu.unicauca.factory.algorithm;

import static co.edu.unicauca.factory.algorithm.AbstractFactory.EVALUATIONS_TT;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.Comparator;
import org.uma.jmetal.algorithm.singleobjective.random_search.RandomSearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

/**
 *Factory to Random algorithms builders, with configuration needed
 * for elm problem. 
 */
public class RandomFactory extends AbstractFactory
{
    private final static Comparator<DoubleSolution> COMPARATOR_RANDOM = new ObjectiveComparator<>(0, ObjectiveComparator.Ordering.DESCENDING);
    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, 
            DoubleProblem problem) 
    {
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT?EVALUATIONS_TT:EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        switch(name)
        {
            case "Random":
                builder = this.getRandom(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm "+name+" not exists");
        }
        return builder;
    }
    
    private AlgorithmBuilder getRandom(int evaluations, DoubleProblem problem)
    {
        return new RandomSearchBuilder<>(problem)
                        .setMaxEvaluations(evaluations)
                        .setComparator(COMPARATOR_RANDOM)
                        .setPenalizeValue(PENALIZE_VALUE);
    }
    
}
