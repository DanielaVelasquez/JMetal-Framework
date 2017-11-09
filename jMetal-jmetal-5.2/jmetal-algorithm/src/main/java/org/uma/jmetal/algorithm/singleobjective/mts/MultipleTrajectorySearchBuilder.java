package org.uma.jmetal.algorithm.singleobjective.mts;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;


public class MultipleTrajectorySearchBuilder implements AlgorithmBuilder<MultipleTrajectorySearch>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    private int populationSize;
    
    /**
     * Algorithm's problem
     */
    private DoubleProblem problem;
    /**
     * Algorithm's comparator
     */
    private Comparator<DoubleSolution> comparator ;
    /**
     * Maximun number of generations
     */
    private int maxEvaluations;
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
     * Default population to evolve on the algorithm
     */
    private List<DoubleSolution> default_population;
    
    private double penalize_value;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public MultipleTrajectorySearchBuilder(DoubleProblem p)
    {
        this.problem = p;
        this.populationSize = 5;
        this.local_search_test = 3;
        this.local_search_best = 100;
        this.bonus_2 = 1;
        this.lower_bound_b = 0.1;
        this.upper_bound_b = 0.3;
        this.number_of_foreground = 5;
        this.local_search = 75;
        this.bonus_1 = 10;
        this.lower_bound_a = 0.4;
        this.upper_bound_a = 0.5;
        this.lower_bound_c = 0;
        this.upper_bound_c = 1;
        this.maxEvaluations = 3000;
        this.penalize_value = 1;
        comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
        
    }
     public MultipleTrajectorySearch build()
     {
        MultipleTrajectorySearch mts = new MultipleTrajectorySearch(populationSize, problem, comparator, maxEvaluations, local_search_test, local_search, local_search_best, number_of_foreground, bonus_1, bonus_2, lower_bound_a, upper_bound_a, lower_bound_b, upper_bound_b, lower_bound_c, upper_bound_c,penalize_value);
        if(this.default_population != null)
            mts.setPopulation(default_population);
        return mts;
     }

    public double getPenalizeValue() {
        return penalize_value;
    }

     
     

    public DoubleProblem getProblem() {
        return problem;
    }


    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getLocalSearchTest() {
        return local_search_test;
    }

    public int getLocalSearch() {
        return local_search;
    }

    public int getLocalSearch_best() {
        return local_search_best;
    }

    public int getNumberOfForeground() {
        return number_of_foreground;
    }

    public double getBonus1() {
        return bonus_1;
    }

    public double getBonus2() {
        return bonus_2;
    }

    public double getLowerBoundA() {
        return lower_bound_a;
    }

    public double getUpperBoundA() {
        return upper_bound_a;
    }

    public double getLowerBoundB() {
        return lower_bound_b;
    }

    public double getUpperBoundB() {
        return upper_bound_b;
    }

    public double getLowerBoundC() {
        return lower_bound_c;
    }

    public double getUpperBoundC() {
        return upper_bound_c;
    }

    public MultipleTrajectorySearchBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public MultipleTrajectorySearchBuilder setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
        return this;
    }

    public MultipleTrajectorySearchBuilder setMaxEvaluations(int FE) {
        if(FE <= 0)
            throw new JMetalException("Function evaluations is negative or cero: " + FE);
        this.maxEvaluations = FE;
        return this;
    }
    public MultipleTrajectorySearchBuilder setPopulationSize(int populationSize) {
        if(populationSize <= 0)
            throw new JMetalException("Max generations is negative or cero: " + populationSize);
        this.populationSize = populationSize;
        return this;
    }

    public MultipleTrajectorySearchBuilder setLocalSearchTest(int local_search_test) {
        this.local_search_test = local_search_test;
        return this;
    }

    public MultipleTrajectorySearchBuilder setLocalSearch(int local_search) {
        this.local_search = local_search;
        return this;
    }

    public MultipleTrajectorySearchBuilder setLocalSearchBest(int local_search_best) {
        this.local_search_best = local_search_best;
        return this;
    }

    public MultipleTrajectorySearchBuilder setNumberOfForeground(int number_of_foreground) {
        if(number_of_foreground < 0)
            throw new JMetalException("Number of foreground is negative: " + number_of_foreground);
        
        this.number_of_foreground = number_of_foreground;
        return this;
    }

    public MultipleTrajectorySearchBuilder setBonus1(double bonus_1) {
        if(bonus_1 < 0)
            throw new JMetalException("Bonus 1 is negative: " + bonus_1);
        
        this.bonus_1 = bonus_1;
        return this;
    }

    public MultipleTrajectorySearchBuilder setBonus2(double bonus_2) {
        if(bonus_2 < 0)
            throw new JMetalException("Bonus 2 is negative: " + bonus_2);
        
        this.bonus_2 = bonus_2;
        return this;
    }

    public MultipleTrajectorySearchBuilder setBoundsA(double lower_bound_a, double upper_bound_a) {
        if(lower_bound_a > upper_bound_a)
            throw new JMetalException("Lower bound "+lower_bound_a+" is greater than upper bound " + upper_bound_a);
        if(lower_bound_a == upper_bound_a)
            throw new JMetalException("Lower bound "+lower_bound_a+" is equal to upper bound " + upper_bound_a);
        
        this.lower_bound_a = lower_bound_a;
        this.upper_bound_a = upper_bound_a;
        return this;
    }

    public MultipleTrajectorySearchBuilder setBoundsB(double lower_bound_b, double upper_bound_b) {
        if(lower_bound_b > upper_bound_b)
            throw new JMetalException("Lower bound "+lower_bound_b+" is greater than upper bound " + upper_bound_b);
        if(lower_bound_b == upper_bound_b)
            throw new JMetalException("Lower bound "+lower_bound_b+" is equal to upper bound " + upper_bound_b);
        
        this.lower_bound_b = lower_bound_b;
        this.upper_bound_b = upper_bound_b;
        return this;
    }

    public MultipleTrajectorySearchBuilder setBoundsC(double lower_bound_c, double upper_bound_c) {
        if(lower_bound_c > upper_bound_c)
            throw new JMetalException("Lower bound "+lower_bound_c+" is greater than upper bound " + upper_bound_c);
        if(lower_bound_c == upper_bound_c)
            throw new JMetalException("Lower bound "+lower_bound_c+" is equal to upper bound " + upper_bound_c);
        
        this.lower_bound_c = lower_bound_c;
        this.upper_bound_c = upper_bound_c;
        return this;
    }

    public MultipleTrajectorySearchBuilder setDefaultPopulation(List<DoubleSolution> defalult_population) {
        this.default_population = defalult_population;
        return this;
    }
    
    
    public MultipleTrajectorySearchBuilder setPenalizeValue(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }

}
