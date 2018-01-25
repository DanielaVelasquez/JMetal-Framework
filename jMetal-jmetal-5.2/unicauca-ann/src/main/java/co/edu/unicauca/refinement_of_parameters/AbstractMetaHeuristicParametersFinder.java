package co.edu.unicauca.refinement_of_parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.algorithm.AbstractBuilderFactory;
import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

public abstract class AbstractMetaHeuristicParametersFinder 
{
    protected double values[][];
    protected int algorithmId;
    protected int sizeConfiguration;
    protected AbstractELMEvaluator.EvaluatorType type;
    protected AbstractBuilderFactory factory;
    protected AlgorithmBuilder builder;
    protected DoubleProblem problem;
    protected int configuration[];
    private DataBaseConnection connection;
    protected AbstractParametersFactory parametersFactory;



    public AbstractMetaHeuristicParametersFinder(int algorithmId,
                  DataBaseConnection connection,AbstractELMEvaluator.EvaluatorType type,
                  DoubleProblem problem,
                  AbstractParametersFactory parametersFactory) throws Exception
    {
        
        this.algorithmId = algorithmId;
        this.connection = connection;
        this.type = type;
        this.problem = problem;
        this.parametersFactory = parametersFactory;
        this.getParams();
    }
    
    protected void getConfiguration(String configurationString)
    {
        String split[] = configurationString.split(",");
        sizeConfiguration = split.length;
        configuration = new int[sizeConfiguration];
        for(int i = 0; i < sizeConfiguration; i++)
        {
            configuration[i] = Integer.parseInt(split[i]);
        }
    }
    
    public Algorithm getAlgorithm(String configurationString) throws Exception
    {
        this.getConfiguration(configurationString);
        return configureAlgorithm(configuration);
    }

    private void getParams() throws Exception
    {
        if(values != null)
            return;
        int rows, columns;
        String query = 
        "SELECT valuesParams\n"+
        "FROM params\n"+
        "INNER JOIN algorithm ON paramsAlg = idParams\n"+
        "WHERE idAlg = "+algorithmId;
        ResultSet result = connection.seleccion(query);
        result.next();
        String paramsString = result.getString(1);
        String variables[] = paramsString.split(";");
        rows = variables.length;
        
        String line[] = variables[0].split(",");
        columns = line.length;
        
        values = new double[rows][columns];
        for(int i = 0; i < rows; i++)
        {
            line = variables[i].split(",");
            for(int j = 0; j < columns; j++)
            {
                values[i][j] = Double.parseDouble(line[j]);
            }
        }
        
    }
    protected abstract Algorithm configureAlgorithm(int configuration[]) throws Exception;
}
