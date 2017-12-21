package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.AbstractFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;

public abstract class ParamsFinder 
{
    protected double values[][];
    protected int algorithmId;
    protected int sizeConfiguration;
    protected AbstractELMEvaluator.EvaluatorType type;
    protected AbstractFactory factory;
    protected AlgorithmBuilder builder;
    protected DoubleProblem problem;
    private DataBaseConnection connection;
    

    public ParamsFinder(int algorithmId,
                  DataBaseConnection connection,AbstractELMEvaluator.EvaluatorType type,
                  DoubleProblem problem) throws Exception
    {
        
        this.algorithmId = algorithmId;
        this.connection = connection;
        this.type = type;
        this.problem = problem;
        this.getParams();
    }
    
    public Algorithm getAlgorithm(String configurationString)
    {
        String split[] = configurationString.split(",");
        sizeConfiguration = split.length;
        int configuration[] = new int[sizeConfiguration];
        for(int i = 0; i < sizeConfiguration; i++)
        {
            configuration[i] = Integer.parseInt(split[i]);
        }
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
    protected abstract Algorithm configureAlgorithm(int configuration[]);
}
