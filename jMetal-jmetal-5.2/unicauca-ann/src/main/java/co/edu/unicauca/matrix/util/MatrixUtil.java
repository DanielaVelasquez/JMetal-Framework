package co.edu.unicauca.matrix.util;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class MatrixUtil 
{
    /**
     * Randomly fill a matrix with a specific size, using JMetalRandom
     * @param rows Number of rows 
     * @param cols Number of columns
     * @param min_value Minimum value allowed in the matrix
     * @param max_value Max value allowed in the matrix
     * @return Matrix with 'rows' number of rows and 'cols' number of columns and random values
     */
    public static DenseMatrix randomFill(int rows, int cols,double min_value, double max_value)
    {
        DenseMatrix matrix = new DenseMatrix(rows, cols);
        JMetalRandom rnd = JMetalRandom.getInstance();
        for(int i = 0; i<rows;i++)
            for(int j = 0; j < cols; j++)
                matrix.set(i, j, rnd.nextDouble(min_value, max_value));
        return matrix;
    }
    /**
     * Randomly fill a vector with a specific size, using JMetalRandom
     * @param size Number of cells in the vector 
     * @param min_value Minimum value allowed in the vector
     * @param max_value Max value allowed in the vector
     * @return Vector with specified valued and random values
     */
    public static DenseVector randomFill(int size,double min_value, double max_value)
    {
        DenseVector vector = new DenseVector(size);
        JMetalRandom rnd = JMetalRandom.getInstance();
        for(int i = 0; i<size;i++)
            vector.set(i,rnd.nextDouble(min_value, max_value));
        return vector;
    }
}
