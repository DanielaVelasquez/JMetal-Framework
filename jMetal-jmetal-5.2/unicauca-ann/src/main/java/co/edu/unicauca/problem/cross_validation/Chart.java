package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Chart extends AbstractCrossValidationEvaluator {

    public Chart() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Chart", new DataSet("src/resources-elm", "chart.train", 60), new DataSet("src/resources-elm", "chart.test", 60), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
