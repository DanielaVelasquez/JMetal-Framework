package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemDE;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemSaNSDE;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentDE;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentSaNSDE;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class DECC_G implements Algorithm
{
    /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
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
     * Number of subcomponents
     */
    private double subcomponent;
    
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    
    private JMetalRandom randomGenerator ;
    /**
     * Best individual when needed
     */
    private DoubleSolution best_inidvidual;
    /**
     * Index in population of the best individual when asked
     */
    private int best_index;
    /**
     * A random individual when asked
     */
    private DoubleSolution random_inidividual;
    /**
     * Index in population from a random individual when asked
     */
    private int random_index;
    /**
     * Worst individual found when asked
     */
    private DoubleSolution worst_inidividual;
    /**
     * Index in population from worst indiviual when  asked
     */
    private int worst_index;
    /**
     * Builder to create sansde algorithm, with params arranged
     */
    private SaNSDEBuilder sansdeBuilder;
    /**
     * Builder to create DE algorithm with params arrenged
     */
    private DEFrobeniusBuilder deFrobeniusBuilder;
    /**
     * Maximun number of evaluations
     */
    private int maxEvaluations;
    /**
     * current evaluations
     */
    private int evaluations;
    /**
     * Value to penalize a solution in case maxEvaluations are reached
     */
    private double penalize_value;

    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public DECC_G(int subcomponent, int FEs, int wFes, 
            DoubleProblem p, int population_size, 
            Comparator<DoubleSolution> comparator, SaNSDEBuilder sansdeBuilder,
            DEFrobeniusBuilder deFrobeniusBuilder, int maxEvaluations,
            double penalize_value)
    {
        this.subcomponent = subcomponent;
        this.FE = FEs;
        this.wFEs = wFes;
        this.problem = p;
        this.populationSize = population_size;
        this.comparator = comparator;
        this.sansdeBuilder = sansdeBuilder;
        this.deFrobeniusBuilder = deFrobeniusBuilder;
        this.maxEvaluations = maxEvaluations;
        this.penalize_value = penalize_value;
        
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
     * Increments the number of generations evaluated
     */
    protected void updateProgress() {
        evaluations += 1;
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
    /**
     * Sets an individual function value to a penalization value given by 
     * the user
     * @param solution solution to penalize with a bad function value
     */
    protected void penalize(DoubleSolution solution){
        solution.setObjective(0, this.penalize_value);
    }
    private void evaluatePopulation(List<DoubleSolution> population) {
       int i = 0;
        int populationSize = population.size();
        while(!isStoppingConditionReached() && i < populationSize)
        {
            DoubleSolution solution = population.get(i);
            this.problem.evaluate(solution);
            System.out.println("FE "+solution.getObjective(0));
            i++;
            this.updateProgress();
        }
        for(int j = i; j < populationSize; j++)
        {
            DoubleSolution solution = population.get(i);
            this.penalize(solution);
        }
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
    
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }
    /**
     * Determines how many evalutions can be perfomed according to the 
     * number of current of evalutions, max evaluations and number of
     * evaluations wanting to perfomr
     * @param nextNumberEvaluations next number of evaluations that 
     * are likely to perfomr
     * @return number of possible evalations
     */
    private int getPossibleEvaluations(int nextNumberEvaluations)
    {
        if(this.evaluations + nextNumberEvaluations > maxEvaluations)
            return this.maxEvaluations - evaluations;
        return nextNumberEvaluations;
    }
    
    @Override
    public void run() 
    {
       this.evaluations = 0;
       this.population = this.createInitialPopulation();
       
       this.evaluatePopulation(this.population);
       this.n = this.problem.getNumberOfVariables();
       this.s = this.n / (int)this.subcomponent;
       
       int missing_genes = (int) (this.n - (this.s * this.subcomponent));
       subcomponent_problem_DE = new SubcomponentDoubleProblemDE(problem);
       
       while(!isStoppingConditionReached())
       {
           List<Integer> index = this.randPerm(this.n);
           boolean load_missing_genes = false;
           //w_population.clear();
           int l = 0;
           int u = -1;
           for(int j = 0; j < subcomponent; j++)
           {  
               l = u + 1;
               u = l + this.s - 1;
               
               if(u> this.n)
               {
                   u = this.n - 1;
               }
               
               List<Integer> sublist = index.subList(l, u + 1);
               subcomponent_problem_SaNSDE = new SubcomponentDoubleProblemSaNSDE(sublist,problem);
               
               List<DoubleSolution> subpopulation = this.copy(this.population);
               
               int evaluationsToPerfom = this.getPossibleEvaluations(FE);
               
               if(evaluationsToPerfom == 0) 
                   continue;
               
               evaluations += evaluationsToPerfom;
               
               SaNSDE sansde = sansdeBuilder
                               .setMaxEvaluations(evaluationsToPerfom)
                               .setProblem(subcomponent_problem_SaNSDE)
                               .setPopulationSize(populationSize)
                               .setComparator(comparator)
                               .build();
               sansde.setPopulation(subpopulation);
               
               
               sansde.run();
               if(!load_missing_genes && missing_genes>0 && (j+1)==missing_genes)
               {
                   this.s = this.s + 1;
                   load_missing_genes = true;
               }
           }
           this.findIndividuals();
           
           int evaluationsToPerfom = this.getPossibleEvaluations(wFEs);
               
            if(evaluationsToPerfom == 0) 
                continue;
            
           evaluations += evaluationsToPerfom;
           
           DEFrobenius de = deFrobeniusBuilder
                                      .setMaxEvaluations(evaluationsToPerfom)
                                      .setProblem(subcomponent_problem_DE)
                                      .setPopulationSize(populationSize)
                                      .build();
           
           //de.setPopulation(w_population);
           this.chooseIndexWPopulation((int) Math.ceil(subcomponent));
           subcomponent_problem_DE.setSolution(best_inidvidual);
           de.run();
           DoubleSolution ans = de.getResult();
           if(this.getBest(best_inidvidual, ans)==ans)
           {
               this.multiply(best_inidvidual, ans);
               population.set(best_index, best_inidvidual);
           }
           
           evaluationsToPerfom = this.getPossibleEvaluations(wFEs);
               
            if(evaluationsToPerfom == 0) 
                continue;
           evaluations += evaluationsToPerfom;
           de = deFrobeniusBuilder
                .setMaxEvaluations(evaluationsToPerfom)
                .setProblem(subcomponent_problem_DE)
                .setPopulationSize(populationSize)
                .build();
           
           subcomponent_problem_DE.setSolution(random_inidividual);
           de.run();
           ans = de.getResult();
           if(this.getBest(random_inidividual, ans)==ans)
           {
               this.multiply(random_inidividual, ans);
               population.set(random_index, random_inidividual);
           }
           evaluationsToPerfom = this.getPossibleEvaluations(wFEs);
               
            if(evaluationsToPerfom == 0) 
                continue;
            
            evaluations += evaluationsToPerfom;
           
           de = deFrobeniusBuilder
                .setMaxEvaluations(evaluationsToPerfom)
                .setProblem(subcomponent_problem_DE)
                .setPopulationSize(populationSize)
                .build();
           
           subcomponent_problem_DE.setSolution(worst_inidividual);
           de.run();
           
           ans = de.getResult();
           if(this.getBest(worst_inidividual, ans)==ans)
           {
               this.multiply(worst_inidividual,  ans);
               population.set(worst_index, worst_inidividual);
           }
           
           //this.evaluatePopulation(population);
       }
        System.out.println("evaluations "+evaluations);
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
        if(comparison == 0)
        {
            try
            {
                double b_s1 = (double) s1.getAttribute("B");
                double b_s2 = (double) s2.getAttribute("B");
                if(b_s1 <= b_s2)
                    return s1;
                else
                    return s2;
            }
            catch(Exception e)
            {
                return s1;
            }
        }
        else if(comparison < 0)
            return s1;
        else
            return s2;
    }
    @Override
    public DoubleSolution getResult() {
        Collections.sort(getPopulation(), comparator) ;
        DoubleSolution best =  getPopulation().get(0);
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
            if(s!=individual)
            {
                int n = this.getProblem().getNumberOfVariables();
                for(int i = 0; i < n; i++)
                {
                    if(s.getVariableValue(i) == individual.getVariableValue(i))
                    {
                        if(i== n-1)
                            return true;
                    }
                    else
                        break;
                }
            }
        }
        return false;
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
