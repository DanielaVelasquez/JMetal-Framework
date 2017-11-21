
package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;


public class SaNSDETest {
    
    private SaNSDE instance; 
    
    public SaNSDETest() {
        
        String problemName = "co.edu.unicauca.problem.training_testing.Iris";
        DoubleProblem problem = null;
        
        instance = new SaNSDEBuilder(problem)
            .setProblem(problem)
            .setPopulationSize(10)
            .setPenalizeValue(0)
            .setMaxEvaluations(3000)
            .build();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initProgress method, of class SaNSDE.
     */
    @Test
    public void testInitProgress() {
        System.out.println("initProgress");        
        instance.initProgress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProgress method, of class SaNSDE.
     */
    @Test
    public void testUpdateProgress() {
        System.out.println("updateProgress");
        SaNSDE instance = null;
        instance.updateProgress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isStoppingConditionReached method, of class SaNSDE.
     */
    @Test
    public void testIsStoppingConditionReached() {
        System.out.println("isStoppingConditionReached");
        ------
        boolean expResult = false;
        boolean result = instance.isStoppingConditionReached();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createInitialPopulation method, of class SaNSDE.
     */
    @Test
    public void testCreateInitialPopulation() {
        System.out.println("createInitialPopulation");
        SaNSDE instance = null;
        List<DoubleSolution> expResult = null;
        List<DoubleSolution> result = instance.createInitialPopulation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of penalize method, of class SaNSDE.
     */
    @Test
    public void testPenalize() {
        System.out.println("penalize");
        DoubleSolution solution = null;
        SaNSDE instance = null;
        instance.penalize(solution);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of evaluatePopulation method, of class SaNSDE.
     */
    @Test
    public void testEvaluatePopulation() {
        System.out.println("evaluatePopulation");
        List<DoubleSolution> population = null;
        SaNSDE instance = null;
        List<DoubleSolution> expResult = null;
        List<DoubleSolution> result = instance.evaluatePopulation(population);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selection method, of class SaNSDE.
     */
    @Test
    public void testSelection() {
        System.out.println("selection");
        List<DoubleSolution> population = null;
        SaNSDE instance = null;
        List<DoubleSolution> expResult = null;
        List<DoubleSolution> result = instance.selection(population);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reproduction method, of class SaNSDE.
     */
    @Test
    public void testReproduction() {
        System.out.println("reproduction");
        List<DoubleSolution> matingPopulation = null;
        SaNSDE instance = null;
        List<DoubleSolution> expResult = null;
        List<DoubleSolution> result = instance.reproduction(matingPopulation);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of replacement method, of class SaNSDE.
     */
    @Test
    public void testReplacement() {
        System.out.println("replacement");
        List<DoubleSolution> population = null;
        List<DoubleSolution> offspringPopulation = null;
        SaNSDE instance = null;
        List<DoubleSolution> expResult = null;
        List<DoubleSolution> result = instance.replacement(population, offspringPopulation);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResult method, of class SaNSDE.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        SaNSDE instance = null;
        DoubleSolution expResult = null;
        DoubleSolution result = instance.getResult();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class SaNSDE.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        SaNSDE instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class SaNSDE.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        SaNSDE instance = null;
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
