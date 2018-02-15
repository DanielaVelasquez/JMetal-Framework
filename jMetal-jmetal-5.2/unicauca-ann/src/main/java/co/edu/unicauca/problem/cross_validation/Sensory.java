package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Sensory extends AbstractCrossValidationEvaluator {

    public Sensory() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Sensory", new DataSet("src/resources-elm", "sensory.train", 11), new DataSet("src/resources-elm", "sensory.test", 11), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
