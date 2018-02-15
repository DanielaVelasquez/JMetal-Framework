package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import java.io.IOException;

public class Leaf extends TrainingTestingEvaluator {

    public Leaf() throws IOException {
        super(50, new DataSet("src/resources-elm", "leaf.train", 14), new DataSet("src/resources-elm", "leaf.test", 14), new Sigmoid(), new MultiplicationMethod(null), "Leaf", 3000);
    }
}
