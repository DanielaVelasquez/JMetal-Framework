package co.edu.unicauca.elm;

import co.edu.unicauca.matrix.util.MatrixUtil;
import co.edu.unicauca.function.Function;
import co.edu.unicauca.matrix.MoorePenroseInverse;
import no.uib.cipr.matrix.DenseMatrix;



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
    /**
     * Activation function 
     * Type of activation function: 
     * - 'SIG' for Sigmoidal function 
     * - 'SIN' for Sine function 
     * - 'HARDLIM' for Hardlim function
     * - 'TRIBAS' for Triangular basis function 
     * - 'RADBAS' for Radial basis function
     */
    /*public enum ActivationFunction
    {
        SIG,
        SIN,
        HARDLIM,
        TRIBAS,
        RADBAS
    }*/
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     *Activation Function  
     */
    private Function function;
    /**
     * Time while testing (in seconds)
     */
    private double testing_time;
    /**
     * ELM accuracy while testing
     */
    private double testing_accuracy;
    /**
     * Time while training (in seconds)
     */
    private double training_time;
    /**
     * ELM accuracy while training
     */
    private double training_accuracy;    
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
    private DenseMatrix Y_training;
    /**
     * Output testing data
     */
    private DenseMatrix Y_testing;
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
     */
    public ELM(ELMType elm_type,int hidden_neurons,Function activation_function)
    {
        this.elm_type = elm_type;
        this.hidden_neurons = hidden_neurons;
        this.function = activation_function;
        this.training_time = 0;
        this.testing_time = 0;
        this.training_accuracy = 0;
        this.testing_accuracy = 0;
        this.output_neurons = 0;
        this.num_testing_data = 0;
        this.num_training_data = 0;
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
        DenseMatrix pinvH = MoorePenroseInverse.getMoorePenroseInverse(0.000001, H);
        
        DenseMatrix transT = new DenseMatrix(num_training_data,output_neurons);
        tabular_training.transpose(transT);
        
        output_weight = new DenseMatrix(num_training_data,output_neurons);
        pinvH.mult(transT, output_weight);
        
        calculate_output(H, num_training_data);
        training_accuracy = evaluate(tabular_training);
        long end_training =  System.currentTimeMillis();
        training_time = (end_training - start_training) * 1.0f / 1000;
    }
    /**
     *  Calculate the matrix's output
     */
    public void calculate_output(DenseMatrix H,int numData)
    {
        T = new DenseMatrix(output_neurons,numData);
        H.mult(output_weight,T);
        T.transpose();
    }
    /**
     * Calculate the output matrix of the hidden layer
     * @return the output matrix of the hidden layer
     */
    private DenseMatrix calculateH(DenseMatrix X) 
    {
        /*int numData = X.numColumns();
        DenseMatrix H = new DenseMatrix(numData, hidden_neurons);
        
        for(int i = 0; i < numData; i++)
        {
            for(int j = 0; j < hidden_neurons; j++)
            {
                //Get jth row from input_weight
                int[] row = {j};
                //Indexes available in input_weight columns
                int[] column = Matrices.index(0, input_weight.numColumns());
                Matrix wTemp = Matrices.getSubMatrix(input_weight, row, column);
                DenseMatrix w = new DenseMatrix(wTemp);
                
                //Get i-th column from X
                Vector xTemp = Matrices.getColumn(X, i);
                DenseMatrix x = new DenseMatrix(xTemp);
                
                //Multiply w*x
                DenseMatrix mult = new DenseMatrix(1,1);
                w.mult(x, mult);
                
                //Get value from multiplication
                double wx = mult.get(0, 0);
                double b = bias_hidden_neurons.get(j, 0);
                //Apply the activation function
                double aux = function.evaluate(wx + b);
                H.set(i, j, aux);
            }
            
        }
        return H;
        */
        int numData = X.numColumns();
        DenseMatrix tempH = new DenseMatrix(numData, hidden_neurons);
        input_weight.mult(X, tempH);
        
        DenseMatrix bias_matrix = new DenseMatrix(numData, hidden_neurons);
        for (int i = 0; i < numData; i++) {
            for (int j = 0; j < hidden_neurons; j++) {
                bias_matrix.set(i, j, bias_hidden_neurons.get(j, 0));
            }
        }
        
        tempH.add(bias_matrix);
        
        DenseMatrix H = new DenseMatrix(numData, hidden_neurons);
        for (int i = 0; i < numData; i++) {
            for (int j = 0; j < hidden_neurons; j++) {
                H.set(i,j,function.evaluate(tempH.get(i, j)));
            }
        }
        
        return H;
    }
    /**
     * Determines accuracy in ELM from a tabular output and the network output
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
}
