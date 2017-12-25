package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;

public class ParametersResults
{
    private DataBaseConnection connection;
    private String nameProblem;
    private AbstractELMEvaluator.EvaluatorType type;
    private String typeName;
    private String algorithm;
    private String valuesConf;
    private int seed;
    private int algorithmId;
    private int idTask;    
    
    private void ejecutar() throws Exception
    {
        connection = DataBaseConnection.getInstancia();
        String algoritmoNombre = "SaDE";
        String conf = "Configuracion2";
        
        FileReader frDataSet = new FileReader("DataSet");
        BufferedReader bfDataset = new BufferedReader(frDataSet);
        
        FileWriter fwAlgoritmo = new FileWriter(algoritmoNombre + ".csv");
        PrintWriter pwAlgoritmo = new PrintWriter(fwAlgoritmo);
        
        FileReader frConfiguracion = new FileReader(conf);       
        BufferedReader bfConfiguracion = new BufferedReader(frConfiguracion);
        
        String dataset;
        String configuracion;        
        String imprimir = bfDataset.readLine();
        
        while((configuracion = bfConfiguracion.readLine()) != null)
        {
            imprimir += "," + configuracion.replace(',', '-');
        }
        
        pwAlgoritmo.println(imprimir);
       
        while((dataset = bfDataset.readLine()) != null)
        {
            imprimir = dataset;
            
            frConfiguracion = new FileReader(conf);       
            bfConfiguracion = new BufferedReader(frConfiguracion);
            
            while((configuracion = bfConfiguracion.readLine()) != null)
            {
                String query = 
                "SELECT AVG(results.testResults) AS test\n" +
                "FROM results\n" +
                "INNER JOIN task ON taskResults = idTask\n" +
                "INNER JOIN problem ON problemTask = idProblem\n" +
                "INNER JOIN algorithm ON algorithmTask = idAlg\n" +
                "INNER JOIN configuration ON idConf = configurationTask\n" +
                "WHERE valuesConf = '" + configuracion + "' AND algorithm.nameAlg = '" + algoritmoNombre + "' AND problem.nameProblem = '" + dataset + "'\n" +
                "GROUP BY algorithmTask, problemTask, nameProblem, nameAlg\n" +
                "ORDER BY nameProblem";
                
                ResultSet result = connection.seleccion(query);
                result.next();
                String valor = result.getString(1);
                imprimir += "," + valor.replace(',', '.');
            }
            
            pwAlgoritmo.println(imprimir);
        }
        pwAlgoritmo.close();
    }
    
    public static void main(String[] args) {
        ParametersResults pr = new ParametersResults();
        try
        {
            pr.ejecutar();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
