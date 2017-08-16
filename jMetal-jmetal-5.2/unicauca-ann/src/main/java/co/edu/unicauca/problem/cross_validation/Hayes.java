package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Hayes extends AbstractCrossValidationEvaluator {

    public Hayes() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Hayes", new DataSet("src/resources-elm", "hayes.train", 5), new DataSet("src/resources-elm", "hayes.test", 5), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
