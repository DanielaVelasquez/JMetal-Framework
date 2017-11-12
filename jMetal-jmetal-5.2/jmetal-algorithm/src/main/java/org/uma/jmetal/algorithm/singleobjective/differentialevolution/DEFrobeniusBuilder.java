package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;


public class DEFrobeniusBuilder implements AlgorithmBuilder<DEFrobenius>
{
  private DoubleProblem problem;
  private int populationSize;
  private int maxEvaluations;
  private DifferentialEvolutionCrossover crossoverOperator;
  private DifferentialEvolutionSelection selectionOperator;
  private double penalize_value;

  public DEFrobeniusBuilder(DoubleProblem problem) {
    this.problem = problem;
    this.populationSize = 100;
    this.maxEvaluations = 3000;
    this.crossoverOperator = new DifferentialEvolutionCrossover(0.5, 0.6, "rand/1/bin");
    this.selectionOperator = new DifferentialEvolutionSelection();
    this.penalize_value = 0;
  }
  
  public DEFrobenius build() {
    return new DEFrobenius(problem, maxEvaluations, populationSize, crossoverOperator,
        selectionOperator, penalize_value);
  }

  public DEFrobeniusBuilder setPopulationSize(int populationSize) {
    if (populationSize < 0) {
      throw new JMetalException("Population size is negative: " + populationSize);
    }

    this.populationSize = populationSize;

    return this;
  }

  public DEFrobeniusBuilder setMaxEvaluations(int maxEvaluations) {
    if (maxEvaluations <= 0) {
      throw new JMetalException("MaxEvaluations is negative or zero: " + maxEvaluations);
    }

    this.maxEvaluations = maxEvaluations;

    return this;
  }

  public DEFrobeniusBuilder setCrossover(DifferentialEvolutionCrossover crossover) {
    if(crossover == null)
      throw new JMetalException("crossover can't be null");
    this.crossoverOperator = crossover;

    return this;
  }

  public DEFrobeniusBuilder setSelection(DifferentialEvolutionSelection selection) {
    if(selection == null)
      throw new JMetalException("selection can't be null");
    this.selectionOperator = selection;

    return this;
  }

  public DEFrobeniusBuilder setPenalizeValue(double penalize_value) {
    this.penalize_value = penalize_value;

    return this;
  }
  public DEFrobeniusBuilder setProblem(DoubleProblem problem) {
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

