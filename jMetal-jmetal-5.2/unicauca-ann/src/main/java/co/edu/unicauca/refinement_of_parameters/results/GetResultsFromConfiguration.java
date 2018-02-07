
package co.edu.unicauca.refinement_of_parameters.results;

import co.edu.unicauca.database.DataBaseConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;


public class GetResultsFromConfiguration 
{

    private static HashMap<Integer,String> datasets;
    private static DataBaseConnection db;
    private static double[][] values;
    private static int configuration[];
    private static ArrayList<String> search = new  ArrayList();
    private static int totalDataSets;
    public static void main(String[] args) throws Exception
    {
       db = DataBaseConnection.getInstancia();
       datasets = new HashMap<>();
       String algorithm = "MOS";
       String type = "cv";
       loadSearch();
       int id = getIdAlgorithm(algorithm);
       loadValues(id);
       //print(values);
       int ca = getCoveringArray(id);

       String query = "SELECT valuesConf, idConf FROM configuration WHERE coveringArrayConf = "+ ca;
       ResultSet result = db.seleccion(query);
       readDataSets();
       
       HashMap<String,String> vocabulario = new HashMap<>();
       while(result.next())
       {
           String configurationString = result.getString(1).trim();
           int idConfiguration = result.getInt(2);
           getConfiguration(configurationString);
           String v  = "";
           for(int i = 0; i < configuration.length; i++)
           {
               if(i + 1 == configuration.length)
                   v += values[i][configuration[i]]+"";
               else
                 v += values[i][configuration[i]]+":";
           }
           vocabulario.put(v,configurationString);
       }
       
       for(String a:search)
       {
           String v = vocabulario.get(a);
           query = "SELECT problem.nameProblem AS DataSet, AVG(results.testResults) AS test\n" +
                    "FROM results\n" +
                    "INNER JOIN task ON taskResults = idTask\n" +
                    "INNER JOIN problem ON problemTask = idProblem\n" +
                    "INNER JOIN algorithm ON algorithmTask = idAlg\n" +
                    "INNER JOIN configuration ON idConf = configurationTask\n" +
                    "INNER JOIN type ON idType  = typeTask\n" +
                    "WHERE valuesConf = '"+v+"' and nameType = '"+type+"'\n" +
                    "GROUP BY  algorithmTask, problemTask, nameProblem, nameAlg, valuesConf, idConf, nameType\n" +
                    "ORDER BY nameProblem, valuesConf";
           ResultSet r = db.seleccion(query);
           while(r.next())
           {
               System.out.printf("%s",r.getString(1));
               System.out.printf("%6.4f",r.getDouble(2));
               System.out.println("");
//System.out.println(r.getString(1)+""+r.getString(2).replace(".", ","));
           }
       }
       

    }
    private static void loadSearch()
    {
        search.add("100.0:0.3:4.0:0.5");
    }
    private static void readDataSets() throws Exception
    {
        String query = "SELECT idProblem, nameProblem FROM problem";
        ResultSet result = db.seleccion(query);
        while(result.next())
        {
            datasets.put(result.getInt(1), result.getString(2));
            //System.out.println(result.getString(2));
        }
        query = "SELECT COUNT(idProblem)  FROM problem";
        result = db.seleccion(query);
        result.next();
        totalDataSets = result.getInt(1);
    }
    private static void getConfiguration(String configurationString)
    {
        String split[] = configurationString.split(",");
        int sizeConfiguration = split.length;
        configuration = new int[sizeConfiguration];
        for(int i = 0; i < sizeConfiguration; i++)
        {
            configuration[i] = Integer.parseInt(split[i].trim());
        }
    }
    private static int getCoveringArray(int alg) throws Exception
    {
        String query = "SELECT coveringArrayAlg FROM algorithm WHERE idAlg = "+alg;
        ResultSet result = db.seleccion(query);
        result.next();
        return result.getInt(1);
    }
    private static void print(String [][] value)
    {
        for (int i = 0; i < value.length; i++)
        {
            for(int j = 0; j< value[0].length; j++)
            {
                System.out.printf("%6s", value[i][j]);
            }
            System.out.println("");     
        } 
    }
    private static void loadValues(int algorithmId) throws Exception
    {
        int rows, columns;
        String query = 
        "SELECT valuesParams\n"+
        "FROM params\n"+
        "INNER JOIN algorithm ON paramsAlg = idParams\n"+
        "WHERE idAlg = "+algorithmId;

        ResultSet result = db.seleccion(query);
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

    private static int getIdAlgorithm(String name) throws Exception
    {
        String query = "SELECT idAlg FROM algorithm WHERE nameAlg = '"+name+"'";
        ResultSet result = db.seleccion(query);
        result.next();
        return result.getInt(1);
    }
    
}
