package co.edu.unicauca.elm;

import co.edu.unicauca.matrix.util.MatrixUtil;
import co.edu.unicauca.function.Function;
import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

/**
 * Extreme Learning Machine
 */
public class ELM 
{
    /**-----------------------------------------------------------------------------------------
    * Constants
    *-----------------------------------------------------------------------------------------*/
    /**
     * Minimum value allowed in ELM's weight
     */
    private static final double MIN_VALUE = 0;
    /**
     * Max value allowed in ELM's weight
     */
    private static final double MAX_VALUE = 1;

    /**-----------------------------------------------------------------------------------------
     * Enums
     *-----------------------------------------------------------------------------------------*/
    /**
     * ELM Type for classification and regression
     */
    public enum ELMType
    {
        REGRESSION,
        CLASSIFICATION
    }
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     *Activation Function  
     */
    private Function function;
    /**
     * ELM accuracy while training or training
     * it contains accuracy of the last activity performed
     */
    private double accuracy;    
    /**
     * ELM type (Regression or Classification)
     */    
    private ELMType elm_type;
    /**
     * Number of input neurons
     */
    private int input_neurons;
    /**
     * Number of hidden neurons assigned to the ELM
     */
    private int hidden_neurons;
    /**
     * Number of output neurons
     */
    private int output_neurons;
    /**
     * Number of data
     */
    private int number_data;
    /**
     * Bias of hidden neurons
     */
    private DenseVector bias_hidden_neurons;
    /**
     * Matrix input weight - Weights between input layer and the hidden layer
     * w_ij is the weight between neuron i (in input layer) and neuron j (in the hidden layer)
     */
    private DenseMatrix input_weight;
    /**
     * Matrix output weight - Weights between the hidden layer and output layer 
     * b_jk represents the weight between the neuron j (in the hidden layer) and neuron k (in output layer)
     */
    private DenseMatrix output_weight;
    /**
     * Input data, where every column is a data set row
     */
    private DenseMatrix X;
    /**
     * Output training/testing data
     */
    private DenseVector Y;
    /**
     * Tabular output data, represt the output data as a tabular
     * matrix with -1 and 1 values
     */
    private DenseMatrix tabular;
    /**
     * Output vector of the network
     */
    private DenseVector T;
    /**
     * Method calculates moore-penrose pseudoinverse
     */
    private AbstractMoorePenroseMethod inverse;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    /**
     * Construct an ELM
     * @param elm_type Elm type - classification or regression
     * @param hidden_neurons Number of hidden neurons assigned to the ELM
     * @param activation_function Type of activation function - Type of activation function: 'sig' for
     * Sigmoidal function 'sin' for Sine function 'hardlim' for Hardlim function
     * 'tribas' for Triangular basis function 'radbas' for Radial basis function
     * @param classes number of classes
     * @param inverse Method for calculation of moore penrose inverse
     */
    public ELM(ELMType elm_type,int hidden_neurons,Function activation_function,int classes, AbstractMoorePenroseMethod inverse)
    {
        this.elm_type = elm_type;
        this.hidden_neurons = hidden_neurons;
        this.function = activation_function;
        this.output_neurons = classes;
        this.accuracy = 0;
        this.number_data = 0;
        this.inverse = inverse;
    }
    /**
     * Train an artificial neural network using ELM algorithm 
     * with the input and output data given, if input weight and/or 
     * bias are not defined in the ELM they will be randomly assigned
     */
    public void train()
    {
        /**
         * In case the input weights is not defined in the ELM
         * they will be randomly assigned
         */
        if(input_weight == null)
            input_weight = MatrixUtil.randomFill(hidden_neurons, input_neurons, MIN_VALUE, MAX_VALUE);
        /**
         * In case the bias of hidden neurosn is not defined in the ELM
         * they will be randomly assigned
         */
        if(bias_hidden_neurons == null)
            bias_hidden_neurons = MatrixUtil.randomFill(hidden_neurons, MIN_VALUE, MAX_VALUE);
        
        //Get output matrix from hidden layer
        DenseMatrix H = calculateH(X);
        DenseMatrix pinvH = inverse.calculate(H);//MultiplicationMethod.getMoorePenroseInverse(0.000001, H);
        DenseMatrix transT = new DenseMatrix(number_data,output_neurons);
        tabular.transpose(transT);
        
        output_weight = new DenseMatrix(hidden_neurons,output_neurons);
        pinvH.mult(transT, output_weight);
        
        DenseMatrix T = calculate_output(H, number_data);
        accuracy = evaluate(tabular,T);
    }
    /**
     * Test the artificial neural network with the input and output data given
     */
    public void test()
    {
        //Get output matrix from hidden layer
        DenseMatrix H = calculateH(X);
        DenseMatrix T = calculate_output(H, number_data);
        accuracy = evaluate(tabular,T);
    }
    /**
     *  Calculate the matrix's output
     */
    private DenseMatrix calculate_output(DenseMatrix H,int numData)
    {
        DenseMatrix TTemp = new DenseMatrix(numData,output_neurons);
        H.mult(output_weight,TTemp);
        DenseMatrix T = new DenseMatrix(output_neurons,numData);
        TTemp.transpose(T);
        return T;
    }
    /**
     * Calculate the output matrix of the hidden layer
     * @return the output matrix of the hidden layer
     */
    private DenseMatrix calculateH(DenseMatrix X) 
    {
        int numData = X.numColumns();
        DenseMatrix tempH = new DenseMatrix(hidden_neurons, numData );
        input_weight.mult(X, tempH);
        
        DenseMatrix bias_matrix = new DenseMatrix( hidden_neurons,numData);
        for (int i = 0; i < hidden_neurons; i++) {
            for (int j = 0; j < numData; j++) {
                bias_matrix.set(i, j, bias_hidden_neurons.get(i));
            }
        }
        
        tempH.add(bias_matrix);
        
        DenseMatrix HTemp = new DenseMatrix(hidden_neurons,numData);
        for (int i = 0; i <hidden_neurons ; i++) {
            for (int j = 0; j < numData; j++) {
                HTemp.set(i,j,function.evaluate(tempH.get(i, j)));
            }
        }
        DenseMatrix H = new DenseMatrix(numData,hidden_neurons);
        HTemp.transpose(H);
        return H;
        
    }
    /**
     * Contrast the output of the neural network (T) and a tabular output (Y)
     * besides creates the output network with the original format
     * @param tabular tabular matrix 
     * @param TNetwork output from the network
     * @return ELM's accuracy
     */
    private double evaluate(DenseMatrix tabular, DenseMatrix TNetwork)
    {
        double accuracy = 0;
        if (elm_type == ELMType.CLASSIFICATION)
        {
            int numCols = tabular.numColumns();
            int errors = 0;
            this.T = new DenseVector(numCols);
            /**
             * Finds the index where the biggest value is in every T column 
             * and tabular column, if the indexes aren't the same, an error is
             * counted
             */
            for(int j = 0; j < numCols; j++)
            {
                double maxY = tabular.get(0, j);
                int indexY = 0;

                double maxT = TNetwork.get(0, j);
                int indexT = 0;

                for(int i = 1; i < output_neurons; i++)
                {
                    if(tabular.get(i, j) > maxY)
                    {
                        maxY = tabular.get(i, j);
                        indexY = i;
                    }

                    if(TNetwork.get(i, j) > maxT)
                    {
                        maxT = TNetwork.get(i, j);
                        indexT = i;
                    }
                }

                if(indexY != indexT)
                {
                    errors++;
                }
                //Original format for the output network
                T.set(j,indexT);
            }

            accuracy = 1 - ((double) errors / (double) numCols);
        }
        else
        {
            /**
             * Square differences between tabular and output network
             */
            int numCols = tabular.numColumns();
            this.T = new DenseVector(numCols);
            double aux = 0;            
            
            for(int j = 0; j < numCols; j++)
            {
                double valueY = tabular.get(0, j);
                double valueT = TNetwork.get(0, j);
                aux += Math.pow(valueY - valueT, 2);
                //Original format for the output network
                this.T.set(j,valueT);
            }

            accuracy = Math.sqrt(aux / numCols);
        }
        
        return accuracy;
    }
    /**
     * Creates tabular from a output vector
     * @param y output vector
     * @return tabular matrix (fill with 1 and -1)
     */
    private DenseMatrix tabularOutput(DenseVector y)
    {
        DenseMatrix Y;
        if(elm_type == ELMType.CLASSIFICATION)
        {
            int n = y.size();
            Y = new DenseMatrix(output_neurons, n);
            
            
            for(int j = 0; j < n; j++)
            {
                for(int i = 0; i < output_neurons; i++)
                {
                    if(i == y.get(j))
                        Y.set(i, j, 1);
                    else
                        Y.set(i,j,-1);
                }
            }
        }
        else
        {
            Y = new DenseMatrix(output_neurons, 1);
            
            for(int i =0; i < output_neurons; i++)
            {
                Y.set(i,0,y.get(i));
            }
        }
        return Y;
    }
    
    public DenseMatrix getInputWeight() {
        return input_weight;
    }

    public void setInputWeight(DenseMatrix input_weight) {
        this.input_weight = input_weight;
        this.input_neurons = this.input_weight.numColumns();
    }

    public void setY(DenseVector Y) {
        this.Y = Y;
        this.tabular = tabularOutput(Y);
    }

    public DenseVector getBiasHiddenNeurons() {
        return bias_hidden_neurons;
    }

    public void setBiasHiddenNeurons(DenseVector bias_hidden_neurons) {
        this.bias_hidden_neurons = bias_hidden_neurons;
    }

    public void setX(DenseMatrix X) {
        this.X = X;
        this.number_data = this.X.numColumns();
        this.input_neurons = this.X.numRows();
    }
    
    public DenseMatrix getX() {
        return X;
    }
    /**
     * Returns networks´s output
     * @return networks´s output
     */
    public DenseVector getOutputNetwork() {
        return T;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getInputNeurons() {
        return input_neurons;
    }

    public int getHiddenNeurons() {
        return hidden_neurons;
    }

   
   
}
