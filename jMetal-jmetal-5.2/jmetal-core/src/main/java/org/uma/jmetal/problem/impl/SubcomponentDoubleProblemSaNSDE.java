package org.uma.jmetal.problem.impl;

import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentSaNSDE;
import java.util.List;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;


public class SubcomponentDoubleProblemSaNSDE implements DoubleProblem
{
    private List<Integer> index;
    private DoubleProblem original_problem;

    
    public SubcomponentDoubleProblemSaNSDE(List<Integer> index, DoubleProblem original_problem)
    {
        this.index = index;
        this.original_problem = original_problem;
    }
    
    public int getIndex(int i)
    {
        return this.index.get(i);
    }

    @Override
    public Double getLowerBound(int index) {
        return original_problem.getLowerBound(getIndex(index));
    }

    @Override
    public Double getUpperBound(int index) {
       return original_problem.getUpperBound(getIndex(index));
    }

    @Override
    public int getNumberOfVariables() {
        return index.size();
    }

    @Override
    public int getNumberOfObjectives() {
        return original_problem.getNumberOfObjectives();
    }

    @Override
    public int getNumberOfConstraints() {
        return original_problem.getNumberOfConstraints();
    }

    @Override
    public String getName() {
       return original_problem.getName();
    }

    @Override
    public void evaluate(DoubleSolution solution) {
        DoubleSolutionSubcomponentSaNSDE s = (DoubleSolutionSubcomponentSaNSDE)solution;
        this.original_problem.evaluate(s.getSolution());
    }

    @Override
    public DoubleSolution createSolution() {
        return original_problem.createSolution();
    }
}
