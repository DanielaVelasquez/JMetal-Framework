package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Wdbc extends TrainingTestingEvaluator {

    public Wdbc() throws IOException {
        super(50, new DataSet("src/resources-elm", "wdbc.train", 30), new DataSet("src/resources-elm", "wdbc.test", 30), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Wdbc", 3000);
    }

}
