package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Yeast extends TrainingTestingEvaluator {

    public Yeast() throws IOException {
        super(50, new DataSet("src/resources-elm", "yeast.train", 8), new DataSet("src/resources-elm", "yeast.test", 8), new Sigmoid(), new MultiplicationMethod(null), "Yeast", 3000);
    }
}
