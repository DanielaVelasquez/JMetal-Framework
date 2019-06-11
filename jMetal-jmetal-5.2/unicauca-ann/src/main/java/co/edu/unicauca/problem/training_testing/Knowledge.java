package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Knowledge extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Knowledge() throws IOException {
		super(50, new DataSet("src/resources-elm", "knowledge.train", 5),
				new DataSet("src/resources-elm", "knowledge.test", 5), new Sigmoid(), new MultiplicationMethod(null),
				"Knowledge", 3000);
	}
}
