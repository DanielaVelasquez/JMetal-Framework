package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.impl.RandomDistribution;

public class SolisAndWets implements Algorithm
{
    /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Problem to solve
     */
    private DoubleProblem problem;
    /**
     * The current best
     */
    private int hit;
    /**
     * Number of times doesn't find a better solution in neighborhood
     */
    private int fail;
    /**
     * Best's index in arrays
     */
    private int bestIndex;
    /**
     * The vector that indicates the direction to find better solutions
     */
    private DoubleSolution best;
    /**
     * Number of times find a better solution in neighborhood
     */
    private double[] bias;
    /**
     * The neighboorhood of best
     */
    private ArrayList<DoubleSolution> neighborhood;    
    /**
     * The deviation for neighborhood
     */
    private ArrayList<double[]> deviationNeighborhood;
    /**
     * The size of individuals in neighborhood
     */
    private int sizeNeighborhood;
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    /**
     * Standar deviation for all deviation arrays
     */
    private double rho;
    /**
     * Number of execution cycles
     */
    private int numCycles;
    
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    public SolisAndWets(DoubleProblem problem, Comparator<DoubleSolution> comparator, int numCycles, double rho, int sizeNeighborhood)
    {
        this.problem = problem;
        this.comparator = comparator;
        this.numCycles = numCycles;
        this.rho = rho;
        this.sizeNeighborhood = sizeNeighborhood;
        neighborhood = new ArrayList<>();
        deviationNeighborhood = new ArrayList<>();        
    }
    
    /**
     * Creates an initial random individual
     * @return initial individual with random values
     */
    private DoubleSolution createInitialIndividual() 
    {
        return getProblem().createSolution();
    }
    
    /**
     * Create a neighborhood based in the best individual
     */
    private void generateNeighborhood()
    {
        for(int i = 0; i < sizeNeighborhood; i = i + 2)
        {
            generateTwoNeighbors(i);
        }
    }
    
    /**
     * Creates two neighbors based in best individual
     * @param index where the individual will be saved
     */
    private void generateTwoNeighbors(int index)
    {
        double[] newDeviation = calculateNewDeviation();
        int sizeSolution = best.getNumberOfVariables();
        DoubleSolution individual1 = getProblem().createSolution();
        DoubleSolution individual2 = getProblem().createSolution();
        
        for(int i = 0; i < sizeSolution; i++)
        {
            individual1.setVariableValue(i, (best.getVariableValue(i) + bias[i] + newDeviation[i]));
            individual2.setVariableValue(i, (best.getVariableValue(i) - bias[i] - newDeviation[i]));
        }
        
        neighborhood.set(index, individual1);
        neighborhood.set(index + 1, individual2);
        deviationNeighborhood.set(index, newDeviation);
        deviationNeighborhood.set(index + 1, newDeviation);
    }
    
    /**
     * Calculate a new deviation array
     * @param index the individual to calculate a new deviation
     */
    private double[] calculateNewDeviation()
    {
        int sizeDeviation = best.getNumberOfVariables();
        double[] deviationAux = new double[sizeDeviation];
        RandomDistribution distributionRnd = RandomDistribution.getInstance();
        
        for(int i = 0; i < sizeDeviation; i++)
        {
            double value = distributionRnd.nextGaussian(0, rho);
            deviationAux[i] = value;
        }
        
        return deviationAux;
    }
    
    /**
     * Save in bestIndex the position of the best individual in neighborhood
     */
    private void findBestIndividual()
    {
        bestIndex = 0;
        
        for(int i = 0; i < neighborhood.size(); i++)
        {
            if(comparator.compare(neighborhood.get(bestIndex), neighborhood.get(i)) < 1)
            {
                bestIndex = i;
            }
        }
    }
    
    /**
     * Update the bias for next iteration
     * @param ecuation the ecuation wich will be used for update the bias
     */
    private void updateBias(int ecuation)
    {
        int sizeBias = bias.length;
        double[] deviationFromBestNeighborhood = deviationNeighborhood.get(bestIndex);        
        
        if(ecuation == 1)
        {
            for(int i = 0; i < sizeBias; i++)
            {
                double biasValue = (0.2 * bias[i]) + (0.4 * (deviationFromBestNeighborhood[i] + bias[i]));
                bias[i] = biasValue;
            }
        }
        else if(ecuation == 2)
        {
            for(int i = 0; i < sizeBias; i++)
            {
                double biasValue = bias[i] - (0.4 * (deviationFromBestNeighborhood[i] + bias[i]));
                bias[i] = biasValue;
            }
        }
    }    
    
    @Override
    public void run() 
    {
        best = createInitialIndividual();
        hit = 0;
        fail = 0;
        
        for(int i = 0; i < numCycles; i++)
        {
            generateNeighborhood();
            findBestIndividual();

            if(comparator.compare(neighborhood.get(bestIndex), best) < 1)
            {
                if(bestIndex % 2 == 0)
                {   
                    updateBias(1); 
                }
                else
                {
                    updateBias(2);
                }

                best = neighborhood.get(bestIndex);
                hit++;
                fail = 0;
            }
            else
            {
                fail++;
                hit = 0;
            }

            if(hit > 2)
            {
                rho = 2 * rho;
                hit = 0;
            }

            if(fail > 1)
            {
                rho = rho / 2;
                if(rho < 1)
                {
                    rho = 1;
                }
                fail = 0;
            }
        }
    }

    @Override
    public DoubleSolution getResult() {
        return best;
    }

    @Override
    public String getName() {
        return "Solis and Wets";
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DoubleProblem getProblem() {
        return problem;
    }
}