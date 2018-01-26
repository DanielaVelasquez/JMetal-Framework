package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.factory.algorithm.DifferentialEvolutionFactory;
import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DEUnicaucaBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

public class DEUnicaucaParameters extends AbstractAlgortihmsParametersFinder
{
    private double cr ;
    private double f ;

    public DEUnicaucaParameters(AbstractParametersFactory parametersFactory ) throws Exception {
       super(parametersFactory);
       factory = new DifferentialEvolutionFactory(parametersFactory);
    }
 

    @Override
    public AlgorithmBuilder configureAlgorithm(int[] configuration, 
                                               int index, 
                                               double[][] values, 
                                               AbstractELMEvaluator.EvaluatorType type,
                                               DoubleProblem problem) throws Exception {
        cr = values[index][configuration[index]];
        f = values[index + 1][configuration[index + 1]];
       
       
        builder = factory.getAlgorithm("DEUnicauca", type, problem);
        return ((DEUnicaucaBuilder) builder)
                            .setCrossover(new DifferentialEvolutionCrossover(cr, f, "rand/1/bin"));
    }

    @Override
    public int getNumberParameters() {
        return 2;
    }
}
