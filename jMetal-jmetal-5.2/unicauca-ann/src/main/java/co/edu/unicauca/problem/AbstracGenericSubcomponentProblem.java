package co.edu.unicauca.problem;

import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import org.uma.jmetal.solution.Solution;

public abstract class AbstracGenericSubcomponentProblem extends AbstractGenericProblem
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Lower variable value in evaluation
     */
    protected int l;
    /**
     * Upper variable value in evaluation
     */
    protected int u;
    /**
     * 
     */
    protected Solution solution;
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    public AbstracGenericSubcomponentProblem(int l, int u)
    {
        this.l = l;
        this.u = u;
    }
    
    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getU() {
        return u;
    }

    public void setU(int u) {
        this.u = u;
    }
    
}
