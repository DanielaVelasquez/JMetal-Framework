package co.edu.unicauca.exec.experiment;

import co.edu.unicauca.database.DataBaseConnection;
import java.sql.ResultSet;
import java.util.HashMap;

public class Run 
{
    /**
    * Determina si ya acabo de realizar la actividad
    */
    private static final String ACABO = "acabo";
    /**
    * Determina si debe empezar o no con la actividad
    */
    private static final String COMENZAR = "comenzar";
    /**
    * Determina si está realizando afinamiento de parametros: true
    * En caso contrario está ejecutando los algoritmos: false
    */
    private static final String PARAMETRIZAR = "parametrizar";
    
    private HashMap<String, Boolean> propiedades;
    private int pc;
    private DataBaseConnection connection;
    
    public Run(int pc)
    {
        this.pc = pc;
        System.out.println("Hi, I am computer number "+pc);
        propiedades.put(ACABO, false);
        propiedades.put(COMENZAR, true);
        propiedades.put(PARAMETRIZAR, true);
        
        try
        {
            connection = DataBaseConnection.getInstancia();
            this.leerPropiedad(ACABO);
            while(!propiedades.get(ACABO))
            {
                this.leerPropiedad(COMENZAR);
                while(propiedades.get(COMENZAR))
                {
                    
                    this.leerPropiedad(COMENZAR);
                }
                this.leerPropiedad(ACABO);
            }
        }
        catch(Exception e)
        {
            
        }
    }
    
    private void leerPropiedad(String nombre) throws Exception
    {
        String query = "SELECT valor FROM EJECUTAR WHERE propiedad = '"+nombre+"'";
        ResultSet result = connection.seleccion(query);
        if(result.next())
            throw new Exception("Value not found on properties: "+nombre);
        int value = result.getInt(1);
        propiedades.put(nombre, value == 0?false: true);
    }
    
    public static void main(String[] args) 
    {
        
    }
    
}
