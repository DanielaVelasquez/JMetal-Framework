package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;

public class Iris extends AbstractCrossValidationEvaluator {

    public Iris() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Iris", new DataSet("src/resources-elm", "iris.train", 4), new DataSet("src/resources-elm", "iris.test", 4), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
