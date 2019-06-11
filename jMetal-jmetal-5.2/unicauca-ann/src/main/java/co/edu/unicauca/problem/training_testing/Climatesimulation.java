package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Climatesimulation extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Climatesimulation() throws IOException {
		super(50, new DataSet("src/resources-elm", "climatesimulation.train", 18),
				new DataSet("src/resources-elm", "climatesimulation.test", 18), new Sigmoid(),
				new MultiplicationMethod(null), "ClimateSimulation", 3000);
	}
}