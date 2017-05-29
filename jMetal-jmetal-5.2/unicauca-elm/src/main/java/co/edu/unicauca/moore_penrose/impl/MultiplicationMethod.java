package co.edu.unicauca.moore_penrose.impl;

import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;

/**
* A+ = A∗(AA∗)−1 Where A* A's transpose and A+ pseudoinverse of A
* http://buzzard.ups.edu/courses/2014spring/420projects/math420-UPS-spring-2014-macausland-pseudo-inverse.pdf
* page 4
*/
public class MultiplicationMethod extends AbstractMoorePenroseMethod
{
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public MultiplicationMethod(double[] values)
    {
        super(values);
    }
    
    public DenseMatrix calculate(DenseMatrix A) 
    {
              
        int n = A.numRows();
        int m = A.numColumns();
        //A*
        DenseMatrix transpose = new  DenseMatrix(m,n);
        A.transpose(transpose);
        
        //AA*
        DenseMatrix moore_penrose1 = new DenseMatrix(m,m);
        transpose.mult(A, moore_penrose1);
        
        //(AA∗)−1
        DenseMatrix I = Matrices.identity(m);
        DenseMatrix moore_penrose2 = I.copy();
        moore_penrose1.solve(I, moore_penrose2);
        //A∗(AA∗)−1
        DenseMatrix moore_penrose = new DenseMatrix(m,n);
        moore_penrose2.mult(transpose,moore_penrose);
        return moore_penrose;
    }

    
}
