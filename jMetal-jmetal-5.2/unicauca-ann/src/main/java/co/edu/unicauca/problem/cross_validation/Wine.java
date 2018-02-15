package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Wine extends AbstractCrossValidationEvaluator {

    public Wine() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Wine", new DataSet("src/resources-elm", "wine.train", 13), new DataSet("src/resources-elm", "wine.test", 13), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
