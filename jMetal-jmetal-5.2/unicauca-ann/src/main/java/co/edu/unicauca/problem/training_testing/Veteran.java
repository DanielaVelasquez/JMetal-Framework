package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Veteran extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Veteran() throws IOException {
		super(50, new DataSet("src/resources-elm", "veteran.train", 7),
				new DataSet("src/resources-elm", "veteran.test", 7), new Sigmoid(), new MultiplicationMethod(null),
				"Veteran", 3000);
	}
}
