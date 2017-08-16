package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;

/**
 *
 * @author danielavelasquezgarzon
 */
public class WineWhite extends AbstractCrossValidationEvaluator {

    public WineWhite() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Wine(White)", new DataSet("src/resources-elm", "wine(white).train", 11), new DataSet("src/resources-elm", "wine(white).test", 11), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }

}
