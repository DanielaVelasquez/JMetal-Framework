package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Indian extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Indian() throws IOException {
		super(50, new DataSet("src/resources-elm", "indian.train", 10),
				new DataSet("src/resources-elm", "indian.test", 10), new Sigmoid(), new MultiplicationMethod(null),
				"Indian", 3000);
	}
}
