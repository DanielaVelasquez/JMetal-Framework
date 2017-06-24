package org.uma.jmetal.problem.singleobjective.ann;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm.ELM;
import co.edu.unicauca.elm.util.ELMUtil;
import co.edu.unicauca.function.Function;
import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class AbstractELMEvaluator extends AbstractDoubleProblem
{
     /**-----------------------------------------------------------------------------------------
     * Enums
     *-----------------------------------------------------------------------------------------*/
    /**
     * Evaluator's type
     * TT: Training and Testing
     * CV: CrossValidation
     */
    public enum EvaluatorType
    {
        TT,
        CV
    }
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Extreme learning machine that will be execute to 
     * calculate the input weights accuracy for an specific data set
     */
    private ELM elm;
    /**
     * Collection of data used for ELM
     */
    private DataSet data_set;
    /**
     * Evaluator's type
     */
    private EvaluatorType type;
    /**
     * Evaluator's name, identify the problem that is solving
     */
    private String name;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    /**
     * Creates a new ELM evaluator
     * @param hidden_neurons number of hidden neurons for ELM
     * @param data_set collection of data used for ELM
     * @param activation_function Type of activation function
     * @param inverse Moore Penrose Method for using in ELM
     * @param type Evaluator's type
     * @param name identifies the problem it's solving
     */
    public AbstractELMEvaluator(int hidden_neurons, DataSet data_set, Function activation_function,AbstractMoorePenroseMethod inverse, EvaluatorType type,String name)
    {
        this.data_set = data_set;
        this.elm = new ELM(ELMUtil.getELMType(data_set), hidden_neurons, activation_function, hidden_neurons, inverse);
        this.elm.setX(data_set.getX());
        this.elm.setY(data_set.getY());
        this.type = type;
        this.name = name;
    }

    @Override
    public void evaluate(DoubleSolution solution) 
    {
        
    }
    
}
