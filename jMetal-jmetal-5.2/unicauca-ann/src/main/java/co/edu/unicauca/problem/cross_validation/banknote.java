package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;


public class banknote extends CrossValidationEvaluator {

    public banknote() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "banknote", new DataSet("src/resources-elm", "banknote.train", 4), new DataSet("src/resources-elm", "banknote.test", 4), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
