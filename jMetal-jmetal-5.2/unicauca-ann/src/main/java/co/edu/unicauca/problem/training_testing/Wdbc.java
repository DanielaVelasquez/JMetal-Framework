package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Wdbc extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Wdbc() throws IOException {
		super(50, new DataSet("src/resources-elm", "wdbc.train", 30), new DataSet("src/resources-elm", "wdbc.test", 30),
				new Sigmoid(), new MultiplicationMethod(null), "Wdbc", 3000);
	}
}
