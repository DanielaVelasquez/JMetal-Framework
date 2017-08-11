package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;

public class Banknote extends CrossValidationEvaluator
{
    public Banknote() throws Exception
    {
        super(AbstractELMEvaluator.EvaluatorType.CV,"Iris", new DataSet("src/resources-elm", "banknote.train", 4),new DataSet("src/resources-elm", "banknote.test", 4), 10, 20, new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000001}));
    }
}
