package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

public abstract class AbstractMOSAlgorithm <S extends Solution<?>, R>  implements Algorithm<R>
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
     * Quality value asociated to each offspring subpopulation
     */
    private List quality_values;
    
     /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    @Override
    public void run() 
    {
        this.population = createInitialPopulation();
        this.participation_ratio = distributeParticipationTecniques();
        this.population = evaluatePopulation(this.population);
        
        while(isStoppingConditionReached())
        {
            this.quality_values = this.updateQualityOf(this.tecniques);
            this.participation_ratio = this.updateParticipationRatios(this.quality_values);
            int j = 0;
            for(Algorithm tecnique: tecniques)
            {
                while(this.isTecniqueStoppingConditionReached(j))
                {
                    this.offspring_subpopulation.addAll(this.evolve(tecnique, j));
                }
                j++;
            }
            this.population = this.combine(this.population, this.offspring_subpopulation);
        }
    }

    @Override
    public abstract R getResult();
    
    @Override
    public abstract String getName() ;

    @Override
    public abstract String getDescription();
    
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
    protected abstract List<S> evaluatePopulation(List<S> population);
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
     * @param j sunindex tecnique
     * @return population found by algorithm
     */
    protected abstract List evolve(Algorithm tecnique, int j);
    /**
     * Combine population and offspring population according to a pre-established criterion to generate next generation
     * @param population current population
     * @param offspring_population subpopulatin produces by tecniques
     * @return population which it is a combination between previos population and a subpopulation
     */
    protected abstract List combine(List<S> population, List<S> offspring_population);
}
