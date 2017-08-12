package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Fertility extends AbstractCrossValidationEvaluator {

    public Fertility() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Fertility", new DataSet("src/resources-elm", "fertility.train", 9), new DataSet("src/resources-elm", "fertility.test", 9), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
