package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.Comparator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.FrobeniusComparator;


public class DECC_GBuilder implements AlgorithmBuilder
{
  private DoubleProblem problem;
  private Comparator<DoubleSolution> comparator ;
  private int subcomponets;
  private int FEs;
  private int wFes;
  private int population_size;
  private SaNSDEBuilder sansdeBuilder;
  private DEUnicaucaBuilder deFrobeniusBuilder;
  private int maxEvaluations;
  private double penalize_value;


  public DECC_GBuilder(DoubleProblem problem) {
    this.problem = problem;
    this.comparator = new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
    this.subcomponets = 5;
    this.FEs = 50;
    this.wFes = 50;
    this.population_size = 50;
    this.maxEvaluations = 3000;
    this.penalize_value = 1;
    this.sansdeBuilder = new SaNSDEBuilder(problem);
    this.deFrobeniusBuilder = new DEUnicaucaBuilder(problem);
  }

  @Override
  public DECC_G build() {
    return new DECC_G(subcomponets,  FEs, wFes, problem, population_size, comparator, sansdeBuilder, deFrobeniusBuilder, maxEvaluations, penalize_value);
  }
  public DECC_GBuilder setSaNSDEBuilder(SaNSDEBuilder sansdeBuilder) {
    if (sansdeBuilder == null) 
    {
      throw new JMetalException("sansdeBuilder can't be null ");
    }

    this.sansdeBuilder = sansdeBuilder;

    return this;
  }
  public DECC_GBuilder setDEBuilder(DEUnicaucaBuilder deFrobeniusBuilder) {
    if (deFrobeniusBuilder == null) {
      throw new JMetalException("DEBuilder can't be null ");
    }

    this.deFrobeniusBuilder = deFrobeniusBuilder;

    return this;
  }

  public DECC_GBuilder setPopulationSize(int populationSize) {
    if (populationSize < 3) {
      throw new JMetalException("Population size must . be greater than 3: " + populationSize);
    }

    this.population_size = populationSize;

    return this;
  }


  
  public DECC_GBuilder setwFes(int wFes) {
    if (wFes < 0) {
      throw new JMetalException("wFes is negative: " + wFes);
    }
    this.wFes = wFes;
    return this;
  }
  public DECC_GBuilder setSubcomponets(int subcomponets) {
    if (subcomponets < 0) {
        throw new JMetalException("subcomponets is negative: " + subcomponets);
    }
    this.subcomponets = subcomponets;
    return this;
   }
  
   public DECC_GBuilder setFEs(int FEs) {
    if (FEs < 0) {
        throw new JMetalException("FEs is negative: " + FEs);
    }
    this.FEs = FEs;
    return this;
   }
   public DECC_GBuilder setMaxEvaluations(int FE) {
        if(FE <= 0)
            throw new JMetalException("Function evaluations is negative or cero: " + FE);
        this.maxEvaluations = FE;
        return this;
    }
    public DECC_GBuilder setPenalizeValue(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }

  public DECC_GBuilder setComparator(Comparator<DoubleSolution> comparator) {
    if(comparator == null)
    {
        throw new JMetalException("compartor can't be null ");
    }
    this.comparator = comparator;
    return this;
  }
  
  public DECC_GBuilder setProblem(DoubleProblem problem) {
    if(problem == null)
    {
        throw new JMetalException("problem can't be null ");
    }
    this.problem = problem;
    return this;
  }
    
  

  public Comparator<DoubleSolution> getComparator() {
    return comparator;
  }

  

  public int getPopulation_size() {
    return population_size;
  }

  public SaNSDEBuilder getSansdeBuilder() {
    return sansdeBuilder;
  }

  public DEUnicaucaBuilder getDeFrobeniusBuilder() {
    return deFrobeniusBuilder;
  }

  
  public DoubleProblem getProblem() {
    return problem;
  }

  public int getPopulationSize() {
    return population_size;
  }


  public int getSubcomponets() {
    return subcomponets;
  }
  public int getFEs() {
    return FEs;
  }
  public int getwFes() {
    return wFes;
  }
}

