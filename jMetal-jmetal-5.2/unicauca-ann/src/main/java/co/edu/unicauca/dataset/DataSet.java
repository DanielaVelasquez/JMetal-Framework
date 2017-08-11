package co.edu.unicauca.dataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

/**
 * Collection of data, represent a single statistical data matrix
 */
public class DataSet {

    /**
     * -----------------------------------------------------------------------------------------
     * Atributes
     * -----------------------------------------------------------------------------------------
     */
    /**
     * Path where the file is allocated
     */
    private String path;
    /**
     * File name that containts data set File must contain in its firt row:
     * number-data number-variables number-clases
     */
    private String file_name;
    /**
     * Data, every column represents an instance
     */
    private DenseMatrix X;
    /**
     * Result for i-th column of instance X, it represents the class atribute
     */
    private DenseVector Y;
    /**
     * Number of posibles results for instances
     */
    private int number_classes;
    /**
     * Index for add a new column
     */
    private int index;

    /**
     * -----------------------------------------------------------------------------------------
     * Methods
     * -----------------------------------------------------------------------------------------
     */
    /**
     * Creates a data set reading it from a file
     *
     * @param path path where file is allocated
     * @param file_name File name that containts data set. File must contain in
     * its firt row: number-data number-variables number-clases Every row must
     * be an instance, every colum is a variable and one column is the result
     * from that instance
     * @param result_index
     */
    public DataSet(String path, String file_name, int result_index) throws IOException {
        this.path = path;
        this.file_name = file_name;
        this.loadInstances(result_index);
    }

    /**
     * Creates a data set given an input data and output data
     *
     * @param X input data, every column represents an instance
     * @param Y output data, every value is the result for i-th column on X
     */
    public DataSet(DenseMatrix X, DenseVector Y) throws Exception {
        if (X.numColumns() != Y.size()) {
            throw new Exception("X's number of columns must be the same of Y's size in order they represent a data set ");
        }
        this.X = X;
        this.Y = Y;
    }

    public DataSet(int numData, int numVariables, int number_classes) {

        X = new DenseMatrix(numVariables, numData);
        Y = new DenseVector(numData);
        index = 0;
        this.number_classes = number_classes;
    }

    public void addDataColumn(Vector col) {
        int numData = X.numRows();

        for (int i = 0; i < numData; i++) {
            try {
                X.set(i, index, col.get(i));
            } catch (Exception e) {
                System.out.println("Error insert");
            }
        }
    }

    public void addValueColumn(double value) {
        Y.set(index, value);
    }

    public void nextIndex() {
        index++;
    }

    public int getIndex() {
        return index;
    }

    private void loadInstances(int resul_index) throws FileNotFoundException, IOException {

        BufferedReader br = null;
        FileReader fr = null;

        fr = new FileReader(path + "/" + file_name);
        br = new BufferedReader(fr);
        String[] firstRow = br.readLine().split(" ");
        int numRows = Integer.parseInt(firstRow[0]);
        int numCols = Integer.parseInt(firstRow[1]);
        number_classes = Integer.parseInt(firstRow[2]);

        X = new DenseMatrix(numCols - 1, numRows);
        Y = new DenseVector(numRows);
        String actualLine;
        int i = 0;

        while ((actualLine = br.readLine()) != null) {
            String[] aux = actualLine.split(" ");

            for (int j = 0; j < numCols; j++) {
                double value = Double.parseDouble(aux[j]);
                if (j != resul_index) {
                    X.set(j, i, value);
                } else {
                    Y.set(i, value);
                }
            }
            i++;
        }
    }

    public DenseMatrix getX() {
        return X;
    }

    public DenseVector getY() {
        return Y;
    }

    public int getNumber_classes() {
        return number_classes;
    }

}
