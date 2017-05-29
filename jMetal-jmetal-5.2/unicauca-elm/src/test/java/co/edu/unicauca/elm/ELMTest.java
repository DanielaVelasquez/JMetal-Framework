package co.edu.unicauca.elm;

import co.edu.unicauca.function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import junit.framework.TestCase;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class ELMTest extends TestCase
{
    
    private ELM elm;
    
    public ELMTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        int input_neuron = 2;
        int output_neuron = 2;
        int hidden_neuron = 3;
        int trainig_data = 5;
        
        DenseMatrix input_weight = new DenseMatrix(hidden_neuron,input_neuron);
        input_weight.set(0,0,0.410);
        input_weight.set(0,1,0.208);
        input_weight.set(1,0,0.968);
        input_weight.set(1,1,0.006);
        input_weight.set(2,0,0.940);
        input_weight.set(2,1,0.947);
        
        DenseMatrix bias = new DenseMatrix(hidden_neuron,1);
        bias.set(0,0,0.731);
        bias.set(1,0,0.333);
        bias.set(2,0,0.964);
        
        DenseMatrix X = new DenseMatrix(input_neuron,trainig_data);
        X.set(0,0,5);
        X.set(0,1,4);
        X.set(0,2,6);
        X.set(0,3,8);
        X.set(0,4,11);
        X.set(1,0,5);
        X.set(1,1,8);
        X.set(1,2,8);
        X.set(1,3,10);
        X.set(1,4,9);
        
        DenseVector Y = new DenseVector(trainig_data);
        Y.set(0,0);
        Y.set(1,0);
        Y.set(2,1);
        Y.set(3,1);
        Y.set(4,0);
        
        elm = new ELM(ELM.ELMType.CLASSIFICATION, hidden_neuron, new Sigmoid(),2,new MultiplicationMethod(null));
        elm.setInput_weight(input_weight);
        elm.setBias_hidden_neurons(bias);
        elm.setX_training(X);
        elm.setY_training(Y);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of train method, of class ELM.
     */
    @Test
    public void testTrain() {
        int trainig_data = 5;
        int input_neuron = 2;
        System.out.println("train");
        elm.train();
        // TODO review the generated test code and remove the default call to fail.
        DenseMatrix T = new  DenseMatrix(input_neuron,trainig_data);
        T.set(0,0,0.624);
        T.set(0,1,0.986);
        T.set(0,2,-0.001);
        T.set(0,3,-0.274);
        T.set(0,4,-0.336);
        T.set(1,0,-0.624);
        T.set(1,1,-0.986);
        T.set(1,2,0.001);
        T.set(1,3,0.274);
        T.set(1,4,0.336);
        
        double[] r = elm.getT().getData();
        double[] e = T.getData();
        assertArrayEquals(e, r, 0.8);
    }

    
}
