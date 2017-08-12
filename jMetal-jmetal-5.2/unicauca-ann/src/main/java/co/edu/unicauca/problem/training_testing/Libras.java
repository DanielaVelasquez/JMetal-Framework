package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.problem.cross_validation.*;
import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Libras extends TrainingTestingEvaluator {

    public Libras() throws IOException {
        super(20, new DataSet("src/resources-elm", "libras.train", 90), new DataSet("src/resources-elm", "libras.test", 90), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Libras");
    }

}
