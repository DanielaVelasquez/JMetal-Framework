package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemDE;
import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemSaNSDE;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentDE;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentSaNSDE;
import org.uma.jmetal.util.Util;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class DECC_G implements Algorithm
{
    /**
     * ------------------------------------------------------------------------
     * Based on
     * https://www.sciencedirect.com/science/article/pii/S002002550800073X ****
     * Z. Yang, k. Tang and X. Yao, “Large scale evolutionary optimization 
     * using cooperative coevolution,” Information Sciences., vol. 178, 
     * pp. 2985-2999, 2008.
     * -------------------------------------------------------------------------
     */
    
    /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    private final static int LOWER_BOUND = 0;
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Individuals of the population
     */
    private List<DoubleSolution> population;
    /**
     * Problem to solve
     */
    private DoubleProblem problem ;
    /**
     * Problem created to solve subcomponents on DE algorithm
     */
    private SubcomponentDoubleProblemDE subcomponent_problem_DE;
    /**
     * Problem created to solve subcomponents on SaNSDE algorithm
     */
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
     * Number of variables on a solution
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
     * Determines the problem is maximizing or minimizing
     */
    private Comparator<DoubleSolution> comparator;
    /**
     * Random number generator
     */
    private JMetalRandom randomGenerator ;
    /**
     * Best individual when needed
     */
    private DoubleSolution best_individual;
    /**
     * Index in population of the best individual when asked
     */
    private int best_index;
    /**
     * A random individual when asked
     */
    private DoubleSolution random_individual;
    /**
     * Index in population from a random individual when asked
     */
    private int random_index;
    /**
     * Worst individual found when asked
     */
    private DoubleSolution worst_individual;
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
    private DEUnicaucaBuilder deFrobeniusBuilder;
    /**
     * Maximum number of evaluations
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
    /**
     * Creates an algorithm DECC-G with specific parameters
     * @param subcomponent number of subcomponents
     * @param FEs number of evaluations for SaNSDE algorithm
     * @param wFes number of evaluations for DE algorithm
     * @param p problem to solve
     * @param population_size number of individuals in the population
     * @param comparator determines if problem is maximizing or minimizing
     * @param sansdeBuilder builder to create SaNSDE algorithm, the builder
     *                      is configured with the values that are needed for 
     *                      SaNSDE
     * @param deFrobeniusBuilder  builder to create DE algorithm, the builder
     *                            is configured with the values that are needed 
     *                            for DE
     * @param maxEvaluations Maximum number of evaluations
     * @param penalize_value Value use to penalize solutions when stopping 
     *                       condition is reached 
     */
    public DECC_G(int subcomponent, int FEs, int wFes, 
            DoubleProblem p, int population_size, 
            Comparator<DoubleSolution> comparator, SaNSDEBuilder sansdeBuilder,
            DEUnicaucaBuilder deFrobeniusBuilder, int maxEvaluations,
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
           List<Integer> index = Util.createRandomPermutation(n);
           boolean load_missing_genes = false;
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
               
               List<DoubleSolution> subpopulation = this.createSubcomponent(this.population);
               
               int evaluationsToPerfom = this.getPossibleEvaluations(FE);
               
               if(evaluationsToPerfom == 0) 
                   continue;
               
               this.updateProgress(evaluationsToPerfom);
               
               SaNSDE sansde = sansdeBuilder
                               .setMaxEvaluations(evaluationsToPerfom)
                               .setProblem(subcomponent_problem_SaNSDE)
                               .setPopulationSize(populationSize)
                               .setComparator(comparator)
                               .build();
               sansde.setPopulation(subpopulation);
               sansde.run();
               this.getIndividualsFromSubpopulation(sansde.getPopulation());
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
            
           this.updateProgress(evaluationsToPerfom);
           DEUnicauca de = deFrobeniusBuilder
                                      .setMaxEvaluations(evaluationsToPerfom)
                                      .setProblem(subcomponent_problem_DE)
                                      .setPopulationSize(populationSize)
                                      .build();
           
           
           this.chooseIndexWPopulation();
           subcomponent_problem_DE.setSolution(best_individual);
           de.run();
           DoubleSolution ans = ((DoubleSolutionSubcomponentDE) de.getResult()).getCompleteSolution();
           ans = this.getBest(best_individual, ans);
           population.set(best_index, ans);
           
           
           evaluationsToPerfom = this.getPossibleEvaluations(wFEs);
               
            if(evaluationsToPerfom == 0) 
                continue;
            
           this.updateProgress(evaluationsToPerfom);
           de = deFrobeniusBuilder
                .setMaxEvaluations(evaluationsToPerfom)
                .setProblem(subcomponent_problem_DE)
                .setPopulationSize(populationSize)
                .build();
           
           subcomponent_problem_DE.setSolution(random_individual);
           de.run();
           ans = ((DoubleSolutionSubcomponentDE) de.getResult()).getCompleteSolution();
           ans = this.getBest(best_individual, ans);
           population.set(random_index, ans);
           
           evaluationsToPerfom = this.getPossibleEvaluations(wFEs);
               
            if(evaluationsToPerfom == 0) 
                continue;
            
           this.updateProgress(evaluationsToPerfom);
           de = deFrobeniusBuilder
                .setMaxEvaluations(evaluationsToPerfom)
                .setProblem(subcomponent_problem_DE)
                .setPopulationSize(populationSize)
                .build();
           
           subcomponent_problem_DE.setSolution(worst_individual);
           de.run();
           
           ans = ((DoubleSolutionSubcomponentDE) de.getResult()).getCompleteSolution();
           ans = this.getBest(best_individual, ans);
           population.set(worst_index, ans);
           
       }
    }
    @Override
    public String getName() {
       return "DECC-G";
    }

    @Override
    public String getDescription() {
        return "Large Scale Evolutionary Optimization Using "+
                "Cooperative Coevolution";
    }
        
    @Override
    public DoubleSolution getResult() {
        DoubleSolution best =  getPopulation().get(0);
        for(int i = 1; i < populationSize; i++)
        {
            DoubleSolution s = this.getPopulation().get(i);
            best = this.getBest(best, s);
        }
        return best;
    }
    /**
     * Increments the number of individuals evaluated,
     * it is called every time one individual or several is evaluated
     * @param evaluations number evaluations performed
     */
    protected void updateProgress(int evaluations) {
        this.evaluations += evaluations;
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
    protected void penalize(DoubleSolution solution)
    {
        solution.setObjective(0, this.penalize_value);
    }
    /**
     * Evaluate individuals from a population as long as stopping condition
     * is not reached, if this happen, the individuals that were not 
     * evaluated are penalized
     * @param population individuals to evaluate
     */
    private void evaluatePopulation(List<DoubleSolution> population) 
    {
       int i = 0;
        int populationSize = population.size();
        /**
         * <p>
         * While maxEvaluation is not reached and there are individuals
         * who has not been evaluted, every individual is send to evaluation
         * </p>
         */
        while(!isStoppingConditionReached() && i < populationSize)
        {
            DoubleSolution solution = population.get(i);
            this.problem.evaluate(solution);
            i++;
            this.updateProgress(1);
        }
        /**
         * If there are individual who were not evaluated, they are penalized
         */
        for(int j = i; j < populationSize; j++)
        {
            DoubleSolution solution = population.get(j);
            this.penalize(solution);
        }
    }
    /**
     * Get real individuals who are wrapped on a SaNSDE subcomponent solution
     * @param subpopulation individuals used on a SaNSDE subcomponent problem
     */
    private void getIndividualsFromSubpopulation(List<DoubleSolution> subpopulation)
    {
        for(int i = 0; i < populationSize; i++)
        {
            DoubleSolutionSubcomponentSaNSDE other = (DoubleSolutionSubcomponentSaNSDE)subpopulation.get(i);
            population.set(i, other.getSolution());
        }
    }
    /**
     * Sets the best and worst individual and its indexes
     * @param population individuals to select best and worst individuals
     */
    private void selectBestWorstIndividual(List<DoubleSolution> population)
    {
        best_index = 0;
        worst_index = 0;
        
        best_individual = population.get(best_index);
        worst_individual = population.get(worst_index);
        
        for(int i = 1; i < populationSize; i++)
        {
            DoubleSolution next = population.get(i);
            
            //if next individual is better than current best
            if(comparator.compare(next, best_individual) < 0)
            {
                best_individual = next;
                best_index = i;
            }
            else
            {
                //if next is worst than current worst
                if(comparator.compare(next, worst_individual) > 0)
                {
                    worst_individual = next;
                    worst_index = i;
                }
            }
        }
    }
    /**
     * Find best, worst an a random individual from population
     */
    private void findIndividuals()
    {
        this.selectBestWorstIndividual(population);
        do
        {
            random_index = randomGenerator.nextInt(LOWER_BOUND, populationSize-1);
        }while(random_index == best_index || random_index== worst_index);
        
        random_individual = population.get(random_index);
    }
    /**
     * Creates subcomponents solution for SaNSDE algorithm
     * @param population individuals to create subcomponents from
     * @return population wrapped on a solution as a subcomponent in SaNSDE algorithm
     */
    private List<DoubleSolution> createSubcomponent(List<DoubleSolution> population)
    {
        List<DoubleSolution> subpopulation = new ArrayList<>();
        for(DoubleSolution s: population)
        {
           subpopulation.add(new DoubleSolutionSubcomponentSaNSDE(s, subcomponent_problem_SaNSDE));
        }
        return subpopulation;
    }
    /**
     * Choose indexes that DE is going to use to evolve on,
     * it chooses the number of subcomponents created for
     * SaNSDE is the number of indexes selected for the problem
     * of DE
     */
    private void chooseIndexWPopulation()
    {
        int size = (int) Math.ceil(subcomponent);
        List<Integer> index = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            int value = randomGenerator.nextInt(0, n-1);
            
            while(index.contains(value))
            {
                value = randomGenerator.nextInt(0, n-1);
            }
            index.add(value);
              
        }
        subcomponent_problem_DE.setIndex(index);
    }

    
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }
    /**
     * Calculate number of possible evaluations according to next posible number of evaluations
     * @param nextEvaluations next amonunt of evaluations wanting to perform
     * @return number of evaluations allowed to perfom
     */
    private int getPossibleEvaluations(int nextEvaluations)
    {
        return nextEvaluations + evaluations > maxEvaluations?maxEvaluations - evaluations:nextEvaluations;
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
            return s1;
        else
            return s2;
    }
    
    /**
     * Get the current population
     * @return 
     */
    public List<DoubleSolution> getPopulation() {
      return population;
    }
    
    public DoubleProblem getProblem() {
      return problem ;
    }

    public SubcomponentDoubleProblemDE getSubcomponent_problem_DE() {
        return subcomponent_problem_DE;
    }

    public SubcomponentDoubleProblemSaNSDE getSubcomponent_problem_SaNSDE() {
        return subcomponent_problem_SaNSDE;
    }

    public int getS() {
        return s;
    }

    public int getFE() {
        return FE;
    }

    public int getwFEs() {
        return wFEs;
    }

    public int getN() {
        return n;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public double getSubcomponent() {
        return subcomponent;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public SaNSDEBuilder getSansdeBuilder() {
        return sansdeBuilder;
    }

    public DEUnicaucaBuilder getDeFrobeniusBuilder() {
        return deFrobeniusBuilder;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public double getPenalize_value() {
        return penalize_value;
    }
    
    
    
    public void setProblem(DoubleProblem problem) {
      this.problem = problem ;
    }
    
    public void setPopulation(List<DoubleSolution> population) {
        this.population = population;
    }

    public void setFE(int FE) {
        this.FE = FE;
    }

    public void setwFEs(int wFEs) {
        this.wFEs = wFEs;
    }

    public void setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
    }

    public void setSansdeBuilder(SaNSDEBuilder sansdeBuilder) {
        this.sansdeBuilder = sansdeBuilder;
    }

    public void setDeFrobeniusBuilder(DEUnicaucaBuilder deFrobeniusBuilder) {
        this.deFrobeniusBuilder = deFrobeniusBuilder;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public void setPenalize_value(double penalize_value) {
        this.penalize_value = penalize_value;
    }

    public void setSubcomponent(double subcomponent) {
        this.subcomponent = subcomponent;
    }
    
}
