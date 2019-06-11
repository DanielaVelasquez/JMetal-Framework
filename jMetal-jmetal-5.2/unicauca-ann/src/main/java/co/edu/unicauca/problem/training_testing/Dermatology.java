package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Dermatology extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Dermatology() throws IOException {
		super(50, new DataSet("src/resources-elm", "dermatology.train", 34),
				new DataSet("src/resources-elm", "dermatology.test", 34), new Sigmoid(), new MultiplicationMethod(null),
				"Dermatology", 3000);
	}
}
