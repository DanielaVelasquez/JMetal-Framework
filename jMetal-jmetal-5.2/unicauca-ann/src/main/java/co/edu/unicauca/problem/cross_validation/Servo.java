/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

/**
 *
 * @author Daniel
 */
public class Servo extends AbstractCrossValidationEvaluator {

    public Servo() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "Servo", new DataSet("src/resources-elm", "servo.train", 4), new DataSet("src/resources-elm", "servo.test", 4), 5, 50, new Sigmoid(), new RidgeRegressionTheory(new double[]{0, 0000001}), 3000);
    }
}
