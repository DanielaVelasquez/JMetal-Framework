/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;
import co.edu.unicauca.problem.AbstractELMEvaluator;

/**
 *
 * @author danielavelasquezgarzon
 */
public class Iris extends CrossValidationEvaluator{
    
    public Iris() throws IOException 
    {
        super(AbstractELMEvaluator.EvaluatorType.CV,"Iris",new DataSet("src/resources-elm", "iris.train", 4),new DataSet("src/resources-elm", "iris.test", 4),10,3,new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}));
    }
    
}