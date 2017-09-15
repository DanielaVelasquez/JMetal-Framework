package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

public abstract class AbstractHRHMOSAlgorithm <S extends Solution<?>>  implements Algorithm<S>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Algorithms to execute inside a MOS algorithm
     */
    private List<Algorithm> tecniques;
    /**
     * Problem to solve
     */
    private Problem<S> problem ;
    /**
     * Overall shared population in current generation
     */
    private List<S> population;
    /**
     * Offspring subpopulation produced by j-th tecnique at generation i
     */
    private List<S> offspring_subpopulation;
    /**
     * Current generation
     */
    private int i;
    /**
     * Number of tecniques
     */
    private int n;
    /**
     * Participation ratio (Percentage of individuals of the overall shared population)
     * that tecnique j can produce at generation i
     */
    private List participation_ratio;
    /**
     * Quality meausure asociated to each offspring subpopulation
     */
    private List quality_measures;
    /**
     * Population size
     */
    private int populationSize;
    /**
     * Function evaluation for every tecnique in current generation
     */
    private List FE;
    
     /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    @Override
    public void run() 
    {
        this.i = 0;
        this.population = createInitialPopulation();
        evaluatePopulation(this.population);
        this.FE = this.initializeSteps();
        this.participation_ratio = distributeParticipationTecniques();
        
        
        while(isStoppingConditionReached())
        {
            this.i++;
            
            this.quality_measures = this.updateQualityOf(this.tecniques);
            this.participation_ratio = this.updateParticipationRatios(this.quality_measures);
            this.FE = this.updateSteps();
            int j = 0;
            for(Algorithm tecnique: tecniques)
            {
                this.evolve(tecnique, j);
                j++;
            }
            this.population = this.combine(this.population, this.offspring_subpopulation);
        }
    }

    @Override
    public abstract S getResult();
    
    @Override
    public abstract String getName() ;

    @Override
    public abstract String getDescription();
    /**
     * Initialize  the Funcionts evaluations for every technique
     * @return function evaluations for every technique
     */
    protected abstract List initializeSteps();
    /**
     * Update  the Function evaluations for every technique
     * @return function evaluations for every technique
     */
    protected abstract List updateSteps();
    /**
     * Creates initial population
     * @return initial population
     */
    protected abstract  List<S> createInitialPopulation() ;
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
     * Determines if stopping condition was reached when j-th tecnique is evaluated
     * @param j j-th tecnique position
     * @return true if stopping condition was reache, false otherwise
     */
    protected abstract boolean isTecniqueStoppingConditionReached(int j);
    /**
     * Evaluate a population
     * @param population population to evaluate
     * @return population evaluated
     */
    protected abstract void evaluatePopulation(List<S> population);
    /**
     * Updte quality of every tecnique 
     * @param tecniques algorithms to evaluate their quality
     * @return quality value for every tecnique
     */
    protected abstract List updateQualityOf(List tecniques);
    /**
     * Update participation ratios from quality values computed before
     * @param quality_values quality values computed before
     * @return participation ratios updated
     */
    protected abstract List updateParticipationRatios(List quality_values);
    /**
     * Run j-th tecnique
     * @param tecnique algorithm to run
     * @param FE function evalution for techique
     * @return population found by algorithm
     */
    protected abstract List evolve(Algorithm tecnique, int FE);
    /**
     * Combine population and offspring population according to a pre-established criterion to generate next generation
     * @param population current population
     * @param offspring_population subpopulatin produces by tecniques
     * @return population which it is a combination between previos population and a subpopulation
     */
    protected abstract List combine(List<S> population, List<S> offspring_population);
}
