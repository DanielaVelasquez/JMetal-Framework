
package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.problem.cross_validation.*;
import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;


public class Contraceptive extends TrainingTestingEvaluator {

    public Contraceptive() throws IOException {
        super(20, new DataSet("src/resources-elm", "contraseptive.train", 9), new DataSet("src/resources-elm", "contraseptive.test", 9), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Contraseptive");
    }

}