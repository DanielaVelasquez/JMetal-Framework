/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.algorithm.singleobjective.solis_and_wets;

import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWets;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.comparator.FrobeniusComparator;

@RunWith(MockitoJUnitRunner.class)
public class SolisAndWetsTest {
    
    private int maxEvaluations;
    private int sizeNeighborhood;
    private double penalizeValue;
    private SolisAndWets instance;
    
    @Mock private DoubleProblem problem;
    
    @Before
    public void starUp() {
        maxEvaluations = 3000;
        sizeNeighborhood = 5;
        penalizeValue = 17.1234567898765;
        instance = new SolisAndWetsBuilder(problem)
            .setMaxEvaluations(maxEvaluations)
            .setPenalizeValue(penalizeValue)
            .setRho(0.5)
            .build();
        
        Mockito.when(problem.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(problem.getNumberOfVariables()).thenReturn(3);
    }

    /**
     * Test of evaluate method, of class SolisAndWets.
     */
    @Test
    public void testEvaluateAndIsStopingConditionResearched() {
        
        System.out.println("evaluate");
        
        List<DoubleSolution> offspring = new ArrayList<>();        
        ReflectionTestUtils.setField(instance, "offspring_population", offspring);
        ReflectionTestUtils.setField(instance, "maxEvaluations", 2);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        DoubleSolution c = new DefaultDoubleSolution(problem); 
        
        a.setObjective(0, 1);
        b.setObjective(0, 2);
        c.setObjective(0, 3);
        
        ReflectionTestUtils.setField(instance, "penalize_value", penalizeValue);
        
        instance.evaluate(a);
        instance.evaluate(b);
        instance.evaluate(c);
        
        assertEquals(1, a.getObjective(0), 0);
        assertEquals(2, b.getObjective(0), 0);
        assertEquals(penalizeValue, c.getObjective(0), 0);
    }    

    /**
     * Test of fillWith method, of class SolisAndWets.
     */
    @Test
    public void testFillWith() {
        System.out.println("fillWith");
        double[] array = new double[4];
        double value = 1;
        instance.fillWith(array, value);
        
        assertEquals(array[0], 1, 0);
        assertEquals(array[1], 1, 0);
        assertEquals(array[2], 1, 0);
        assertEquals(array[3], 1, 0);
    }
    
    /**
     * Test of improveBest method, of class SolisAndWets.
     */
    @Test
    public void testImproveBest() {
        System.out.println("improveBest");
        
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
        
        //Create 2 solutions
        DoubleSolution a = mock(DoubleSolution.class);
        DoubleSolution b = mock(DoubleSolution.class);
        
        //Set solutions with diferents objectives values
        Mockito.when(a.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(a.getObjective(0)).thenReturn(0.15);
        
        Mockito.when(b.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(b.getObjective(0)).thenReturn(0.76);
        
        ReflectionTestUtils.setField(instance, "best", a);        
        boolean result = ReflectionTestUtils.invokeMethod(instance, "improveBest", b);
        assertEquals(result, false);
        
        Mockito.when(a.getObjective(0)).thenReturn(0.76);
        Mockito.when(b.getObjective(0)).thenReturn(0.15);
        
        ReflectionTestUtils.setField(instance, "best", a);
        result = ReflectionTestUtils.invokeMethod(instance, "improveBest", b);
        assertEquals(result, true);
        
        Mockito.when(b.getObjective(0)).thenReturn(0.76);
        
        result = ReflectionTestUtils.invokeMethod(instance, "improveBest", b);
        assertEquals(result, false);
        
        Mockito.when(a.getAttribute("B")).thenReturn(15.0);
        Mockito.when(b.getAttribute("B")).thenReturn(76.0);
        
        ReflectionTestUtils.setField(instance, "best", a);
        result = ReflectionTestUtils.invokeMethod(instance, "improveBest", b);
        assertEquals(result, false);
        
        Mockito.when(a.getAttribute("B")).thenReturn(76.0);
        Mockito.when(b.getAttribute("B")).thenReturn(15.0);
        
        ReflectionTestUtils.setField(instance, "best", a);
        result = ReflectionTestUtils.invokeMethod(instance, "improveBest", b);
        assertEquals(result, true);
        
        Mockito.when(b.getAttribute("B")).thenReturn(76.0);
        
        result = ReflectionTestUtils.invokeMethod(instance, "improveBest", b);
        assertEquals(result, false);
    }   
}
