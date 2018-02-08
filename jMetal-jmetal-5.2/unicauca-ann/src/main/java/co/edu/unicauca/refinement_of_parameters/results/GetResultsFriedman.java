
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


public class GetResultsFriedman 
{
    private static HashMap<Integer,String> datasets;
    private static DataBaseConnection db;
    private static double[][] values;
    private static int configuration[];
    private static int totalDataSets;
    private static String[][] combinations;
    
    private static HashMap<Integer,String> configuracionesVocabulario;
    private static String NOMBRE_ARCHIVO ;
    private static boolean dividir = false;
    private static String ESCRIBIR = "";
    private static String ESCRIBIR2 = "";
    private static ArrayList<String> algoritmos = new ArrayList();
    private static ArrayList<String> tipos = new ArrayList();
    private static ArrayList<Integer> configuracionesEjecutar = new  ArrayList();
    private static void cargar()
    {
        algoritmos.add("MOS");
        //algoritmos.add("IHDELS");
        //algoritmos.add("DECC_G");
        //tipos.add("cv");
        tipos.add("tt");
    }
    public static void main(String[] args) throws Exception
    {
       db = DataBaseConnection.getInstancia();
       cargar();
       datasets = new HashMap<>();
       for(String algorithm: algoritmos)
       {
           for(String type:tipos)
           {
                dividir = true;
                if(algorithm.equals("MOS"))
                    dividir = false;
                String problem = "clasificacion";
                NOMBRE_ARCHIVO = "afinamiento-parametros-friedman/"+algorithm+"/"+problem+"/"+type+"/"+problem;
                //System.out.println(NOMBRE_ARCHIVO);
                ESCRIBIR = "";
                ESCRIBIR2 = "";
                NOMBRE_ARCHIVO += "-"+algorithm.toLowerCase();
                NOMBRE_ARCHIVO += "-"+type;
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
                ESCRIBIR2 +="DataSet,";
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
                    configuracionesEjecutar.add(idConfiguration);
                    configuracionesVocabulario.put(idConfiguration,configurationString);
                    if(dividir)
                    {
                        if(totalCombinations / 2 > j)
                        {
                            if( j + 1 == totalCombinations / 2)
                                 ESCRIBIR += configurationString;
                             else
                                 ESCRIBIR += configurationString + ",";
                        }
                        else
                        {
                            if( j + 1 == totalCombinations)
                                 ESCRIBIR2 += configurationString;
                             else
                                 ESCRIBIR2 += configurationString + ",";
                        }
                    }
                    else
                    {
                        if( j + 1 == totalCombinations)
                             ESCRIBIR += configurationString;
                         else
                             ESCRIBIR += configurationString + ",";
                    }
                    j++;

                }

                ESCRIBIR += "\n";

                ESCRIBIR2 += "\n";
                DecimalFormat df = new DecimalFormat("#.#####");
                 for(Map.Entry<Integer,String> entry:datasets.entrySet())
                 {
                     int i = 0;
                     ESCRIBIR +=entry.getValue().trim()+",";
                     ESCRIBIR2 +=entry.getValue().trim()+",";

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
                        if(dividir)
                        {
                            if(totalCombinations / 2 > i)
                             {
                                 if(i + 1 == totalCombinations / 2)
                                      ESCRIBIR += replace ;
                                  else
                                      ESCRIBIR += replace +",";
                             }
                             else
                             {
                                 if(i + 1 == configuracionesVocabulario.size())
                                      ESCRIBIR2 += replace ;
                                  else
                                      ESCRIBIR2 += replace +",";
                             }
                        }
                        else
                        {
                            if(i + 1 == configuracionesVocabulario.size())
                                 ESCRIBIR += replace ;
                             else
                                 ESCRIBIR += replace +",";
                        }


                         i++;

                     }

                     ESCRIBIR +="\n";
                     ESCRIBIR2 +="\n";
                 }


                 System.out.println(ESCRIBIR);
                 System.out.println("---------------");
                 System.out.println(ESCRIBIR2);

                 writeFile();
           }
       }
       
       

    }
    private static void writeFile()
    {
        BufferedWriter bw = null;
        try {
            if(dividir)
            {
                File archivo1 = new File(NOMBRE_ARCHIVO+"-p1"+".csv");
                System.out.println(NOMBRE_ARCHIVO);
                bw = new BufferedWriter(new FileWriter(archivo1));
                bw.write(ESCRIBIR);
                bw.close();
                
                File archivo2 = new File(NOMBRE_ARCHIVO+"-p2"+".csv");
                bw = new BufferedWriter(new FileWriter(archivo2));
                bw.write(ESCRIBIR2);
            }
            else
            {
                File archivo = new File(NOMBRE_ARCHIVO+".csv");
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(ESCRIBIR);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(GetResultsFriedman.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(GetResultsFriedman.class.getName()).log(Level.SEVERE, null, ex);
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
