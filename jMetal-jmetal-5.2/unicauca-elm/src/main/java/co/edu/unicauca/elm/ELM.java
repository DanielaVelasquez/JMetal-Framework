package co.edu.unicauca.elm;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.NotConvergedException;

public class ELM 
{
    /**
     * ELM Type for classification and regression
     */
    public enum ELMType
    {
        REGRESSION,
        CLASSIFICATION
    }
    /**
     * Activation function - Type of activation function: 'sig' for
     * Sigmoidal function 'sin' for Sine function 'hardlim' for Hardlim function
     * 'tribas' for Triangular basis function 'radbas' for Radial basis function
     */
    public enum ActivationFunction
    {
        SIG,
        SIN,
        HARDLIM,
        TRIBAS,
        RADBAS
    }
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     *Activation Function  
     */
    private ActivationFunction function;
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
    public ELM(ELMType elm_type,int hidden_neurons,ActivationFunction activation_function)
    {
        this.elm_type = elm_type;
        this.hidden_neurons = hidden_neurons;
        this.function = activation_function;
        this.training_time = 0;
        this.testing_time = 0;
        this.training_accuracy = 0;
        this.testing_accuracy = 0;
        this.output_neurons = 0;
    }
    
    public void train()
    {
        if(input_weight == null)
            
    }
    
}
