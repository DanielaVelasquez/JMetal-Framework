package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm.ELM;
import co.edu.unicauca.elm.util.ELMUtil;
import co.edu.unicauca.elm_function.ELMFunction;
import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import java.util.ArrayList;
import java.util.List;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Vector;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Elm's Evaluator using cross validation files
 */
public abstract class AbstractCrossValidationEvaluator extends AbstractELMEvaluator {

    /**
     * -----------------------------------------------------------------------------------------
     * Atributes
     * -----------------------------------------------------------------------------------------
     */
    /**
     * Number of folders
     */
    private int number_folders;   
    /**
     * Data sets for training ELM
     */
    private List<DataSet> training_folders;
    /**
     * Data sets for testing ELM
     */
    private List<DataSet> testing_folders;

    /**
     * -----------------------------------------------------------------------------------------
     * Methods
     * -----------------------------------------------------------------------------------------
     */
    /**
     * Creates a cross validation evaluator
     *
     * @param type
     * @param name
     * @param training_data_set
     * @param testing_data_set
     * @param number_folders
     * @param hidden_neurons
     * @param activation_function
     * @param inverse
     * @param maxEvaluations Maximun number of evaluations for objective
     * function
     */
    public AbstractCrossValidationEvaluator(EvaluatorType type, String name, DataSet training_data_set, DataSet testing_data_set, int number_folders, int hidden_neurons, ELMFunction activation_function, AbstractMoorePenroseMethod inverse, int maxEvaluations) {
        
        super(type, name, training_data_set, testing_data_set);        
        this.number_folders = number_folders;
        super.elm = new ELM(ELMUtil.getELMType(training_data_set), hidden_neurons, activation_function, training_data_set.getNumber_classes(), inverse);
        int input_neuron = training_data_set.getX().numRows();
        super.elm.setInputNeurons(input_neuron);
        makeFolders();
        super.loadInitalConfiguration();
    }

    private void makeFolders() {
        training_folders = new ArrayList<>();
        testing_folders = new ArrayList<>();
        int trainig_size = super.training_data_set.getX().numColumns();
        int number_variables = training_data_set.getX().numRows();
        int number_data = training_data_set.getX().numColumns();
        int number_clases = training_data_set.getNumber_classes();
        DenseMatrix x = training_data_set.getX();
        DenseVector y = training_data_set.getY();
        int aditionals = number_data % number_folders;
        int sizeFolder = number_data / number_folders;

        for (int i = 0; i < number_folders; i++) {
            if (i < aditionals) {
                training_folders.add(new DataSet((sizeFolder * (number_folders - 1)) + (aditionals - 1), number_variables, number_clases));
                testing_folders.add(new DataSet(sizeFolder + 1, number_variables, number_clases));
            } else {
                training_folders.add(new DataSet((sizeFolder * (number_folders - 1)) + aditionals, number_variables, number_clases));
                testing_folders.add(new DataSet(sizeFolder, number_variables, number_clases));
            }
        }

        for (int i = 0; i < trainig_size; i++) {
            Vector data = Matrices.getColumn(x, i);
            double value = y.get(i);
            int result = i % number_folders;
            for (int j = 0; j < number_folders; j++) {

                if (result != j) {
                    DataSet training = training_folders.get(j);
                    training.addDataColumn(data);
                    training.addValueColumn(value);
                    training.nextIndex();
                } else {
                    DataSet testing = testing_folders.get(j);
                    testing.addDataColumn(data);
                    testing.addValueColumn(value);
                    testing.nextIndex();
                }

            }
        }
        
    }

    @Override
    public double train() {
        double accuracy = 0.0;
        for (int i = 0; i < number_folders; i++) {
            DataSet training = training_folders.get(i);
            DataSet testing = testing_folders.get(i);
            elm.setX(training.getX());
            elm.setY(training.getY());
            elm.train();
            elm.setX(testing.getX());
            elm.setY(testing.getY());
            elm.test();
            accuracy += elm.getAccuracy();
        }

        return (double) (accuracy / (double) number_folders);
    }
    
    @Override
    public double test(DoubleSolution solution) {
        super.getInputWeightsBiasFrom(solution);
        elm.setInputWeight(input_weights);
        elm.setBiasHiddenNeurons(bias);
        elm.setX(training_data_set.getX());
        elm.setY(training_data_set.getY());
        elm.train();
        elm.setX(testing_data_set.getX());
        elm.setY(testing_data_set.getY());
        elm.test();        
        return elm.getAccuracy();
    }
}
