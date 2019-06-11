package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class AutoPrice extends TrainingTestingEvaluator {

	private static final long serialVersionUID = 1L;

	public AutoPrice() throws IOException {
		super(50, new DataSet("src/resources-elm", "autoPrice.train", 15),
				new DataSet("src/resources-elm", "autoPrice.test", 15), new Sigmoid(), new MultiplicationMethod(null),
				"AutoPrice", 3000);
	}
}