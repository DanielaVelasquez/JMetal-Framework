package org.uma.jmetal.algorithm.singleobjective.lbfgsb;

import java.util.Comparator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;


public class LBFGSBBuilder 
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
     
    private DoubleProblem problem;
    
    private Comparator<DoubleSolution> comparator;
    
    private int FE;
    
    private double penalize_value;
    
    private int m;
    
    private double eps;
        
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/

    
    
    public LBFGSBBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.comparator = new ObjectiveComparator<>(0,ObjectiveComparator.Ordering.ASCENDING);
        this.FE = 3000;
        this.penalize_value = 1;
        this.m = 3;
        this.eps = 2.2204e-016;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public LBFGSBBuilder setProblem(DoubleProblem problem) {
        if(problem == null)
            throw new JMetalException("Problem can't be null");
        this.problem = problem;
        return this;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public LBFGSBBuilder setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
        return this;
    }

    public int getFE() {
        return FE;
    }

    public LBFGSBBuilder setFE(int FE) {
        if(FE <= 0)
            throw new JMetalException("Function evalutions is 0 or less: "+FE);
        this.FE = FE;
        return this;
    }

    public double getPenalize_value() {
        return penalize_value;
    }

    public LBFGSBBuilder setPenalize_value(double penalize_value) {
        
        this.penalize_value = penalize_value;
        return this;
    }

    public int getM() {
        return m;
    }

    public LBFGSBBuilder setM(int m) {
        if(m <= 0)
            throw new JMetalException("m must be positive value");
        this.m = m;
        return this;
    }

    public double getEps() {
        return eps;
    }

    public LBFGSBBuilder setEps(double eps) {
        this.eps = eps;
        return this;
    }

    public LBFGSB build()
    {
        return new LBFGSB(problem, comparator, FE, penalize_value, m, eps);
    }
}
