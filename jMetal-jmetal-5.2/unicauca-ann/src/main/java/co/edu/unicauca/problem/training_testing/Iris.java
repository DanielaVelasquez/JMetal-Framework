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
 * @author danielavelasquezgarzon
 */
public class Iris extends TrainingTestingEvaluator{
    
    public Iris() throws IOException 
    {
        super(3, new DataSet("src/resources-elm", "iris.train", 4), new DataSet("src/resources-elm", "iris.test", 4), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Iris");
    }
    
}
