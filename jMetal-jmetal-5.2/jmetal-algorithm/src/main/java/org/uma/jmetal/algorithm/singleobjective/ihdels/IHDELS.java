package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDE;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class IHDELS implements Algorithm<DoubleSolution>
{
    /**
     * ------------------------------------------------------------------------
     * Based on
     * http://simd.albacete.org/actascaepia15/papers/00251.pdf ****
     * D. Molina and F. Herrera, “Hibridación iterativa de DE con búsqueda 
     * local con reinicio para problemas de alta dimensionalidad,” 
     * Actas de la XVI Conferencia CAEPIA., 2015.
     * -------------------------------------------------------------------------
     */
    
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
               .setPenalizeValue(penalize_value)
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
            
            double improvement;
            double newFitness = current_best.getObjective(0);
            if(lastFitness != 0)
                 improvement= Math.abs(lastFitness - newFitness)/lastFitness;
            else
                improvement = Math.abs(lastFitness - newFitness);
            
            //DE population is restarted because solution was not improved
            if(improvement == 0)
            {
                this.population = this.createInitialPopulation();
                this.evaluatePopulation(population);
            }
            
            LocalSearch ls = this.runLS();
            this.best = getBest(best, current_best);
            
            //Local search is restarted because soution was not improved
            if(ls.getRatio() == 0)
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
            else 
                countLS = 0;
            
            current_best = (DoubleSolution) this.getBest(best, current_best).copy();
        }
    }

    @Override
    public DoubleSolution getResult() {
        return best;
    }

    @Override
    public String getName() {
        return "Iterative Hybrid DE with local search";
    }

    @Override
    public String getDescription() {
        return "It is an iterative algorithm that during the run it applies "+
                "iteratively a DE and a local search method, exploring all the "+
                "variables at the same time";
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
        this.updateProgress(evaluations);
        algorithm.setPopulation(population);
        algorithm.setBest(current_best);
        algorithm.setMaxEvaluations(evaluations);
        algorithm.run();
        current_best = algorithm.getResult();
        population = algorithm.getPopulation();
    }
    /**
     * Update number of evaluations performed
     */
    protected void updateProgress(int evals)
    {
        this.evaluations += evals;
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
            this.updateProgress(1);
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
            this.updateProgress(1);
        }
        else
        {
            this.penalize(individual);
        }
    }
    protected void penalize(DoubleSolution solution){
        solution.setObjective(0, this.penalize_value);
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
            this.updateProgress(evaluations);
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
     * Individuals  are created randomly
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
     * Execute a local search
     * @return return the local search executed
     */
    private LocalSearch runLS()
    {
        int evaluations = this.getPossibleEvaluations(FE_LS);
        this.updateProgress(evaluations);
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
            double value = best.getVariableValue(i) + randomGenerator.nextDouble(-0.05, 0.05) * 0.1 * (b - a);
            if(value >= problem.getLowerBound(i) && value <= problem.getUpperBound(i))
                current_best.setVariableValue(i, value);
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

    public List<DoubleSolution> getPopulation() {
        return population;
    }

    public DoubleSolution getBest() {
        return best;
    }

    public DoubleSolution getCurrent_best() {
        return current_best;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public List<LocalSearch> getLocal_searches() {
        return local_searches;
    }

    public int getNumberLS() {
        return numberLS;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public double getPenalize_value() {
        return penalize_value;
    }

    public int getReStart() {
        return reStart;
    }

    public int getPopulation_size() {
        return population_size;
    }

    public int getFE_DE() {
        return FE_DE;
    }

    public int getFE_LS() {
        return FE_LS;
    }

    public SaDEBuilder getSADEbuilder() {
        return SADEbuilder;
    }

    public int getM() {
        return m;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getA() {
        return a;
    }

    public void setPopulation(List<DoubleSolution> population) {
        this.population = population;
    }

    public void setEvaluations(int evaluations) {
        this.evaluations = evaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public void setLocal_searches(List<LocalSearch> local_searches) {
        this.local_searches = local_searches;
    }

    public void setProblem(DoubleProblem problem) {
        this.problem = problem;
    }

    public void setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
    }

    public void setPenalize_value(double penalize_value) {
        this.penalize_value = penalize_value;
    }

    public void setReStart(int reStart) {
        this.reStart = reStart;
    }

    public void setFE_DE(int FE_DE) {
        this.FE_DE = FE_DE;
    }

    public void setFE_LS(int FE_LS) {
        this.FE_LS = FE_LS;
    }

    public void setSADEbuilder(SaDEBuilder SADEbuilder) {
        this.SADEbuilder = SADEbuilder;
    }

    public void setAlgorithm(SaDE algorithm) {
        this.algorithm = algorithm;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }
    
    
    
}
