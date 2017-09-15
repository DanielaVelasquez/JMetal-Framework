package org.uma.jmetal.algorithm.singleobjective.mts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public abstract class AbstractMultipleTrajectorySearch <S extends Solution<?>,P extends Problem<S>>  implements Algorithm<S>
{
    /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    private int LOWER_BOUND_ROW = 0;
    
    private int SOA_DEFAULT_VALUE = -1;
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Number of solutions/Number of levels of each factor
     */
    private int populationSize;
    /**
     * Number of factors
     */
    private int n;
    /**
     * Random generator
     */
    private JMetalRandom randomGenerator ;
    /**
     * Individiuals on population
     */
    private List<S> population;
     /**
     * Problem to solve
     */
    private P problem ;
    /**
     * Determines how a solution should be order
     */
    private Comparator<S> comparator;
    /**
     * Maximun number of function evaluations
     */
    private int FE;
    /**
     * Number of evaluations made
     */
    private int evaluations;
    /**
     * Number of local searh test
     */
    private int local_search_test;
    /**
     * Number of local search
     */
    private int local_search;
    /**
     * Number of local search for the best
     */
    private int local_search_best;
    /**
     * Number of foreground to decide how many solutions are set to true
     */
    private int number_of_foreground;
    /**
     * Determines which individuals in population are enable
     */
    private List<Boolean> enable;
    /**
     * Determines which individuals in population were improve
     */
    private List<Boolean> improve;
    /**
     * Determines the search range for every individual
     */
    private List<Double> search_range;
    /**
     * Bonus 1 value
     */
    private double bonus_1;
    /**
     * Bonus 2 value
     */
    private double bonus_2;
    /**
     * Lower bound for variable a in local search 3
     */
    private double lower_bound_a;
    /**
     * Upper bound for variable a in local search 3
     */
    private double upper_bound_a;
    /**
     * Lower bound for variable b in local search 3
     */
    private double lower_bound_b;
    /**
     * Upper bound for variable b in local search 3
     */
    private double upper_bound_b;
    /**
     * Lower bound for variable c in local search 3
     */
    private double lower_bound_c;
    /**
     * Upper bound for variable c in local search 3
     */
    private double upper_bound_c;
    /**
     * Best individual on population
     */
    private S best;
    
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    
    public AbstractMultipleTrajectorySearch(int populationSize, int n, P problem, Comparator<S> comparator, 
            int cycles, int local_search_test, int local_search, int local_search_best, int number_of_foreground, 
            double bonus_1, double bonus_2, double lower_bound_a, double upper_bound_a, double lower_bound_b, 
            double upper_bound_b, double lower_bound_c, double upper_bound_c) {
        this.populationSize = populationSize;
        this.n = n;
        this.problem = problem;
        this.comparator = comparator;
        this.FE = cycles;
        this.local_search_test = local_search_test;
        this.local_search = local_search;
        this.local_search_best = local_search_best;
        this.number_of_foreground = number_of_foreground;
        this.bonus_1 = bonus_1;
        this.bonus_2 = bonus_2;
        this.lower_bound_a = lower_bound_a;
        this.upper_bound_a = upper_bound_a;
        this.lower_bound_b = lower_bound_b;
        this.upper_bound_b = upper_bound_b;
        this.lower_bound_c = lower_bound_c;
        this.upper_bound_c = upper_bound_c;
        
        this.n = this.problem.getNumberOfVariables();
        this.randomGenerator = JMetalRandom.getInstance();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Builds a simulated orthogonal array SOAmxn
     * @param m number of levels of each factor
     * @param n number of factors
     * @return a simulated orthogonal array
     */
    protected int[][] buildSOA(int m, int n) {
        int[][] SOA = new int[m][n];
        this.fillWith(SOA, SOA_DEFAULT_VALUE);
        
        List<Integer> C  = this.buildSequence(m);
        for(int i = 0; i < n; i++)
        {
            int aux = 0;
            for(int j = 0; j < m;j++)
            {
                int index = this.getIndexWhere(SOA_DEFAULT_VALUE, SOA, i);
                SOA[index][i] = C.get(aux);
                aux++;
                if(aux > m-1)
                    aux = 0;
            }
        }
        return SOA;
    }
    /**
     * Fills an array with an specific value
     * @param array array to fill
     * @param value value to set
     */
    protected void fillWith(int[][] array, int value)
    {
        int rows = array.length;
        int cols = array[0].length;
        for(int i = 0; i < rows;i++)
            for(int j = 0; j< cols;j++)
                array[i][j] = value;
    }
    /**
     * Builds a random permutation of 0,1,...m-1 
     * @param m max value of the sequence
     * @return a random sequence
     */
    protected List<Integer> buildSequence(int m)
    {
        List<Integer> C  = new ArrayList<>();
        while(C.size() != m)
        {
            int value = -1;
            do
            {
                value = randomGenerator.nextInt(LOWER_BOUND_ROW, m - 1);
            }while(C.contains(value));
            C.add(value);
        }
        return C;
    }
    /**
     * Finds row index where a column  in a array has an specfific value
     * @param value value to find
     * @param array collection where to look
     * @param col column where the row index will be selected
     * @return a row index where the array has the value
     */
    protected int getIndexWhere(int value, int[][] array, int col)
    {
        int row = -1;
        int rows = array.length - 1;
        do
        {
            row = randomGenerator.nextInt(LOWER_BOUND_ROW, rows);
        }while(array[row][col] != value);
        return row;
    }
    /**
     * Evaluates a population as maximun FE has not been reached
     * @param population 
     */
    protected void evaluatePopulation(List<S> population) {
        int i = 0;
        while(!isStoppingConditionReached() && i < populationSize)
        {
            S solution = this.population.get(i);
            this.problem.evaluate(solution);
            i++;
            this.updateProgress();
        }
        //TO-DO ¿Que hago con el resto de la población que no alcance a evaluar?
    }
    /**
     * Evaluates an individual
     * @param solution 
     */
    protected void evaluate(S solution)
    {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(solution);
            this.updateProgress();
        }
        //TO-DO ¿Que hago si no lo puede evaluar?
    }
    /**
     * Increments the number of generations evaluated
     */
    protected void updateProgress() {
        evaluations += 1;
    }
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached() {
        return evaluations > FE;
    }
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    protected S getBest(S s1, S s2)
    {
        int comparison = comparator.compare(s1, s2);
        if(comparison > 0 || comparison == 0)
            return s1;
        return s2;
    }
    /**
     * Determines if a solution improve the best solution in population
     * @param s solution to know if improve the best
     * @return true if s improve the best solution, false otherwise
     */
    protected boolean improveBest(S s)
    {
        return this.getBest(best, s) == s;
    }
    /**
     * Determines if a modified individual has a decresead evaluation compared to its orifinal
     * @param original original individual
     * @param modified modified individual
     * @return true if the modified individual has decresead its evaluation, false otherwise
     */
    protected boolean functionValueDegenerates(S original, S modified)
    {
        return this.getBest(original, modified ) == original;
    }
    @Override
    public S getResult() {
        return best;
    }
    /**
     * Creates an initial random population
     * @return initial population with random values
     */
    protected abstract List<S> createInitialPopulation();
    /**
     * Generates an initial solution
     * @param SOA simulated orthogonal array to build the population
     * @return population build from SOA
     */
    protected abstract List<S> generateInitialSolutions(int[][]SOA);
    /**
     * Local search 1 
     * @param population population 
     * @param enable determines which individuals in population are enable
     * @param improve determines which individuals in population were improved
     * @param search_range
     * @param k
     * @return 
     */
    protected abstract double local_search_1(List<S> population, List<Boolean> enable, List<Boolean> improve, List<Double> search_range, int k);
    @Override
    public abstract String getName();

    @Override
    public abstract String getDescription();
    
    
    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public P getProblem() {
        return problem;
    }

    public void setProblem(P problem) {
        this.problem = problem;
    }

    public Comparator<S> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<S> comparator) {
        this.comparator = comparator;
    }

    public int getCycles() {
        return FE;
    }

    public void setCycles(int cycles) {
        this.FE = cycles;
    }

    public int getLocal_search_test() {
        return local_search_test;
    }

    public void setLocal_search_test(int local_search_test) {
        this.local_search_test = local_search_test;
    }

    public int getLocal_search() {
        return local_search;
    }

    public void setLocal_search(int local_search) {
        this.local_search = local_search;
    }

    public int getLocal_search_best() {
        return local_search_best;
    }

    public void setLocal_search_best(int local_search_best) {
        this.local_search_best = local_search_best;
    }

    public int getNumber_of_foreground() {
        return number_of_foreground;
    }

    public void setNumber_of_foreground(int number_of_foreground) {
        this.number_of_foreground = number_of_foreground;
    }

    public double getBonus_1() {
        return bonus_1;
    }

    public void setBonus_1(double bonus_1) {
        this.bonus_1 = bonus_1;
    }

    public double getBonus_2() {
        return bonus_2;
    }

    public void setBonus_2(double bonus_2) {
        this.bonus_2 = bonus_2;
    }

    public double getLower_bound_a() {
        return lower_bound_a;
    }

    public void setLower_bound_a(double lower_bound_a) {
        this.lower_bound_a = lower_bound_a;
    }

    public double getUpper_bound_a() {
        return upper_bound_a;
    }

    public void setUpper_bound_a(double upper_bound_a) {
        this.upper_bound_a = upper_bound_a;
    }

    public double getLower_bound_b() {
        return lower_bound_b;
    }

    public void setLower_bound_b(double lower_bound_b) {
        this.lower_bound_b = lower_bound_b;
    }

    public double getUpper_bound_b() {
        return upper_bound_b;
    }

    public void setUpper_bound_b(double upper_bound_b) {
        this.upper_bound_b = upper_bound_b;
    }

    public double getLower_bound_c() {
        return lower_bound_c;
    }

    public void setLower_bound_c(double lower_bound_c) {
        this.lower_bound_c = lower_bound_c;
    }

    public double getUpper_bound_c() {
        return upper_bound_c;
    }

    public void setUpper_bound_c(double upper_bound_c) {
        this.upper_bound_c = upper_bound_c;
    }
    
    
}
