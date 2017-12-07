package org.uma.jmetal.algorithm.search_range;

import java.util.List;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

/**
 * Search range for every variable in one individual
 */
public abstract class SearchRange< S extends Solution<?>,P extends Problem<S>>
{
    protected List search_range;
    protected P problem;
    /**
     * Number of variables of the 
     */
    protected int n;

    public SearchRange(P problem) {
        this.problem = problem;
        this.n = this.problem.getNumberOfVariables();
    }
    
    /**
     * Creates a search range for every variable of solution
     * @param solution 
     */
    public abstract void create(S solution);
    /**
     * Updates search range for every variable of solution
     * @param solution 
     */
    public abstract void update(S solution);
    /**
     * Divide all search values to half its value
     */
    protected abstract void divide();
        
    public abstract SearchRange copy();
    
    protected void setSearchRange(List search_range)
    {
        this.search_range = search_range;
    }
    public List getSearchRange() {
        return search_range;
    }

}
