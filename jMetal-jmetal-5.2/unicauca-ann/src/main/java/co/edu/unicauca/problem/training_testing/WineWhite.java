package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.problem.cross_validation.*;
import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;

/**
 *
 * @author danielavelasquezgarzon
 */
public class WineWhite extends TrainingTestingEvaluator {

    public WineWhite() throws IOException {
        super(20, new DataSet("src/resources-elm", "wine(white).train", 11), new DataSet("src/resources-elm", "wine(white).test", 11), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Wine(White)");
    }

}
