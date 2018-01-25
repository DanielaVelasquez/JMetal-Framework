package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.factory.algorithm.DifferentialEvolutionFactory;
import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

public class SaNSDEParameters extends AbstractAlgortihmsParametersFinder
{
    private double cr1 ;
    private double f1 ;
    private double cr2 ;
    private double f2 ;

    public SaNSDEParameters(AbstractParametersFactory parametersFactory)
    {
        super(parametersFactory);
        factory = new DifferentialEvolutionFactory(parametersFactory);
    }
 

    @Override
    public AlgorithmBuilder configureAlgorithm(int[] configuration, 
                                                int index, 
                                                double[][] values, 
                                                AbstractELMEvaluator.EvaluatorType type,
                                                DoubleProblem problem)
    {
        cr1 = values[index][configuration[index]];
        f1 = values[index + 1][configuration[index + 1]];
        cr2 = values[index + 2][configuration[index + 2]];
        f2 = values[index + 3][configuration[index + 3]];
       
        builder = factory.getAlgorithm("SaNSDE", type, problem);
        return ((SaNSDEBuilder) builder)
                            .setCrossover(new DifferentialEvolutionCrossover(cr1, f1, "rand/1/bin"))
                            .setCrossoverOperator2(new DifferentialEvolutionCrossover(cr2, f2, "current-to-best/1/bin"));
    }

    @Override
    public int getNumberParameters() {
        return 4;
    }
}
