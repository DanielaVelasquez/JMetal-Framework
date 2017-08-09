package co.edu.unicauca.moore_penrose.impl;

import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;

/**
 * A+ = A∗(AA∗)−1 Where A* A's transpose and A+ pseudoinverse of A
 */
public class MultiplicationMethod extends AbstractMoorePenroseMethod {

    /**
     * -----------------------------------------------------------------------------------------
     * Methods
     * -----------------------------------------------------------------------------------------
     */
    /**
     * @param values
     */
    public MultiplicationMethod(double[] values) {
        super(values);
    }

    /**
     * MP(A) = ((A'A)^-1)A'
     *
     * @param A matrix
     * @return pseudoinverse moore penrose of A
     */
    public DenseMatrix calculate(DenseMatrix A) {

        int n = A.numRows();
        int m = A.numColumns();
        //A*
        DenseMatrix transpose = new DenseMatrix(m, n);
        A.transpose(transpose);

        //A*A
        DenseMatrix ATransposeA = new DenseMatrix(m, m);
        transpose.mult(A, ATransposeA);

        //(AA∗)−1
        DenseMatrix I = Matrices.identity(m);
        DenseMatrix inv = I.copy();
        ATransposeA.solve(I, inv);
        //A∗(AA∗)−1
        DenseMatrix moore_penrose = new DenseMatrix(m, n);
        inv.mult(transpose, moore_penrose);
        return moore_penrose;
    }

}
