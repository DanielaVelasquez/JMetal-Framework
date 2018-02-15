package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Knowledge extends AbstractCrossValidationEvaluator {

    public Knowledge() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Knowledge", new DataSet("src/resources-elm", "knowledge.train", 5), new DataSet("src/resources-elm", "knowledge.test", 5), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
