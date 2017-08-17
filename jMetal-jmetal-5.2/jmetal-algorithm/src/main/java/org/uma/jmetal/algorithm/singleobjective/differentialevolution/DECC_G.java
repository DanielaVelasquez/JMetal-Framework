package org.uma.jmetal.algorithm.singleobjective.differentialevolution;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemDE;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemSaNSDE;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentDE;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentSaNSDE;
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
    //private List<DoubleSolution> w_population;
    /**
     * Problem to solve
     */
    private DoubleProblem problem ;
    
    private SubcomponentDoubleProblemDE subcomponent_problem_DE;
    
    private SubcomponentDoubleProblemSaNSDE subcomponent_problem_SaNSDE;
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
    /**
     * Number of subcomponents
     */
    private double subcomponent;

    
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
    public DECC_G(int subcomponent, int cycles, int FEs, int wFes, DoubleProblem p, int population_size, SolutionListEvaluator<DoubleSolution> evaluator, Comparator<DoubleSolution> comparator)
    {
        this.subcomponent = subcomponent;
        this.cycles = cycles;
        this.FE = FEs;
        this.wFEs = wFes;
        this.problem = p;
        this.populationSize = population_size;
        this.evaluator = evaluator;
        this.comparator = comparator;
        
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
    private void randomWeight(List<DoubleSolution> w_population, int column)
    {
        int size = w_population.size();
        for(int i = 0; i < size; i++)
        {
            
            DoubleSolution s = w_population.get(i);
            double value = randomGenerator.nextDouble(s.getLowerBound(column), s.getUpperBound(column));
            s.setVariableValue(column, value);
        }
    } 
    private void replaceInPopulation(List<DoubleSolution> subpopulation, int l, int u, List<Integer> index)
    {
        for(int i = 0; i < populationSize; i++)
        {
            DoubleSolution original = population.get(i);
            DoubleSolution other = subpopulation.get(i);
            this.copyObjectivesFrom(original, other);
            int k = 0;
            for(int j = l; j <= u; j++)
            {
                original.setVariableValue(index.get(j), other.getVariableValue(k));
                k++;
            }
        }
    }
    private void copyObjectivesFrom(DoubleSolution origin,DoubleSolution destination)
    {
        int size = origin.getNumberOfObjectives();
        for(int i = 0; i < size; i++)
        {
            destination.setObjective(i, origin.getObjective(i));
        }
    }
    
    private void findIndividuals()
    {
        Collections.sort(getPopulation(), comparator) ;
        //Collections.sort(w_population, comparator) ; //REVISAR SI EL W_POPULATION SE ARREGLA IGUAL QUE EL POPULATION
        best_inidvidual = population.get(best_index);
        worst_inidividual = population.get(worst_index);
        
        do
        {
            random_index = randomGenerator.nextInt(LOWER_BOUND, populationSize);
        }while(random_index != best_index && random_index!= worst_index);
        
        random_inidividual = population.get(random_index);
    }
    private List<Integer> randPerm(int n)
    {
        List<Integer> list = new ArrayList<>();
        while(list.size() != n)
        {
            int value = randomGenerator.nextInt(LOWER_BOUND, n - 1);
            if(!list.contains(value))
                list.add(value);
        }
        return list;
    }
    private List<DoubleSolution> copy(List<DoubleSolution> population)
    {
        List<DoubleSolution> subpopulation = new ArrayList<>();
        for(DoubleSolution s: population)
        {
           subpopulation.add(new DoubleSolutionSubcomponentSaNSDE(s, subcomponent_problem_SaNSDE));
        }
        return subpopulation;
    }
    private double[] getVariables(DoubleSolution s)
    {
        int size = s.getNumberOfVariables();
        double[] variables = new double[size];
        for(int i = 0; i < size; i++)
        {
            variables[i] = s.getVariableValue(i);
        }
        return variables;
    }
    private List<DoubleSolution> initWPopulation(int size, int variables)
    {
        List<DoubleSolution> s = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            DoubleSolutionSubcomponentDE solution = new DoubleSolutionSubcomponentDE(new double[1], new double[variables],subcomponent_problem_DE);
            s.add(solution);
        }
        return s;
    }
    private double[] getObjectives(DoubleSolution s)
    {
        int size = s.getNumberOfObjectives();
        double[] objectives = new double[size];
        for(int i = 0; i < size; i++)
        {
            objectives[i] = s.getObjective(i);
        }
        return objectives;
    }
    
    private void multiply(DoubleSolution original, DoubleSolution weight)
    {
        List<Integer> index = subcomponent_problem_DE.getIndex();
        int i = 0;
        for(Integer j:index)
        {
            double value = original.getVariableValue(j);
            double newValue = weight.getVariableValue(i) * value;
            original.setVariableValue(j, newValue);
            i++;
        }
        this.copyObjectivesFrom(original, weight);
    }
    private void chooseIndexWPopulation(int size)
    {
        List<Integer> index = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            int value = randomGenerator.nextInt(0, populationSize-1);
            
            while(index.contains(value))
            {
                value = randomGenerator.nextInt(0, populationSize-1);
            }
            index.add(value);
              
        }
        subcomponent_problem_DE.setIndex(index);
    }

    public void setPopulation(List<DoubleSolution> population) {
        this.population = population;
    }
    
    @Override
    public void run() 
    {
       if(this.population == null)
        this.population = this.createInitialPopulation();
       
       this.evaluatePopulation(this.population);
       this.n = population.get(0).getNumberOfVariables();
       this.s = this.n / (int)this.subcomponent;
       //AbstractELMEvaluator p = (AbstractELMEvaluator) problem;
       //this.subcomponent = (double)this.n/(double)this.s;
       
       subcomponent_problem_DE = new SubcomponentDoubleProblemDE(problem);
       //w_population = this.initWPopulation(populationSize, (int) Math.ceil(subcomponent));
       
       //subcomponent_problem = new 
       for(int i = 0; i < this.cycles; i++)
       {
           List<Integer> index = this.randPerm(this.n);
           //w_population.clear();
           for(int j = 0; j < subcomponent; j++)
           {
               int l = j * this.s;
               int u = ((j+1) * s) - 1;
               
               if(u>n)
                   u = n - 1;
               
               List<Integer> sublist = index.subList(l, u + 1);
               subcomponent_problem_SaNSDE = new SubcomponentDoubleProblemSaNSDE(sublist,problem);
               
               List<DoubleSolution> subpopulation = this.copy(this.population);
               SaNSDE sansde = new SaNSDE(subcomponent_problem_SaNSDE, FE, populationSize, CROSSOVER_1, CROSSOVER_2, SELECTION, evaluator, comparator);
               sansde.setPopulation(subpopulation);
               
               
               sansde.run();
               subpopulation = sansde.getPopulation();
               //randomWeight(this.w_population, j); //TODO los valores que se creen deben cuplir con las restricciones del problema original!!!!
               this.replaceInPopulation(subpopulation, l, u,index);
               //this.evaluatePopulation(population);
           }
           this.findIndividuals();
           
           DifferentialEvolution de = new DifferentialEvolution(subcomponent_problem_DE, wFEs, populationSize, CROSSOVER_1,SELECTION, evaluator);
           //de.setPopulation(w_population);
           this.chooseIndexWPopulation((int) Math.ceil(subcomponent));
           subcomponent_problem_DE.setSolution(best_inidvidual);
           de.run();
           DoubleSolution ans = de.getResult();
           if(comparator.compare(ans, best_inidvidual)<1)
           {
               this.multiply(best_inidvidual, ans);
               population.set(best_index, best_inidvidual);
           }
           
           subcomponent_problem_DE.setSolution(random_inidividual);
           de.setEvaluations(0);
           de.run();
           ans = de.getResult();
           if(comparator.compare(ans, random_inidividual)<1)
           {
               this.multiply(random_inidividual, ans);
               population.set(random_index, random_inidividual);
           }
           
           subcomponent_problem_DE.setSolution(worst_inidividual);
           de.setEvaluations(0);
           de.run();
           
           ans = de.getResult();
           if(comparator.compare(ans, worst_inidividual)<1)
           {
               this.multiply(worst_inidividual,  ans);
               population.set(worst_index, worst_inidividual);
           }
           //this.evaluatePopulation(population);
       }
    }

    @Override
    public DoubleSolution getResult() {
        Collections.sort(getPopulation(), comparator) ;
        return getPopulation().get(0);
    }

    @Override
    public String getName() {
       return "DECC-G";
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
