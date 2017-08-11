package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class chart extends CrossValidationEvaluator {

    public chart() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "chart", new DataSet("src/resources-elm", "chart.train", 60), new DataSet("src/resources-elm", "chart.test", 60), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
