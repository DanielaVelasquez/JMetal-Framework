package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Shuttle extends AbstractCrossValidationEvaluator {

    public Shuttle() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "shuttle", new DataSet("src/resources-elm", "shuttle.train", 9), new DataSet("src/resources-elm", "shuttle.test", 9), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}