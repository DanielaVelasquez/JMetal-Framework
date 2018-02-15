package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Ionosphere extends AbstractCrossValidationEvaluator {

    public Ionosphere() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Ionosphere", new DataSet("src/resources-elm", "ionosphere.train", 34), new DataSet("src/resources-elm", "ionosphere.test", 34), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
