package org.uma.jmetal.algorithm.singleobjective.mts;

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
    private int LOWER_BOUND_ROW = 0; //Este ya se usa para otra cosa diferenet que en el algoritmo
    
    private int LOWER_BOUND = -1;
    
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
     * Maximun number of cycles
     */
    private int cycles;
    /**
     * Number of iterations made
     */
    private int iteration;
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
    private List<Boolean> enable;
    /**
     * Determines which individuals in population were improve
     */
    private List<Boolean> improve;
    /**
     * Determines the search range for every individual
     */
    private List<Double> search_range;
    /**
     * Bonus 1 value
     */
    private double bonus_1;
    /**
     * Bonus 2 value
     */
    private double bonus_2;
    /**
     * Lower bound for variable a in local search 3
     */
    private double lower_bound_a;
    /**
     * Upper bound for variable a in local search 3
     */
    private double upper_bound_a;
    /**
     * Lower bound for variable b in local search 3
     */
    private double lower_bound_b;
    /**
     * Upper bound for variable b in local search 3
     */
    private double upper_bound_b;
    /**
     * Lower bound for variable c in local search 3
     */
    private double lower_bound_c;
    /**
     * Upper bound for variable c in local search 3
     */
    private double upper_bound_c;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public MultipleTrajectorySearch(int populationSize, DoubleProblem problem, SolutionListEvaluator<DoubleSolution> evaluator, 
            Comparator<DoubleSolution> comparator, int maxGenerations, int local_search_test, int local_search, int local_search_best,
            int number_of_foreground, double bonus_1, double bonus_2, double lower_bound_a, double upper_bound_a, double lower_bound_b, 
            double upper_bound_b, double lower_bound_c, double upper_bound_c)
    {
        this.populationSize = populationSize;
        this.problem = problem;
        this.evaluator = evaluator;
        this.comparator = comparator;
        this.cycles = maxGenerations;
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
        
        this.n = this.problem.getNumberOfVariables();
        this.randomGenerator = JMetalRandom.getInstance();
    }

    /**
     * Builds a simulated orthogonal array SOAmxn
     * @param m number of levels of each factor
     * @param n number of factors
     * @return a simulated orthogonal array
     */
    private int[][] buildSOA(int m, int n) {
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
     * Finds row index where a column  in a array has an specfific value
     * @param value value to find
     * @param array collection where to look
     * @param col column where the row index will be selected
     * @return a row index where the array has the value
     */
    private int getIndexWhere(int value, int[][] array, int col)
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
     * @param m max value of the sequence
     * @return a random sequence
     */
    private List<Integer> buildSequence(int m)
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
        return iteration > cycles;
    }
    /**
     * Increments the number of generations evaluated
     */
    private void updateProgress() {
        iteration += 1;
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
        //TO-DO Si s es el mejor se puede decir que no lo mejoro?
        if(s==best)
            return false;
        return this.getBestBetween(best, s) == s;
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
    /**
     * Determines if a new value can be assigned to an individual
     * @param new_value new value to assign
     * @param index index in the solution to  change
     * @param solution solution which is going to change
     * @return true if the new value is between the allowed value in a specific
     * index int the individual, false otherwise
     */
    private boolean isInBounds(double new_value, int index, DoubleSolution solution)
    {
        return new_value >= solution.getLowerBound(index) && new_value <= solution.getUpperBound(index);
    }
    private double local_search_1(int index)
    {
        double SR = this.search_range.get(index);
        boolean improve_i = this.improve.get(index);
        DoubleSolution individual;
        DoubleSolution original ;
        int best_index = this.getIndexBest(this.population);
        
        
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
            individual = this.population.get(index);
            original = (DoubleSolution) individual.copy();
            
            double original_value = individual.getVariableValue(i);
            double original_objective = individual.getObjective(0);
            
            double new_value = original_value - SR;
            if(this.isInBounds(new_value, i, individual))
            {
                individual.setVariableValue(i, new_value);
                this.problem.evaluate(individual);
            }
            
            
            double new_objective = individual.getObjective(0);
            //Individual improve best
            //TO-DO ¿Se busca que sea mejor que el mejor de la poblacion o que se mejor que lo que estaba?
            if(this.improveBest(original))
            {
                grade = grade + bonus_1;
                
            }
            
            //Individual gets same value than original individual
            if(new_objective == original_objective)
            {
                this.population.set(index, original);
            }
            else
            {
                //System.out.print("i: "+i);
                /*if(i>=35)
                    System.out.println("i "+i+" "+population.get(index).getVariableValueString(35));*/
                if(this.functionValueDegenerates(original, individual))
                {
                   individual.setVariableValue(i, original_value + 0.5 * SR);
                   this.problem.evaluate(individual);
                   //new_objective = individual.getObjective(0);
                   
                   if(this.improveBest(individual))
                   {
                       grade += bonus_1;
                   }
                   
                   if(this.functionValueDegenerates(original, individual))
                   {
                      //System.out.println(" "+i);
                      this.population.set(index, original);
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
        DoubleSolution individual;
                
        DoubleSolution original;
        
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
            individual = this.population.get(index);
            original = (DoubleSolution) individual.copy();
            
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
                this.population.set(index, original);
            }
            else
            {
                if(this.functionValueDegenerates(original,individual))
                {
                    individual = (DoubleSolution) original.copy();
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
                    
                    if(this.functionValueDegenerates(original, individual))
                    {
                        this.population.set(index, original);
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
    
    private double local_search_3(int index)
    {
        double SR = this.search_range.get(index);
        boolean improve_i = this.improve.get(index);
        DoubleSolution individual;
        double individual_objective;
        DoubleSolution original;
        
        double grade = 0;
        
        DoubleSolution x1, y1, x2;
        double x1_value, y1_value, x2_value;
        double x1_new, y1_new, x2_new;
        
        individual = this.population.get(index);
        original = (DoubleSolution) individual.copy();
        
        for(int i = 0;  i < this.n; i++)
        {
            individual = this.population.get(index);
            individual_objective = individual.getObjective(0);
            
            x1 = (DoubleSolution) individual.copy();
            y1 = (DoubleSolution) individual.copy();
            x2 = (DoubleSolution) individual.copy();
            
            x1_value = x1.getVariableValue(i);
            y1_value = y1.getVariableValue(i);
            x2_value = x2.getVariableValue(i);
            
            //TO-DO ¿ Qué pasa si no puede hacer la suma o la resta?
            //Que tal si se sale de los límites permitidos de la variable
            //Se pude hacer así preguntando???
            x1_new = x1_value + 0.1;
            y1_new = y1_value + 0.1;
            x2_new = x2_value - 0.2;
            
            if(this.isInBounds(x1_new, i, x1))
            {
                x1.setVariableValue(i, x1_new);
                this.problem.evaluate(x1);
            }
                
            if(this.isInBounds(y1_new, i, y1))
            {
                y1.setVariableValue(i, y1_new);
                this.problem.evaluate(y1);
            }
             
            if(this.isInBounds(x2_new, i, x2))
            {
                x2.setVariableValue(i, x2_new);
                this.problem.evaluate(x2);
            }
            
            //TO-DO ¿ Se actualiza el mejor?
            //Se cambia de la población??
            if(this.improveBest(x1))
            {
                grade += bonus_1;
                int best = this.getIndexBest(this.population);
                this.population.set(best, x1);
            }
            
            if(this.improveBest(y1))
            {
                grade += bonus_1;
                int best = this.getIndexBest(this.population);
                this.population.set(best, y1);
            }
            
            if(this.improveBest(x2))
            {
                grade += bonus_1;
                int best = this.getIndexBest(this.population);
                this.population.set(best, x2);
            }
            
            //TO-DO ¿solo se puede si es monobojetivo??
            //TO-DO ¿Se puede hacer? no se debe tener en cuenta si es maximizando
            // o si está minimizando
            double D1 = individual_objective - x1.getObjective(0);
            double D2 = individual_objective - y1.getObjective(0);
            double D3 = individual_objective - x2.getObjective(0);
            
            
            //X1 is better than original individual
            if(this.getBestBetween(individual, x1)==x1)
            {
                grade += bonus_2;
            }
            //Y1 is better than original individual
            if(this.getBestBetween(individual, y1) == y1)
            {
                grade += bonus_2;
            }
            //x2 is better than original individual
            if(this.getBestBetween(individual, x2) == x2)
            {
                grade += bonus_2;
            }
            double a = randomGenerator.nextDouble(lower_bound_a, upper_bound_a);
            double b = randomGenerator.nextDouble(lower_bound_b, upper_bound_b);
            double c = randomGenerator.nextDouble(lower_bound_c, upper_bound_c);
            
            double xi = individual.getVariableValue(i);
            double new_xi =  xi + a * (D1 - D2) + b * (D3 - 2 * D1 ) + c;
            
            if(this.isInBounds(new_xi, i, individual))
            {
                individual.setVariableValue(i, new_xi);
                this.problem.evaluate(individual);
            }
            //TO-DO es del original o del original de la itreacion anterior
            if(this.functionValueDegenerates(original, individual))
            {
                this.population.set(index, original);
            }
            else
            {
                grade += bonus_2;
            }
        }
        
        if(this.functionValueDegenerates(original, individual))
        {
            this.population.set(index, original);
        }
        else
        {
            grade += bonus_2;
        }
        
        this.improve.set(index, improve_i);
        this.search_range.set(index, SR);
        return grade;
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
        //TO-DO ¿Cómo determinar automaticamente le lower bound y upper bound?
        this.iteration = 0;
        //TO-DO hacer que n sea del tamaño de los genes de cada individuo
        //TO-DO ¿Cómo hacer que m sea mútiplo de n? n es el número de genes
        //TO-DO Si está utilizando MOS, la población se la debe pasar
        //¿así para evitar la creación de una población inicial?
        this.n = this.problem.getNumberOfVariables();
        if(this.population == null)
        {
            //TO-DO ¿ Está bien construido SOA?
            //TO-DO todos los valores de los individuos son 1,-1,0.5 y -0.5
            int[][]SOA = this.buildSOA(populationSize, n);
            this.population = this.generateInitialSolutions(SOA);
        }
        
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
        //TO_Do ¿cómo se cuentan las evaluaciones si hay evaluaciones de test y dentro de las busquedas locales??
        while(!isStoppingConditionReached())
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
                        //TO-DO ¿ls 2 se corre con la mejora de ls1 y así?¿se debe hacer así?
                        //Seria mejor hacer una copia de la población y después de la busqueda de prueba si dejarlos modificar
                        //Un vez termina la prueba que haga la copia de los individuos originales
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
            //Creo que es de mayor a menor
            this.chooseSolutionsToEnable(grades, enable);
            this.updateProgress();
        }
        
    }

    @Override
    public Object getResult() {
        Collections.sort(this.population, comparator);
        return this.population.get(0);
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

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public List<DoubleSolution> getPopulation() {
        return population;
    }

    public void setPopulation(List<DoubleSolution> population) {
        this.population = population;
    }

    public int getMaxGenerations() {
        return cycles;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.cycles = maxGenerations;
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
    
    
    
}
