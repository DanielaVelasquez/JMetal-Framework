package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Cpu extends AbstractCrossValidationEvaluator {

    public Cpu() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Cpu", new DataSet("src/resources-elm", "cpu.train", 6), new DataSet("src/resources-elm", "cpu.test", 6), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}
