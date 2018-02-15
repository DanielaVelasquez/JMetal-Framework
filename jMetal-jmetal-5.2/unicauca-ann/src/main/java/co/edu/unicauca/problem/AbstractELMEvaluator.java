package co.edu.unicauca.problem;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm.ELM;
import java.util.ArrayList;
import java.util.List;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public abstract class AbstractELMEvaluator extends AbstractDoubleProblem {

    /**
     * -----------------------------------------------------------------------------------------
     * Enums
     * -----------------------------------------------------------------------------------------
     */
    
    /**
     * Evaluator's type
     */
    public enum EvaluatorType {

        /**
         * Trainig and testing
         */
        TT,
        /**
         * Cross validation
         */
        CV
    }
    
    /**
     * -----------------------------------------------------------------------------------------
     * Atributes
     * -----------------------------------------------------------------------------------------
     */
    
    /**
     * Collection of data for training ELM, it's use when looking for best input
     * weigths
     */
    protected DataSet training_data_set;
    /**
     * Collection of data for testing ELM
     */
    protected DataSet testing_data_set;
    /**
     * Extreme learning machine that will be execute to calculate the input
     * weights accuracy for an specific data set
     */
    protected ELM elm;
    /**
     * Evaluator's type
     */
    private EvaluatorType type;
    /**
     * Evaluator's name, identify the problem that is solving
     */
    private String name;
    /**
     * Input weight's from last given solution
     */
    protected DenseMatrix input_weights;
    /**
     * Bias values from last given solution
     */
    protected DenseVector bias;

    /**
     * -----------------------------------------------------------------------------------------
     * Methods
     * -----------------------------------------------------------------------------------------
     */
    
    /**
     * Creates a new ELM evaluator
     *
     * @param type Evaluator's type
     * @param name identifies the problem it's solving
     * @param training_data_set collection of data for training ELM
     * @param testing_data_set collection of data for testing ELM
     */
    public AbstractELMEvaluator(EvaluatorType type, String name, DataSet training_data_set, DataSet testing_data_set) {
        this.training_data_set = training_data_set;
        this.testing_data_set = testing_data_set;
        this.type = type;
        this.name = name;
    }

    @Override
    public void evaluate(DoubleSolution solution) {
        getInputWeightsBiasFrom(solution);
        elm.setInputWeight(input_weights);
        elm.setBiasHiddenNeurons(bias);
        double accuracy = this.train();
        solution.setObjective(0, (accuracy));
        solution.setAttribute("B", elm.getOuputWightNorm());
    }
    
    /**
     * Obtains input weigths and bias from a solution
     * @param solution Solution whic contains weight between hidden layer and
     * input layer and finally bias for i-th hidden neuron
     */
    protected void getInputWeightsBiasFrom(DoubleSolution solution)
    {
        int numberOfVariables = getNumberOfVariables();
        int hidden_neurons = elm.getHiddenNeurons();
        int input_neurons = elm.getInputNeurons();
        int row = 0;
        int col = 0;
        input_weights = new DenseMatrix(hidden_neurons, input_neurons);
        bias = new DenseVector(hidden_neurons);
        int i = 0;
        
        while (i < numberOfVariables) 
        {
            input_weights.set(row, col, solution.getVariableValue(i));
            col++;
            if (col >= input_neurons) {
                i++;
                bias.set(row, solution.getVariableValue(i));
                col = 0;
                row++;
            }
            i++;
        }
    }

    /**
     * Load initial configuration for an ELMEvaluator
     */
    protected void loadInitalConfiguration() {
        int numberOfVariables = elm.getHiddenNeurons() * elm.getInputNeurons() + elm.getHiddenNeurons();
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(1);
        setNumberOfConstraints(0);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-1.0);
            upperLimit.add(1.0);
        }
        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    public ELM getElm() {
        return elm;
    }

    public EvaluatorType getType() {
        return type;
    }

    public String getProblemName() {

        return this.name;
    }

    /**
     * Trains an ELM using traing data set(s), with some random input weight
     * @return accuracy obtained from training
     */
    public abstract double train();

    /**
     * Test an ELM using testing data set(s), with best input weights found
     * @param solution
     */
    public abstract double test(DoubleSolution solution);
}