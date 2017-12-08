package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.Comparator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;


public class SaDEBuilder implements AlgorithmBuilder<SaDE>
{
    private DoubleProblem problem;
    private int maxEvaluations;
    private int populationSize;
    private DifferentialEvolutionCrossover crossoverOperator;
    private DifferentialEvolutionCrossover crossoverOperator2;
    private DifferentialEvolutionSelection selectionOperator;
    private Comparator<DoubleSolution> comparator;
    private double penalize_value;
    

    public SaDEBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.maxEvaluations = 3000;
        this.populationSize = 10;
        this.crossoverOperator =  new DifferentialEvolutionCrossover(0.6, 0.5, "rand/1/bin");
        this.crossoverOperator2 = new DifferentialEvolutionCrossover(0.5, 0.7, "current-to-best/1/bin");
        this.comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
        this.selectionOperator = new DifferentialEvolutionSelection();
        this.penalize_value = 1;
    }
    
    @Override
    public SaDE build() {
        return new SaDE(problem, maxEvaluations, populationSize, crossoverOperator, crossoverOperator2, selectionOperator, comparator, penalize_value);
    }

    public SaDEBuilder setProblem(DoubleProblem problem) {
        if(problem == null)
            throw new JMetalException("problem can't be null");
        this.problem = problem;
        return this;
    }

    public SaDEBuilder setMaxEvaluations(int maxEvaluations) {
        if(maxEvaluations <= 0)
            throw new JMetalException("maxEvaluations is negative  or zero");
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public SaDEBuilder setPopulationSize(int populationSize) {
        if(populationSize <=0)
            throw new JMetalException("populationSize is negative or zero");
        this.populationSize = populationSize;
        return this;
    }

    public SaDEBuilder setCrossoverOperator(DifferentialEvolutionCrossover crossoverOperator) {
        if(crossoverOperator==null)
            throw new JMetalException("crossOperator can't be null");
        this.crossoverOperator = crossoverOperator;
        return this;
    }

    public SaDEBuilder setCrossoverOperator2(DifferentialEvolutionCrossover crossoverOperator2) {
        if(crossoverOperator2 == null)
            throw new JMetalException("crossOperator2 can't be null");
        this.crossoverOperator2 = crossoverOperator2;
        return this;
    }

    public SaDEBuilder setSelectionOperator(DifferentialEvolutionSelection selectionOperator) {
        if(selectionOperator == null)
            throw new JMetalException("selectionOperator can't be null");
        this.selectionOperator = selectionOperator;
        return this;
    }

    public SaDEBuilder setComparator(Comparator<DoubleSolution> comparator) {
        if(comparator == null)
            throw new JMetalException("comparator can't be null");
        this.comparator = comparator;
        return this;
    }

    public SaDEBuilder setPenalizeValue(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public DifferentialEvolutionCrossover getCrossoverOperator() {
        return crossoverOperator;
    }

    public DifferentialEvolutionCrossover getCrossoverOperator2() {
        return crossoverOperator2;
    }

    public DifferentialEvolutionSelection getSelectionOperator() {
        return selectionOperator;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public double getPenalize_value() {
        return penalize_value;
    }
}
