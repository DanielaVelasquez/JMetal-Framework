package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import org.uma.jmetal.algorithm.impl.AbstractDifferentialEvolution;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@SuppressWarnings("serial")
public class DEFrobenius extends AbstractDifferentialEvolution<DoubleSolution> {
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
   * @param evaluator
   */
  public DEFrobenius(DoubleProblem problem, int maxEvaluations, int populationSize,
      DifferentialEvolutionCrossover crossoverOperator,
      DifferentialEvolutionSelection selectionOperator, double penalize_value) {
    setProblem(problem); ;
    this.maxEvaluations = maxEvaluations;
    this.populationSize = populationSize;
    this.crossoverOperator = crossoverOperator;
    this.selectionOperator = selectionOperator;
    this.penalize_value = penalize_value;
    comparator = new ObjectiveComparator<DoubleSolution>(0);
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
            DoubleSolution solution = population.get(i);
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
            pop.add(getBest(population.get(i), offspringPopulation.get(i)));
        }

        Collections.sort(pop, comparator) ;
        return pop;
    }

  /**
   * Returns the best individual
   */
  @Override 
  public DoubleSolution getResult() 
  {
        DoubleSolution best = getPopulation().get(0);
        for(int i = 1; i < populationSize; i++)
        {
            DoubleSolution s = this.getPopulation().get(i);
            if(s.getObjective(0) == best.getObjective(0))
            {
                best = this.getBest(best, s);
            }
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
            
        if (comparison == 0) 
        {
            try
            {                
                double bS1 = (double) s1.getAttribute("B");
                double bS2 = (double) s2.getAttribute("B");

                if(bS1 <= bS2)
                {
                    return s1;
                }
                else
                {                        
                    return s2;
                }
            }
            catch(Exception ex)
            {
                return s1;
            }
        } 
        else if (comparison < 0)
        {                
            return s1;
        }
        else
        {
            return s2;
        }
    }
}