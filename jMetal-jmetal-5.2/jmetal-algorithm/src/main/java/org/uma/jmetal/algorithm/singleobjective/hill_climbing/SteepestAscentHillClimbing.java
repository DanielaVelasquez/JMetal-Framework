package org.uma.jmetal.algorithm.singleobjective.hill_climbing;

import java.util.Comparator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.Tweak;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;


public class SteepestAscentHillClimbing  <S extends Solution<?>,P extends Problem<S>>  implements Algorithm<S>
{
    
     /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    private int maxEvaluations;
    private int evaluations;
    private Comparator<S> comparator;
    private Tweak tweak;
    private int n;
    private S best;
    private P problem;
    private double penalize_value;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    @Override
    public void run() {
        best = problem.createSolution();
        this.evaluate(best);
        
        while(!isStoppingConditionReached())
        {
            S r = (S) tweak.tweak(best.copy());
            this.evaluate(r);
            int nEvaluations = this.getPossibleEvaluations(n);
            
            for(int i = 0; i < nEvaluations; i++)
            {
                S w = (S) tweak.tweak(best.copy());
                this.evaluate(w);
                
                if (comparator.compare(w, r) > 0)
                {
                    r = w;
                }
            }
            
            if (comparator.compare(r, best) > 0)
            {
                best = r;
            }
        }
        
    }

    @Override
    public S getResult() 
    {
        return this.best;
    }

    @Override
    public String getName() {
        return "Steepest Ascent Hill Climbing";
    }

    @Override
    public String getDescription() {
        return "We can make this algorithm a little more aggressive: create n “tweaks” to a candidate solution all at one time, and then possibly adopt the best one";
    }
    
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
    private boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }
    
    /**
     * Calculate number of possible evaluations according to next posible number of evaluations
     * @param nextEvaluations next amonunt of evaluations wanting to perform
     * @return number of evaluations allowed to perfom
     */
    private int getPossibleEvaluations(int nextEvaluations)
    {
        return nextEvaluations + evaluations > maxEvaluations?maxEvaluations - evaluations:nextEvaluations;
    }  
    
    /**
     * Update number of evaluations performed
     */
    protected void updateProgress()
    {
        this.evaluations += 1;
    }
    
    /**
     * Evaluates an individual as long as max evaluations hasnot been reache
     * @param individual solution to evaluate
     */
    protected void evaluate(S individual)
    {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(individual);
            this.updateProgress();
        }
        else
        {
            this.penalize(individual);
        }
    }
    
    protected void penalize(S solution){
        solution.setObjective(0, this.penalize_value);
    }
}
