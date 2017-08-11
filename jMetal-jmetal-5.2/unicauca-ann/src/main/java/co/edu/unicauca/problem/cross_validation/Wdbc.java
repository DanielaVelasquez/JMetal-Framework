package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Wdbc extends AbstractCrossValidationEvaluator {

    public Wdbc() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "wdbc", new DataSet("src/resources-elm", "wdbc.train", 30), new DataSet("src/resources-elm", "wdbc.test", 30), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
