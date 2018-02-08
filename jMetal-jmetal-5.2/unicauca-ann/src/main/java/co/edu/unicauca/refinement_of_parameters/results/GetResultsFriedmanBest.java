
package co.edu.unicauca.refinement_of_parameters.results;

import co.edu.unicauca.database.DataBaseConnection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GetResultsFriedmanBest 
{
    private static HashMap<Integer,String> datasets;
    private static DataBaseConnection db;
    private static double[][] values;
    private static int configuration[];
    private static int totalDataSets;
    private static String[][] combinations;
    
    private static HashMap<Integer,String> configuracionesVocabulario;
    private static String NOMBRE_ARCHIVO ;
    private static String ESCRIBIR = "";
    private static ArrayList<String> search = new  ArrayList();
    private static ArrayList<Integer> configuracionesEjecutar = new  ArrayList();
    public static void main(String[] args) throws Exception
    {
       db = DataBaseConnection.getInstancia();
      
       datasets = new HashMap<>();
       String algorithm = "DECC_G";
       String type = "tt";
        
        NOMBRE_ARCHIVO = "afinamiento-parametros-friedman-best/regresion";
        ESCRIBIR = "";
        NOMBRE_ARCHIVO += "-"+algorithm.toLowerCase();
        NOMBRE_ARCHIVO += "-"+type;
        
       search.add("0.5:0.2:0.35:0.2:0.2:0.95");
	search.add("0.35:0.35:0.35:0.35:0.7:0.5");
	search.add("0.7:0.7:0.5:0.7:0.35:0.7");
	search.add("0.7:0.35:0.7:0.7:0.5:0.95");
	search.add("0.35:0.5:0.5:0.5:0.5:0.95");
	search.add("0.5:0.7:0.5:0.95:0.5:0.5");
	search.add("0.7:0.95:0.35:0.5:0.95:0.2");
	search.add("0.35:0.7:0.95:0.5:0.2:0.35");
	search.add("0.7:0.35:0.2:0.35:0.35:0.2");
	search.add("0.95:0.2:0.2:0.2:0.5:0.7");




        
        int id = getIdAlgorithm(algorithm);
        loadValues(id);
        //print(values);
        int ca = getCoveringArray(id);
        String query = "SELECT COUNT(*) from configuration WHERE coveringArrayConf = "+ ca;
        ResultSet r = db.seleccion(query);
        r.next();
        int totalCombinations = r.getInt(1);
        query = "SELECT valuesConf, idConf FROM configuration WHERE coveringArrayConf = "+ ca;
        ResultSet result = db.seleccion(query);
        readDataSets();
        combinations = new String[totalCombinations][totalDataSets];
        ESCRIBIR +="DataSet,";
        configuracionesVocabulario = new HashMap<>();
        int j = 0;
        while(result.next())
        {

            String configurationString = result.getString(1);
            int idConfiguration = result.getInt(2);
            getConfiguration(configurationString);



            configurationString = "";
            for(int i = 0; i < configuration.length; i++)
            {
                if(i + 1 == configuration.length)
                 configurationString += values[i][configuration[i]];
                else
                    configurationString += values[i][configuration[i]]+":";
            }
            if(search.contains(configurationString))
            {
                
                configuracionesVocabulario.put(idConfiguration,configurationString);
                //System.out.println(""+idConfiguration);
                configuracionesEjecutar.add(idConfiguration);
                if( j + 1 == search.size())
                    ESCRIBIR += configurationString;
               else 
                    ESCRIBIR += configurationString + ",";
                j++;
            }
            
            
        }

        ESCRIBIR += "\n";

       
        DecimalFormat df = new DecimalFormat("#.#####");
         for(Map.Entry<Integer,String> entry:datasets.entrySet())
         {
             int i = 0;
             ESCRIBIR +=entry.getValue().trim()+",";
            

             for(Integer idConfiguration : configuracionesEjecutar)
             {

                 //int idConfiguration = conf.getKey();
                 query = 
                 "SELECT AVG(results.testResults) AS test\n" +
                 "FROM results\n" +
                 "INNER JOIN task ON taskResults = idTask\n" +
                 "INNER JOIN problem ON problemTask = idProblem\n" +
                 "INNER JOIN algorithm ON algorithmTask = idAlg\n" +
                 "INNER JOIN configuration ON idConf = configurationTask\n" +
                 "INNER JOIN type ON idType = typeTask\n"+
                 "WHERE idConf = " + idConfiguration + " AND algorithm.nameAlg = '" + algorithm + "' AND problem.nameProblem = '" + entry.getValue() + "'  AND type.nameType = '"+type+"'\n" ;
                 //"GROUP BY algorithmTask, problemTask, nameProblem, nameAlg\n";
                 ResultSet resultado = db.seleccion(query);
                 resultado.next();
                 String valor = df.format(resultado.getFloat(1));

                String replace = valor.replace(',', '.');
                
                
                
                if(i + 1 == configuracionesVocabulario.size())
                     ESCRIBIR += replace ;
                 else
                     ESCRIBIR += replace +",";
               


                 i++;

             }

             ESCRIBIR +="\n";
             
         }



            //print(combinateions);
         //System.out.println(ESCRIBIR);
         writeFile();
           
       
       
       

    }
    
    private static void writeFile()
    {
        BufferedWriter bw = null;
        try {
            
            
                File archivo = new File(NOMBRE_ARCHIVO+".csv");
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(ESCRIBIR);
            
            
        } catch (IOException ex) {
            Logger.getLogger(GetResultsFriedmanBest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(GetResultsFriedmanBest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void readDataSets() throws Exception
    {
        String query = "SELECT idProblem, nameProblem FROM problem";
        ResultSet result = db.seleccion(query);
        
        ArrayList l = new ArrayList();
        while(result.next())
        {
            datasets.put(result.getInt(1), result.getString(2));
            l.add(result.getString(2).trim());
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
