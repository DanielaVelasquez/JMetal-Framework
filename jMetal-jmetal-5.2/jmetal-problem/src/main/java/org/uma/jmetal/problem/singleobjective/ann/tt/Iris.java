/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.problem.singleobjective.ann.tt;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.function.Function;
import co.edu.unicauca.function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

/**
 *
 * @author danielavelasquezgarzon
 */
public class Iris extends TrainingTestingEvaluator{
    
    public Iris() throws IOException 
    {
        super(3, new DataSet("unicauca-ann/src/resources", "iris.train", 4), new DataSet("unicauca-ann/src/resources", "iris.test", 4), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Iris");
    }
    
}
