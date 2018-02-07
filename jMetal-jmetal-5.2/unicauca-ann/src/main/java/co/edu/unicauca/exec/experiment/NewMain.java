
package co.edu.unicauca.exec.experiment;

import co.edu.unicauca.database.DataBaseConnection;


public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        DataBaseConnection connection = DataBaseConnection.getInstancia();
        while(true)    
        {
            try {
                
                connection.modificacion("UPDATE ejecutar SET valor = 1 WHERE propiedad = 'comenzar'");
            } catch (Exception ex) {
                connection.reiniciarConexion();
            }
        }
        
    }
    
}
