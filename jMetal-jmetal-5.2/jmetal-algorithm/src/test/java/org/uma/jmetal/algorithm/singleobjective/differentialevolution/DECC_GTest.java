package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

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
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemSaNSDE;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentSaNSDE;
import org.uma.jmetal.util.comparator.FrobeniusComparator;

@RunWith(MockitoJUnitRunner.class)
public class DECC_GTest
{
    private DECC_G instance;
    private int penalize_value;
    @Mock
    private DoubleProblem problem;
    
    public DECC_GTest()
    {
    }
    
    @Before
    public void startUp() 
    {
        penalize_value = 10;
        instance = new DECC_GBuilder(problem)
                        .setDEBuilder(new DEUnicaucaBuilder(problem))
                        .setPenalizeValue(penalize_value)
                        .build();
        Mockito.when(problem.getNumberOfObjectives()).thenReturn(1);
    }
    
    /**
     * Test of getResult method, of class DECC_G.
     */
    @Test
    public void testGetResult()
    {
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
     * Test of getResult method, of class DECC_G.
     */
    @Test
    public void testGetResultFrobenius()
    {
        System.out.println("getResult");
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
        
        //Create 3 solutions
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        //Set solutions with diferents objectives values
        a.setObjective(0, 0.76);
        a.setAttribute("B", 100.0);
        
        b.setObjective(0, 0.76);
        b.setAttribute("B", 12.0);
        
        
        //Add solutions to population
        List<DoubleSolution> population = new ArrayList<>();
        population.add(a);
        population.add(b);
        //Set population to algorithm
        instance.setPopulation(population);
        ReflectionTestUtils.setField(instance, "populationSize", population.size());
        
        DoubleSolution expResult = b;
        DoubleSolution result = instance.getResult();
        System.out.println("->"+result.getAttribute("B"));
        assertEquals(expResult, result);
    }
    /**
     * Test of updateProgress method, of class DECC_G.
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
     * Test of penalize method, of class DECC_G.
     */
    @Test
    public void testPenalize()
    {
        System.out.println("penalize");
        DoubleSolution solution = new DefaultDoubleSolution(problem);
        Mockito.when(solution.getNumberOfObjectives()).thenReturn(1);
        instance.penalize(solution);
        assertEquals(solution.getObjective(0), penalize_value, 0);
    }
    
    @Test
    public void testEvaluatePopulation()
    {
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
    @Test
    public void testGetIndividualsFromSubpopulation()
    {
        System.out.println("getIndividualsFromSubpopulation");
        
        DoubleSolution solution = new DefaultDoubleSolution(problem);
        SubcomponentDoubleProblemSaNSDE subcomponent_problem = new SubcomponentDoubleProblemSaNSDE(null, problem);
        DoubleSolutionSubcomponentSaNSDE subcomponent = new DoubleSolutionSubcomponentSaNSDE(solution, subcomponent_problem);
        
        List<DoubleSolution> subpopulation = new ArrayList<>();
        subpopulation.add(subcomponent);
        
        ReflectionTestUtils.setField(instance, "populationSize", 1);
        
        List<DoubleSolution> population = new ArrayList<>();
        population.add(new DefaultDoubleSolution(problem));
        
        instance.setPopulation(population);
        
        ReflectionTestUtils.invokeMethod(instance, "getIndividualsFromSubpopulation", subpopulation);
        
        assertEquals("Individual was not obtained",instance.getPopulation().get(0), solution);
    }
    
    @Test
    public void testSelectBestWorstIndividual()
    {
        System.out.println("selectBestWorstIndividual");
        
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
        
        //Create 3 solutions
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        DoubleSolution c = new DefaultDoubleSolution(problem);
        
        //Add solutions to population
        List<DoubleSolution> population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);
        

        
        //Set population to algorithm
        instance.setPopulation(population);
        ReflectionTestUtils.setField(instance, "populationSize", population.size());
        
        //Different objectives values
        //Set solutions with diferents objectives values
        a.setObjective(0, 0.76);
        a.setAttribute("B", 100.0);
        
        b.setObjective(0, 0.85);
        b.setAttribute("B", 12.0);
        
        c.setObjective(0, 0.31);
        c.setAttribute("B", 11.0);
        
        
        ReflectionTestUtils.invokeMethod(instance, "selectBestWorstIndividual", population);
        
        DoubleSolution best = (DoubleSolution) ReflectionTestUtils.getField(instance, "best_individual");
        DoubleSolution worst = (DoubleSolution) ReflectionTestUtils.getField(instance, "worst_individual");
        
        assertEquals("Best individual not found",c, best);
        assertEquals("Worst individual not found",b, worst);
        //--------------------------------------
        //Two equals objectives values
        a.setObjective(0, 0.34);
        a.setAttribute("B", 100.0);
        
        b.setObjective(0, 0.34);
        b.setAttribute("B", 12.0);
        
        c.setObjective(0, 0.76);
        c.setAttribute("B", 11.0);
        
        
        ReflectionTestUtils.invokeMethod(instance, "selectBestWorstIndividual", population);
        
        best = (DoubleSolution) ReflectionTestUtils.getField(instance, "best_individual");
        worst = (DoubleSolution) ReflectionTestUtils.getField(instance, "worst_individual");
        
        assertEquals("Best individual not found",b, best);
        assertEquals("Worst individual not found",c, worst);
        
        //--------------------------
        //Al individuals the same         
        a.setObjective(0, 0.34);
        a.setAttribute("B", 12.0);
        
        b.setObjective(0, 0.34);
        b.setAttribute("B", 12.0);
        
        c.setObjective(0, 0.34);
        c.setAttribute("B", 12.0);
        
        
        ReflectionTestUtils.invokeMethod(instance, "selectBestWorstIndividual", population);
        
        best = (DoubleSolution) ReflectionTestUtils.getField(instance, "best_individual");
        worst = (DoubleSolution) ReflectionTestUtils.getField(instance, "worst_individual");
        
        assertEquals("Best individual not found",a, best);
        assertEquals("Worst individual not found",a, worst);   
    }
    
    @Test
    public void testFindIndividuals()
    {
        System.out.println("selectBestWorstIndividual");
        
        instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
        
        //Create 4 solutions
        DoubleSolution a = new DefaultDoubleSolution(problem);
        DoubleSolution b = new DefaultDoubleSolution(problem);
        DoubleSolution c = new DefaultDoubleSolution(problem);
        DoubleSolution d = new DefaultDoubleSolution(problem);
        
        //Add solutions to population
        List<DoubleSolution> population = new ArrayList<>();
        population.add(a);
        population.add(b);
        population.add(c);
        population.add(d);
        instance.setPopulation(population);
        ReflectionTestUtils.setField(instance, "populationSize", population.size());
        //--------------------------------------
        //Different objectives values
         a.setObjective(0, 0.76);
        a.setAttribute("B", 100.0);
        
        b.setObjective(0, 0.11);
        b.setAttribute("B", 12.0);
        
        c.setObjective(0, 0.31);
        c.setAttribute("B", 11.0);
        
        d.setObjective(0, 0.85);
        d.setAttribute("B", 180.0);
        
        ReflectionTestUtils.invokeMethod(instance, "findIndividuals");
        
        DoubleSolution best = (DoubleSolution) ReflectionTestUtils.getField(instance, "best_individual");
        DoubleSolution worst = (DoubleSolution) ReflectionTestUtils.getField(instance, "worst_individual");
        DoubleSolution random = (DoubleSolution) ReflectionTestUtils.getField(instance, "random_individual");
        
        assertNotEquals(best, random);
        assertNotEquals(worst, random);
    }
    
    /**
    * Test of isStoppingConditionReached method, of class SaNSDE.
    */
   @Test
   public void testIsStoppingConditionReached() {
       System.out.println("isStoppingConditionReached");
       
       boolean expResult;
       boolean result;
       int maxEvaluations = 5;
       int evaluations = maxEvaluations - 1;
       
       instance.setMaxEvaluations(maxEvaluations);
       
       ReflectionTestUtils.setField(instance, "evaluations", evaluations);
       expResult = false;
       result = ReflectionTestUtils.invokeMethod(instance, "isStoppingConditionReached");
       assertEquals(expResult, result);
       
       evaluations = maxEvaluations;
       ReflectionTestUtils.setField(instance, "evaluations", evaluations);
       expResult = true;
       result = ReflectionTestUtils.invokeMethod(instance, "isStoppingConditionReached");
       assertEquals(expResult, result);
       
       evaluations = maxEvaluations + 1;
       ReflectionTestUtils.setField(instance, "evaluations", evaluations);
       expResult = true;
       result = ReflectionTestUtils.invokeMethod(instance, "isStoppingConditionReached");
       assertEquals(expResult, result);
   }
   /**
    * Test of getPossibleEvaluations method, of class SaNSDE.
    */
   @Test
   public void testGetPossibleEvaluations() {
       System.out.println("isStoppingConditionReached");
       
       int expResult;
       int result;
       int maxEvaluations = 5;
       int evaluations = 0;
       
       instance.setMaxEvaluations(maxEvaluations);
       
       ReflectionTestUtils.setField(instance, "evaluations", evaluations);
       expResult = 3;
       result = ReflectionTestUtils.invokeMethod(instance, "getPossibleEvaluations", 3);
       assertEquals(expResult, result);
       
       evaluations = maxEvaluations;
       ReflectionTestUtils.setField(instance, "evaluations", evaluations);
       expResult = 0;
       result = ReflectionTestUtils.invokeMethod(instance, "getPossibleEvaluations",3);
       assertEquals(expResult, result);
       
       evaluations = 3;
       ReflectionTestUtils.setField(instance, "evaluations", evaluations);
       expResult = 2;
       result = ReflectionTestUtils.invokeMethod(instance, "getPossibleEvaluations",3);
       assertEquals(expResult, result);
   }
   /**
    * Test of getBest method, of class SaNSDE.
    */
   @Test
   public void testGetBest()
   {
       System.out.println("getBest");
       
       instance.setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0));
       
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
}
