package org.uma.jmetal.algorithm.singleobjective.mts;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;


public class MTS_LS1Builder implements AlgorithmBuilder<MTS_LS1>
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
     * Bonus 1 value
     */
    private double bonus_1;
    /**
     * Bonus 2 value
     */
    private double bonus_2;
   
    /**
     * Default population to evolve on the algorithm
     */
    private List<DoubleSolution> default_population;
    
    private double penalize_value;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public MTS_LS1Builder(DoubleProblem p)
    {
        this.problem = p;
        this.populationSize = 5;
        this.bonus_2 = 1;
        this.bonus_1 = 10;
        this.maxEvaluations = 1000;
        this.penalize_value = 1;
        comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
        
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

    

    public double getBonus1() {
        return bonus_1;
    }

    public double getBonus2() {
        return bonus_2;
    }


    public MTS_LS1Builder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public MTS_LS1Builder setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
        return this;
    }

    public MTS_LS1Builder setMaxEvaluations(int FE) {
        if(FE <= 0)
            throw new JMetalException("Function evaluations is negative or cero: " + FE);
        this.maxEvaluations = FE;
        return this;
    }
    public MTS_LS1Builder setPopulationSize(int populationSize) {
        if(populationSize <= 0)
            throw new JMetalException("Max generations is negative or cero: " + populationSize);
        this.populationSize = populationSize;
        return this;
    }
    public MTS_LS1Builder setBonus1(double bonus_1) {
        if(bonus_1 < 0)
            throw new JMetalException("Bonus 1 is negative: " + bonus_1);
        
        this.bonus_1 = bonus_1;
        return this;
    }

    public MTS_LS1Builder setBonus2(double bonus_2) {
        if(bonus_2 < 0)
            throw new JMetalException("Bonus 2 is negative: " + bonus_2);
        
        this.bonus_2 = bonus_2;
        return this;
    }


    public MTS_LS1Builder setDefaultPopulation(List<DoubleSolution> defalult_population) {
        this.default_population = defalult_population;
        return this;
    }
    
    
    public MTS_LS1Builder setPenalizeValue(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }

    @Override
    public MTS_LS1 build() {
        MTS_LS1 mts = new MTS_LS1(populationSize, problem, comparator, maxEvaluations, penalize_value, bonus_1, bonus_2);
        if(this.default_population != null)
            mts.setPopulation(default_population);
        return mts;
    }

}
