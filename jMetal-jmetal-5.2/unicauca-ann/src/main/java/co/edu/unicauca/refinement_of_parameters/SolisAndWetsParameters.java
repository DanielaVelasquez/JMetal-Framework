package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.factory.algorithm.SolisAndWetsFactory;
import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

public class SolisAndWetsParameters extends AbstractAlgortihmsParametersFinder
{
    private int sizeNeighborhood;
    private double rho;

    public SolisAndWetsParameters(AbstractParametersFactory parametersFactory )
    {
        super(parametersFactory);
        factory = new SolisAndWetsFactory(parametersFactory);
        
    }

    @Override
    public AlgorithmBuilder configureAlgorithm(int[] configuration, 
                                                int index, 
                                                double[][] values,
                                                AbstractELMEvaluator.EvaluatorType type, 
                                                DoubleProblem problem) 
    {
        sizeNeighborhood =  (int) values[index][configuration[index]];
        rho =  values[index + 1][configuration[index + 1]];
        builder = factory.getAlgorithm("SolisAndWets", type, problem);
        return ((SolisAndWetsBuilder) builder)
                            .setRho(rho)
                            .setSizeNeighborhood(sizeNeighborhood);
    }

    @Override
    public int getNumberParameters() 
    {
        return 2;
    }
}
