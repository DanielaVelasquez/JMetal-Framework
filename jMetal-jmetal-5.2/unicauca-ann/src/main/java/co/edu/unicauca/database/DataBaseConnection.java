package co.edu.unicauca.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class DataBaseConnection 
{
    private Connection connection;
    private static DataBaseConnection instancia;
    
    //constructor privado se hace uso del patron singleton
    private DataBaseConnection()
    {
        
    }
    
    //Metodo que conecta a la base de datos
    //Retorna un objeto de tipo Connection y lanza una exception si no logra conectarse
    public void conectar() 
    {
        //Connect to database
        String hostName = "sql7001.site4now.net";
        String dbName = "DB_A2D155_primerExperimento";
        String user = "DB_A2D155_primerExperimento_admin";
        String password = "Experimento123";
        
        /*String hostName = "localhost";
        String dbName = "elm-experimentoRegresion";
        String user = "elm";
        String password = "elm";*/
        
        //String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);        
        //String url = String.format("jdbc:sqlserver://localhost:1433;database=elm-experiment;user=elm;password=elm;");
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;", hostName, dbName, user, password);        

        try {
                connection = DriverManager.getConnection(url);
                //String schema = connection.getSchema();
                /*System.out.println("Successful connection - Schema: " + schema);

                System.out.println("Query data example:");
                System.out.println("=========================================");

                // Create and execute a SELECT SQL statement.
                String selectSql = "INsert into ejemplo values('Ejemplo intersión')";

                try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(selectSql)) {

                        // Print results from select statement
                        System.out.println("Top 20 categories:");
                        while (resultSet.next())
                        {
                            System.out.println(resultSet.getString(1) + " "
                                + resultSet.getString(2));
                        }
                }*/
        }
        catch (Exception e) {
                e.printStackTrace();
        }
    }
    
    //Metodo que obtiene la unica instancia creada del objeto, se declara estatica
    public static DataBaseConnection getInstancia() throws Exception
    {
        if (instancia == null)
        {
            instancia = new DataBaseConnection();
            
            try
            {
                instancia.conectar();
            }
            catch(Exception e)
            {
                throw new Exception("Error al conectar con la base de datos : " + e.getMessage());
            }
        }

        return instancia;
    }
    
    //El parametro consulta es la consulta en sql que se envia para que se ejecute
    //El atributo PreparedStatement es para preparar un query para poderlo ejecutar luego y guardar la informacion en el ResultSet
    //El atributo ResultSet es la tabla de registros que se devuelve con la informacion de la consulta
    //Retorna un resultset que contiene la tabla con los resultados de la consulta, en caso de ser una modificacion se retorna null
    public ResultSet seleccion(String query) throws Exception
    {
        try
        {
            //Esta sentencia (System.out.println) no es necesaria, es solo para verificar cuales son los query que se estan ejecutando

            //System.out.println("query: " + query);
            PreparedStatement preparar = connection.prepareStatement(query);
            ResultSet resultado = preparar.executeQuery();

            return resultado;
        }
        catch(Exception e)
        {
            throw new Exception("Hubo un error ejecutando el query : '" + query + "' mensaje de error: \"" + e.getMessage() + "\"");
        }
    }
    
    public void modificacion(String query) throws Exception
    {
        try
        {
            //Esta sentencia (System.out.println) no es necesaria, es solo para verificar cuales son los query que se estan ejecutando

            //System.out.println("query: " + query);
            PreparedStatement preparar = connection.prepareStatement(query);
            preparar.executeUpdate();
            
        }
        catch(Exception e)
        {
            throw new Exception("Hubo un error ejecutando el query : '" + query + "' mensaje de error: \"" + e.getMessage() + "\"");
        }
    }
}