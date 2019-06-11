package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Diabetes extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Diabetes() throws IOException {
		super(50, new DataSet("src/resources-elm", "diabetes.train", 8),
				new DataSet("src/resources-elm", "diabetes.test", 8), new Sigmoid(), new MultiplicationMethod(null),
				"Diabetes", 3000);
	}
}
