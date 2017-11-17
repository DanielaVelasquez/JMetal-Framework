package co.edu.unicauca.exec.cross_validation.harmony;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 *
 */
public class RunCMD {

    public static void main(String[] args) {

        ControladorExp exp = new ControladorExp();
        JMetalRandom.getInstance().setRandomGenerator(null);//necesaria para ejecutar los experimentos

        try {
            exp.ejecutar(args);
            
        } catch (IOException ex) {
            Logger.getLogger(RunCMD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
