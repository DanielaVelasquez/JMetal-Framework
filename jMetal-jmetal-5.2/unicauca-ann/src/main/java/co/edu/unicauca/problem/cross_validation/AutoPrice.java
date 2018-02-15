package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class AutoPrice extends AbstractCrossValidationEvaluator {

    public AutoPrice() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "AutoPrice", new DataSet("src/resources-elm", "autoPrice.train", 15), new DataSet("src/resources-elm", "autoPrice.test", 15), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }

}