package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class optdigits extends CrossValidationEvaluator{

    public optdigits() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "optdigits", new DataSet("src/resources-elm", "optdigits.train", 64), new DataSet("src/resources-elm", "optdigits.test", 64), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
