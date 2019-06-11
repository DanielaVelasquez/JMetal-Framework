
package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Contraceptive extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Contraceptive() throws IOException {
		super(50, new DataSet("src/resources-elm", "contraceptive.train", 9),
				new DataSet("src/resources-elm", "contraceptive.test", 9), new Sigmoid(),
				new MultiplicationMethod(null), "Contraseptive", 3000);
	}
}
