package co.edu.unicauca.exec.experiment;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.experiment.Experiment;
import co.edu.unicauca.refinement_of_parameters.Parameters;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private enum TASK{parameters, execute};
    private TASK task;
    private Experiment experiment;
    private Parameters parameters;
    
    public Run(int pc) throws Exception
    {
        this.pc = pc;
        System.out.println("Hi, I am computer number "+pc);
        propiedades = new HashMap<>();
        propiedades.put(ACABO, false);
        propiedades.put(COMENZAR, true);
        propiedades.put(PARAMETRIZAR, true);
        
        
        connection = DataBaseConnection.getInstancia();
        this.leerPropiedad(ACABO);
        while(!propiedades.get(ACABO))
        {
            this.leerPropiedad(COMENZAR);
            while(propiedades.get(COMENZAR))
            {
                this.leerPropiedad(PARAMETRIZAR);
                task = this.propiedades.get(PARAMETRIZAR) == true?TASK.parameters:TASK.execute;
                
                if(task == TASK.execute)
                {
                    System.out.println("I am running meta-heuristics");
                    experiment = new Experiment(this, pc);
                }
                else
                {
                    System.out.println("I am looking for the perfect parameters");
                    parameters = new Parameters(this, pc);
                }
                
                this.leerPropiedad(COMENZAR);
            }
            this.leerPropiedad(ACABO);
        }
        
    }
    
    private void leerPropiedad(String nombre) throws Exception
    {
        String query = "SELECT valor FROM EJECUTAR WHERE propiedad = '"+nombre+"'";
        ResultSet result = connection.seleccion(query);
        if(!result.next())
            throw new Exception("Value not found on properties: "+nombre);
        int value = result.getInt(1);
        propiedades.put(nombre, value == 0?false: true);
        //System.out.println("Property '"+nombre+"' is set to "+propiedades.get(nombre));
    }
    
    public boolean isExexuting() throws Exception
    {
        this.leerPropiedad(PARAMETRIZAR);
        return propiedades.get(PARAMETRIZAR) == false;
    }
    
    public boolean isDefiningParameters() throws Exception
    {
         this.leerPropiedad(PARAMETRIZAR);
        return propiedades.get(PARAMETRIZAR) == true;
    }
    
    public boolean canRun() throws Exception
    {
        this.leerPropiedad(COMENZAR);
        return propiedades.get(COMENZAR);
    }
    
    public boolean isOver() throws Exception
    {
        this.leerPropiedad(ACABO);
        return propiedades.get(ACABO);
    }
    
    public static void main(String[] args) 
    {
        try 
        {
            int pc = Integer.parseInt(args[0]);
            //-
            new Run(pc);
        } catch (Exception ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}