package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Cardiotocography extends CrossValidationEvaluator {

    public Cardiotocography() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Cardiotocography", new DataSet("src/resources-elm", "Cardiotocography.train", 21), new DataSet("src/resources-elm", "Cardiotocography.test", 21), 5, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
