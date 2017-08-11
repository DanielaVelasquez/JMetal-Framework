package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class glass extends CrossValidationEvaluator {

    public glass() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "glass", new DataSet("src/resources-elm", "glass.train", 9), new DataSet("src/resources-elm", "glass.test", 9), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
