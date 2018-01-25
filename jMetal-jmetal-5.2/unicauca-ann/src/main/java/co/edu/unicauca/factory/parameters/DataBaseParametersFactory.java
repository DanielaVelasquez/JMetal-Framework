package co.edu.unicauca.factory.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import org.uma.jmetal.util.JMetalException;


public class DataBaseParametersFactory extends AbstractParamertersFactory
{
    private DataBaseConnection connection;
    @Override
    public double getValue(String parameter, AbstractELMEvaluator.EvaluatorType evaluatorType, String algorithmName)throws Exception
    {
        connection = DataBaseConnection.getInstancia();
        String type = evaluatorType == AbstractELMEvaluator.EvaluatorType.CV?"cv":"tt";
        String query = "SELECT valor FROM parametros"+
                       "WHERE nameAlg = '"+algorithmName+"' AND"+
                       "parametro = '"+parameter+"' AND"+
                       "tipo = '"+type+"'";
        ResultSet result = connection.seleccion(query);
        if(result.next())
        {
            return result.getDouble(1);
        }
        else
        {
            throw new JMetalException("Parameter not found.\n"+parameter+" for algorithm "+algorithmName+" with evaluation "+type);
        }
    }
    
}
