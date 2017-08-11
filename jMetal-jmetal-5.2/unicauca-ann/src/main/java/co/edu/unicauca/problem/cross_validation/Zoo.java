package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Zoo extends CrossValidationEvaluator {

    public Zoo() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "zoo", new DataSet("src/resources-elm", "zoo.train", 16), new DataSet("src/resources-elm", "zoo.test", 16), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
