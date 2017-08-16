package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Blood extends AbstractCrossValidationEvaluator {

    public Blood() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Blood", new DataSet("src/resources-elm", "blood.train", 4), new DataSet("src/resources-elm", "blood.test", 4), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
