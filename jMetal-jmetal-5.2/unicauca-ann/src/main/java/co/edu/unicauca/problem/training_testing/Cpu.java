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
public class Cpu extends TrainingTestingEvaluator{
    
    public Cpu() throws IOException 
    {
        super(20, new DataSet("src/resources-elm", "cpu.train", 6), new DataSet("src/resources-elm", "cpu.test", 6), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Cpu", 3000);
    }
}
