package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbingBuilder;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;

@RunWith(MockitoJUnitRunner.class)
public class IHDELSTest 
{
    private IHDELS instance;
    private double penalize_value;
    @Mock
    private DoubleProblem problem;
    
    public IHDELSTest() 
    {
        
    }
    
    @Before
    public void startUp() 
    {
        penalize_value = 1.234567789;
        
        instance = new IHDELSBuilder(problem)
                    .setPenalize_value(penalize_value)
                    .addLocalSearch(new LSMTS_LS1(new MTS_LS1Builder(problem)))
                    .addLocalSearch(new LSHillClimbing(new HillClimbingBuilder(problem)))
                    .build();
        Mockito.when(problem.getNumberOfObjectives()).thenReturn(1);
    }
    
    /**
     * Test of updateProgress method, of class IHDELS.
     */
    @Test
    public void testUpdateProgress() {
        System.out.println("updateProgress");
        int evaluations = 10;
        ReflectionTestUtils.setField(instance, "evaluations", 15);
        instance.updateProgress(evaluations);
        int expResult = 25;
        assertEquals(expResult, instance.getEvaluations());
    }
    
    /**
     * Test of evaluatePopulation method, of class IHDELS.
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
        
        ReflectionTestUtils.invokeMethod(instance, "evaluatePopulation", population);
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
     * Test of evaluate method, of class IHDELS.
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
     * Test of penalize method, of class IHDELS.
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
     * Test of clone method, of class IHDELS.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        int m = 5;
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
        
        for(DoubleSolution s : population)
        {
            for(int i = 0; i < m; i++)
            {
                s.setAttribute(i, i + 1);
            }
        }
        
        boolean completed = true;
        boolean expResult = true;
        List<DoubleSolution> result = instance.clone(population);
        
        for(DoubleSolution s : result)
        {
            for(int i = 0; i < m; i++)
            {
                if((int) s.getAttribute(i) != i +1)
                    completed = false;
            }
        }
        
        assertEquals(expResult, completed);
    }

    /**
     * Test of createInitialSolution method, of class IHDELS.
     */
    @Test
    public void testCreateInitialSolution() {
        System.out.println("createInitialSolution");
        int n = 4;
        ReflectionTestUtils.setField(instance, "m", n);
        Mockito.when(problem.getNumberOfVariables()).thenReturn(n);
        DoubleSolution a = new DefaultDoubleSolution(problem);
        
        
        
        for(int i = 0; i < n - 1; i++)
        {
            Mockito.when(problem.getLowerBound(i)).thenReturn(-1.0);
            Mockito.when(problem.getUpperBound(i)).thenReturn(1.0);
        }
        Mockito.when(problem.getLowerBound(n - 1)).thenReturn(1.0);
        Mockito.when(problem.getUpperBound(n - 1)).thenReturn(2.0);
        
        Mockito.when(problem.createSolution()).thenReturn(a);
        
        DoubleSolution solution = instance.createInitialSolution();
        
        boolean expResult = true;
        boolean result = true;
        for(int i = 0; i < n-1 && result; i++)
        {
            if((double)solution.getVariableValue(i) != 0)
                result = false;
        }
        
        if((double) a.getVariableValue(n-1) != 1.5)
            result = false;
        assertEquals(expResult, result);
    }

    /**
     * Test of selectLocalSearch method, of class IHDELS.
     */
    @Test
    public void testSelectLocalSearch() {
        System.out.println("selectLocalSearch");
        LocalSearch a = mock(LocalSearch.class);
        LocalSearch b = mock(LocalSearch.class);
        LocalSearch c = mock(LocalSearch.class);
        
        Mockito.when(a.getRatio()).thenReturn(0.3);
        Mockito.when(b.getRatio()).thenReturn(0.4);
        Mockito.when(c.getRatio()).thenReturn(0.5);
        
        List<LocalSearch> local_searches = new ArrayList<>();
        local_searches.add(a);
        local_searches.add(b);
        local_searches.add(c);
        
        ReflectionTestUtils.setField(instance, "numberLS", local_searches.size());
        instance.setLocal_searches(local_searches);
        
        //Shoul select  local search c
        LocalSearch result = instance.selectLocalSearch();
        assertEquals(result, c);
        
        Mockito.when(a.getRatio()).thenReturn(0.5);
        Mockito.when(b.getRatio()).thenReturn(0.2);
        Mockito.when(c.getRatio()).thenReturn(0.3);
        
        //Shoul select  local search a
        result = instance.selectLocalSearch();
        assertEquals(result, a);
        
        //Shoul select local search a
        Mockito.when(a.getRatio()).thenReturn(0.2);
        Mockito.when(b.getRatio()).thenReturn(0.2);
        Mockito.when(c.getRatio()).thenReturn(0.2);
        
        result = instance.selectLocalSearch();
        assertEquals(result, a);
        
        /*IHDELS instance = null;
        LocalSearch expResult = null;
        LocalSearch result = instance.selectLocalSearch();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");*/
    }
}
