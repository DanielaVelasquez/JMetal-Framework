package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.problem.cross_validation.*;
import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Shuttle extends TrainingTestingEvaluator {

    public Shuttle() throws IOException {
        super(20, new DataSet("src/resources-elm", "shuttle.train", 9), new DataSet("src/resources-elm", "shuttle.test", 9), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Shuttle", 3000);
    }

}
