package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Diabetes extends AbstractCrossValidationEvaluator {

    public Diabetes() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Diabetes", new DataSet("src/resources-elm", "diabetes.train", 8), new DataSet("src/resources-elm", "diabetes.test", 8), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
