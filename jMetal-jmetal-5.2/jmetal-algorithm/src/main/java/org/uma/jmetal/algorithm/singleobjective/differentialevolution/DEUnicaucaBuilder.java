package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.Comparator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.FrobeniusComparator;


public class DEUnicaucaBuilder implements AlgorithmBuilder<DEUnicauca>
{
  private DoubleProblem problem;
  private int populationSize;
  private int maxEvaluations;
  private DifferentialEvolutionCrossover crossoverOperator;
  private DifferentialEvolutionSelection selectionOperator;
  private double penalize_value;
  private Comparator<DoubleSolution> comparator;

  public DEUnicaucaBuilder(DoubleProblem problem) {
    this.problem = problem;
    this.populationSize = 100;
    this.maxEvaluations = 3000;
    this.crossoverOperator = new DifferentialEvolutionCrossover(0.7, 0.5, "rand/1/bin");
    this.selectionOperator = new DifferentialEvolutionSelection();
    this.comparator = new FrobeniusComparator(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
    this.penalize_value = 0;
  }
  
  @Override
  public DEUnicauca build() {
    return new DEUnicauca(problem, maxEvaluations, populationSize, crossoverOperator,
        selectionOperator, penalize_value, comparator);
  }

  public DEUnicaucaBuilder setPopulationSize(int populationSize) {
    if (populationSize < 0) {
      throw new JMetalException("Population size is negative: " + populationSize);
    }

    this.populationSize = populationSize;

    return this;
  }

  public DEUnicaucaBuilder setMaxEvaluations(int maxEvaluations) {
    if (maxEvaluations <= 0) {
      throw new JMetalException("MaxEvaluations is negative or zero: " + maxEvaluations);
    }

    this.maxEvaluations = maxEvaluations;

    return this;
  }

  public DEUnicaucaBuilder setCrossover(DifferentialEvolutionCrossover crossover) {
    if(crossover == null)
      throw new JMetalException("crossover can't be null");
    this.crossoverOperator = crossover;

    return this;
  }
  public DEUnicaucaBuilder setComparator(Comparator<DoubleSolution>  comparator) {
    if(comparator == null)
      throw new JMetalException("comparator can't be null");
    this.comparator = comparator;

    return this;
  }
  public DEUnicaucaBuilder setSelection(DifferentialEvolutionSelection selection) {
    if(selection == null)
      throw new JMetalException("selection can't be null");
    this.selectionOperator = selection;

    return this;
  }


  public DEUnicaucaBuilder setPenalizeValue(double penalize_value) {
    this.penalize_value = penalize_value;

    return this;
  }
  public DEUnicaucaBuilder setProblem(DoubleProblem problem) {
    if(problem == null)
        throw new JMetalException("problem can't be null");
    this.problem = problem;
    return this;
  }

  public DoubleProblem getProblem() {
    return problem;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public int getMaxEvaluations() {
    return maxEvaluations;
  }

  public DifferentialEvolutionCrossover getCrossoverOperator() {
    return crossoverOperator;
  }

  public DifferentialEvolutionSelection getSelectionOperator() {
    return selectionOperator;
  }

  public double getPenalizeValue() {
    return penalize_value;
  }
}

