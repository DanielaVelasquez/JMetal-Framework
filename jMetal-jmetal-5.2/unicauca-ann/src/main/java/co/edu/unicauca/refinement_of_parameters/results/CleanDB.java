package co.edu.unicauca.refinement_of_parameters.results;

import co.edu.unicauca.database.DataBaseConnection;
import java.sql.ResultSet;

public class CleanDB
{

    public static void main(String[] args) throws Exception
    {
        DataBaseConnection connection = DataBaseConnection.getInstancia();
        int eliminados = 0;
        String query = "select taskResults, t2.c\n" +
                        "from results AS R\n" +
                        "inner join \n" +
                        "(select count(taskResults) as c,taskResults  AS TR\n" +
                        "from results\n" +
                        "group by taskResults) as t2 ON T2.TR  = R.taskResults\n" +
                        "WHERE T2.C > 1";
        ResultSet tareas = connection.seleccion(query);
        while(tareas.next())
        {
            int idTask = tareas.getInt(1);
            
            query = "SELECT idResults FROM results WHERE taskResults = "+idTask;
            ResultSet eliminar = connection.seleccion(query);
            eliminar.next();
            while(eliminar.next())
            {
                query = "DELETE results WHERE idResults = "+eliminar.getInt(1);
                connection.modificacion(query);
                eliminados++;
            }

        }
        System.out.println("Eliminados: "+1);
        
    }
    
}
