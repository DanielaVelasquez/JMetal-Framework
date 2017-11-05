package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Hayes extends TrainingTestingEvaluator {

    public Hayes() throws IOException {
        super(50, new DataSet("src/resources-elm", "hayes.train", 5), new DataSet("src/resources-elm", "hayes.test", 5), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Hayes", 3000);
    }

}
