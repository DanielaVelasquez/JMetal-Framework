package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Cpu extends AbstractCrossValidationEvaluator {

    public Cpu() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Cpu", new DataSet("src/resources-elm", "cpu.train", 6), new DataSet("src/resources-elm", "cpu.test", 6), 5, 50, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 0000001}), 3000);
    }
}
