package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Planning extends TrainingTestingEvaluator {

    public Planning() throws IOException {
        super(50, new DataSet("src/resources-elm", "planning.train", 12), new DataSet("src/resources-elm", "planning.test", 12), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Planning", 3000);
    }

}
