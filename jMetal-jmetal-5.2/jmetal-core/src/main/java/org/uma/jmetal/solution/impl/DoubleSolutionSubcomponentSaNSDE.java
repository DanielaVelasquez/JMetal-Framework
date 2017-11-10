/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.solution.impl;


import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemSaNSDE;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class DoubleSolutionSubcomponentSaNSDE implements DoubleSolution
{

    private DoubleSolution solution;
    private SubcomponentDoubleProblemSaNSDE problem;

    public DoubleSolutionSubcomponentSaNSDE(DoubleSolution solution, SubcomponentDoubleProblemSaNSDE problem) {
        this.solution = solution;
        this.problem = problem;
    }

    public DoubleSolution getSolution() {
        return solution;
    }
    
    
    
    @Override
    public Double getLowerBound(int index) {
        return solution.getLowerBound(problem.getIndex(index));
    }

    @Override
    public Double getUpperBound(int index) {
        return solution.getUpperBound(problem.getIndex(index));
    }

    @Override
    public void setObjective(int index, double value) {
       solution.setObjective(index, value);
    }

    @Override
    public double getObjective(int index) {
        return solution.getObjective(index);
    }

    @Override
    public Double getVariableValue(int index) {
        return solution.getVariableValue(problem.getIndex(index));
    }

    @Override
    public void setVariableValue(int index, Double value) {
        solution.setVariableValue(problem.getIndex(index), value);
    }

    @Override
    public String getVariableValueString(int index) {
        return getVariableValue(problem.getIndex(index)).toString() ;
    }

    @Override
    public int getNumberOfVariables() {
        return problem.getNumberOfVariables();
    }

    @Override
    public int getNumberOfObjectives() {
        return problem.getNumberOfObjectives();
    }

    @Override
    public Solution<Double> copy() {
        return new DoubleSolutionSubcomponentSaNSDE((DoubleSolution) solution.copy(), problem);
    }

    @Override
    public void setAttribute(Object id, Object value) {
      solution.setAttribute(id, value);
    }

    @Override
    public Object getAttribute(Object id) {
      return solution.getAttribute(id);
    }
    
}
