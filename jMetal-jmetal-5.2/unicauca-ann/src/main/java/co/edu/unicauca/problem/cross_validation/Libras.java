package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Libras extends AbstractCrossValidationEvaluator {

    public Libras() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Libras", new DataSet("src/resources-elm", "libras.train", 90), new DataSet("src/resources-elm", "libras.test", 90), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
