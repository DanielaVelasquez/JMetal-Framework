package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Vertebral3C extends AbstractCrossValidationEvaluator {

    public Vertebral3C() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Vertebral(3c)", new DataSet("src/resources-elm", "vertebral(3c).train", 6), new DataSet("src/resources-elm", "vertebral(3c).test", 6), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
