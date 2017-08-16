package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;


public class Banknote extends TrainingTestingEvaluator
{
    public Banknote() throws IOException 
    {
        super(20, new DataSet("src/resources-elm", "banknote.train", 4), new DataSet("src/resources-elm", "banknote.test", 4), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Banknote", 3000);
    }
}