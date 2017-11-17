/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

/**
 *
 * @author Daniel
 */
public class BodyFat extends TrainingTestingEvaluator{
    
    public BodyFat() throws IOException 
    {
        super(20, new DataSet("src/resources-elm", "bodyFat.train", 14), new DataSet("src/resources-elm", "bodyFat.test", 14), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "BodyFat", 3000);
    }
}
