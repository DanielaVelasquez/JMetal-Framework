package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.ArrayDoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class DECC_G implements Algorithm
{
    /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    private static double CR = 0.5 ;
    private static double F = 0.5 ;
    private static DifferentialEvolutionCrossover CROSSOVER_1 = new DifferentialEvolutionCrossover(CR, F, "rand/1/bin");
    private static DifferentialEvolutionCrossover CROSSOVER_2 = new DifferentialEvolutionCrossover(CR, F, "current-to-best/1/bin");
    DifferentialEvolutionSelection SELECTION = new DifferentialEvolutionSelection();
    private int LOWER_BOUND = 0;
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Individiuals
     */
    private List<DoubleSolution> population;
     /**
     * Individiuals for Diferential evolution algorithm 
     */
    private List<DoubleSolution> w_population;
    /**
     * Problem to solve
     */
    private DoubleProblem problem ;
    
    private DoubleProblem subcomponent_problem;
    /**
     * Subcomponent dimension size
     */
    private int s;
    /**
     * Number of fitness evaluation for SaNSDE algorithm
     */
    private int FE;
    /**
     * Number of fitness evaluation for DE algorithm
     */
    private int wFEs;
    /**
     * Number of dimension (variables) of the function
     */
    private int n;
    /**
     * Population size
     */
    private int populationSize;
    /**
     * Number of cycles that the algorithm should run
     */
    private int cycles;
    
    private SolutionListEvaluator<DoubleSolution> evaluator;
    
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    
    private JMetalRandom randomGenerator ;
    
    private DoubleSolution best_inidvidual;
    private int best_index;
    
    private DoubleSolution random_inidividual;
    private int random_index;
    
    private DoubleSolution worst_inidividual;
    private int worst_index;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public DECC_G()
    {
        randomGenerator = JMetalRandom.getInstance();
        best_index = 0;
        worst_index = populationSize - 1;
    }
    
    public List<DoubleSolution> getPopulation() {
      return population;
    }

    public void setProblem(DoubleProblem problem) {
      this.problem = problem ;
    }
    public DoubleProblem getProblem() {
      return problem ;
    }
    /**
     * Creates an initial random population
     * @return initial population with random values
     */
    private List<DoubleSolution> createInitialPopulation() {
        List<DoubleSolution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
          DoubleSolution newIndividual = getProblem().createSolution();
          population.add(newIndividual);
        }
        return population;
    } 
    private List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
       return evaluator.evaluate(population, getProblem());
    }
    private List<DoubleSolution> getSubPopulation(int l,int u)
    {
        List<DoubleSolution> population = new ArrayList<>(populationSize);
        int size = u - l + 1;
        for(int i = 0; i < populationSize; i++)
        {
            //DoubleSolution solution = new ArrayDoubleSolution();
        }
        return population;
    }
    private List<DoubleSolution> randomWeight(List<DoubleSolution> w_population, int objective)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
    private void replaceInPopulation(List<DoubleSolution> subpopulation, int l, int u)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void findIndividuals()
    {
        Collections.sort(getPopulation(), comparator) ;
        Collections.sort(w_population, comparator) ; //REVISAR SI EL W_POPULATION SE ARREGLA IGUAL QUE EL POPULATION
        best_inidvidual = population.get(best_index);
        worst_inidividual = population.get(worst_index);
        
        do
        {
            random_index = randomGenerator.nextInt(LOWER_BOUND, populationSize);
        }while(random_index != best_index && random_index!= worst_index);
        
        random_inidividual = population.get(random_index);
    }
    @Override
    public void run() 
    {
       int subcomponent = this.n/this.s;
       //subcomponent_problem = new 
       for(int i = 0; i < this.cycles; i++)
       {
           
           for(int j = 0; j < subcomponent; j++)
           {
               int l = ((j+1) - 1) * this.s + 1;
               int u = j * s;
               List<DoubleSolution> subpopulation = getSubPopulation(l, u);
               SaNSDE sansde = new SaNSDE(problem, FE, populationSize, CROSSOVER_1, CROSSOVER_2, SELECTION, evaluator, comparator);
               sansde.run();
               subpopulation = sansde.getPopulation();
               w_population = randomWeight(this.w_population, j);
               this.replaceInPopulation(subpopulation, l, u);
               this.evaluatePopulation(population);
           }
           this.findIndividuals();
           DifferentialEvolution de = new DifferentialEvolution(problem, wFEs, populationSize, CROSSOVER_1,SELECTION, evaluator);
           de.setPopulation(w_population);
           
           
           //NO estaria encontrando el mismo las 3 veces? No porque el random crea individiuos diferentes
           de.run();
           population.set(best_index, de.getResult());
           
           de.run();
           population.set(random_index, de.getResult());
           
           de.run();
           population.set(worst_index, de.getResult());
           
           this.evaluatePopulation(population);
       }
    }

    @Override
    public DoubleSolution getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
