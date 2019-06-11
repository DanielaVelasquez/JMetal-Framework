package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Zoo extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Zoo() throws IOException {
		super(50, new DataSet("src/resources-elm", "zoo.train", 16), new DataSet("src/resources-elm", "zoo.test", 16),
				new Sigmoid(), new MultiplicationMethod(null), "Zoo", 3000);
	}
}
