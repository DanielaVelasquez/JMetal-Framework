package org.uma.jmetal.algorithm.singleobjective.hill_climbing;

import java.util.Comparator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.Tweak;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;


public class SteepestAscentHillClimbing  implements Algorithm<Solution>
{
    
     /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    private int maxEvaluations;
    private int evaluations;
    private Comparator<Solution> comparator;
    private Tweak tweak;
    private int n;
    private Solution best;
    private Problem problem;
    private double penalize_value;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    
    
    public SteepestAscentHillClimbing(int maxEvaluations, Comparator<Solution> comparator, Tweak tweak, int n, Problem problem, double penalize_value) {
        this.maxEvaluations = maxEvaluations;
        this.comparator = comparator;
        this.tweak = tweak;
        this.n = n;
        this.problem = problem;
        this.penalize_value = penalize_value;
        
        this.evaluations = 0;
    }

    @Override
    public void run() {
        if(best==null)
            best = problem.createSolution();
        this.evaluate(best);
        
        while(!isStoppingConditionReached())
        {
            Solution r = (Solution) best.copy();
            tweak.tweak(r);
            this.evaluate(r);
            int nEvaluations = this.getPossibleEvaluations(n);
            
            for(int i = 0; i < nEvaluations; i++)
            {
                Solution w = (Solution) best.copy(); 
                tweak.tweak(w);
                this.evaluate(w);
                
                if (comparator.compare(w, r) < 0)
                {
                    r = w;
                }
            }
            
            if (comparator.compare(r, best) < 0)
            {
                best = r;
            }
        }
    }

    @Override
    public Solution getResult() 
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
    protected void evaluate(Solution individual)
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
    
    protected void penalize(Solution solution){
        solution.setObjective(0, this.penalize_value);
    }

    public int getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(int evaluations) {
        this.evaluations = evaluations;
    }

    public Solution getBest() {
        return best;
    }

    public void setBest(Solution best) {
        this.best = best;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public Comparator<Solution> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<Solution> comparator) {
        this.comparator = comparator;
    }

    public Tweak getTweak() {
        return tweak;
    }

    public void setTweak(Tweak tweak) {
        this.tweak = tweak;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public double getPenalize_value() {
        return penalize_value;
    }

    public void setPenalize_value(double penalize_value) {
        this.penalize_value = penalize_value;
    }
}
