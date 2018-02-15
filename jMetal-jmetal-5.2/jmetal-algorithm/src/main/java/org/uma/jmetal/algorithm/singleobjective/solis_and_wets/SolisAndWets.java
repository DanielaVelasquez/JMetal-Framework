package org.uma.jmetal.algorithm.singleobjective.solis_and_wets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.impl.RandomDistribution;

public class SolisAndWets implements Algorithm<DoubleSolution>
{
    /**
     * ------------------------------------------------------------------------
     * Based on
     * https://www.math.ucdavis.edu/~rjbw/mypage/Miscellaneous_files/randSearch.pdf ****
     * F. Solis and R. Wets, “Minimization by Random Search Techniques,”
     * Mathematics of Operations Research., vol. 6, pp. 19-30, 1981.
     * -------------------------------------------------------------------------
     */
    
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
     * Number of evaluations
     */
    private int maxEvaluations;
    /**
     * List of individual generated
     */
    private List<DoubleSolution> offspring_population;
    /**
     * Number of actual evaluations
     */
    private int evaluations;
    /**
     * Default value for objective in solution when evaluations >= maxEvaluations
     */
    private double penalize_value;
    
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    public SolisAndWets(DoubleProblem problem, Comparator<DoubleSolution> comparator, int maxEvaluations, double rho, int sizeNeighborhood, DoubleSolution initialSolution, double penalize_value)
    {
        this.problem = problem;
        this.comparator = comparator;
        this.maxEvaluations = maxEvaluations;
        this.rho = rho;
        this.sizeNeighborhood = sizeNeighborhood;
        neighborhood = new ArrayList<>();
        deviationNeighborhood = new ArrayList<>();
        this.best = initialSolution;
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
        neighborhood.clear();
        deviationNeighborhood.clear();
        for(int i = 0; i < sizeNeighborhood; i = i + 2)
        {
            generateTwoNeighbors();
        }
    }
    
    /**
     * Creates two neighbors based in best individual
     */
    private void generateTwoNeighbors()
    {
        double[] newDeviation = calculateNewDeviation();
        int sizeSolution = best.getNumberOfVariables();
        DoubleSolution individual1 = getProblem().createSolution();
        DoubleSolution individual2 = getProblem().createSolution();
        
        for(int i = 0; i < sizeSolution; i++)
        {   
            //TODO verificar si el nuevo valor cumple con los parametros
            individual1.setVariableValue(i, (best.getVariableValue(i) + bias[i] + newDeviation[i]));
            individual2.setVariableValue(i, (best.getVariableValue(i) - bias[i] - newDeviation[i]));
        }
        
        this.evaluate(individual1);
        this.evaluate(individual2);        
        neighborhood.add(individual1);
        neighborhood.add(individual2);
        deviationNeighborhood.add(newDeviation);
        deviationNeighborhood.add(newDeviation);
    }
    
    /**
    * Evaluates an individual
    * @param solution 
    */
    protected void evaluate(DoubleSolution solution)
    {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(solution);
            this.offspring_population.add((DoubleSolution) solution.copy());
            this.updateProgress();
        }
        else
        {
            this.penalize(solution);
        }
    }
    
    /**
    * Increments the number of generations evaluated
    */
    protected void updateProgress() {
        evaluations += 1;
    }
    
    /**
     * Penalize a solution with the worst value
     * @param s the solution to penalize
     */
    private void penalize(DoubleSolution s)
    {
        s.setObjective(0, this.penalize_value);
    }
    
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached()
    {
        return evaluations >= maxEvaluations;
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
        DoubleSolution bestPopulation = neighborhood.get(0);
        
        for(int i = 1; i < neighborhood.size(); i++)
        {
            DoubleSolution ind = neighborhood.get(i);
            int comparison = comparator.compare(bestPopulation, ind);
            
            if(comparison < 0)
            {
                bestIndex = i;
                bestPopulation = ind;
            }
            else if (comparison == 0)
            {
                try
                {
                    double B = (double) bestPopulation.getAttribute("B");
                    double BI = (double) ind.getAttribute("B");
                    
                    if(BI < B)
                    {
                        bestIndex = i;
                        bestPopulation = ind;
                    }
                }
                catch(Exception ex)
                {
                    
                }
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
    
    /**
     * Fills an array with an specific value
     * @param array array to fill
     * @param value value to set
     */
    protected void fillWith(double[] array, double value)
    {
        int rows = array.length;
        for(int i = 0; i < rows;i++)
            array[i] = value;
    }
    
    @Override
    public void run() 
    {
        this.offspring_population = new ArrayList<>();
        bias = new double[problem.getNumberOfVariables()];
        fillWith(bias, 0);
        
        if(best == null)
        {
            best = createInitialIndividual();
            this.evaluate(best);
        }
        
        hit = 0;
        fail = 0;
        
        while(!isStoppingConditionReached())
        {
            generateNeighborhood();
            findBestIndividual();
            DoubleSolution ind = neighborhood.get(bestIndex);
            
            if(improveBest(ind))
            {
                if(bestIndex % 2 == 0)
                {   
                    updateBias(1); 
                }
                else
                {
                    updateBias(2);
                }

                best = ind;
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
                fail = 0;
                
                if(rho < 1)
                {
                    rho = 1;
                }
            }
        }
    }
    
    private boolean improveBest(DoubleSolution ind)
    {
        int comparison = comparator.compare(best, ind);
        
        if(comparison <= 0)
        {
            return false;
        }
        return true;        
    }
    
    public List getPopulation()
    {
        return this.offspring_population;
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

    public void setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
    }
}