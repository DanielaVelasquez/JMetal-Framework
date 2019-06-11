package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Shuttle extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Shuttle() throws IOException {
		super(50, new DataSet("src/resources-elm", "shuttle.train", 9),
				new DataSet("src/resources-elm", "shuttle.test", 9), new Sigmoid(), new MultiplicationMethod(null),
				"Shuttle", 3000);
	}
}
