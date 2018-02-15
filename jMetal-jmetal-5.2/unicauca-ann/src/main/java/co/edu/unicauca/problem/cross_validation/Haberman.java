package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Haberman extends AbstractCrossValidationEvaluator {

    public Haberman() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Haberman", new DataSet("src/resources-elm", "haberman.train", 3), new DataSet("src/resources-elm", "haberman.test", 3), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
