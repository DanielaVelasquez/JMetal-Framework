package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Connectionist extends AbstractCrossValidationEvaluator {

    public Connectionist() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Connectionist", new DataSet("src/resources-elm", "connectionist.train", 60), new DataSet("src/resources-elm", "connectionist.test", 60), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
