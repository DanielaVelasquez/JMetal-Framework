/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.elm;

import co.edu.unicauca.function.impl.Sigmoid;
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
        int f = 3;
        int c = 3;
        DenseMatrix m = new DenseMatrix(f, c);
        int s = 1;
        for(int i=0; i < f; i++)
            for(int j = 0; j< c; j++)
            {
                m.set(i, j, s);
                s++;
            }
        
        
        ELM elm = new ELM(ELM.ELMType.REGRESSION, 3, new Sigmoid());
        elm.setInput_weight(m);
        elm.calculateH(m);
    }
    
}
