package org.uma.jmetal.algorithm.singleobjective.mts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.util.SearchRange;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

//ISSUES
//Todos los individuos son iguales al principo

public abstract class AbstractMultipleTrajectorySearch <S extends Solution<?>,P extends Problem<S>>  implements Algorithm<S>
{
    /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    private int LOWER_BOUND_ROW = 0;
    
    private int SOA_DEFAULT_VALUE = -1;
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
    private int FE;
    /**
     * Number of evaluations made
     */
    private int evaluations;
    /**
     * Number of local searh test
     */
    private int local_search_test;
    /**
     * Number of local search
     */
    private int local_search;
    /**
     * Number of local search for the best
     */
    private int local_search_best;
    /**
     * Number of foreground to decide how many solutions are set to true
     */
    private int number_of_foreground;
    /**
     * Determines which individuals in population are enable
     */
    protected List<Boolean> enable;
    /**
     * Determines which individuals in population were improve
     */
    protected List<Boolean> improve;
    /**
     * Determines the search range for every individual
     */
    protected List<SearchRange> search_range;
    /**
     * Bonus 1 value
     */
    protected double bonus_1;
    /**
     * Bonus 2 value
     */
    protected double bonus_2;
    /**
     * Lower bound for variable a in local search 3
     */
    protected double lower_bound_a;
    /**
     * Upper bound for variable a in local search 3
     */
    protected double upper_bound_a;
    /**
     * Lower bound for variable b in local search 3
     */
    protected double lower_bound_b;
    /**
     * Upper bound for variable b in local search 3
     */
    protected double upper_bound_b;
    /**
     * Lower bound for variable c in local search 3
     */
    protected double lower_bound_c;
    /**
     * Upper bound for variable c in local search 3
     */
    protected double upper_bound_c;
    /**
     * Best individual on population
     */
    protected S best;
    /**
     * Best xi found on testing
     */
    protected S best_xi;
    /**
     * Value to set to solutions when evaluations are over
     */
    protected double penalize_value;

    
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    
    public AbstractMultipleTrajectorySearch(int populationSize,  P problem, Comparator<S> comparator, 
            int FE, int local_search_test, int local_search, int local_search_best, int number_of_foreground, 
            double bonus_1, double bonus_2, double lower_bound_a, double upper_bound_a, double lower_bound_b, 
            double upper_bound_b, double lower_bound_c, double upper_bound_c, double penalize_value) {
        this.populationSize = populationSize;
        this.problem = problem;
        this.comparator = comparator;
        this.FE = FE;
        this.local_search_test = local_search_test;
        this.local_search = local_search;
        this.local_search_best = local_search_best;
        this.number_of_foreground = number_of_foreground;
        this.bonus_1 = bonus_1;
        this.bonus_2 = bonus_2;
        this.lower_bound_a = lower_bound_a;
        this.upper_bound_a = upper_bound_a;
        this.lower_bound_b = lower_bound_b;
        this.upper_bound_b = upper_bound_b;
        this.lower_bound_c = lower_bound_c;
        this.upper_bound_c = upper_bound_c;
        this.penalize_value = penalize_value;
        
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
        
        this.best = getBest(this.population);
        this.enable = new ArrayList<>();
        this.improve = new ArrayList<>();
        this.search_range = new ArrayList<>();
        List<Double> grades = new ArrayList<>();
        for(int i = 0; i < populationSize; i++)
        {
            enable.add(Boolean.TRUE);
            improve.add(Boolean.TRUE);
            search_range.add(this.buildSearchRange(this.population.get(i)));
            grades.add((double)0);
        }
        while(!isStoppingConditionReached())
        {
            
            
            for(int i = 0; i < populationSize && !isStoppingConditionReached(); i++)
            {
                S xi = this.population.get(i);
                //TO-DO puedo ponerlo por defecto en -1???
                double grade_xi = -1;
                if(enable.get(i))
                {
                    grade_xi = 0;
                    
                    this.best_xi = xi;
                    
                    double LS1_test_grade = 0;
                    double LS2_test_grade = 0;
                    double LS3_test_grade = 0;
                    for(int j = 0 ; j < local_search_test && !isStoppingConditionReached(); j++)
                    {
                        LS1_test_grade += this.local_search_1((S) xi.copy(), i, true);
                        LS2_test_grade += this.local_search_2((S) xi.copy(), i, true);
                        LS3_test_grade += this.local_search_3((S) xi.copy(), i, true);
                    }
                    
                    xi = (S) best_xi.copy();
                    this.population.set(i, xi);
                    
                    List<Double> test_grades = new ArrayList<>();
                    test_grades.add(LS1_test_grade);
                    test_grades.add(LS2_test_grade);
                    test_grades.add(LS3_test_grade);
                    
                    //PONER EL MEJOR DE LOS TEST, REVISAR QUE NO ESTE EN LA POBLACION
                    
                    //TO-DO cómo se determina cual es el mejor?
                    int best_local_search = chooseBestLocalSearch(test_grades);
                    
                    for(int j = 0; j < local_search && !isStoppingConditionReached(); j++)
                    {
                        xi = this.population.get(i);
                        switch(best_local_search)
                        {
                            case 1:
                                grade_xi += local_search_1(xi,i,false);
                                break;
                            case 2:
                                grade_xi += local_search_2(xi,i,false);
                                break;
                            case 3:
                                grade_xi += local_search_3(xi,i,false);
                                break;
                        }
                        this.review_best();
                    }
                }
                //los grados se estan guardando de acuerdo a los individuos habilitados
                grades.set(i,grade_xi);
            }
            //Find the best solution
            //TO-DO ¿Qué pasa si el mejor no está en la población?? se da en el caso de que el mejor se encontrara en el testeo
            int best_index = this.getBestIndex(population);
            //DoubleSolution best_individual = this.population.get(best_index);
            //int best_search_range = search_range.get(best_index);
            for(int i = 0; i < local_search_best && !isStoppingConditionReached(); i++)
            {
                this.local_search_1(best,best_index,false);
                this.review_best();
            }
            
            for(int i = 0; i < populationSize && !isStoppingConditionReached(); i++)
            {
                enable.set(i, Boolean.FALSE);
            }
            this.chooseSolutionsToEnable(grades, enable);
        }
    }
    protected void review_best()
    {
        S best_population = this.getBest(population);
        this.best = this.getBest(best, best_population);
    }
    /**
     * Builds a simulated orthogonal array SOAmxn
     * @param m number of levels of each factor
     * @param n number of factors
     * @return a simulated orthogonal array
     */
    protected int[][] buildSOA(int m, int n) {
        int[][] SOA = new int[m][n];
        this.fillWith(SOA, SOA_DEFAULT_VALUE);
        
        List<Integer> C  = this.buildSequence(m);
        for(int i = 0; i < n; i++)
        {
            int aux = 0;
            for(int j = 0; j < m;j++)
            {
                int index = this.getIndexWhere(SOA_DEFAULT_VALUE, SOA, i);
                SOA[index][i] = C.get(aux);
                aux++;
                if(aux > m-1)
                    aux = 0;
            }
        }
        return SOA;
    }
    /**
     * Fills an array with an specific value
     * @param array array to fill
     * @param value value to set
     */
    protected void fillWith(int[][] array, int value)
    {
        int rows = array.length;
        int cols = array[0].length;
        for(int i = 0; i < rows;i++)
            for(int j = 0; j< cols;j++)
                array[i][j] = value;
    }
    /**
     * Builds a random permutation of 0,1,...m-1 
     * @param m max value of the sequence
     * @return a random sequence
     */
    protected List<Integer> buildSequence(int m)
    {
        List<Integer> C  = new ArrayList<>();
        while(C.size() != m)
        {
            int value = -1;
            do
            {
                value = randomGenerator.nextInt(LOWER_BOUND_ROW, m - 1);
            }while(C.contains(value));
            C.add(value);
        }
        return C;
    }
    /**
     * Finds row index where a column  in a array has an specfific value
     * @param value value to find
     * @param array collection where to look
     * @param col column where the row index will be selected
     * @return a row index where the array has the value
     */
    protected int getIndexWhere(int value, int[][] array, int col)
    {
        int row = -1;
        int rows = array.length - 1;
        do
        {
            row = randomGenerator.nextInt(LOWER_BOUND_ROW, rows);
        }while(array[row][col] != value);
        return row;
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
            S solution = population.get(i);
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
        return evaluations >= FE;
    }
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    protected S getBest(S s1, S s2)
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
    /**
     * Choose the best test values
     * @param values values of local searches grades, they must be order by localsearch
     * @return index of best local search
     */
    private int chooseBestLocalSearch(List<Double> values)
    {
        int best_index = 0;
        double best = values.get(best_index);
        int size = values.size();
        for(int i = 1; i< size; i++)
        {
            double value = values.get(i);
            if(value>best)
            {
                best = value;
                best_index = i;
            }
        }
        return best_index + 1;
    }
    /**
     * Choose number of foregorund individuals whose grade are best among
     * the population and set their corresponding enable to true
     * @param grades individual's grade
     * @param enable indivual's enable
     */
    private void chooseSolutionsToEnable(List<Double> grades, List<Boolean> enable)
    {
        
        List<Integer> index = new ArrayList<>();
        boolean full = false;
        while(index.size()<number_of_foreground && !full)
        {
            double value_best = -1;
            int best_index = -1;
            for(int i = 0; i< populationSize; i++)
            {
                double value = grades.get(i);
                if(!index.contains(i) && value>value_best)
                {
                    value_best = value;
                    best_index = i;
                }
            }
            if(best_index != -1)
                index.add(best_index);
            else
                full = true;
        }
        for(Integer i : index)
        {
            enable.set(i, Boolean.TRUE);
        }
    }
    protected S getBest(List<S> population)
    {
        S best = population.get(0);
        for(int i = 1; i < populationSize; i++)
        {
            S next = population.get(i);
            best = this.getBest(best, next);
            
        }
        return best;
    }
    protected int getBestIndex(List<S> population)
    {
        S best = population.get(0);
        for(int i = 1; i < populationSize; i++)
        {
            S next = population.get(i);
            int comparison = this.comparator.compare(best, next);
            if(comparison < 0)
            {
                best = next;
            }
            else if(comparison == 0)
            {
                try
                {
                    best = this.getBest(best, next);
                }catch(Exception e){}
            }
        }
        return this.population.indexOf(best);
    }


    @Override
    public S getResult() {
        try
        {
            for(S s: population)
            {
                if(best!=s)
                {
                    best = this.getBest(best, s);
                }
            }
        }
        catch(Exception e)
        {
            
        }
        return best;
        /*Collections.sort(this.population, comparator);
        return this.population.get(4);*/
        //return best;
    }
    /**
     * Determines if first individual is better than second individual
     * @param original original inidividual
     * @param modified modified individual
     * @return if original inididivual is better than modified individual
     */
    protected boolean isBetterOriginal(S original, S modified)
    {
        int comparison = comparator.compare(original, modified);
        if(comparison == 0)
        {
            try
            {
                double b_s1 = (double) original.getAttribute("B");
                double b_s2 = (double) modified.getAttribute("B");
                if(b_s1 <= b_s2)
                    return true;
                else
                    return false;
            }
            catch(Exception e)
            {
                return true;
            }
        }
        else if(comparison < 1)
            return true;
        else
            return false;
    }
        /**
     * Determines if an individual is already in a population, in other words
     * if there is another individual with the same values
     * @param population collection of individuals
     * @param individual one indivual
     * @return true if here is another individual with the same values, false otherwise
     */
    protected abstract boolean inPopulation(List<S> population, S individual);
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
     * Local search 2 
     * @param population population 
     * @param enable determines which individuals in population are enable
     * @param improve determines which individuals in population were improved
     * @param search_range search range for every indiviuals in population
     * @param k index to make local search
     * @return grade, how good was the local search
     */
    protected abstract double local_search_2(S xi, int index, boolean testing);
    /**
     * Local search 3
     * @param population population 
     * @param enable determines which individuals in population are enable
     * @param improve determines which individuals in population were improved
     * @param search_range search range for every indiviuals in population
     * @param k index to make local search
     * @return grade, how good was the local search
     */
    protected abstract double local_search_3(S xi, int index, boolean testing);
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
        return FE;
    }

    public void setCycles(int cycles) {
        this.FE = cycles;
    }

    public int getLocal_search_test() {
        return local_search_test;
    }

    public void setLocal_search_test(int local_search_test) {
        this.local_search_test = local_search_test;
    }

    public int getLocal_search() {
        return local_search;
    }

    public void setLocal_search(int local_search) {
        this.local_search = local_search;
    }

    public int getLocal_search_best() {
        return local_search_best;
    }

    public void setLocal_search_best(int local_search_best) {
        this.local_search_best = local_search_best;
    }

    public int getNumber_of_foreground() {
        return number_of_foreground;
    }

    public void setNumber_of_foreground(int number_of_foreground) {
        this.number_of_foreground = number_of_foreground;
    }

    public double getBonus_1() {
        return bonus_1;
    }

    public void setBonus_1(double bonus_1) {
        this.bonus_1 = bonus_1;
    }

    public double getBonus_2() {
        return bonus_2;
    }

    public void setBonus_2(double bonus_2) {
        this.bonus_2 = bonus_2;
    }

    public double getLower_bound_a() {
        return lower_bound_a;
    }

    public void setLower_bound_a(double lower_bound_a) {
        this.lower_bound_a = lower_bound_a;
    }

    public double getUpper_bound_a() {
        return upper_bound_a;
    }

    public void setUpper_bound_a(double upper_bound_a) {
        this.upper_bound_a = upper_bound_a;
    }

    public double getLower_bound_b() {
        return lower_bound_b;
    }

    public void setLower_bound_b(double lower_bound_b) {
        this.lower_bound_b = lower_bound_b;
    }

    public double getUpper_bound_b() {
        return upper_bound_b;
    }

    public void setUpper_bound_b(double upper_bound_b) {
        this.upper_bound_b = upper_bound_b;
    }

    public double getLower_bound_c() {
        return lower_bound_c;
    }

    public void setLower_bound_c(double lower_bound_c) {
        this.lower_bound_c = lower_bound_c;
    }

    public double getUpper_bound_c() {
        return upper_bound_c;
    }

    public void setUpper_bound_c(double upper_bound_c) {
        this.upper_bound_c = upper_bound_c;
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
    
    
}
