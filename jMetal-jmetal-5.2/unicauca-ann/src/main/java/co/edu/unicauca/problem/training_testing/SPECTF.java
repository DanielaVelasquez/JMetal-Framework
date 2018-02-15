package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class SPECTF extends TrainingTestingEvaluator {

    public SPECTF() throws IOException {
        super(50, new DataSet("src/resources-elm", "SPECTF.train", 44), new DataSet("src/resources-elm", "SPECTF.test", 44), new Sigmoid(), new MultiplicationMethod(null), "SPECTF", 3000);
    }
}
