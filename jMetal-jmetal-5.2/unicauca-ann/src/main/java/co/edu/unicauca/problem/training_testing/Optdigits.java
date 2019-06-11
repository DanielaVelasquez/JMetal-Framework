package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Optdigits extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Optdigits() throws IOException {
		super(50, new DataSet("src/resources-elm", "optdigits.train", 64),
				new DataSet("src/resources-elm", "optdigits.test", 64), new Sigmoid(), new MultiplicationMethod(null),
				"Opdigits", 3000);
	}
}
