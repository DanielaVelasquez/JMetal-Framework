package org.uma.jmetal.algorithm.singleobjective.mts;

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
import org.uma.jmetal.algorithm.search_range.SearchRange;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.comparator.FrobeniusComparator;

@RunWith(MockitoJUnitRunner.class)
public class AbstractMTS_LS1Test 
{
    private AbstractMTS_LS1 instance;
    private double penalize_value;
    @Mock
    private DoubleProblem problem;
    @Before
    public void startUp()
    {
        penalize_value = 1.23456789;
        instance = new MTS_LS1Builder(problem)
                   .setPenalizeValue(penalize_value)
                   .build();
        Mockito.when(problem.getNumberOfObjectives()).thenReturn(1);
        ReflectionTestUtils.setField(instance, "offspring_population", new ArrayList<>());
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
       
       
    }

    /**
     * Test of buildSOA method, of class AbstractMTS_LS1.
     */
    @Test
    public void testBuildSOA() 
    {
        System.out.println("buildSOA");
        int m = 3;
        int n = 10;
        int[][] result = instance.buildSOA(m, n);
        boolean isCompleted = true;
        for(int j = 0; j < n && isCompleted; j++)
        {
            List<Integer> numbers = new ArrayList<>();
            for(int i = 0; i < m && isCompleted; i++)
            {
                if(numbers.contains(result[i][j]))
                    isCompleted = false;
                else
                    numbers.add(result[i][j]);
            }
        }
        boolean expectedResult = true;
        assertEquals(expectedResult, isCompleted);
    }

    /**
     * Test of evaluatePopulation method, of class AbstractMTS_LS1.
     */
    @Test
    public void testEvaluatePopulation() {
        System.out.println("evaluatePopulation");
        
        //Testing las individual is penalized
        double objective_a = 1;
        double objective_b = 2;
        double objective_c  = 3;
        //Create 3 solutions
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        DoubleSolution c = new DefaultDoubleSolution(problem);
        
        a.setObjective(0, objective_a);
        b.setObjective(0, objective_b);
        c.setObjective(0, objective_c);
        
        //Add solutions to population
        List<DoubleSolution> population = new ArrayList<>();
        population.add(c);
        population.add(b);
        population.add(a);
        
        instance.setMaxEvaluations(2);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        instance.evaluatePopulation(population);
        assertEquals("Individual a should of been penalized (1)",a.getObjective(0), penalize_value, 0);
        assertEquals("Individual b should not has been objective (1)"+objective_b,b.getObjective(0), objective_b, 0);
        assertEquals("Individual c should not has been objective (1)"+objective_c,c.getObjective(0), objective_c, 0);
        //-----------------------
        //Only one individual is evaluated
        //------------------------
        a.setObjective(0, objective_a);
        population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);
        
        instance.setMaxEvaluations(1);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        
        ReflectionTestUtils.invokeMethod(instance, "evaluatePopulation", population);
        
        assertEquals("Individual a should not has been objective (2)"+objective_a,a.getObjective(0), objective_a, 0);
        assertEquals("Individual b should of been penalized (2)",b.getObjective(0), penalize_value, 0);
        assertEquals("Individual c should of been penalized (2)",c.getObjective(0), penalize_value, 0);
        
        //-----------------------
        //All individual penalized
        //------------------------
        a.setObjective(0, objective_a);
        b.setObjective(0, objective_b);
        c.setObjective(0, objective_c);
        
        instance.setMaxEvaluations(3);
        ReflectionTestUtils.setField(instance, "evaluations", 3);
        
        ReflectionTestUtils.invokeMethod(instance, "evaluatePopulation", population);
        
        assertEquals("Individual a should of been penalized (3)",a.getObjective(0), penalize_value, 0);
        assertEquals("Individual b should of been penalized (3)",b.getObjective(0), penalize_value, 0);
        assertEquals("Individual c should of been penalized (3)",c.getObjective(0), penalize_value, 0);
        
        //-----------------------
        //All individuals should of been evaluated
        //------------------------
        a.setObjective(0, objective_a);
        b.setObjective(0, objective_b);
        c.setObjective(0, objective_c);
        
        instance.setMaxEvaluations(3);
        ReflectionTestUtils.setField(instance, "evaluations", 0);
        
        ReflectionTestUtils.invokeMethod(instance, "evaluatePopulation", population);
        
        assertEquals("Individual a should not has been objective (4)"+objective_a,a.getObjective(0), objective_a, 0);
        assertEquals("Individual b should not has been objective (4)"+objective_b,b.getObjective(0), objective_b, 0);
        assertEquals("Individual c should not has been objective (4)"+objective_c,c.getObjective(0), objective_c, 0);
    }
    
    /**
     * Test of evaluate method, of class AbstractMTS_LS1.
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
        
        assertEquals(penalize_value, solution.getObjective(0), 0);
    }
    /**
     * Test of updateProgress method, of class AbstractMTS_LS1.
     */
    @Test
    public void testUpdateProgress() {
        System.out.println("updateProgress");
        int evaluations = 10;
        ReflectionTestUtils.setField(instance, "evaluations", 15);
        instance.updateProgress();
        int expResult = 16;
        assertEquals(expResult, instance.getEvaluations());
    }
    
    /**
     * Test of getBest method, of class AbstractMTS_LS1.
     */
    @Test
    public void testGetBest() {
        System.out.println("getBest");
       
       //Create 6 solutions
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
     * Test of improveBest method, of class AbstractMTS_LS1.
     */
    @Test
    public void testImproveBest() {
        System.out.println("improveBest");
        
        DoubleSolution solution = new DefaultDoubleSolution(problem);
        double objective = 5.5555;
        solution.setObjective(0, objective);
        
        DoubleSolution best = new DefaultDoubleSolution(problem);
        double bestObjective = 0;
        best.setObjective(0, bestObjective);
        
        boolean expResult;
        
        instance.setBest(best);
        
        //Best was not improved by fitness
        boolean result = instance.improveBest(solution);
        expResult = false;
        assertEquals(expResult, result);
        
        //Best was improved by fitness
        objective = 0;
        bestObjective = 5.5555;
        
        solution.setObjective(0, objective);
        best.setObjective(0, bestObjective);
        
        result = instance.improveBest(solution);
        expResult = true;
        assertEquals(expResult, result);
        
        //Best was not improved by frobenius norm
        bestObjective = 0;
        best.setObjective(0, bestObjective);
        
        double frobenius = 123;
        double bestFrobenius = 12;
        
        solution.setAttribute("B", frobenius);
        best.setAttribute("B", bestFrobenius);
        
        result = instance.improveBest(solution);
        expResult = false;
        assertEquals(expResult, result);
        
        //Best was improved by frobenius norm
        
        bestFrobenius = 1234;
        best.setAttribute("B", bestFrobenius);
        result = instance.improveBest(solution);
        expResult = true;
        assertEquals(expResult, result);
    }
    /**
     * Test of functionValueDegenerates method, of class AbstractMTS_LS1.
     */
    @Test
    public void testFunctionValueDegenerates() {
        System.out.println("functionValueDegenerates");
        DoubleSolution original = new DefaultDoubleSolution(problem);
        DoubleSolution modified = new DefaultDoubleSolution(problem);
        
        //Function values degenerates
        double objOriginal = 13;
        double objModified = 23;
        
        original.setObjective(0, objOriginal);
        modified.setObjective(0, objModified);
        
        boolean expResult = true;
        boolean result = instance.functionValueDegenerates(original, modified);
        assertEquals(expResult, result);
        
        //Function values was not degenerates
        
        objOriginal = 100;
        objModified = 12;
        
        original.setObjective(0, objOriginal);
        modified.setObjective(0, objModified);
        
        expResult = false;
        result = instance.functionValueDegenerates(original, modified);
        assertEquals(expResult, result);
        
        //Function values stay the same, it degenerates
        
        objModified = 100;
        modified.setObjective(0, objModified);
        
        expResult = true;
        result = instance.functionValueDegenerates(original, modified);
        assertEquals(expResult, result);
        
    }
    /**
     * Test of getBest method, of class AbstractMTS_LS1.
     */
    @Test
    public void testGetBestList() {
        System.out.println("getBest (List)");
        
        DoubleSolution best = new DefaultDoubleSolution(problem);
        double bestObjective = 0;
        best.setObjective(0, bestObjective);
        
        //Testing las individual is penalized
        double objective_a = 1;
        double objective_b = 2;
        double objective_c  = 3;
        //Create 3 solutions
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        DoubleSolution c = new DefaultDoubleSolution(problem);
        
        a.setObjective(0, objective_a);
        b.setObjective(0, objective_b);
        c.setObjective(0, objective_c);
        
        List<DoubleSolution> population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);
        ReflectionTestUtils.setField(instance, "populationSize", population.size());
        
        //Best is the best among all
        Object result = instance.getBest(population, best);
        assertEquals(best, result);
        
        //a is the best, because there is not best provided
        result = instance.getBest(population, null);
        assertEquals(a, result);
        
        //c is the best
        objective_c = 0;
        c.setObjective(0, objective_c);
        result = instance.getBest(population, null);
        assertEquals(c, result);
        
        //c is the best, best is provided
        bestObjective = 4;
        best.setObjective(0, bestObjective);
        result = instance.getBest(population, null);
        assertEquals(c, result);   
    }
    /**
     * Test of getResult method, of class AbstractMTS_LS1.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
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
        DoubleSolution result = (DoubleSolution) instance.getResult();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isBetterOriginal method, of class AbstractMTS_LS1.
     */
    @Test
    public void testIsBetterOriginal() {
        System.out.println("isBetterOriginal");
        DoubleSolution original = new DefaultDoubleSolution(problem);
        DoubleSolution modified = new DefaultDoubleSolution(problem);
        
        //Original is better
        double objOriginal = 13;
        double objModified = 23;
        
        original.setObjective(0, objOriginal);
        modified.setObjective(0, objModified);
        
        boolean expResult = true;
        boolean result = instance.isBetterOriginal(original, modified);
        assertEquals(expResult, result);
        
        //Original is not better
        
        objOriginal = 100;
        objModified = 12;
        
        original.setObjective(0, objOriginal);
        modified.setObjective(0, objModified);
        
        expResult = false;
        result = instance.isBetterOriginal(original, modified);
        assertEquals(expResult, result);
        
        //Function values stay the same, original is better
        
        objModified = 100;
        modified.setObjective(0, objModified);
        
        expResult = true;
        result = instance.functionValueDegenerates(original, modified);
        assertEquals(expResult, result);
    }
    /**
     * Test of buildSearchRange method, of class AbstractMTS_LS1.
     */
    @Test
    public void testBuildSearchRange() {
        System.out.println("buildSearchRange");
        DoubleSolution a = mock(DoubleSolution.class);
        int n = 2;
        
        for(int i = 0; i < n - 1; i++)
        {
            Mockito.when(a.getLowerBound(i)).thenReturn(-1.0);
            Mockito.when(a.getUpperBound(i)).thenReturn(1.0);
        }
        
        Mockito.when(a.getLowerBound(n - 1)).thenReturn(1.0);
        Mockito.when(a.getUpperBound(n - 1)).thenReturn(2.0);
        
        Mockito.when(problem.getNumberOfVariables()).thenReturn(n);
        
        
        SearchRange result = instance.buildSearchRange(a);
        List<Double> resulSR = result.getSearchRange();
        List<Double> expResult = new ArrayList();
        expResult.add(1.0);
        expResult.add(0.5);
        
        boolean correct = true;
        
        for(int i = 0; i < n; i++)
        {
            if(((double) expResult.get(i)) != ((double)(resulSR.get(i))))
            {
                correct = false;
                break;
            }
        }
                
        assertEquals(true, correct);
    }
    
    /**
     * Test of penalize method, of class AbstractMTS_LS1.
     */
    @Test
    public void testPenalize() {
        System.out.println("penalize");
        DoubleSolution solution = new DefaultDoubleSolution(problem);
        Mockito.when(solution.getNumberOfObjectives()).thenReturn(1);
        instance.penalize(solution);
        assertEquals(solution.getObjective(0), penalize_value, 0);
    }
    /**
     * Test of replacement method, of class SaNSDE.
     */
    @Test
    public void testInPopulation()
    {
        System.out.println("replacement");
        
        Mockito.when(problem.getNumberOfVariables()).thenReturn(3);
        List<DoubleSolution> population = new ArrayList<>();
        
        DoubleSolution a = mock(DoubleSolution.class);
        DoubleSolution b = mock(DoubleSolution.class);        
        DoubleSolution c = mock(DoubleSolution.class);        
        
        Mockito.when(a.getNumberOfVariables()).thenReturn(3);
        Mockito.when(a.getVariableValue(0)).thenReturn(0.15);
        Mockito.when(a.getVariableValue(1)).thenReturn(0.25);
        Mockito.when(a.getVariableValue(2)).thenReturn(0.37);
        
        Mockito.when(b.getNumberOfVariables()).thenReturn(3);
        Mockito.when(b.getVariableValue(0)).thenReturn(0.1);
        Mockito.when(b.getVariableValue(1)).thenReturn(0.98);
        Mockito.when(b.getVariableValue(2)).thenReturn(-0.67);
        
        population.add(a);
        population.add(b);
        
        boolean result = ReflectionTestUtils.invokeMethod(instance, "inPopulation", population, a);
        assertEquals(true, result);
        
        Mockito.when(c.getNumberOfVariables()).thenReturn(3);
        Mockito.when(c.getVariableValue(0)).thenReturn(0.15);
        Mockito.when(c.getVariableValue(1)).thenReturn(0.25);
        Mockito.when(c.getVariableValue(2)).thenReturn(0.37);
        
        result = instance.inPopulation(population, c);
        assertEquals(true, result);
        
        Mockito.when(c.getVariableValue(2)).thenReturn(0.3);
        result = instance.inPopulation(population, c);
        assertEquals(false, result);
    }
    /**
     * Test of generateInitialSolutions method, of class AbstractMTS_LS1.
     */
    /*@Test
    public void testGenerateInitialSolutions() {
        System.out.println("generateInitialSolutions");
        int m = 3;
        int n = 10;
        
        ReflectionTestUtils.setField(instance, "n", n);
        int[][] SOA = instance.buildSOA(m, n);
        
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        DoubleSolution c = new DefaultDoubleSolution(problem);
        
        for(int i = 0; i < n; i++)
        {
            Mockito.when(a.getLowerBound(i)).thenReturn(-1.0);
            Mockito.when(b.getLowerBound(i)).thenReturn(-1.0);
            Mockito.when(c.getLowerBound(i)).thenReturn(-1.0);
            
            Mockito.when(a.getUpperBound(i)).thenReturn(-1.0);
            Mockito.when(b.getUpperBound(i)).thenReturn(-1.0);
            Mockito.when(c.getUpperBound(i)).thenReturn(-1.0);
        }
        
        List population = new ArrayList();
        population.add(a);
        population.add(b);
        population.add(c);
        
        
        int populationSize = 3;
        
        Mockito.when(instance.createInitialPopulation(m)).thenReturn(population);
        List<Double> expResult = new ArrayList();
        expResult.add(-1.0);
        expResult.add(0.0);
        expResult.add(1.0);
        
        List<DoubleSolution> result = instance.generateInitialSolutions(SOA, populationSize);
        boolean ans = true;
        for(DoubleSolution s: result)
        {
            for(int i = 0; i < n; i++)
            {
                if(!expResult.contains(s.getAttribute(i)))
                {
                    ans = false;
                    break;
                }
            }
        }
        
        assertEquals(true, ans);
    }*/
}
