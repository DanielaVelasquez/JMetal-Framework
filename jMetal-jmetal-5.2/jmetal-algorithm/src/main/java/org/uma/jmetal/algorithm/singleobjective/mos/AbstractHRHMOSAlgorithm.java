package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.util.Tecnique;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

public abstract class AbstractHRHMOSAlgorithm <S extends Solution<?>>  implements Algorithm<S>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Algorithms to execute inside a MOS algorithm
     * wrapped in its own executer
     */
    protected List<Tecnique> tecniques;
    /**
     * Problem to solve
     */
    protected Problem<S> problem ;
    /**
     * Overall shared population in current generation
     */
    protected S individual;
    /**
     * Current generation
     */
    protected int i;
    /**
     * Number of evaluation perfomed
     */
    protected int evaluations;
    /**
     * Maximun number of generations
     */
    protected int maxEvaluations;
    /**
     * Number of tecniques
     */
    protected int n;
    /**
     * Participation ratio (Percentage of individuals of the overall shared population)
     * that tecnique j can produce at generation i
     */
    protected List participation_ratio;
    /**
     * Quality meausure asociated to each offspring subpopulation
     */
    protected List quality_measures;

    /**
     * Function evaluation for every cycle
     */
    protected int FE;
    /**
     * Determines how a solution should be order
     */
    protected Comparator<S> comparator;
    /**
     * Contains index of tecniques with best quality value
     */
    protected List<Integer> best_tecniques_qualities;
    /**
     * Maximun qualitiy value
     */
    protected double quality_max;
    /**
     * Reduction factor
     */
    protected double E;
    /**
     * Value to penalize a solution in case evaluations are over
     */
    protected double penalize_value;
    
     /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    
    
    
    public AbstractHRHMOSAlgorithm(List<Tecnique> tecniques, Problem<S> problem, int maxEvaluations, int FE, Comparator<S> comparator, double E, double penalize_value) 
    {
        this.tecniques = tecniques;
        this.problem = problem;
        this.maxEvaluations = maxEvaluations;
        this.FE = FE;
        this.comparator = comparator;
        this.E = E;
        this.penalize_value = penalize_value;
    }

    @Override
    public void run() {
        this.i = 0;
        this.n = this.tecniques.size();
        
        this.participation_ratio = distributeParticipationTecniques();
        this.individual = executeTecniques();
        
        
        
        while(!isStoppingConditionReached())
        {
            this.quality_measures = this.updateQualityOf(this.tecniques);
            this.participation_ratio = this.updateParticipationRatios();
            this.individual = this.executeTecniques();
        
            this.i++;
        }
    }
    /**
     * Update number of evaluations performed
     */
    protected void updateProgress(int evaluations)
    {
        this.evaluations += evaluations;
    }
    /**
     * Get tecniques with best quality value and store their
     * indexes in best_tecniques_qualities list and assign
     * the maximun quality value found
     */
    protected void findBestQualityTecniques()
    {
        best_tecniques_qualities = new ArrayList<>();
        best_tecniques_qualities.add(0);
        quality_max = (double) this.quality_measures.get(0);
        for(int k = 1; k < n; k++)
        {
            double value = (double) this.quality_measures.get(k);
            if(value > quality_max)
            {
                best_tecniques_qualities.clear();
                best_tecniques_qualities.add(k);
                quality_max = value;
            }
            else if(value == quality_max)
            {
                best_tecniques_qualities.add(k);
            }
        }
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
            this.updateProgress(1);
        }
        for(int j = i; j < populationSize; j++)
        {
            S solution = population.get(i);
            this.penalize(solution);
        }
        //TO-DO ¿Que hago con el resto de la población que no alcance a evaluar?
    }
    protected void penalize(S solution){
        solution.setObjective(0, this.penalize_value);
    }
    @Override
    public abstract S getResult();
    
    @Override
    public abstract String getName() ;

    @Override
    public abstract String getDescription();

    
    /**
     * Each tecnique produces a subset of individuals according to its participation ratio
     * @return initial population
     */
    protected abstract  S executeTecniques() ;
    /**
     * Uniformily distribute participation among tecniques
     * @return 
     */
    protected abstract List distributeParticipationTecniques();
    /**
     * Determines if stopping condition was reached
     * @return true if stopping condition was reache, false otherwise
     */
    protected abstract boolean isStoppingConditionReached();

    /**
     * Updte quality of every tecnique 
     * @param tecniques algorithms to evaluate their quality
     * @return quality value for every tecnique
     */
    protected abstract List updateQualityOf(List<Tecnique> tecniques);
    /**
     * Update participation ratios from quality values computed before
     * @return participation ratios updated
     */
    protected abstract List updateParticipationRatios();

}
