package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;

public class WineWhite extends AbstractCrossValidationEvaluator {

    public WineWhite() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Wine(White)", new DataSet("src/resources-elm", "wine(white).train", 11), new DataSet("src/resources-elm", "wine(white).test", 11), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
