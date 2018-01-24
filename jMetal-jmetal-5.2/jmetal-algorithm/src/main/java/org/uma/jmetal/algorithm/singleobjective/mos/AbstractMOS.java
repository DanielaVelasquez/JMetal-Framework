package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.technique.Technique;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

public abstract class AbstractMOS <S extends Solution<?>>  implements Algorithm<S>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Algorithms to execute inside a MOS algorithm
     * wrapped in its own executer
     */
    protected List<Technique> techniques;
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
     * Number of techniques
     */
    protected int n;
    /**
     * Participation ratio (Percentage of individuals of the overall shared population)
     * that technique j can produce at generation i
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
     * Contains index of techniques with best quality value
     */
    protected List<Integer> best_techniques_qualities;
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

    
    
    
    public AbstractMOS(List<Technique> techniques, Problem<S> problem, int maxEvaluations, int FE, Comparator<S> comparator, double E, double penalize_value) 
    {
        this.techniques = techniques;
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
        this.n = this.techniques.size();
        
        this.participation_ratio = distributeParticipationTecniques();
        this.individual = executeTecniques();
        
        
        
        while(!isStoppingConditionReached())
        {
            this.quality_measures = this.updateQualityOf(this.techniques);
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
     * Get techniques with best quality value and store their
     * indexes in best_techniques_qualities list and assign
     * the maximun quality value found
     */
    protected void findBestQualityTecniques()
    {
        best_techniques_qualities = new ArrayList<>();
        best_techniques_qualities.add(0);
        quality_max = (double) this.quality_measures.get(0);
        
        for(int k = 1; k < n; k++)
        {
            double value = (double) this.quality_measures.get(k);
            if(value > quality_max)
            {
                best_techniques_qualities.clear();
                best_techniques_qualities.add(k);
                quality_max = value;
            }
            else if(value == quality_max)
            {
                best_techniques_qualities.add(k);
            }
        }
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
            S solution = population.get(j);
            this.penalize(solution);
        }
       
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
     * Each technique produces a subset of individuals according to its participation ratio
     * @return initial population
     */
    protected abstract  S executeTecniques() ;
    /**
     * Uniformily distribute participation among technique
     * @return 
     */
    protected abstract List distributeParticipationTecniques();
    /**
     * Determines if stopping condition was reached
     * @return true if stopping condition was reache, false otherwise
     */
    protected abstract boolean isStoppingConditionReached();

    /**
     * Updte quality of every technique 
     * @param technique algorithms to evaluate their quality
     * @return quality value for every technique
     */
    protected abstract List updateQualityOf(List<Technique> techniques);
    /**
     * Update participation ratios from quality values computed before
     * @return participation ratios updated
     */
    protected abstract List updateParticipationRatios();

}
