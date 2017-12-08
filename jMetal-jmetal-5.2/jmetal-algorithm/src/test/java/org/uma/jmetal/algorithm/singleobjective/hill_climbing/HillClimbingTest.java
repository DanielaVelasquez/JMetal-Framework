/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.algorithm.singleobjective.hill_climbing;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;

@RunWith(MockitoJUnitRunner.class)
public class HillClimbingTest {
    
    private HillClimbing instance;
    private double penalizeValue;
    @Mock private DoubleProblem problem;
    
    public HillClimbingTest() {
    }
    
    @Before
    public void starUp() 
    {
        penalizeValue = 123.456789;
        instance = new HillClimbingBuilder(problem)
                    .setPenalizeValue(penalizeValue)
                    .build();
        Mockito.when(problem.getNumberOfObjectives()).thenReturn(1);
    }


    /**
     * Test of updateProgress method, of class HillClimbing.
     */
    @Test
    public void testUpdateProgress() {
        System.out.println("updateProgress");
        ReflectionTestUtils.setField(instance, "evaluations", 15);
        double expResult = 16;
        instance.updateProgress();
        double result = instance.getEvaluations();
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(expResult, result, 0);
    }

    /**
     * Test of evaluate method, of class HillClimbing.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        
        DoubleSolution solution = new DefaultDoubleSolution(problem);
        double objective = 5.5555;
        solution.setObjective(0, objective);
        
        //Evaluate one
        instance.setMaxEvaluations(1);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        
        instance.evaluate(solution);
        
        assertEquals(objective, solution.getObjective(0), 0);
        //Penalize the solution
        instance.setMaxEvaluations(1);
        ReflectionTestUtils.setField(instance, "evaluations", 1);
        instance.evaluate(solution);
        
        assertEquals(penalizeValue, solution.getObjective(0), 0);
    }

    /**
     * Test of penalize method, of class HillClimbing.
     */
    @Test
    public void testPenalize() {
        System.out.println("penalize");
        DoubleSolution solution = new DefaultDoubleSolution(problem);
        Mockito.when(solution.getNumberOfObjectives()).thenReturn(1);
        instance.penalize(solution);
        assertEquals(solution.getObjective(0), penalizeValue, 0);
    }
    
}
