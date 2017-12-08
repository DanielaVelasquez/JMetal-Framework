package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.ArrayList;
import java.util.Arrays;
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
public class SaDETest {
    
    private int maxEvaluations;
    private int sizePopulation;
    private double penalizeValue;
    private SaDE instance;    
        
    @Mock private DoubleProblem problem;
    
    @Before
    public void starUp() {
        maxEvaluations = 3000;
        sizePopulation = 3;
        penalizeValue = 17.1234567898765;
        instance = new SaDEBuilder(problem)
            .setMaxEvaluations(maxEvaluations)
            .setPopulationSize(sizePopulation)
            .setPenalizeValue(penalizeValue)
            .build();
        
        Mockito.when(problem.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(problem.getNumberOfVariables()).thenReturn(3);
    }

    /**
     * Test of isStoppingConditionReached method, of class SaDE.
     */
    @Test
    public void testIsStoppingConditionReached() {
        System.out.println("isStoppingConditionReached");
        boolean expResult;
        boolean result;
        int evaluations;
        
        evaluations = maxEvaluations - 1;
        ReflectionTestUtils.setField(instance, "evaluations", evaluations);
        expResult = false;
        result = instance.isStoppingConditionReached();
        assertEquals(expResult, result);
        
        evaluations = maxEvaluations;
        ReflectionTestUtils.setField(instance, "evaluations", evaluations);
        expResult = true;
        result = instance.isStoppingConditionReached();
        assertEquals(expResult, result);
        
        evaluations = maxEvaluations + 1;
        ReflectionTestUtils.setField(instance, "evaluations", evaluations);
        expResult = true;
        result = instance.isStoppingConditionReached();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of createInitialPopulation method, of class SaDE.
     */
    @Test
    public void testCreateInitialPopulation() {
        System.out.println("createInitialPopulation");
        
        List<DoubleSolution> result = instance.createInitialPopulation();        
        assertEquals(sizePopulation, result.size());
    }

    /**
     * Test of getBest method between two solutions, of class SaDE.
     */
    @Test
    public void testGetBest()
    {
        System.out.println("getBest");
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
        
        //Create 2 solutions
        DoubleSolution a = mock(DoubleSolution.class);
        DoubleSolution b = mock(DoubleSolution.class);
        
        //Set solutions with diferents objectives values
        Mockito.when(a.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(a.getObjective(0)).thenReturn(0.15);
        
        Mockito.when(b.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(b.getObjective(0)).thenReturn(0.76);
        
        DoubleSolution best = ReflectionTestUtils.invokeMethod(instance, "getBest", a, b);
        assertEquals(best, a);
        
        Mockito.when(a.getObjective(0)).thenReturn(0.76);
        Mockito.when(b.getObjective(0)).thenReturn(0.15);
        
        best = ReflectionTestUtils.invokeMethod(instance, "getBest", a, b);
        assertEquals(best, b);
        
        Mockito.when(b.getObjective(0)).thenReturn(0.76);
        
        best = ReflectionTestUtils.invokeMethod(instance, "getBest", a, b);
        assertEquals(best, a);
        
        Mockito.when(a.getAttribute("B")).thenReturn(15.0);
        Mockito.when(b.getAttribute("B")).thenReturn(76.0);
        
        best = ReflectionTestUtils.invokeMethod(instance, "getBest", a, b);
        assertEquals(best, a);
        
        Mockito.when(a.getAttribute("B")).thenReturn(76.0);
        Mockito.when(b.getAttribute("B")).thenReturn(15.0);
        
        best = ReflectionTestUtils.invokeMethod(instance, "getBest", a, b);
        assertEquals(best, b);
        
        Mockito.when(b.getAttribute("B")).thenReturn(76.0);
        
        best = ReflectionTestUtils.invokeMethod(instance, "getBest", a, b);
        assertEquals(best, a);
    }
    
    /**
     * Test of getBest in population method, of class SaDE.
     */
    @Test
    public void testGetBestPopulation()
    {
        System.out.println("getBestPopulation");        
        ReflectionTestUtils.setField(instance, "populationSize", 2);
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
        List<DoubleSolution> population = new ArrayList<>();
        
        //Create 2 solutions
        DoubleSolution a = mock(DoubleSolution.class);
        DoubleSolution b = mock(DoubleSolution.class);
        population.add(a);
        population.add(b);
        
        //Set solutions with diferents objectives values
        Mockito.when(a.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(a.getObjective(0)).thenReturn(0.15);
        
        Mockito.when(b.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(b.getObjective(0)).thenReturn(0.76);
        
        DoubleSolution best = instance.getBest(population);
        assertEquals(best, a);
        
        Mockito.when(a.getObjective(0)).thenReturn(0.76);
        Mockito.when(b.getObjective(0)).thenReturn(0.15);
        
        best = instance.getBest(population);
        assertEquals(best, b);
        
        Mockito.when(b.getObjective(0)).thenReturn(0.76);
        
        best = instance.getBest(population);
        assertEquals(best, a);
        
        Mockito.when(a.getAttribute("B")).thenReturn(15.0);
        Mockito.when(b.getAttribute("B")).thenReturn(76.0);
        
        best = instance.getBest(population);
        assertEquals(best, a);
        
        Mockito.when(a.getAttribute("B")).thenReturn(76.0);
        Mockito.when(b.getAttribute("B")).thenReturn(15.0);
        
        best = instance.getBest(population);
        assertEquals(best, b);
        
        Mockito.when(b.getAttribute("B")).thenReturn(76.0);
        
        best = instance.getBest(population);
        assertEquals(best, a);
    }
    
    /**
     * Test of evaluatePopulation method, of class SaDE.
     */
    @Test
    public void testEvaluatePopulation() {
        System.out.println("evaluatePopulation");
        //Create 3 solutions
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        DoubleSolution c = new DefaultDoubleSolution(problem);

        a.setObjective(0, 1);
        b.setObjective(0, 2);
        c.setObjective(0, 3);

        //Add solutions to population
        List<DoubleSolution> population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);

        instance.setMaxEvaluations(2);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        instance.evaluatePopulation(population);
        
        assertEquals(a.getObjective(0), 1, 0);
        assertEquals(b.getObjective(0), 2, 0);
        assertEquals(c.getObjective(0), penalizeValue, 0);
        
        //----------------------------------
        a.setObjective(0, 1);
        b.setObjective(0, 2);
        c.setObjective(0, 3);
        
        population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);

        instance.setMaxEvaluations(0);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        instance.evaluatePopulation(population);

        assertEquals(a.getObjective(0), penalizeValue, 0);
        assertEquals(b.getObjective(0), penalizeValue, 0);
        assertEquals(c.getObjective(0), penalizeValue, 0);
        
        //----------------------------------
        a.setObjective(0, 1);
        b.setObjective(0, 2);
        c.setObjective(0, 3);
        
        population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);

        instance.setMaxEvaluations(3);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        instance.evaluatePopulation(population);

        assertEquals(a.getObjective(0), 1, 0);
        assertEquals(b.getObjective(0), 2, 0);
        assertEquals(c.getObjective(0), 3, 0);
    }
    
    
    /**
     * Test of selection method, of class SaDE.
     */
    @Test
    public void testSelection() {
        System.out.println("selection");
        List<DoubleSolution> population = Arrays.<DoubleSolution>asList(mock(DoubleSolution.class));

        List<DoubleSolution> offspringPopulation = instance.selection(population);
        assertEquals(population, offspringPopulation);
    }
    
    /**
     * Test of getResult method, of class SaDE.
     */
    @Test
    public void testGetResult() {
        
        System.out.println("getResult");
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
        //Create 3 solutions
        DoubleSolution a = mock(DoubleSolution.class);
        DoubleSolution b = mock(DoubleSolution.class);
        DoubleSolution c = mock(DoubleSolution.class);
        //Set solutions with diferents objectives values
        Mockito.when(a.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(a.getObjective(0)).thenReturn(0.005);
        Mockito.when(b.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(b.getObjective(0)).thenReturn(0.76);
        Mockito.when(c.getNumberOfObjectives()).thenReturn(1);
        Mockito.when(c.getObjective(0)).thenReturn(0.0);
        //Add solutions to population
        List<DoubleSolution> population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);
        //Set population to algorithm
        instance.setPopulation(population);
        ReflectionTestUtils.setField(instance, "populationSize", population.size());

        DoubleSolution expResult = c;
        DoubleSolution result = instance.getResult();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of penalize method, of class DECC_G.
     */
    @Test
    public void testPenalize()
    {
        System.out.println("penalize");
        DoubleSolution solution = new DefaultDoubleSolution(problem);
        Mockito.when(solution.getNumberOfObjectives()).thenReturn(1);
        instance.penalize(solution);
        assertEquals(solution.getObjective(0), penalizeValue, 0);
    }
}
