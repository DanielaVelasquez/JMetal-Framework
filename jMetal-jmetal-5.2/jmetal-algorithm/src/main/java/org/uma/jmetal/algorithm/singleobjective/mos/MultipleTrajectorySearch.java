package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class MultipleTrajectorySearch implements Algorithm
{
     /**-----------------------------------------------------------------------------------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    private int LOWER_BOUND = 0; //Este ya se usa para otra cosa diferenet que en el algoritmo
    
    private int UPPER_BOUND = 1;
    
    private int SOA_DEFAULT_VALUE = -1;
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Number of solutions/Number of levels of each factor
     */
    private int populationSize;
    /**
     * Number of factors
     */
    private int n;
    /**
     * Random generator
     */
    private JMetalRandom randomGenerator ;
    /**
     * Individiuals
     */
    private List<DoubleSolution> population;
    /**
     * Problem to solve
     */
    private DoubleProblem problem ;
    /**
     * It evaluates the population
     */
    private SolutionListEvaluator<DoubleSolution> evaluator;
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    /**
     * Maximun number of generations
     */
    private int maxGenerations;
    /**
     * Number of generations made
     */
    private int generations;
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
    
    private List<Boolean> enable;
    private List<Boolean> improve;
    private List<Double> search_range;
    
    private double bonus_1;
    private double bonus_2;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    /**
     * Builds a simulated orthogonal array SOAmxn
     * @param m number of levels of each factor
     * @param n number of factors
     * @return a simulated orthogonal array
     */
    private int[][] buildSOA(int m, int n)
    {
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
                if(aux>m-1)
                    aux = 0;
            }
        }
        return SOA;
    }
    /**
     * Finds row index where a column  in a array has an specfific value
     * @param value value to find
     * @param array collection where to look
     * @param col column where the row index will be selected
     * @return a row index where the array has the value
     */
    private int getIndexWhere(int value, int[][] array, int col)
    {
        int row = -1;
        int rows = array.length;
        do
        {
            row = randomGenerator.nextInt(LOWER_BOUND, rows);
        }while(array[row][col] != value);
        return row;
    }
    /**
     * Fills an array with an specific value
     * @param array array to fill
     * @param value value to set
     */
    private void fillWith(int[][] array, int value)
    {
        int rows = array.length;
        int cols = array[0].length;
        for(int i = 0; i < rows;i++)
            for(int j = 0; j< cols;j++)
                array[i][j] = value;
    }
    /**
     * Builds a random permutation of 0,1,...m-1 
     * @param m
     * @return 
     */
    private List<Integer> buildSequence(int m)
    {
        List<Integer> C  = new ArrayList<>();
        while(C.size() != m)
        {
            int value = -1;
            do
            {
                value = randomGenerator.nextInt(LOWER_BOUND, m);
            }while(C.contains(value));
            C.add(value);
        }
        return C;
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
     * Generates an initial solution
     * @param SOA simulated orthogonal array to build the population
     * @return population build from SOA
     */
    private List<DoubleSolution> generateInitialSolutions(int[][]SOA)
    {
        List<DoubleSolution> p = this.createInitialPopulation();
        for(int i = 0; i < populationSize; i++)
        {
            DoubleSolution individual = p.get(i);
            for(int j = 0; j < n; j++)
            {
                double value = individual.getLowerBound(j) + ((individual.getUpperBound(j) -  individual.getLowerBound(j)) * SOA[i][j])/(populationSize - 1);
                individual.setVariableValue(j, value);
            }
        }
        return p;
    }
    private List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
        this.updateProgress();
       return evaluator.evaluate(population, getProblem());
    }
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached() {
        return generations > maxGenerations;
    }
    /**
     * Increments the number of generations evaluated
     */
    private void updateProgress() {
        generations += 1;
    }
    private DoubleSolution getBestBetween(DoubleSolution s1, DoubleSolution s2)
    {
        List<DoubleSolution> aux = new ArrayList<>();
        aux.add(s1);
        aux.add(s2);
        Collections.sort(aux, comparator);
        return aux.get(0);
    }
    /**
     * Determines if a solution improve the best solution in population
     * @param s solution to know if improve the best
     * @return true if s improve the best solution, false otherwise
     */
    private boolean improveBest(DoubleSolution s)
    {
        int best_index = this.getIndexBest(this.population);
        DoubleSolution best = this.population.get(best_index);
        
        return this.getBestBetween(best, s) == s;
    }
    /**
     * Determines wether a solution who has been changed has degenerate or improved
     * @param s solution to study
     * @param variable_changed variable changed in solution
     * @param variable value on the variable changed
     * @param objective original value on its objective
     * @return true if s has improved because of the change, false if s has not improve
     */
    private boolean functionValueDegenerates(DoubleSolution s, int variable_changed, double variable, double objective)
    {
        DoubleSolution original = (DoubleSolution) s.copy();
        original.setObjective(0, objective);
        original.setAttribute(variable_changed, variable);
        
        return this.getBestBetween(original, s) == original;
    }
    /**
     * Determines if a modified individual has a decresead evaluation compared to its orifinal
     * @param original original individual
     * @param modified modified individual
     * @return true if the modified individual has decresead its evaluation, false otherwise
     */
    private boolean functionValueDegenerates(DoubleSolution original, DoubleSolution modified)
    {
        return this.getBestBetween(original, modified )== original;
    }
    private double local_search_1(int index)
    {
        double SR = this.search_range.get(index);
        boolean improve_i = this.improve.get(index);
        DoubleSolution individual = this.population.get(index);
        
        int best_index = this.getIndexBest(this.population);
        DoubleSolution best = this.population.get(best_index);
        double objective_best = best.getObjective(0);
        
        double grade = 0;
        
        if(!improve_i)
        {
            SR = SR / 2;
            if(SR < Math.pow(10, -15));
            {
                SR = (UPPER_BOUND - LOWER_BOUND) * 0.4;
            }
        }
        improve_i = false;
        for(int i = 0; i < n; i++)
        {
            double original_value = individual.getVariableValue(i);
            double original_objective = individual.getObjective(0);
            individual.setVariableValue(i, original_value - SR);
            this.problem.evaluate(individual);
            
            double new_objective = individual.getObjective(0);
            //Individual improve best
            //TO-DO ¿Se busca que sea mejor que el mejor de la poblacion o que se mejor que lo que estaba?
            if(this.improveBest(individual))
            {
                grade = grade + bonus_1;
            }
            
            //Individual gets same value than best individual
            if(new_objective == objective_best)
            {
                individual.setObjective(0, original_objective);
                individual.setAttribute(i, original_value);
            }
            else
            {
                if(this.functionValueDegenerates(individual, i, original_value, original_objective))
                {
                   individual.setVariableValue(i, original_value + 0.5 * SR);
                   this.problem.evaluate(individual);
                   //new_objective = individual.getObjective(0);
                   
                   if(this.improveBest(individual))
                   {
                       grade += bonus_1;
                   }
                   
                   if(this.functionValueDegenerates(individual, i, original_value, original_objective))
                   {
                       individual.setObjective(0, original_objective);
                       individual.setAttribute(i, original_value);
                   }
                   else
                   {
                       grade += bonus_2;
                       improve_i = true;
                   }
                }
                else
                {
                    grade += bonus_2;
                    improve_i = true;
                }
                
            }
        }
        this.improve.set(index, improve_i);
        this.search_range.set(index, SR);
        return grade;
    }
    private double local_search_2(int index)
    {
        double SR = this.search_range.get(index);
        boolean improve_i = this.improve.get(index);
        DoubleSolution individual = this.population.get(index);
        
        DoubleSolution copy = (DoubleSolution) individual.copy();
        
        double grade = 0;
        
        if(!improve_i)
        {
            SR = SR / 2;
            if(SR < Math.pow(10, -15));
            {
                SR = (UPPER_BOUND - LOWER_BOUND) * 0.4;
            }
        }
        improve_i = false;
        for(int l = 0; l < n; l++)
        {
            double original_value = individual.getVariableValue(l);
            double original_objective = individual.getObjective(0);
            
            int [] r = new int[n];
            double [] D = new double[n];
            
            for(int i = 0; i < n; i++)
            {
                r[i] = randomGenerator.nextInt(0, 3);
                D[i] = randomGenerator.nextDouble(-1, 1);
            }
            
            for(int i = 0; i < n; i++)
            {
                if(r[i]==0)
                {
                    double value = individual.getVariableValue(i);
                    individual.setVariableValue(i, value - SR * D[i]);
                }
            }
            
            this.problem.evaluate(individual);
            double new_objective = individual.getObjective(0);
            
            if(this.improveBest(individual))
            {
                grade += bonus_1;
            }
            
            if(new_objective == original_objective)
            {
                this.population.set(index, copy);
            }
            else
            {
                if(this.functionValueDegenerates(copy,individual))
                {
                    individual = (DoubleSolution) copy.copy();
                    for(int i = 0; i < n; i++)
                    {
                        if(r[i]==0)
                        {
                            double value = individual.getVariableValue(i);
                            individual.setAttribute(i, value + 0.5 * SR * D[i]);
                        }
                    }
                    
                    this.problem.evaluate(individual);
                    new_objective = individual.getObjective(0);
                    
                    if(this.improveBest(individual))
                    {
                        grade += bonus_1;
                    }
                    
                    if(this.functionValueDegenerates(copy, individual))
                    {
                        this.population.set(index, copy);
                    }
                    else
                    {
                        grade += bonus_2;
                        improve_i = true;
                    }
                    
                    
                }
                else
                {
                    grade += bonus_2;
                    improve_i = true;
                }
            }
            
            
        }
        
        this.improve.set(index, improve_i);
        this.search_range.set(index, SR);
        return grade;
    }
    private int local_search_3(int index)
    {
        return 0;
    }
    /**
     * Choose the best test values
     * @param values values to choose from
     * @return index of best value found
     */
    private int chooseBest(List<Double> values)
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
        return best_index;
    }
    /**
     * Returns the index of the inidividual with the best fitness
     * @param population population to find best individual 
     * @return index were the best indivual can be located
     */
    private int getIndexBest(List<DoubleSolution> population)
    {
        List<DoubleSolution> population_clone = new ArrayList<>();
        population_clone.addAll(population);
        Collections.sort(population_clone, comparator) ;
        return population.indexOf(population_clone.get(0));
    }
    /**
     * Choose number of foregorund individuals whose grade are best among
     * the population and set their corresponding enable to true
     * @param grades individual's grade
     * @param enable indivual's enable
     */
    private void chooseSolutionsToEnable(List<Double> grades, List<Boolean> enable)
    {
         Collections.sort(grades);//Ordena ascendentemente
         int index = populationSize - 1;
         for(int i = 0; i < number_of_foreground; i++)
         {
             enable.set(index, Boolean.TRUE);
             index--;
         }
    }
    @Override
    public void run() 
    {
        this.generations = 0;
        //TO-DO hacer que n sea del tamaño de los genes de cada individuo
        //TO-DO ¿Cómo hacer que m sea mútiplo de n? n es el número de genes 
        int[][]SOA = this.buildSOA(populationSize, n);
        this.population = this.generateInitialSolutions(SOA);
        this.evaluatePopulation(this.population);
        
        this.enable = new ArrayList<>();
        this.improve = new ArrayList<>();
        this.search_range = new ArrayList<>();
        
        for(int i = 0; i < populationSize; i++)
        {
            enable.add(Boolean.TRUE);
            improve.add(Boolean.TRUE);
            //TO-DO ¿Cuál es el upper y lower bounde? ¿Cómo se establece?
            search_range.add((double)(UPPER_BOUND - LOWER_BOUND)/(double)2);
        }
        
        while(isStoppingConditionReached())
        {
            List<Double> grades = new ArrayList<>();
            for(int i = 0; i < populationSize; i++)
            {
                DoubleSolution xi = this.population.get(i);
                double search_range_xi = search_range.get(i);
                boolean improve_i = improve.get(i);
                //TO-DO puedo ponerlo por defecto en -1???
                double grade_xi = -1;
                if(enable.get(i))
                {
                    grade_xi = 0;
                    double LS1_test_grade = 0;
                    double LS2_test_grade = 0;
                    double LS3_test_grade = 0;
                    //TO-Do local search test se entrega al algoritmo
                    for(int j = 0 ; j < local_search_test; j++)
                    {
                        LS1_test_grade += this.local_search_1(i);
                        LS2_test_grade += this.local_search_2(i);
                        LS3_test_grade += this.local_search_3(i);
                    }
                    List<Double> test_grades = new ArrayList<>();
                    test_grades.add(LS1_test_grade);
                    test_grades.add(LS2_test_grade);
                    test_grades.add(LS3_test_grade);
                    
                    //TO-DO cómo se determina cual es el mejor?
                    int best_local_search = chooseBest(test_grades);
                    
                    for(int j = 0; j < local_search; j++)
                    {
                        switch(best_local_search)
                        {
                            case 1:
                                grade_xi += local_search_1(i);
                            case 2:
                                grade_xi += local_search_2(i);
                                break;
                            case 3:
                                grade_xi += local_search_3(i);
                                break;
                        }
                    }
                }
                //
                grades.add(grade_xi);
            }
            //Find the best solution
            int best_index = this.getIndexBest(this.population);
            //DoubleSolution best_individual = this.population.get(best_index);
            //int best_search_range = search_range.get(best_index);
            for(int i = 0; i < local_search_best; i++)
            {
                this.local_search_1(best_index);
            }
            
            for(int i = 0; i < populationSize; i++)
            {
                enable.set(i, Boolean.FALSE);
            }
            //TO-DO ¿Cómo se miden los grados? demayor a menor? o viceversa?
            this.chooseSolutionsToEnable(grades, enable);
        }
        
    }

    @Override
    public Object getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return "Mulitple Trajectory Search";
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public void setProblem(DoubleProblem problem) {
        this.problem = problem;
    }
    
}
