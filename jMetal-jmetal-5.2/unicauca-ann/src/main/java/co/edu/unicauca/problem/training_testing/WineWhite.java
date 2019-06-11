package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class WineWhite extends TrainingTestingEvaluator {

	private static final long serialVersionUID = 1L;

	public WineWhite() throws IOException {
		super(50, new DataSet("src/resources-elm", "wine(white).train", 11),
				new DataSet("src/resources-elm", "wine(white).test", 11), new Sigmoid(), new MultiplicationMethod(null),
				"Wine(White)", 3000);
	}
}
