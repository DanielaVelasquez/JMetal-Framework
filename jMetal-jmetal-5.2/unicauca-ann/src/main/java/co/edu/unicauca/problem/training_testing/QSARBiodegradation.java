package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class QSARBiodegradation extends TrainingTestingEvaluator {

    public QSARBiodegradation() throws IOException {
        super(50, new DataSet("src/resources-elm", "QSARBiodegradation.train", 41), new DataSet("src/resources-elm", "QSARBiodegradation.test", 41), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "QSARBiodegradation", 3000);
    }

}
