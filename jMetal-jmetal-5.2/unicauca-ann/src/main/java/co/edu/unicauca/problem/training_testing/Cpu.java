package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Cpu extends TrainingTestingEvaluator {
	private static final long serialVersionUID = 1L;

	public Cpu() throws IOException {
		super(50, new DataSet("src/resources-elm", "cpu.train", 6), new DataSet("src/resources-elm", "cpu.test", 6),
				new Sigmoid(), new MultiplicationMethod(null), "Cpu", 3000);
	}
}
