package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Soybean extends AbstractCrossValidationEvaluator {

    public Soybean() throws IOException {
        //miss values
        super(AbstractELMEvaluator.EvaluatorType.CV, "Soybean", new DataSet("src/resources-elm", "soybean.train", 35), new DataSet("src/resources-elm", "soybean.test", 35), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
