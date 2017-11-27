package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDE;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.util.LocalSearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class IHDELS implements Algorithm<DoubleSolution>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Population on the algorithm
     */
    private List<DoubleSolution> population;
    /**
     * Best solution found during execution
     */
    private DoubleSolution best;
    /**
     * Current best solution at current generation
     */
    private DoubleSolution current_best;
    /**
     * Total evaluations performed during execution
     */
    private int evaluations;
    /**
     * Maximun number of evaluations
     */
    private int maxEvaluations;
    /**
     * Local searches 
     */
    private List<LocalSearch> local_searches;
    /**
     * Number of local searches 
     */
    private int numberLS;
    /**
     * Problem to solve
     */
    protected DoubleProblem problem ;
     /**
     * Determines how a solution should be order
     */
    protected Comparator<DoubleSolution> comparator;
    /**
     * Value to penalize a solution in case evaluations are over
     */
    protected double penalize_value;
    /**
     * Number of executions without improvement
     */
    protected int reStart;
    /**
     * Population size
     */
    protected int population_size;
    /**
     * Random generator
     */
    protected JMetalRandom randomGenerator ;
    /**
     * Number of evaluations for DE
     */
    protected int FE_DE;
    /**
     * Number of evaluations for a local search
     */
    protected int FE_LS;
    /**
     * Evolutionary algorithm builder
     */
    protected SaDEBuilder SADEbuilder;
    /**
     * Evolutionary algorithm 
     */
    protected SaDE algorithm;
    /**
     * Number of variables in a solution
     */
    protected int m;
    /**
     * Threshold use to identify when an improvement is enough
     */
    private double threshold;
    /**
     * Search domain first value
     */
    private double a;
    /**
     * Search domain last value
     */
    private double b;
    
    public IHDELS(int maxEvaluations, List<LocalSearch> local_searches, 
            DoubleProblem problem, Comparator<DoubleSolution> comparator, 
            double penalize_value, int reStart, int population_size, int FE_DE,
            int FE_LS, SaDEBuilder builder, double threshold, double a, double b) 
    {
        this.maxEvaluations = maxEvaluations;
        this.local_searches =  local_searches;
        this.problem = problem;
        this.comparator = comparator;
        this.penalize_value = penalize_value;
        this.reStart = reStart;
        this.population_size =  population_size;
        this.FE_DE = FE_DE;
        this.FE_LS = FE_LS;
        this.SADEbuilder = builder;
        this.threshold = threshold;
        this.a = a;
        this.b = b;
        this.randomGenerator = JMetalRandom.getInstance();
    }

    @Override
    public void run() {
        
        
        
        this.m = this.problem.getNumberOfVariables();
        this.numberLS = this.local_searches.size();
        //Creates SaDE algorithm
        SADEbuilder.setComparator(comparator)
               .setPenalize_value(penalize_value)
               .setPopulationSize(population_size);
        algorithm =  SADEbuilder.build();
        
        //Create random initial population
        this.population = this.createInitialPopulation();
        this.evaluatePopulation(population);
        
        //Create initial solution
        DoubleSolution initial_solution = this.createInitialSolution();
        this.evaluate(initial_solution);
        
        
        this.current_best = this.executeLocalSearches(initial_solution, population);
        
        this.best =  (DoubleSolution) this.current_best.copy();
        int countLS = 0;
        
        while(!isStoppingConditionReached())
        {
            double lastFitness = this.current_best.getObjective(0);
            
            this.runSaDE();
            this.best = getBest(best, current_best);
            
            double improvement = (current_best.getObjective(0) - lastFitness)/lastFitness;
            
            //DE population is restarted because solution was not improved
            if(improvement == 0)
            {
                this.population = this.createInitialPopulation();
                this.evaluatePopulation(population);
            }
            
            lastFitness = this.current_best.getObjective(0);
            LocalSearch ls = this.runLS();
            this.best = getBest(best, current_best);
            improvement = (current_best.getObjective(0) - lastFitness)/lastFitness;
            
            
            //Local search is restarted because soution was not improved
            if(improvement == 0)
            {
                ls.restart();
            }
            
            if(ls.getRatio()<threshold)
            {
                countLS++;
                if(countLS == reStart)
                {
                    this.restartAlgorithm();
                    countLS = 0;
                }
            }
            
            current_best = this.getBest(best, current_best);
            
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
     * Run SaDE algorithm
     */
    protected void runSaDE()
    {
        int evaluations =  this.getPossibleEvaluations(FE_DE);
        algorithm.setPopulation(population);
        algorithm.setBest(current_best);
        algorithm.setMaxEvaluations(evaluations);
        algorithm.run();
        current_best = algorithm.getResult();
    }
    /**
     * Update number of evaluations performed
     */
    protected void updateProgress()
    {
        this.evaluations += 1;
    }
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    protected  DoubleSolution getBest(DoubleSolution s1, DoubleSolution s2)
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
        else if(comparison < 1)
            return s1;
        else
            return s2;
    }
    /**
     * Evaluates a population while maximun FE has not been reached
     * @param population 
     */
    protected void evaluatePopulation(List<DoubleSolution> population) {
        int i = 0;
        int populationSize = population.size();
        while(!isStoppingConditionReached() && i < populationSize)
        {
            DoubleSolution solution = population.get(i);
            this.problem.evaluate(solution);
            i++;
            this.updateProgress();
        }
        for(int j = i; j < populationSize; j++)
        {
            DoubleSolution solution = population.get(j);
            this.penalize(solution);
        }
    }
    /**
     * Evaluates an individual as long as max evaluations hasnot been reache
     * @param individual solution to evaluate
     */
    protected void evaluate(DoubleSolution individual)
    {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(individual);
            this.updateProgress();
        }
        else
        {
            this.penalize(individual);
        }
    }
    protected void penalize(DoubleSolution solution){
        solution.setObjective(0, this.penalize_value);
    }
    protected DoubleSolution getRandomIndividual(List<DoubleSolution> population)
    {
        DoubleSolution individual = null;
        do
        {
            int index = randomGenerator.nextInt(0, population_size);
            individual = population.get(index);
        }while(comparator.compare(current_best, individual) == 0); //TO-DO si funcioan asi???
        return individual;
    }
    /**
     * Execute all local searches, all LS executes as inital params
     * individual S and population
     * @param from first individual
     * @param population population to start 
     * @return Best individual found during execution of local searches
     */
    protected DoubleSolution executeLocalSearches(DoubleSolution from, List<DoubleSolution> population)
    {
        DoubleSolution best = (DoubleSolution) from.copy();
        for(LocalSearch ls:local_searches)
        {
            int evaluations = this.getPossibleEvaluations(FE_LS);
            if(evaluations == 0)
                continue;
            DoubleSolution ans = (DoubleSolution) ls.evolve(from.copy(),this.clone(population), problem, comparator,evaluations);
            best = getBest(best, ans);
        }
        return best;
    }
    /**
     * Clonated individuals from a population into another list
     * @param population population to clone
     * @return a copy of the population
     */
    protected List<DoubleSolution> clone(List<DoubleSolution> population)
    {
        List<DoubleSolution> copy = new ArrayList<>();
        for(DoubleSolution individual:population)
        {
            copy.add((DoubleSolution) individual.copy());
        }
        return copy;
    }
    /**
     * Each tecnique produces a subset of individuals according to its participation ratio
     * @return initial population
     */
    protected  List<DoubleSolution> createInitialPopulation() 
    {
        List<DoubleSolution> population =  new ArrayList<>();
        for(int i = 0; i < population_size; i++)
        {
            population.add(problem.createSolution());
        }
        return population;
    }
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }
    /**
     * Creates initial solution where every variable is made by (upper + lower)/2
     * restrictions
     * @return initial solution
     */
    protected DoubleSolution createInitialSolution(){
        DoubleSolution individual = problem.createSolution();
        for(int i = 0; i < this.m; i++)
        {
            individual.setVariableValue(i, (individual.getUpperBound(i) + individual.getLowerBound(i))/2);
        }
        return individual;
    }
    protected LocalSearch selectLocalSearch()
    {
        LocalSearch ls = this.local_searches.get(0);
        double ratioBest = ls.getRatio();
        for(int i  = 1; i < this.numberLS; i++)
        {
            LocalSearch next = this.local_searches.get(i);
            double ratioNext = next.getRatio();
            
            if(ratioNext > ratioBest)
            {
                ratioBest = ratioNext;
                ls = next;
            }
        }
        return ls;
    }
  
    /**
     * Update probabilities of local searchesd
     */
    protected void updateProbabities(){
        
    }
    /**
     * Execute a local search
     * @return return the local search executed
     */
    private LocalSearch runLS()
    {
        int evaluations = this.getPossibleEvaluations(FE_LS);
        LocalSearch ls = this.selectLocalSearch();
        current_best = (DoubleSolution) ls.evolve( current_best, population, problem, comparator,evaluations);
        return ls;
    }
    /**
     * Restart all algorith, it changes current best and restar all
     * local searches to its original params
     */
    private void restartAlgorithm()
    {
        this.changeCurrentBest();
        
        this.population = this.createInitialPopulation();
        this.evaluatePopulation(population);
        
        this.reStartLocalSearches();
        
        
    }
    /**
     * Change currente best according to the following equation
     * current_best[i] = best[i] + rand(-0.05, 0.05) * 0.1 * (b-a)
     * where b and a are the search domain
     */
    private void changeCurrentBest()
    {
        for(int i = 0; i < m; i++)
        {
            current_best.setVariableValue(i, best.getVariableValue(i) + randomGenerator.nextDouble(-0.05, 0.05) * 0.1 * (b - a));
        }
        this.evaluate(current_best);
    }
    /**
     * Restar all local searches
     */
    private void reStartLocalSearches()
    {
        for(LocalSearch ls:local_searches)
        {
            ls.restart();
        }
    }
    
}
