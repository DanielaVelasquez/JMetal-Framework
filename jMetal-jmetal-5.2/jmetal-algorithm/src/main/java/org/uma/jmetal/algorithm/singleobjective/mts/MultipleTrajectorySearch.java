package org.uma.jmetal.algorithm.singleobjective.mts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.util.DoubleSearchRange;
import org.uma.jmetal.algorithm.util.SearchRange;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;


public class MultipleTrajectorySearch extends AbstractMultipleTrajectorySearch<DoubleSolution, DoubleProblem>
{
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public MultipleTrajectorySearch(int populationSize, DoubleProblem problem, 
            Comparator<DoubleSolution> comparator, int FE, int local_search_test, 
            int local_search, int local_search_best, int number_of_foreground, 
            double bonus_1, double bonus_2, double lower_bound_a, double upper_bound_a, 
            double lower_bound_b, double upper_bound_b, double lower_bound_c, double upper_bound_c) {
        super(populationSize, problem, comparator, FE, local_search_test, local_search, local_search_best, number_of_foreground, bonus_1, bonus_2, lower_bound_a, upper_bound_a, lower_bound_b, upper_bound_b, lower_bound_c, upper_bound_c);
    }

    @Override
    protected List<DoubleSolution> createInitialPopulation() {
        List<DoubleSolution> population = new ArrayList<>(this.populationSize);
        for (int i = 0; i < populationSize; i++) {
          DoubleSolution newIndividual = getProblem().createSolution();
          population.add(newIndividual);
        }
        return population;
    }

    @Override
    protected List<DoubleSolution> generateInitialSolutions(int[][] SOA) {
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

    @Override
    public String getName() {
        return "Multiple Trajectory Search";
    }

    @Override
    public String getDescription() {
        return "MTS uses multiple agents to search using one of the three candidate local search methods";
    }

    @Override
    protected SearchRange buildSearchRange(DoubleSolution solution) {
        DoubleSearchRange sr = new DoubleSearchRange(problem);
        sr.create(solution);
        return sr;
    }

    @Override
    protected double local_search_1(DoubleSolution xi, int index, boolean testing) {
        SearchRange SR;
        if(testing){
            SR = this.search_range.get(index).copy();
        }else{
            SR = this.search_range.get(index); 
        }
        List<Double> SRList = SR.getSearchRange();
        boolean improve_i = this.improve.get(index);
        DoubleSolution copy;
        
        
        double grade = 0;
        
        if(!improve_i)
        {
            SR.update(xi);
        }
        improve_i = false;
        for(int i = 0; i < n; i++)
        {
            copy = (DoubleSolution) xi.copy();
            
            double original_value = xi.getVariableValue(i);
            double original_objective = xi.getObjective(0);
            
            double new_value = original_value - SRList.get(i);
            if(this.inBounds(new_value, i, xi))
            {
                xi.setVariableValue(i, new_value);
                this.evaluate(xi);
            }
            
            
            double new_objective = xi.getObjective(0);
            //Individual improve best
            //TO-DO ¿Se busca que sea mejor que el mejor de la poblacion o que se mejor que lo que estaba?
            if(this.improveBest(xi) && !this.inPopulation(population, xi))
            {
                best =  (DoubleSolution) xi.copy();  
                grade = grade + bonus_1;
            }
            
            //Individual gets same value than original individual
            if(new_objective == original_objective)
            {
                if(testing)
                    xi = copy;
                else
                    this.population.set(index, copy);
            }
            else
            {
                if(this.functionValueDegenerates(copy, xi) || this.inPopulation(population, xi))
                {
                   new_value = original_value + 0.5 * SRList.get(i);
                   if(this.inBounds(new_value, i, xi))
                   {
                        xi.setVariableValue(i, new_value);
                        this.evaluate(xi);
                        if(this.improveBest(xi)  && !this.inPopulation(population, xi))
                        {
                            best =  (DoubleSolution) xi.copy();
                            grade += bonus_1;
                        }

                        if(this.functionValueDegenerates(copy, xi)  || this.inPopulation(population, xi))
                        {
                           if(testing)
                             xi = copy;
                           else
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
                       if(testing)
                        xi = copy;
                       else
                        this.population.set(index, copy);
                   }
                   
                }
                else
                {
                    grade += bonus_2;
                    improve_i = true;
                }
                
            }
        }
        if(!testing)
        {
            this.improve.set(index, improve_i);
            this.search_range.set(index, SR);
        }
        return grade;
    }

    @Override
    protected double local_search_2(DoubleSolution xi, int index, boolean testing) {
        SearchRange SR;
        if(testing){
            SR = this.search_range.get(index).copy();
        }else{
            SR = this.search_range.get(index); 
        }
        List<Double> SRList = SR.getSearchRange();
        boolean improve_i = this.improve.get(index);
        DoubleSolution copy;
        
        
        double grade = 0;
        
        if(!improve_i)
        {
            SR.update(xi);
        }
        improve_i = false;
        
        for(int l = 0; l < n; l++)
        {
            copy = (DoubleSolution) xi.copy();
            
            double original_objective = xi.getObjective(0);
            
            double [] r = new double[n];
            double [] D = new double[n];
            
            for(int i = 0; i < n; i++)
            {
                r[i] = randomGenerator.nextDouble(0, 1);
                D[i] = randomGenerator.nextDouble(-1, 1);
            }
            
            for(int i = 0; i < n; i++)
            {
                if(r[i]<=0.25)
                {
                    double value = xi.getVariableValue(i) - SRList.get(i) * D[i];
                    if(this.inBounds(value, i, xi))
                        xi.setVariableValue(i, value );
                }
            }
            
            this.evaluate(xi);
            double new_objective = xi.getObjective(0);
            
            
            if(this.improveBest(xi) && !this.inPopulation(population, xi))
            {
                best =  (DoubleSolution) xi.copy();
                grade += bonus_1;
            }
            
            if(new_objective == original_objective)
            {
                if(testing)
                    xi = copy;
                else
                    this.population.set(index, copy);
            }
            else
            {
                if(this.functionValueDegenerates(copy,xi) || this.inPopulation(population, xi))
                {
                    xi = (DoubleSolution) copy.copy();
                    for(int i = 0; i < n; i++)
                    {
                        if(r[i]<=0.25)
                        {
                            double value = xi.getVariableValue(i) + 0.5 * SRList.get(i) * D[i];
                            if(this.inBounds(value, i, xi))
                               xi.setVariableValue(i, value );
                        }
                    }
                    
                    this.evaluate(xi);
                    new_objective = xi.getObjective(0);
                    
                    if(this.improveBest(xi) && !this.inPopulation(population, xi))
                    {
                        best =  (DoubleSolution) xi.copy();
                        grade += bonus_1;
                    }
                    
                    if(this.functionValueDegenerates(copy, xi) || this.inPopulation(population, xi))
                    {
                        if(testing)
                            xi = copy;
                        else
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
        if(!testing)
        {
            this.improve.set(index, improve_i);
            this.search_range.set(index, SR);
        }
        return grade;
    }

    @Override
    protected double local_search_3(DoubleSolution xi, int index, boolean testing) {
        SearchRange SR;
        if(testing){
            SR = this.search_range.get(index).copy();
        }else{
            SR = this.search_range.get(index); 
        }
        List<Double> SRList = SR.getSearchRange();
        boolean improve_i = this.improve.get(index);
        DoubleSolution copy;
        double individual_objective;
        
        double grade = 0;
        
        DoubleSolution x1, y1, x2;
        double x1_value, y1_value, x2_value;
        double x1_new, y1_new, x2_new;
        
        
        DoubleSolution original = (DoubleSolution) xi.copy();
        
        for(int i = 0;  i < this.n; i++)
        {
            individual_objective = xi.getObjective(0);
            copy = (DoubleSolution) xi.copy();
            
            x1 = (DoubleSolution) xi.copy();
            y1 = (DoubleSolution) xi.copy();
            x2 = (DoubleSolution) xi.copy();
            
            x1_value = x1.getVariableValue(i);
            y1_value = y1.getVariableValue(i);
            x2_value = x2.getVariableValue(i);
            
            //TO-DO ¿ Qué pasa si no puede hacer la suma o la resta?
            //Que tal si se sale de los límites permitidos de la variable
            //Se pude hacer así preguntando???
            x1_new = x1_value + 0.1;
            y1_new = y1_value - 0.1;
            x2_new = x2_value + 0.2;
            
            if(this.inBounds(x1_new, i, x1))
            {
                x1.setVariableValue(i, x1_new);
                this.evaluate(x1);
            }
                
            if(this.inBounds(y1_new, i, y1))
            {
                y1.setVariableValue(i, y1_new);
                this.evaluate(y1);
            }
             
            if(this.inBounds(x2_new, i, x2))
            {
                x2.setVariableValue(i, x2_new);
                this.evaluate(x2);
            }
            
            //TO-DO ¿ Se actualiza el mejor?
            //Se cambia de la población??
            if(this.improveBest(x1))
            {
                grade += bonus_1;
                this.best = (DoubleSolution) x1.copy();
            }
            
            if(this.improveBest(y1) )
            {
                grade += bonus_1;
                this.best = (DoubleSolution) x1.copy();
            }
            
            if(this.improveBest(x2))
            {
                grade += bonus_1;
                this.best = (DoubleSolution) x1.copy();
            }
            
            //TO-DO ¿solo se puede si es monobojetivo??
            //TO-DO ¿Se puede hacer? no se debe tener en cuenta si es maximizando
            // o si está minimizando
            double D1 = individual_objective - x1.getObjective(0);
            double D2 = individual_objective - y1.getObjective(0);
            double D3 = individual_objective - x2.getObjective(0);
            
            
            //X1 is better than original individual
            if(this.getBest(xi, x1)==x1)
            {
                grade += bonus_2;
            }
            //Y1 is better than original individual
            if(this.getBest(xi, y1) == y1)
            {
                grade += bonus_2;
            }
            //x2 is better than original individual
            if(this.getBest(xi, x2) == x2)
            {
                grade += bonus_2;
            }
            double a = randomGenerator.nextDouble(lower_bound_a, upper_bound_a);
            double b = randomGenerator.nextDouble(lower_bound_b, upper_bound_b);
            double c = randomGenerator.nextDouble(lower_bound_c, upper_bound_c);
            
            double xi_value = xi.getVariableValue(i);
            double new_xi =  xi_value + a * (D1 - D2) + b * (D3 - 2 * D1 ) + c;
            
            if(this.inBounds(new_xi, i, xi))
            {
                xi.setVariableValue(i, new_xi);
                this.evaluate(xi);
                //TO-DO es del original o del original de la itreacion anterior
                if(this.functionValueDegenerates(copy, xi))
                {
                    if(!testing)
                        this.population.set(index, original);
                }
                else
                {
                    grade += bonus_2;
                }
            }
            
        }
        
        if(this.functionValueDegenerates(original, xi)  || this.inPopulation(population, xi))
        {
            if(!testing)
                this.population.set(index, original);
        }
        else
        {
            grade += bonus_2;
        }
        
        if(!testing)
        {
            this.improve.set(index, improve_i);
            this.search_range.set(index, SR);
        }
        return grade;
    }
    /**
     * Determines if a new value can be assigned to an individual
     * @param new_value new value to assign
     * @param index index in the solution to  change
     * @param solution solution which is going to change
     * @return true if the new value is between the allowed value in a specific
     * index int the individual, false otherwise
     */
    private boolean inBounds(double new_value, int index, DoubleSolution solution)
    {
        return new_value >= solution.getLowerBound(index) && new_value <= solution.getUpperBound(index);
    }

    @Override
    protected boolean inPopulation(List<DoubleSolution> population, DoubleSolution individual)
    {
        for(DoubleSolution s: population)
        {
            if(s!=individual)
            {
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

}
