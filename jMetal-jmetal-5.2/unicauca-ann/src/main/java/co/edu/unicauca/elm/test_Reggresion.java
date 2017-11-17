package co.edu.unicauca.elm;

import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

public class test_Reggresion {

    public static void main(String[] args) throws Exception {

        System.out.println(
                "lfjdfjafladfjd");
        ELM elm;
        int input_neuron = 2;
        int output_neuron = 3;
        int hidden_neuron = 3;
        int trainig_data = 6;

     
       

        elm = new ELM(ELM.ELMType.CLASSIFICATION, hidden_neuron, new Sigmoid(), output_neuron, new RidgeRegressionTheory(new double[]{0.0000000001}), 10);


        elm.train();

        System.out.println(
                "Accuracy: " + elm.getAccuracy());

        double valuesx[] = elm.getInputWeight().getData();
        for (int i = 0;
                i < valuesx.length;
                i++) {
            System.out.println(" , " + valuesx[i]);
        }
        /*   double valuesB[] = elm.getOuputWeigh();
        
         System.out.println("----ouput wi");
         for (int i = 0; i < valuesB.length; i++) {
         System.out.println(" , " + valuesB[i]);
         }*/

        System.out.println(
                "-+++");
        elm.test();

        System.out.println(
                "Test Accuracy: " + elm.getAccuracy());

        System.out.println(
                "Values\n");
        double values[] = elm.getOutputNetwork().getData();
        for (int i = 0;
                i < values.length;
                i++) {
            System.out.println("* " + values[i]);
        }
    }
}
