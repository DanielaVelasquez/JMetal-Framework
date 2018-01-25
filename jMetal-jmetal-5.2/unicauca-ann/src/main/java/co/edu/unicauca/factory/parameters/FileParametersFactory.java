package co.edu.unicauca.factory.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.HashMap;


public class FileParametersFactory extends AbstractParamertersFactory
{
    private HashMap<String, Double> values;
    
    public FileParametersFactory()
    {
        values = new HashMap<>();
        this.loadParameters();
    }
    
    private void loadParameters()
    {
        
    }
    
    public void putParameter(String parameter, 
                              AbstractELMEvaluator.EvaluatorType evaluatorType, 
                              String algorithmName, double value)
    {
        values.put(this.getKey(parameter, evaluatorType, algorithmName), value);
    }
    /**
     * Create the key to save a parameter on the hashmap
     * @param parameter name of the parameter
     * @param evaluatorType type of evaluation where the parameter is valid
     * @param algorithmName algorithm's name
     * @return string to save the parameter value
     */
    private String getKey(String parameter, 
                          AbstractELMEvaluator.EvaluatorType evaluatorType, 
                          String algorithmName)
    {
        String type = evaluatorType == AbstractELMEvaluator.EvaluatorType.CV?"cv":"tt";
        return parameter+"-"+type+"-"+algorithmName;
    }
    
    @Override
    public double getValue(String parameter, AbstractELMEvaluator.EvaluatorType evaluatorType, String algorithmName) 
    {
        return values.get(this.getKey(parameter, evaluatorType, algorithmName));
    }
}
