package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.problem.cross_validation.*;
import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Connectionist extends TrainingTestingEvaluator {

    public Connectionist() throws IOException {
        super(20, new DataSet("src/resources-elm", "connectionist.train", 60), new DataSet("src/resources-elm", "connectionist.test", 60), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Connectionist");
    }

}
