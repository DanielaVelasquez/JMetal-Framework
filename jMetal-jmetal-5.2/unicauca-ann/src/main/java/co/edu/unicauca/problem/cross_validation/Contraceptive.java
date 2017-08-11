
package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;


public class Contraceptive extends CrossValidationEvaluator {

    public Contraceptive() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "contraceptive", new DataSet("src/resources-elm", "contraceptive.train", 9), new DataSet("src/resources-elm", "contraceptive.test", 9), 10, 3, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}));
    }

}
