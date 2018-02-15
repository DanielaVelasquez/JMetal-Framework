package org.uma.jmetal.algorithm.singleobjective.mts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.search_range.impl.DoubleSearchRange;
import org.uma.jmetal.algorithm.search_range.SearchRange;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Util;


public class MTS_LS1 extends AbstractMTS_LS1<DoubleSolution, DoubleProblem>
{
    /**
     * ------------------------------------------------------------------------
     * Based on
     * http://ieeexplore.ieee.org/document/4631210/ ****
     * L. Tseng and C. Chen, “Multiple Trajectory Search for Large 
     * Scale Global Optimization,” 2008 IEEE World Congress on
     * Computational Intelligence, pp 3052-3059, 2008. 
     * -------------------------------------------------------------------------
     */
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    

    public MTS_LS1(int populationSize, DoubleProblem problem, Comparator<DoubleSolution> comparator, int FE, double penalize_value, double bonus_1, double bonus_2) {
        super(populationSize, problem, comparator, FE, penalize_value, bonus_1, bonus_2);
    }

    @Override
    protected List<DoubleSolution> createInitialPopulation(int populationSize) {
        List<DoubleSolution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newIndividual = getProblem().createSolution();
            population.add(newIndividual);
        }
        return population;
    }

    @Override
    protected List<DoubleSolution> generateInitialSolutions(int[][] SOA, int populationSize) {
        List<DoubleSolution> p = this.createInitialPopulation(populationSize);
        for(int i = 0; i < populationSize; i++)
        {
            DoubleSolution individual = p.get(i);
            for(int j = 0; j < n; j++)
            {
                double value = individual.getLowerBound(j) + ((individual.getUpperBound(j) -  individual.getLowerBound(j)) * SOA[i][j])/(populationSize - 1);
                individual.setVariableValue(j, value);
            }
        }
        this.evaluatePopulation(p);
        return p;
    }

    @Override
    public String getName() {
        return "Multiple Trajectory Search - Local search 1";
    }

    @Override
    public String getDescription() {
        return "MTS-LS1 uses original MTS algorithm and only perfomr its local search 1";
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
        List<Integer> sequence = Util.createRandomPermutation(n);
        for(int j = 0; j < n; j++)
        {
            int i = sequence.get(j);
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
            if(new_objective == original_objective && this.isBetterOriginal(copy, xi))
            {
                if(!testing)
                {
                    this.population.set(index, copy);
                }
                xi = copy;
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
                           if(!testing)
                            {
                                this.population.set(index, copy);
                            }
                            xi = copy;
                        }
                        else
                        {
                            grade += bonus_2;
                            improve_i = true;
                        }
                   }
                   else
                   {
                       if(!testing)
                        {
                            this.population.set(index, copy);
                        }
                        xi = copy;
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
            if(s != individual)
            {
                int n = this.getProblem().getNumberOfVariables();
                
                for(int i = 0; i < n; i++)
                {
                    double sValue = s.getVariableValue(i);
                    double individualValue = individual.getVariableValue(i);
                    
                    if(sValue == individualValue)
                    {
                        if(i == n-1)
                        {
                            return true;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                return true;
            }
        }
        
        return false;
    } 
    
}
