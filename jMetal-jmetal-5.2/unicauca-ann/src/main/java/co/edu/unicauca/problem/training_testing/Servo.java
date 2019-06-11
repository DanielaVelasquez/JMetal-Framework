package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Servo extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Servo() throws IOException {
		super(50, new DataSet("src/resources-elm", "servo.train", 4), new DataSet("src/resources-elm", "servo.test", 4),
				new Sigmoid(), new MultiplicationMethod(null), "Servo", 3000);
	}
}
