package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Climatesimulation extends TrainingTestingEvaluator {

    public Climatesimulation() throws IOException {
        super(50, new DataSet("src/resources-elm", "climatesimulation.train", 18), new DataSet("src/resources-elm", "climatesimulation.test", 18), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "ClimateSimulation", 3000);
    }
}