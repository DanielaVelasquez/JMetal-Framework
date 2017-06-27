package org.uma.jmetal.problem.singleobjective.ann.cv;

import co.edu.unicauca.dataset.DataSet;
import java.util.ArrayList;
import java.util.List;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Vector;
import org.uma.jmetal.problem.singleobjective.ann.AbstractELMEvaluator;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Elm's Evaluator using cross validation files
 */
public abstract class CrossValidationEvaluator extends AbstractELMEvaluator
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
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
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    /**
     * Creates a cross validation evaluator
     * @param type
     * @param name
     * @param training_data_set
     * @param testing_data_set
     * @param number_folders 
     */
    public CrossValidationEvaluator(EvaluatorType type, String name, DataSet training_data_set, DataSet testing_data_set, int number_folders) {
        super(type, name, training_data_set, testing_data_set);
        this.number_folders = number_folders;
        makeFolders();
    }
    
    private void makeFolders()
    {
        training_folders = new ArrayList<>();
        testing_folders = new ArrayList<>();
        int trainig_size = super.training_data_set.getX().numColumns();
        
        int number_variables = training_data_set.getX().numRows();
        int number_data = training_data_set.getX().numColumns();
        DenseMatrix x = training_data_set.getX();
        DenseVector y = training_data_set.getY();
        int aditionals = number_data % number_folders;
        
        for (int i = 0; i < number_folders; i++)
        {
            if(i < aditionals)
            {
                training_folders.add(new DataSet(number_folders + 1, number_variables));
                testing_folders.add(new DataSet(number_folders + 1, number_variables));
            }
            else
            {
                training_folders.add(new DataSet(number_folders, number_variables));
                testing_folders.add(new DataSet(number_folders, number_variables));
            }
        }
        
        for (int i = 0; i < trainig_size; i++)
        {
            Vector data = Matrices.getColumn(x, i);
            double value = y.get(i);
            for (int j = 0; j < number_folders; j++)
            {
                if(i % j != i)
                {
                    DataSet training = training_folders.get(j);
                    training.addDataColumn(data);
                    training.addValueColumn(value);
                    training.nextIndex();
                }
                else
                {
                    DataSet testing = testing_folders.get(j);
                    testing.addDataColumn(data);
                    testing.addValueColumn(value);
                    testing.nextIndex();
                }
                
            }
        }
    }
    @Override
    public double train() 
    {
        double accuracy = 0;
        for (int i = 0; i < number_folders; i++)
        {
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
    public double test(DoubleSolution solution)
    {
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
