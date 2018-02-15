package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import org.uma.jmetal.algorithm.impl.AbstractDifferentialEvolution;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@SuppressWarnings("serial")
public class DEUnicauca extends AbstractDifferentialEvolution<DoubleSolution> {
  
    /**
     * ------------------------------------------------------------------------
     * Based on
     * https://link.springer.com/article/10.1023/A:1008202821328 ****
     * R. Storn and K. Price, “Differential Evolution – A Simple and Efficient
     * Heuristic for global Optimization over Continuous Spaces,”
     * Journal of Global Optimization, vol. 11, pp. 341-359, 1997.
     * -------------------------------------------------------------------------
     */
    
  private int populationSize;
  private int maxEvaluations;
  private Comparator<DoubleSolution> comparator;
  private int evaluations;
  private double penalize_value;

  /**
   * Constructor
   *
   * @param problem Problem to solve
   * @param maxEvaluations Maximum number of evaluations to perform
   * @param populationSize
   * @param crossoverOperator
   * @param selectionOperator
     * @param penalize_value
     * @param comparator
   */
  public DEUnicauca(DoubleProblem problem, int maxEvaluations, int populationSize,
      DifferentialEvolutionCrossover crossoverOperator,
      DifferentialEvolutionSelection selectionOperator, double penalize_value,
      Comparator<DoubleSolution> comparator) {
    setProblem(problem); ;
    this.maxEvaluations = maxEvaluations;
    this.populationSize = populationSize;
    this.crossoverOperator = crossoverOperator;
    this.selectionOperator = selectionOperator;
    this.penalize_value = penalize_value;
    this.comparator = comparator;
  }
  
  public int getEvaluations() {
    return evaluations;
  }

  public void setEvaluations(int evaluations) {
    this.evaluations = evaluations;
  }

  @Override protected void initProgress() {
    //evaluations = 1;//this.populationSize;
  }

  @Override protected void updateProgress() {
   // evaluations += 1;//this.populationSize;
  }

  @Override protected boolean isStoppingConditionReached() {
    return evaluations >= maxEvaluations;
  }

  @Override protected List<DoubleSolution> createInitialPopulation() {
    this.evaluations = 0;
    if(getPopulation() !=null)
        return this.getPopulation();
      List<DoubleSolution> population = new ArrayList<>(populationSize);
    for (int i = 0; i < populationSize; i++) {
      DoubleSolution newIndividual = getProblem().createSolution();
      population.add(newIndividual);
    }
    return population;
  }
  /**
     * Sets an individual function value to a penalization value given by 
     * the user
     * @param solution solution to penalize with a bad function value
     */
    protected void penalize(DoubleSolution solution){
        solution.setObjective(0, this.penalize_value);
    }

  @Override protected List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
    int i = 0;
        int populationSize = population.size();
        while(!isStoppingConditionReached() && i < populationSize)
        {
            DoubleSolution solution = population.get(i);
            this.getProblem().evaluate(solution);
            i++;
            this.evaluations++;
        }
        for(int j = i; j < populationSize; j++)
        {
            DoubleSolution solution = population.get(j);
            this.penalize(solution);
        }
        return population;
  }

  @Override protected List<DoubleSolution> selection(List<DoubleSolution> population) {
    return population;
  }

  @Override protected List<DoubleSolution> reproduction(List<DoubleSolution> matingPopulation) {
    List<DoubleSolution> offspringPopulation = new ArrayList<>();

    for (int i = 0; i < populationSize; i++) {
      selectionOperator.setIndex(i);
      List<DoubleSolution> parents = selectionOperator.execute(matingPopulation);

      crossoverOperator.setCurrentSolution(matingPopulation.get(i));
      List<DoubleSolution> children = crossoverOperator.execute(parents);

      offspringPopulation.add(children.get(0));
    }

    return offspringPopulation;
  }

    @Override protected List<DoubleSolution> replacement(List<DoubleSolution> population, List<DoubleSolution> offspringPopulation) 
    {
        List<DoubleSolution> pop = new ArrayList<>();
        
        for (int i = 0; i < populationSize; i++)
        {
            DoubleSolution p = population.get(i);
            DoubleSolution o = offspringPopulation.get(i);
            DoubleSolution s = this.getBest(p, o);

            if(!this.inPopulation(pop, s))
            {
                pop.add(s);
            }
            else
            {
                if(s == o)
                {
                    pop.add(p);
                }
                else
                {
                    pop.add(o);
                }
            }
        }
        
        return pop;
    }

    /**
     * @return best individual in population
     */
    @Override
    public DoubleSolution getResult() 
    {
        DoubleSolution best = getPopulation().get(0);
        
        for(int i = 1; i < populationSize; i++)
        {
            DoubleSolution s = this.getPopulation().get(i);
            best = this.getBest(best, s);
        }
        
        return best;
    }

  @Override public String getName() {
    return "DE" ;
  }

  @Override public String getDescription() {
    return "Differential Evolution Algorithm" ;
  }
    
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    private DoubleSolution getBest(DoubleSolution s1, DoubleSolution s2)
    {
        int comparison = comparator.compare(s1, s2);
        
        if(comparison <= 0)
        {
            return s1;
        }
        else
        {
            return s2;
        }
    }
    
    /**
     * Determines if an individual with the same values already exists on a
     * population
     * @param population population to search
     * @param individual individual to compare with individuals in population
     * @return 
     */
    private boolean inPopulation(List<DoubleSolution> population, DoubleSolution individual)
    {
        for(DoubleSolution s: population)
        {
            if(s != individual)
            {
                int n = this.getProblem().getNumberOfVariables();
                
                for(int i = 0; i < n; i++)
                {
                    double sValue = s.getVariableValue(i);
                    double individualValue = individual.getVariableValue(i);
                    
                    if(sValue == individualValue)
                    {
                        if(i == n-1)
                        {
                            return true;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                return true;
            }
        }
        
        return false;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }
}