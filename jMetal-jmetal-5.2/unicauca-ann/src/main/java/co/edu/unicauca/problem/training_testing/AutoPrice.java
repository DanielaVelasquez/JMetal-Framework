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
 * @author fietp
 */
public class AutoPrice extends TrainingTestingEvaluator{
    
    public AutoPrice() throws IOException 
    {
        super(20, new DataSet("src/resources-elm", "autoPrice.train", 15), new DataSet("src/resources-elm", "autoPrice.test", 15), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "AutoPrice", 3000);
    }
}