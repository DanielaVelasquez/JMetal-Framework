package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.problem.cross_validation.*;
import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Vertebral3C extends TrainingTestingEvaluator {

    public Vertebral3C() throws IOException {
        super(20, new DataSet("src/resources-elm", "vertebral(3c).train", 6), new DataSet("src/resources-elm", "vertebral(3c).test", 6), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Vertebral(3C)", 3000);
    }

}
