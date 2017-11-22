/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author danielavelasquezgarzon
 */
public class DECC_GTest {
    
    public DECC_GTest() {
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
     * Test of getPopulation method, of class DECC_G.
     */
    @Test
    public void testGetPopulation() {
        System.out.println("getPopulation");
        DECC_G instance = null;
        List<DoubleSolution> expResult = null;
        List<DoubleSolution> result = instance.getPopulation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setProblem method, of class DECC_G.
     */
    @Test
    public void testSetProblem() {
        System.out.println("setProblem");
        DoubleProblem problem = null;
        DECC_G instance = null;
        instance.setProblem(problem);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProblem method, of class DECC_G.
     */
    @Test
    public void testGetProblem() {
        System.out.println("getProblem");
        DECC_G instance = null;
        DoubleProblem expResult = null;
        DoubleProblem result = instance.getProblem();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProgress method, of class DECC_G.
     */
    @Test
    public void testUpdateProgress() {
        System.out.println("updateProgress");
        DECC_G instance = null;
        instance.updateProgress();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of penalize method, of class DECC_G.
     */
    @Test
    public void testPenalize() {
        System.out.println("penalize");
        DoubleSolution solution = null;
        DECC_G instance = null;
        instance.penalize(solution);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPopulation method, of class DECC_G.
     */
    @Test
    public void testSetPopulation() {
        System.out.println("setPopulation");
        List<DoubleSolution> population = null;
        DECC_G instance = null;
        instance.setPopulation(population);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class DECC_G.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        DECC_G instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResult method, of class DECC_G.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        DECC_G instance = null;
        DoubleSolution expResult = null;
        DoubleSolution result = instance.getResult();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class DECC_G.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        DECC_G instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class DECC_G.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        DECC_G instance = null;
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
