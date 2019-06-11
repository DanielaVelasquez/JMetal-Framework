package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Wilt extends TrainingTestingEvaluator {

	private static final long serialVersionUID = 1L;

	public Wilt() throws IOException {
		super(50, new DataSet("src/resources-elm", "wilt.train", 5), new DataSet("src/resources-elm", "wilt.test", 5),
				new Sigmoid(), new MultiplicationMethod(null), "Wilt", 3000);
	}
}
