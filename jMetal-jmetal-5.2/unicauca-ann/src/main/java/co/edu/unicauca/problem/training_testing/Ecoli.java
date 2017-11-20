package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Ecoli extends TrainingTestingEvaluator {

    public Ecoli() throws IOException {
        super(50, new DataSet("src/resources-elm", "ecoli.train", 7), new DataSet("src/resources-elm", "ecoli.test", 7), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Ecoli", 3000);
    }

}
