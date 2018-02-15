package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Climatesimulation extends AbstractCrossValidationEvaluator {

    public Climatesimulation() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "ClimateSimulation", new DataSet("src/resources-elm", "climatesimulation.train", 18), new DataSet("src/resources-elm", "climatesimulation.test", 18), 10, 50, new Sigmoid(), new MultiplicationMethod(null), 3000);
    }
}