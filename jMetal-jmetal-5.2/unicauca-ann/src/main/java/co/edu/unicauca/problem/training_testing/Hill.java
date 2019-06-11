package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Hill extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Hill() throws IOException {
		super(50, new DataSet("src/resources-elm", "hill.train", 100),
				new DataSet("src/resources-elm", "hill.test", 100), new Sigmoid(), new MultiplicationMethod(null),
				"Hill", 3000);
	}
}
