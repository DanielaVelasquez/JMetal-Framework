package co.edu.unicauca.elm;

import co.edu.unicauca.matrix.util.MatrixUtil;
import co.edu.unicauca.function.Function;
import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;



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
     * Time while testing or training (in seconds)
     * it contains time of the last activity performed
     */
    private double time;
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
     * Number of training data
     */
    private int num_training_data;
    /**
     * Number of testing data
     */
    private int num_testing_data;
    /**
     * Matrix bias of hidden neurons
     */
    private DenseMatrix bias_hidden_neurons;
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
     * Input training data, where every column is a data set row
     */
    private DenseMatrix X_training;
    /**
     * Input testing data, where every column is a data set row
     */
    private DenseMatrix X_testing;
    /**
     * Output training data
     */
    private DenseVector Y_training;
    /**
     * Output testing data
     */
    private DenseVector Y_testing;
    /**
     * Tabular output training data
     */
    private DenseMatrix tabular_training;
    /**
     * Tabular output testing data
     */
    private DenseMatrix tabular_testing;
    /**
     * Output matrix of the network
     */
    private DenseMatrix T;
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
     */
    public ELM(ELMType elm_type,int hidden_neurons,Function activation_function,int classes, AbstractMoorePenroseMethod inverse)
    {
        this.elm_type = elm_type;
        this.hidden_neurons = hidden_neurons;
        this.function = activation_function;
        this.output_neurons = classes;
        this.time = 0;
        this.accuracy = 0;
        this.num_testing_data = 0;
        this.num_training_data = 0;
        this.inverse = inverse;
    }
    
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
            bias_hidden_neurons = MatrixUtil.randomFill(hidden_neurons, 1, MIN_VALUE, MAX_VALUE);
        
        //Take current time  
        long start_training = System.currentTimeMillis();
        
        //Get output matrix from hidden layer
        DenseMatrix H = calculateH(X_training);
        DenseMatrix pinvH = inverse.calculate(H);//MultiplicationMethod.getMoorePenroseInverse(0.000001, H);
        DenseMatrix transT = new DenseMatrix(num_training_data,output_neurons);
        tabular_training.transpose(transT);
        
        output_weight = new DenseMatrix(hidden_neurons,output_neurons);
        pinvH.mult(transT, output_weight);
        
        calculate_output(H, num_training_data);
        accuracy = evaluate(tabular_training);
        long end_training =  System.currentTimeMillis();
        time = (end_training - start_training) * 1.0f / 1000;
    }
    /**
     *  Calculate the matrix's output
     */
    private void calculate_output(DenseMatrix H,int numData)
    {
        DenseMatrix TTemp = new DenseMatrix(numData,output_neurons);
        H.mult(output_weight,TTemp);
        T = new DenseMatrix(output_neurons,numData);
        TTemp.transpose(T);
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
                bias_matrix.set(i, j, bias_hidden_neurons.get(i, 0));
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
     * @param tabular tabular matrix 
     * @return ELM's accuracy
     */
    private double evaluate(DenseMatrix tabular)
    {
        double accuracy = 0;
         
        if (elm_type == ELMType.CLASSIFICATION)
        {
            int numCols = tabular.numColumns();
            int errors = 0;
            /**
             * Finds the index where the biggest value is in every T column 
             * and tabular column, if the indexes aren't the same, an error is
             * counted
             */
            for(int j = 0; j < numCols; j++)
            {
                double maxY = tabular.get(0, j);
                int indexY = 0;

                double maxT = T.get(0, j);
                int indexT = 0;

                for(int i = 1; i < output_neurons; i++)
                {
                    if(tabular.get(i, j) > maxY)
                    {
                        maxY = tabular.get(i, j);
                        indexY = i;
                    }

                    if(T.get(i, j) > maxT)
                    {
                        maxT = T.get(i, j);
                        indexT = i;
                    }
                }

                if(indexY != indexT)
                {
                    errors++;
                }
            }

            accuracy = 1 - ((double) errors / (double) numCols);
        }
        else
        {
            /**
             * Square differences between tabular and output network
             */
            int numCols = tabular.numColumns();
            double aux = 0;            
            
            for(int j = 0; j < numCols; j++)
            {
                double valueY = tabular.get(0, j);
                double valueT = T.get(0, j);
                
                aux += Math.pow(valueY - valueT, 2);
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

    public DenseMatrix getInput_weight() {
        return input_weight;
    }

    public void setInput_weight(DenseMatrix input_weight) {
        this.input_weight = input_weight;
        this.input_neurons = this.input_weight.numColumns();
    }

    public DenseVector getY_training() {
        return Y_training;
    }

    public void setY_training(DenseVector Y_training) {
        this.Y_training = Y_training;
        this.tabular_training = tabularOutput(this.Y_training);
    }

    public DenseVector getY_testing() {
        return Y_testing;
    }

    public void setY_testing(DenseVector Y_testing) {
        this.Y_testing = Y_testing;
        this.tabular_testing = tabularOutput(Y_testing);
    }

    public DenseMatrix getBias_hidden_neurons() {
        return bias_hidden_neurons;
    }

    public void setBias_hidden_neurons(DenseMatrix bias_hidden_neurons) {
        this.bias_hidden_neurons = bias_hidden_neurons;
    }

    public DenseMatrix getX_training() {
        return X_training;
    }

    public void setX_training(DenseMatrix X_training) {
        this.X_training = X_training;
        this.num_training_data = this.X_training.numColumns();
        this.input_neurons = this.X_training.numRows();
    }

    public DenseMatrix getX_testing() {
        return X_testing;
    }

    public void setX_testing(DenseMatrix X_testing) {
        this.X_testing = X_testing;
        this.num_testing_data = this.X_testing.numColumns();
        this.input_neurons = this.X_testing.numRows();
    }

    public DenseMatrix getT() {
        return T;
    }

    public double getTime() {
        return time;
    }

    public double getAccuracy() {
        return accuracy;
    }
}
