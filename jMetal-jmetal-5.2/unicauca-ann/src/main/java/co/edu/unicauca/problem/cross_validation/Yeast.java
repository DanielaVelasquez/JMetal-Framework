package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Yeast extends AbstractCrossValidationEvaluator {

    public Yeast() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Yeast", new DataSet("src/resources-elm", "yeast.train", 8), new DataSet("src/resources-elm", "yeast.test", 8), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}