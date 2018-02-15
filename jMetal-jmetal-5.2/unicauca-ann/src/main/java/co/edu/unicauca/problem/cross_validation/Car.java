package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Car extends AbstractCrossValidationEvaluator {

    public Car() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Car", new DataSet("src/resources-elm", "car.train", 6), new DataSet("src/resources-elm", "car.test", 6), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
