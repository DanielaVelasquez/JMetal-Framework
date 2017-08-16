package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Letter extends AbstractCrossValidationEvaluator {

    public Letter() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Letter", new DataSet("src/resources-elm", "letter.train", 16), new DataSet("src/resources-elm", "letter.test", 16), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
