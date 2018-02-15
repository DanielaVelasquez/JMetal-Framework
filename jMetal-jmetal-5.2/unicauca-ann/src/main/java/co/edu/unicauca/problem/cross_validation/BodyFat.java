package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class BodyFat extends AbstractCrossValidationEvaluator {

    public BodyFat() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "BodyFat", new DataSet("src/resources-elm", "bodyFat.train", 14), new DataSet("src/resources-elm", "bodyFat.test", 14), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}