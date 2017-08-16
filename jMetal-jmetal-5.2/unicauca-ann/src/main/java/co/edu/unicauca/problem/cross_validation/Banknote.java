package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;


public class Banknote extends AbstractCrossValidationEvaluator {

    public Banknote() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Banknote", new DataSet("src/resources-elm", "banknote.train", 4), new DataSet("src/resources-elm", "banknote.test", 4), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0,0000001}), 3000);
    }

}
