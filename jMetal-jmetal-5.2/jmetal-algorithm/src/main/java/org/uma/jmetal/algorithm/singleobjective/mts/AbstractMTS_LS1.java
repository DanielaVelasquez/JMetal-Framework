package org.uma.jmetal.algorithm.singleobjective.mts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.search_range.SearchRange;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.Util;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

//ISSUES
//Todos los individuos son iguales al principo

public abstract class AbstractMTS_LS1 <S extends Solution<?>,P extends Problem<S>>  implements Algorithm<S>
{
    /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    private final int SOA_DEFAULT_VALUE = -1;
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Number of solutions/Number of levels of each factor
     */
    protected int populationSize;
    /**
     * Number of factors
     */
    protected int n;
    /**
     * Random generator
     */
    protected JMetalRandom randomGenerator ;
    /**
     * Individiuals on population
     */
    protected List<S> population;
    /**
     * Individiuals produced on algorithm
     */
    protected List<S> offspring_population;
     /**
     * Problem to solve
     */
    protected P problem ;
    /**
     * Determines how a solution should be order
     */
    private Comparator<S> comparator;
    /**
     * Maximun number of function evaluations
     */
    private int maxEvaluations;
    /**
     * Number of evaluations made
     */
    private int evaluations;
    
    /**
     * Determines which individuals in population were improve
     */
    protected List<Boolean> improve;
    /**
     * Determines the search range for every individual
     */
    protected List<SearchRange> search_range;
    
    /**
     * Best individual on population
     */
    protected S best;
    
    /**
     * Value to set to solutions when evaluations are over
     */
    protected double penalize_value;

    /**
     * Bonus 1 value
     */
    protected double bonus_1;
    /**
     * Bonus 2 value
     */
    protected double bonus_2;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    public AbstractMTS_LS1(int populationSize,  P problem,
            Comparator<S> comparator,int FE,   double penalize_value,
            double bonus_1, double bonus_2) {
        this.populationSize = populationSize;
        this.problem = problem;
        this.comparator = comparator;
        this.maxEvaluations = FE;
        this.penalize_value = penalize_value;
        this.bonus_1 = bonus_1;
        this.bonus_2 = bonus_2;
        this.n = this.problem.getNumberOfVariables();
        this.randomGenerator = JMetalRandom.getInstance();
        
    }



    @Override
    public void run() {
        this.evaluations = 0;
        this.offspring_population = new ArrayList<S>();
        //TO-DO ¿Cómo hacer que m sea mútiplo de n? n es el número de genes
        //TO-DO Si está utilizando MOS, la población se la debe pasar
        //¿así para evitar la creación de una población inicial?
        this.n = this.problem.getNumberOfVariables();
        if(this.population == null || this.population.size() < this.populationSize)
        {
            
            if(population != null &&  !population.isEmpty())
            {
                int size = this.populationSize - this.population.size();
                int[][]SOA = this.buildSOA(size, n);
                this.population.addAll(this.generateInitialSolutions(SOA, size));
                
            }
            else
            {
                int[][]SOA = this.buildSOA(populationSize, n);
                this.population = this.generateInitialSolutions(SOA, populationSize); 
            }
        }
        
        this.best = getBest(this.population, best);
        this.improve = new ArrayList<>();
        this.search_range = new ArrayList<>();
        List<Double> grades = new ArrayList<>();
        for(int i = 0; i < populationSize; i++)
        {
            improve.add(Boolean.TRUE);
            search_range.add(this.buildSearchRange(this.population.get(i)));
            grades.add((double)0);
        }
        while(!isStoppingConditionReached())
        {
            for(int i = 0; i < populationSize && !isStoppingConditionReached(); i++)
            {
                S xi = this.population.get(i);
                local_search_1(xi,i,false);
            }
        }
    }
   
    protected int[][] buildSOA(int m, int n) {
        int[][] SOA = new int[m][n];
        Util.fillArrayWith(SOA, SOA_DEFAULT_VALUE);
        
        List<Integer> C  = Util.createRandomPermutation(m);
        for(int i = 0; i < n; i++)
        {
            int aux = 0;
            for(int j = 0; j < m;j++)
            {
                int index = Util.getIndexWhere(SOA_DEFAULT_VALUE, SOA, i);
                SOA[index][i] = C.get(aux);
                aux++;
                if(aux > m-1)
                    aux = 0;
            }
        }
        return SOA;
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
            //long initTime = System.currentTimeMillis();
            this.problem.evaluate(solution);
            //System.out.println("Time: "+(System.currentTimeMillis() - initTime));
            this.offspring_population.add((S) solution.copy());
            i++;
            this.updateProgress();
        }
        for(int j = i; j < populationSize; j++)
        {
            S solution = population.get(j);
            this.penalize(solution);
        }
    }
    
    /**
     * Evaluates an individual
     * @param solution 
     */
    protected void evaluate(S solution)
    {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(solution);
            this.offspring_population.add((S)solution.copy());
            this.updateProgress();
        }
        else
        {
            this.penalize(solution);
        }
    }
    /**
     * Increments the number of generations evaluated
     */
    protected void updateProgress() {
        evaluations += 1;
    }
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
        private boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    private S getBest(S s1, S s2)
    {
        int comparison = comparator.compare(s1, s2);
        if(comparison <= 0)
            return s1;
        else
            return s2;
    }
    /**
     * Determines if a solution improve the best solution in population
     * @param s solution to know if improve the best
     * @return true if s improve the best solution, false otherwise
     */
    protected boolean improveBest(S s)
    {
        return this.getBest(best, s) == s;
    }
    /**
     * Determines if a modified individual has a decresead evaluation compared to its orifinal
     * @param original original individual
     * @param modified modified individual
     * @return true if the modified individual has decresead its evaluation, false otherwise
     */
    protected boolean functionValueDegenerates(S original, S modified)
    {
        return this.getBest(original, modified ) == original;
    }
    
    protected S getBest(List<S> population, S best)
    {
        int index = 0;
    
        if(best == null)
        {
            best = population.get(0);
            index = 1;
        }
        
        for(int i = index; i < populationSize; i++)
        {
            S next = population.get(i);
            best = this.getBest(best, next);
            
        }
        return best;
    }
    

    @Override
    public S getResult()
    {
        for(S s: population)
        {
            best = this.getBest(best, s);
        }
        return best;
        
    }
    /**
     * Determines if first individual is better than second individual
     * @param original original inidividual
     * @param modified modified individual
     * @return if original inididivual is better than modified individual
     */
    protected boolean isBetterOriginal(S original, S modified)
    {
        return getBest(original, modified) == original;
    }
    
    /**
     * Creates an initial random population
     * @return initial population with random values
     */
    protected abstract List<S> createInitialPopulation(int size);
    /**
     * Generates an initial solution
     * @param SOA simulated orthogonal array to build the population
     * @return population build from SOA
     */
    protected abstract List<S> generateInitialSolutions(int[][]SOA, int populationSize);
    /**
     * Local search 1 
     * @param xi inidividual to make local search on 
     * @param index index to make local search
     * @param testing determins if the local search is testing or not
     * @return grade, how good was the local search
     */
    protected abstract double local_search_1(S xi, int index, boolean testing);
   
    /**
     * Build the search range for solution and return it
     * @param solution solution to create search range
     * @return search range to solution
     */
    protected abstract SearchRange buildSearchRange(S solution);
    /**
     * Sets an individual function value to a penalization value given by 
     * the user
     * @param solution solution to penalize with a bad function value
     */
    protected void penalize(S solution){
        solution.setObjective(0, this.penalize_value);
    }
    /**
     * Determines if an individual is already in a population, in other words
     * if there is another individual with the same values
     * @param population collection of individuals
     * @param individual one indivual
     * @return true if here is another individual with the same values, false otherwise
     */
    protected abstract boolean inPopulation(List<S> population, S individual);
    @Override
    public abstract String getName();

    @Override
    public abstract String getDescription();
    
    
    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public P getProblem() {
        return problem;
    }

    public void setProblem(P problem) {
        this.problem = problem;
    }

    public Comparator<S> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<S> comparator) {
        this.comparator = comparator;
    }

    public int getCycles() {
        return maxEvaluations;
    }

    public void setCycles(int cycles) {
        this.maxEvaluations = cycles;
    }

    

    public void setPopulation(List<S> population) {
        this.population = population;
    }

    public List<S> getPopulation() {
        return population;
    }

    public List<S> getOffspringPopulation() {
        return offspring_population;
    }

    public int getEvaluations() {
        return evaluations;
    }
    
    

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public void setEvaluations(int evaluations) {
        this.evaluations = evaluations;
    }

    public void setBest(S best) {
        this.best = best;
    }
    
    
}
