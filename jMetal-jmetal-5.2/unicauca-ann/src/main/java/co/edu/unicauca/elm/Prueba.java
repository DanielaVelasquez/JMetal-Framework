/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.elm;

import co.edu.unicauca.function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.MultiplicationMethod;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Matrix;

/**
 *
 * @author Daniela
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        System.out.println("lfjdfjafladfjd");
        /*ELM elm;
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
        
        elm = new ELM(ELM.ELMType.CLASSIFICATION, hidden_neuron, new Sigmoid(),2,new RidgeRegressionTheory(new double[]{0.000001}));
        elm.setInputWeight(input_weight);
        elm.setBiasHiddenNeurons(bias);
        elm.setX(X);
        elm.setY(Y);
        
        elm.train();
        System.out.println("Accuracy: "+elm.getAccuracy());
        elm.test();
        System.out.println("Test Accuracy: "+elm.getAccuracy());
        
        System.out.println("Values\n");
        double values[] = elm.getOutputNetwork().getData();
        for(int i = 0; i< values.length;i++)
        {
            System.out.println(""+values[i]);
        }
*/
        
    }
    
}
