package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;


public class DEFrobeniusBuilder 
{
  private DoubleProblem problem;
  private int populationSize;
  private int maxEvaluations;
  private DifferentialEvolutionCrossover crossoverOperator;
  private DifferentialEvolutionSelection selectionOperator;
  private SolutionListEvaluator<DoubleSolution> evaluator;

  public DEFrobeniusBuilder(DoubleProblem problem) {
    this.problem = problem;
    this.populationSize = 100;
    this.maxEvaluations = 25000;
    this.crossoverOperator = new DifferentialEvolutionCrossover(0.5, 0.5, "rand/1/bin");
    this.selectionOperator = new DifferentialEvolutionSelection();
    this.evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
  }
  
  public DEFrobenius build() {
    return new DEFrobenius(problem, maxEvaluations, populationSize, crossoverOperator,
        selectionOperator, evaluator);
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
    this.crossoverOperator = crossover;

    return this;
  }

  public DEFrobeniusBuilder setSelection(DifferentialEvolutionSelection selection) {
    this.selectionOperator = selection;

    return this;
  }

  public DEFrobeniusBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
    this.evaluator = evaluator;

    return this;
  }
  public DEFrobeniusBuilder setProblem(DoubleProblem problem) {
    if(problem == null)
        throw new JMetalException("problem can't be null");
    this.problem = problem;
    return this;
  }

  /* Getters */
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

  public SolutionListEvaluator<DoubleSolution> getSolutionListEvaluator() {
    return evaluator;
  }
}

