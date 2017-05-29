package co.edu.unicauca.matrix;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;


public class MoorePenroseInverse 
{
    /*	Moore-Penrose generalized inverse maxtrix
     * 	Theory:Ridge regression
     *	MP(A) = inv((H'*H+lumda*I))*H'
     */
    public static DenseMatrix getMoorePenroseInverse(double lumda,DenseMatrix A)
    {
        int n = A.numColumns();
        int m = A.numRows();
        
        DenseMatrix At = new DenseMatrix(n, m);
        A.transpose(At);
        DenseMatrix AtA = new DenseMatrix(n, n);
        At.mult(A, AtA);

        DenseMatrix I = Matrices.identity(n);
        AtA.add(lumda, I);
        DenseMatrix AtAinv = I.copy();
        AtA.solve(I, AtAinv);

        DenseMatrix Ainv = new DenseMatrix(n, m);
        AtAinv.mult(At, Ainv);
        return Ainv;
    }
    
}
