package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Haberman extends TrainingTestingEvaluator {

    public Haberman() throws IOException {
        super(50, new DataSet("src/resources-elm", "haberman.train", 3), new DataSet("src/resources-elm", "haberman.test", 3), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Haberman", 3000);
    }

}
