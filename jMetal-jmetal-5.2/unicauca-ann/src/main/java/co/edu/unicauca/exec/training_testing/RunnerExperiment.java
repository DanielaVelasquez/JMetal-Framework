/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.exec.training_testing;


import java.io.IOException;

/**
 *
 * @author danielavelasquezgarzon
 */
public class RunnerExperiment extends TrainingTestingExperiment{
    
    public RunnerExperiment() throws IOException {
        super("co.edu.unicauca.problem.training_testing.Haberman");
    }
    
    public static void main(String[] args) throws Exception 
    {
        new RunnerExperiment();
    }
    
}
