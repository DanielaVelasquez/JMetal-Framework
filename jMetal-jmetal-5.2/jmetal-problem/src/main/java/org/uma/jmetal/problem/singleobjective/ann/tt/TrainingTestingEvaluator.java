package org.uma.jmetal.problem.singleobjective.ann.tt;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm.ELM;
import co.edu.unicauca.elm.util.ELMUtil;
import co.edu.unicauca.function.Function;
import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import org.uma.jmetal.problem.singleobjective.ann.AbstractELMEvaluator;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Elm's Evaluator using training and testing files
 */
public abstract class TrainingTestingEvaluator extends AbstractELMEvaluator
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Creates a trainig testing evaluator
     * @param hidden_neurons number of hidden neurons in the ELM
     * @param training_data_set collection of data for training
     * @param testing_data_set collection of data for testing
     * @param activation_function activation function use in ELM
     * @param inverse Method for calculating Moore Penrose inverse
     * @param name Evaluator's name
     */
    public TrainingTestingEvaluator(int hidden_neurons, DataSet training_data_set, DataSet testing_data_set, Function activation_function, AbstractMoorePenroseMethod inverse, String name) {
        super(AbstractELMEvaluator.EvaluatorType.TT, name, training_data_set, testing_data_set);
        super.elm = new ELM(ELMUtil.getELMType(training_data_set), hidden_neurons, activation_function, hidden_neurons, inverse);
        this.elm.setX(training_data_set.getX());
        this.elm.setY(training_data_set.getY());
        
        super.loadInitalConfiguration();
    }

    @Override
    public double train() 
    {
        super.elm.train();
        return elm.getAccuracy();
    }
    
    @Override
    public double test(DoubleSolution solution)
    {
        super.getInputWeightsBiasFrom(solution);
        elm.setInputWeight(input_weights);
        elm.setBiasHiddenNeurons(bias);
        elm.train();
        elm.setX(testing_data_set.getX());
        elm.setY(testing_data_set.getY());
        elm.test();
        this.elm.setX(training_data_set.getX());
        this.elm.setY(training_data_set.getY());
        return elm.getAccuracy();
    }

}
