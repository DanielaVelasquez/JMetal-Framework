package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

public class Climatesimulation extends AbstractCrossValidationEvaluator {

    public Climatesimulation() throws IOException {
        //Distribucion diferente al paper
        super(AbstractELMEvaluator.EvaluatorType.CV, "ClimateSimulation", new DataSet("src/resources-elm", "climatesimulation.train", 18), new DataSet("src/resources-elm", "climatesimulation.test", 18), 5, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 000001}), 3000);
    }
}