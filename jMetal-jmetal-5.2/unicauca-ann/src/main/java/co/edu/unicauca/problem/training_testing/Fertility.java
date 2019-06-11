package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Fertility extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Fertility() throws IOException {
		super(50, new DataSet("src/resources-elm", "fertility.train", 9),
				new DataSet("src/resources-elm", "fertility.test", 9), new Sigmoid(), new MultiplicationMethod(null),
				"Fertility", 3000);
	}
}
