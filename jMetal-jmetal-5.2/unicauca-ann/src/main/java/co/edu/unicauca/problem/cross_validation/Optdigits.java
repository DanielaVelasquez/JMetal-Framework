package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Optdigits extends AbstractCrossValidationEvaluator{

    public Optdigits() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Optdigits", new DataSet("src/resources-elm", "optdigits.train", 64), new DataSet("src/resources-elm", "optdigits.test", 64), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
