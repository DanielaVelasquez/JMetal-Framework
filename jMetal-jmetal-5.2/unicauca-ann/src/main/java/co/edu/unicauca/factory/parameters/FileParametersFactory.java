package co.edu.unicauca.factory.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import org.uma.jmetal.util.JMetalException;


public class FileParametersFactory extends AbstractParametersFactory
{
    private static final String SPLIT_SYMBOL = ";";
    /**
     * Values of parameters
     */
    private HashMap<String, Double> values;
    /**
     * Path where the file is allocated
     */
    private String path;
    /**
     * File name that containts pameters with this specification:
     * name of parameter-type of evaluation-name of algorithm-value 
     */
    private String file_name;
    
    public FileParametersFactory(String path, String file_name) throws Exception
    {
        values = new HashMap<>();
        this.path = path;
        this.file_name = file_name;
        this.loadParameters();
    }
    
    private void loadParameters() throws Exception
    {
        FileReader fr = new FileReader(path + "/" + file_name);
        BufferedReader br = new BufferedReader(fr);
        
        String actualLine;
        while ((actualLine = br.readLine()) != null) 
        {
            String values[] = actualLine.split(SPLIT_SYMBOL);
            String parameter = values[0];
            AbstractELMEvaluator.EvaluatorType evaluatorType = this.loadTypeEvaluation(values[1]);
            String algorithm = values[2];
            double value = Double.parseDouble(values[3]);
            
            this.putParameter(parameter, evaluatorType, algorithm, value);
            
        }

    }
    private AbstractELMEvaluator.EvaluatorType loadTypeEvaluation(String type)
    {
        switch(type)
        {
            case "tt":
                return AbstractELMEvaluator.EvaluatorType.TT;
            case "cv":
                return  AbstractELMEvaluator.EvaluatorType.CV;
            default:
                throw new JMetalException("Type of evaluation invalid: "+type);
        }
    }
    /**
     * Place a parameter on the list
     * @param parameter name of the parameter
     * @param evaluatorType type  of evaluation
     * @param algorithmName name of algorithm
     * @param value  value for the parameter
     */
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
