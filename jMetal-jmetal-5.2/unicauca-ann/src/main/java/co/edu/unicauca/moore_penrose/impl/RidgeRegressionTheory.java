package co.edu.unicauca.moore_penrose.impl;

import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;

public class RidgeRegressionTheory extends AbstractMoorePenroseMethod {

    /**
     * -----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------
     */
    public RidgeRegressionTheory(double[] values) {
        super(values);
    }

    @Override
    public DenseMatrix calculate(DenseMatrix A){
        /*	Moore-Penrose generalized inverse maxtrix
         * 	Theory:Ridge regression
         *	MP(A) = inv((H'*H+lumda*I))*H'
         */

        if (values == null || values.length == 0) {
            throw new UnsupportedOperationException("Values must contain lumda value in its first position.");
        }
        double lumda = values[0];
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
