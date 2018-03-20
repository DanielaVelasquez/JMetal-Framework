package co.edu.unicauca.factory.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;

public abstract class AbstractParametersFactory
{
    /**
     * Gets the value for a specific parameter
     * @param parameter parameter's name
     * @param evaluatorType type of evaluation where the parameter is valid
     * @param algorithmName algorithm's name
     * @return value for a parameter
     */
    public abstract double getValue(String parameter, 
                             AbstractELMEvaluator.EvaluatorType evaluatorType,
                             String algorithmName) throws Exception;
}
