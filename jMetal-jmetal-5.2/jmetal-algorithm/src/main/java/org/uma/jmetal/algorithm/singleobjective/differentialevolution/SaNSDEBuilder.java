package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.Comparator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;


public class SaNSDEBuilder implements AlgorithmBuilder<SaNSDE>
{
  private DoubleProblem problem;
  private int populationSize;
  private int maxEvaluations;
  private DifferentialEvolutionCrossover crossoverOperator;
  private DifferentialEvolutionCrossover crossoverOperator2;
  private DifferentialEvolutionSelection selectionOperator;
  private SolutionListEvaluator<DoubleSolution> evaluator;
  private Comparator<DoubleSolution> comparator ;

  public SaNSDEBuilder(DoubleProblem problem) {
    this.problem = problem;
    this.populationSize = 10;
    this.maxEvaluations = 3000;
    this.crossoverOperator =  new DifferentialEvolutionCrossover(0.4, 0.6, "rand/1/bin");
    this.crossoverOperator2 = new DifferentialEvolutionCrossover(0.5, 0.4, "current-to-best/1/bin");
    this.comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
    this.selectionOperator = new DifferentialEvolutionSelection();
    this.evaluator = new SequentialSolutionListEvaluator<>();
  }
  
  @Override
  public SaNSDE build() {
    return new SaNSDE(problem, maxEvaluations, populationSize, crossoverOperator, crossoverOperator2, selectionOperator, evaluator, comparator);
  }

  public SaNSDEBuilder setPopulationSize(int populationSize) {
    if (populationSize < 0) {
      throw new JMetalException("Population size is negative: " + populationSize);
    }

    this.populationSize = populationSize;

    return this;
  }

  public SaNSDEBuilder setMaxEvaluations(int maxEvaluations) {
    if (maxEvaluations <= 0) {
      throw new JMetalException("MaxEvaluations is negative or zero: " + maxEvaluations);
    }

    this.maxEvaluations = maxEvaluations;

    return this;
  }

  public SaNSDEBuilder setCrossover(DifferentialEvolutionCrossover crossover) {
    if(crossover == null)
            throw new JMetalException("crossover can't be null");
    this.crossoverOperator = crossover;

    return this;
  }

  public SaNSDEBuilder setSelection(DifferentialEvolutionSelection selection) {
    if(selection == null)
            throw new JMetalException("selection can't be null");
    this.selectionOperator = selection;

    return this;
  }

  public SaNSDEBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
    if(evaluator == null)
          throw new JMetalException("evaluator can't be null");
    this.evaluator = evaluator;

    return this;
  }
  
  public SaNSDEBuilder setEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
    if(evaluator == null)
        throw new JMetalException("evaluator can't be null");
    this.evaluator = evaluator;
    return this;
  }
  
  public SaNSDEBuilder setComparator(Comparator<DoubleSolution> comparator) {
    if(comparator == null)
        throw new JMetalException("comparator can't be null");
    this.comparator = comparator;
    return this;
  }
  
  public SaNSDEBuilder setCrossoverOperator2(DifferentialEvolutionCrossover crossoverOperator2) {
    if(crossoverOperator2 == null)
        throw new JMetalException("crossoverOperator2 can't be null");
    this.crossoverOperator2 = crossoverOperator2;
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
  public SolutionListEvaluator<DoubleSolution> getEvaluator() {
    return evaluator;
  }
  public DifferentialEvolutionCrossover getCrossoverOperator2() {
    return crossoverOperator2;
  }
  public SolutionListEvaluator<DoubleSolution> getSolutionListEvaluator() {
    return evaluator;
  }
  public Comparator<DoubleSolution> getComparator() {
    return comparator;
  }
}

