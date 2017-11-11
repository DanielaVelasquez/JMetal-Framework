package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.util.LocalSearch;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public abstract class AbstractIHDELS <S extends Solution<?>,P extends Problem<S>>  implements Algorithm<S>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Population on the algorithm
     */
    private List<S> population;
    /**
     * Best solution found during execution
     */
    private S best;
    /**
     * Current best solution at current generation
     */
    private S current_best;
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
    protected Problem<S> problem ;
     /**
     * Determines how a solution should be order
     */
    protected Comparator<S> comparator;
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
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
            
    @Override
    public void run() {
        this.population = this.createInitialPopulation();
        S initial_solution = this.createInitialSolution();
        this.current_best = this.executeLocalSearches(initial_solution, population);
        
        this.best = (S) this.current_best.copy();
        int countLS = 0;
        while(!isStoppingConditionReached())
        {
            double previous = current_best.getObjective(0);
            //APLICA SADE current_best = SaDE()
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
        }
    }

    @Override
    public S getResult() {
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
    protected  S getBest(S s1, S s2)
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
    protected void evaluatePopulation(List<S> population) {
        int i = 0;
        int populationSize = population.size();
        while(!isStoppingConditionReached() && i < populationSize)
        {
            S solution = population.get(i);
            this.problem.evaluate(solution);
            i++;
            this.updateProgress();
        }
        for(int j = i; j < populationSize; j++)
        {
            S solution = population.get(i);
            this.penalize(solution);
        }
    }
    protected void penalize(S solution){
        solution.setObjective(0, this.penalize_value);
    }
    protected S getRandomIndividual(List<S> population)
    {
        S individual = null;
        do
        {
            int index = randomGenerator.nextInt(0, population_size);
            individual = population.get(index);
        }while(comparator.compare(current_best, individual) == 0); //TO-DO si funcioan asi???
        return individual;
    }
    /**
     * Each tecnique produces a subset of individuals according to its participation ratio
     * @return initial population
     */
    protected abstract  List<S> createInitialPopulation() ;
    /**
     * Determines if stopping condition was reached
     * @return true if stopping condition was reache, false otherwise
     */
    protected abstract boolean isStoppingConditionReached();
    /**
     * Creates initial solution 
     * @return initial solution
     */
    protected abstract S createInitialSolution();
    /**
     * Apply a local search on an initial solution
     * @param initial_solution initial solution
     * @return improvement solution over initial solution
     */
    protected abstract S applyLS(S initial_solution);
    /**
     * Select local search using local search probability
     * @return local search to apply
     */
    protected abstract LocalSearch selectLocalSearch();
    /**
     * Update probabilities of local searchesd
     */
    protected abstract void updateProbabities();
    /**
     * Define if local searches must be restarted
     * @return 
     */
    protected abstract boolean restart();
    /**
     * Execute all local searches, all LS executes as inital params
     * individual S and population
     * @param from first individual
     * @param population population to start 
     * @return Best individual 
     */
    protected abstract S executeLocalSearches(S from, List<S> population);
}
