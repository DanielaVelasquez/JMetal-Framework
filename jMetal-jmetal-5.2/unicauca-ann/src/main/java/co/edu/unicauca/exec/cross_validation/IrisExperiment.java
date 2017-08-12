/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.exec.cross_validation;

import java.io.IOException;

/**
 *
 * @author danielavelasquezgarzon
 */
public class IrisExperiment extends CrossValidationExperiment{
    
    public IrisExperiment() throws IOException {
        super("co.edu.unicauca.problem.cross_validation.Iris");
    }
    
    public static void main(String[] args) throws Exception 
    {
        new IrisExperiment();
    }
    
}
