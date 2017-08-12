package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.Comparator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;


public class DECC_GBuilder 
{
  private DoubleProblem problem;
  private SolutionListEvaluator<DoubleSolution> evaluator;
  private Comparator<DoubleSolution> comparator ;
  private int cycles;
  private int subcomponets;
  private int FEs;
  private int wFes;
  private int population_size;
  private int numCyclesSaNSDE;

  public DECC_GBuilder(DoubleProblem problem) {
    this.problem = problem;
    this.evaluator = new SequentialSolutionListEvaluator<>();
    this.comparator = new ObjectiveComparator<DoubleSolution>(0,ObjectiveComparator.Ordering.ASCENDING);
    this.cycles = 2;
    this.subcomponets = 5;
    this.FEs = 50;
    this.wFes = 50;
    this.population_size = 50;
  }

  public DECC_GBuilder setPopulationSize(int populationSize) {
    if (populationSize < 0) {
      throw new JMetalException("Population size is negative: " + populationSize);
    }

    this.population_size = populationSize;

    return this;
  }

  public DECC_GBuilder setCycles(int cycles) {
    if (cycles < 0) {
      throw new JMetalException("Cycles is negative: " + cycles);
    }

    this.cycles = cycles;

    return this;
  }


  public DECC_GBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
    this.evaluator = evaluator;

    return this;
  }

  public DECC_G build() {
    return new DECC_G(subcomponets, cycles, FEs, wFes, problem, population_size, evaluator, comparator);
  }

  /* Getters */
  public DoubleProblem getProblem() {
    return problem;
  }

  public int getPopulationSize() {
    return population_size;
  }

  public int getCycles() {
    return this.cycles;
  }

    public int getSubcomponets() {
        return subcomponets;
    }

    public DECC_GBuilder setSubcomponets(int subcomponets) {
        if (subcomponets < 0) {
            throw new JMetalException("subcomponets is negative: " + subcomponets);
        }
        this.subcomponets = subcomponets;
        return this;
    }

    public int getFEs() {
        return FEs;
    }

    public DECC_GBuilder setFEs(int FEs) {
        if (FEs < 0) {
            throw new JMetalException("FEs is negative: " + FEs);
        }
        this.FEs = FEs;
        return this;
    }

    public int getwFes() {
        return wFes;
    }

    public DECC_GBuilder setwFes(int wFes) {
      if (wFes < 0) {
        throw new JMetalException("wFes is negative: " + wFes);
      }
        this.wFes = wFes;
        return this;
    }

 
  public SolutionListEvaluator<DoubleSolution> getSolutionListEvaluator() {
    return evaluator;
  }
}

