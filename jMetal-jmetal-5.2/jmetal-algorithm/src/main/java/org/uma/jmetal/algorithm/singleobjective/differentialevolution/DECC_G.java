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
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class DECC_G implements Algorithm
{
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
    private DoubleSolution random_inidividual;
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
    private DEFrobeniusBuilder deFrobeniusBuilder;
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
    @Override
    public String getName() {
       return "DECC-G";
    }

    @Override
    public String getDescription() {
        return "Large Scale Evolutionary Optimization Using "+
                "Cooperative Coevolution";
    }
    /**
     * Increments the number of individuals evaluated,
     * it is called every time one individual or several is evaluated
     * @param evaluations number evaluations performed
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
            this.updateProgress();
        }
        /**
         * If there are individual who were not evaluated, they are penalized
         */
        for(int j = i; j < populationSize; j++)
        {
            DoubleSolution solution = population.get(i);
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
            if(comparator.compare(best_individual, next) < 0)
            {
                best_individual = next;
                best_index = i;
            }
            else
            {
                //if next is worst than current worst
                if(comparator.compare(worst_individual, next) > 0)
                {
                    worst_individual = next;
                    worst_index = i;
                }
            }
        }
    }
    private void findIndividuals()
    {
        this.selectBestWorstIndividual(population);
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
//    private List<DoubleSolution> initWPopulation(int size, int variables)
//    {
//        List<DoubleSolution> s = new ArrayList<>();
//        for(int i = 0; i < size; i++)
//        {
//            DoubleSolutionSubcomponentDE solution = new DoubleSolutionSubcomponentDE(new double[1], new double[variables],subcomponent_problem_DE);
//            s.add(solution);
//        }
//        return s;
//    }
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
        {
            int value = this.maxEvaluations - evaluations;
            return value>0?value:0;
        }
        return nextNumberEvaluations;
    }
    
//    private void imprimir()
//    {
//        for(DoubleSolution s:population)
//        {
//            System.out.println(""+s.getObjective(0)+" "+s.getAttribute("B"));
//        }
//    }
    
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
               
               //ESTO DEBE CAMBIARSE PORQQUE AHORA SANSDE Y DE ESTAN POR CICLOS, PERO DESPUES SI VAN A SER POR EVALUAICONES
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
            
           evaluations += evaluationsToPerfom;
           
           DEFrobenius de = deFrobeniusBuilder
                                      .setMaxEvaluations(evaluationsToPerfom)
                                      .setProblem(subcomponent_problem_DE)
                                      .setPopulationSize(populationSize)
                                      .build();
           
           //de.setPopulation(w_population);
           this.chooseIndexWPopulation((int) Math.ceil(subcomponent));
           subcomponent_problem_DE.setSolution(best_individual);
           de.run();
           DoubleSolution ans = ((DoubleSolutionSubcomponentDE) de.getResult()).getCompleteSolution();
           ans = this.getBest(best_individual, ans);
           population.set(best_index, ans);
           
           
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
           ans = ((DoubleSolutionSubcomponentDE) de.getResult()).getCompleteSolution();
           ans = this.getBest(best_individual, ans);
           population.set(random_index, ans);
           
           evaluationsToPerfom = this.getPossibleEvaluations(wFEs);
               
            if(evaluationsToPerfom == 0) 
                continue;
            
            evaluations += evaluationsToPerfom;
           
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

    
       /**
     * Get the current population
     * @return 
     */
    public List<DoubleSolution> getPopulation() {
      return population;
    }

    public void setProblem(DoubleProblem problem) {
      this.problem = problem ;
    }
    public DoubleProblem getProblem() {
      return problem ;
    }
    
}
