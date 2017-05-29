package co.edu.unicauca.matrix;

import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiplicationMethodTest {
    
    private DenseMatrix A;
    
    private DenseMatrix expResult;
    
    private MultiplicationMethod inverse;
    
    public MultiplicationMethodTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        inverse = new MultiplicationMethod(null);
        
        A = new DenseMatrix(5,3);
        A.set(0, 0, 0.98);
        A.set(0, 1, 0.99);
        A.set(0, 2, 1);
        
        A.set(1, 0, 0.98);
        A.set(1, 1, 0.99);
        A.set(1, 2, 1);
        
        A.set(2, 0, 0.99);
        A.set(2, 1, 1);
        A.set(2, 2, 1);
        
        A.set(3, 0, 1);
        A.set(3, 1, 1);
        A.set(3, 2, 1);
        
        A.set(4, 0, 1);
        A.set(4, 1, 1);
        A.set(4, 2, 1);
        
        expResult = new DenseMatrix(3,5);
        
        expResult.set(0, 0, 0);
        expResult.set(0, 1, 0);
        expResult.set(0, 2, -100);
        expResult.set(0, 3, 50);
        expResult.set(0, 4, 50);
        
        expResult.set(1, 0, -50);
        expResult.set(1, 1, -50);
        expResult.set(1, 2, 200);
        expResult.set(1, 3, -50);
        expResult.set(1, 4, -50);
        
        expResult.set(2, 0, 50);
        expResult.set(2, 1, 50);
        expResult.set(2, 2, -100);
        expResult.set(2, 3, 0.5);
        expResult.set(2, 4, 0.5);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calculate method, of class MultiplicationMethod.
     */
    @Test
    public void testCalculate() {
        System.out.println("calculate");
        DenseMatrix result = inverse.calculate(A);
        
        double[]  r = result.getData();
        double[]  e = expResult.getData();
        
        //Deltha 0.8 results are not exactly equal
        assertArrayEquals(e, r, 0.8);
    }
    
}
