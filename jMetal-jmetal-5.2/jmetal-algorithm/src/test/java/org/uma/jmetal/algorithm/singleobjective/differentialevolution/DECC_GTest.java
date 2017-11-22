package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.Comparator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemDE;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemSaNSDE;
import org.uma.jmetal.solution.DoubleSolution;

public class DECC_GTest
{
    
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
     * Test of updateProgress method, of class DECC_G.
     */
    @Test
    public void testUpdateProgress() {
        System.out.println("updateProgress");
        int evaluations = 0;
        DECC_G instance = null;
        instance.updateProgress(evaluations);
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
     * Test of getSubcomponent_problem_DE method, of class DECC_G.
     */
    @Test
    public void testGetSubcomponent_problem_DE() {
        System.out.println("getSubcomponent_problem_DE");
        DECC_G instance = null;
        SubcomponentDoubleProblemDE expResult = null;
        SubcomponentDoubleProblemDE result = instance.getSubcomponent_problem_DE();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubcomponent_problem_SaNSDE method, of class DECC_G.
     */
    @Test
    public void testGetSubcomponent_problem_SaNSDE() {
        System.out.println("getSubcomponent_problem_SaNSDE");
        DECC_G instance = null;
        SubcomponentDoubleProblemSaNSDE expResult = null;
        SubcomponentDoubleProblemSaNSDE result = instance.getSubcomponent_problem_SaNSDE();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getS method, of class DECC_G.
     */
    @Test
    public void testGetS() {
        System.out.println("getS");
        DECC_G instance = null;
        int expResult = 0;
        int result = instance.getS();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFE method, of class DECC_G.
     */
    @Test
    public void testGetFE() {
        System.out.println("getFE");
        DECC_G instance = null;
        int expResult = 0;
        int result = instance.getFE();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getwFEs method, of class DECC_G.
     */
    @Test
    public void testGetwFEs() {
        System.out.println("getwFEs");
        DECC_G instance = null;
        int expResult = 0;
        int result = instance.getwFEs();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getN method, of class DECC_G.
     */
    @Test
    public void testGetN() {
        System.out.println("getN");
        DECC_G instance = null;
        int expResult = 0;
        int result = instance.getN();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPopulationSize method, of class DECC_G.
     */
    @Test
    public void testGetPopulationSize() {
        System.out.println("getPopulationSize");
        DECC_G instance = null;
        int expResult = 0;
        int result = instance.getPopulationSize();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubcomponent method, of class DECC_G.
     */
    @Test
    public void testGetSubcomponent() {
        System.out.println("getSubcomponent");
        DECC_G instance = null;
        double expResult = 0.0;
        double result = instance.getSubcomponent();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComparator method, of class DECC_G.
     */
    @Test
    public void testGetComparator() {
        System.out.println("getComparator");
        DECC_G instance = null;
        Comparator<DoubleSolution> expResult = null;
        Comparator<DoubleSolution> result = instance.getComparator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSansdeBuilder method, of class DECC_G.
     */
    @Test
    public void testGetSansdeBuilder() {
        System.out.println("getSansdeBuilder");
        DECC_G instance = null;
        SaNSDEBuilder expResult = null;
        SaNSDEBuilder result = instance.getSansdeBuilder();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDeFrobeniusBuilder method, of class DECC_G.
     */
    @Test
    public void testGetDeFrobeniusBuilder() {
        System.out.println("getDeFrobeniusBuilder");
        DECC_G instance = null;
        DEFrobeniusBuilder expResult = null;
        DEFrobeniusBuilder result = instance.getDeFrobeniusBuilder();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxEvaluations method, of class DECC_G.
     */
    @Test
    public void testGetMaxEvaluations() {
        System.out.println("getMaxEvaluations");
        DECC_G instance = null;
        int expResult = 0;
        int result = instance.getMaxEvaluations();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEvaluations method, of class DECC_G.
     */
    @Test
    public void testGetEvaluations() {
        System.out.println("getEvaluations");
        DECC_G instance = null;
        int expResult = 0;
        int result = instance.getEvaluations();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPenalize_value method, of class DECC_G.
     */
    @Test
    public void testGetPenalize_value() {
        System.out.println("getPenalize_value");
        DECC_G instance = null;
        double expResult = 0.0;
        double result = instance.getPenalize_value();
        assertEquals(expResult, result, 0.0);
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
     * Test of setFE method, of class DECC_G.
     */
    @Test
    public void testSetFE() {
        System.out.println("setFE");
        int FE = 0;
        DECC_G instance = null;
        instance.setFE(FE);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setwFEs method, of class DECC_G.
     */
    @Test
    public void testSetwFEs() {
        System.out.println("setwFEs");
        int wFEs = 0;
        DECC_G instance = null;
        instance.setwFEs(wFEs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setComparator method, of class DECC_G.
     */
    @Test
    public void testSetComparator() {
        System.out.println("setComparator");
        Comparator<DoubleSolution> comparator = null;
        DECC_G instance = null;
        instance.setComparator(comparator);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSansdeBuilder method, of class DECC_G.
     */
    @Test
    public void testSetSansdeBuilder() {
        System.out.println("setSansdeBuilder");
        SaNSDEBuilder sansdeBuilder = null;
        DECC_G instance = null;
        instance.setSansdeBuilder(sansdeBuilder);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDeFrobeniusBuilder method, of class DECC_G.
     */
    @Test
    public void testSetDeFrobeniusBuilder() {
        System.out.println("setDeFrobeniusBuilder");
        DEFrobeniusBuilder deFrobeniusBuilder = null;
        DECC_G instance = null;
        instance.setDeFrobeniusBuilder(deFrobeniusBuilder);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMaxEvaluations method, of class DECC_G.
     */
    @Test
    public void testSetMaxEvaluations() {
        System.out.println("setMaxEvaluations");
        int maxEvaluations = 0;
        DECC_G instance = null;
        instance.setMaxEvaluations(maxEvaluations);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPenalize_value method, of class DECC_G.
     */
    @Test
    public void testSetPenalize_value() {
        System.out.println("setPenalize_value");
        double penalize_value = 0.0;
        DECC_G instance = null;
        instance.setPenalize_value(penalize_value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSubcomponent method, of class DECC_G.
     */
    @Test
    public void testSetSubcomponent() {
        System.out.println("setSubcomponent");
        double subcomponent = 0.0;
        DECC_G instance = null;
        instance.setSubcomponent(subcomponent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
