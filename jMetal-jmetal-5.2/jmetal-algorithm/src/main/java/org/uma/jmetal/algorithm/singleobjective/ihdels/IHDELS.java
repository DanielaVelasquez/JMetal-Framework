package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDE;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.util.LocalSearch;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class IHDELS <DoubleSolution,DoubleProblem>  implements Algorithm<DoubleSolution>
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
     * Frequency of updatig
     */
    protected int frec_ls;
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
     * Number of iterations before local search's probabilities are  updated
     */
    protected double frecLS;
    /**
     * Evolutionary algorithm builder
     */
    protected SaDEBuilder builder;
    /**
     * Evolutionary algorithm 
     */
    protected SaDE algorithm;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
            
    @Override
    public void run() 
    {
        /*algorithm =  builder.build();
        
        this.population = this.createInitialPopulation();
        DoubleSolution initial_solution = this.createInitialSolution();
        this.current_best = this.executeLocalSearches(initial_solution, population);
        
        this.best = (S) this.current_best.copy();
        int countLS = 0;
        
        while(!isStoppingConditionReached())
        {
            double previous = current_best.getObjective(0);
            algorithm.setPopulation(population);
            double improvement = previous - current_best.getObjective(0);
            LocalSearch ls = this.selectLocalSearch();
            current_best = (S) ls.evolve( current_best, population, problem, comparator);
            
            //FALTA
            if(countLS == frec_ls)
            {
                this.updateProbabities();
                countLS = 0;
            }
            
            current_best = this.getBest(best, current_best);
            
            if(restart())
            {
                this.current_best = this.getRandomIndividual(population);
            }
        }*/
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
    /*protected  DoubleSolution getBest(DoubleSolution s1, DoubleSolution s2)
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
     * Evaluates a population as maximun FE has not been reached
     * @param population 
     */
    /*protected void evaluatePopulation(List<DoubleSolution> population) {
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
            DoubleSolution solution = population.get(i);
            this.penalize(solution);
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
    /*protected DoubleSolution executeLocalSearches(DoubleSolution from, List<DoubleSolution> population)
    {
        DoubleSolution best = (DoubleSolution) from.copy();
        for(LocalSearch ls:local_searches)
        {
            DoubleSolution ans = (DoubleSolution) ls.evolve(from.copy(),this.clone(population), problem, comparator);
            best = getBest(best, ans);
        }
        return best;
    }
    /**
     * Clonated individuals from a population into another list
     * @param population population to clone
     * @return a copy of the population
     */
    /*protected List<S> clone(List<S> population)
    {
        List<S> copy = new ArrayList<>();
        for(S individual:population)
        {
            copy.add((S) individual.copy());
        }
        return copy;
    }
    /**
     * Each tecnique produces a subset of individuals according to its participation ratio
     * @return initial population
     */
    /*protected  List<DoubleSolution> createInitialPopulation() 
    {
        
    }
    /**
     * Determines if stopping condition was reached
     * @return true if stopping condition was reache, false otherwise
     */
    /*protected boolean isStoppingConditionReached(){
        
    }
    /**
     * Creates initial solution 
     * @return initial solution
     */
    protected DoubleSolution createInitialSolution(){
        return null;
    }
    /**
     * Apply a local search on an initial solution
     * @param initial_solution initial solution
     * @return improvement solution over initial solution
     */
    protected DoubleSolution applyLS(DoubleSolution initial_solution){
        return null;
    }
  
    /**
     * Update probabilities of local searchesd
     */
    protected void updateProbabities(){
        
    }
    /**
     * Define if local searches must be restarted
     * @return 
     */
    /*protected boolean restart(){
        
    }*/
    
}
