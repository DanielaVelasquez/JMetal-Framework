package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Wilt extends AbstractCrossValidationEvaluator {

    public Wilt() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Wilt", new DataSet("src/resources-elm", "wilt.train", 5), new DataSet("src/resources-elm", "wilt.test", 5), 5, 50, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
