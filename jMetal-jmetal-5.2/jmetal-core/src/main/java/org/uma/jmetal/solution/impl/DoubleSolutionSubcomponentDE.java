/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.solution.impl;


import org.uma.jmetal.problem.impl.SubcomponentDoubleProblemDE;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class DoubleSolutionSubcomponentDE implements DoubleSolution
{
    private double[] variables;
    private DoubleSolution complete_solution;
    private SubcomponentDoubleProblemDE problem;

    public DoubleSolutionSubcomponentDE(double[] variables, SubcomponentDoubleProblemDE problem,DoubleSolution complete_solution) {
        this.variables = variables;
        this.problem = problem;
        this.complete_solution = complete_solution;
    }

    @Override
    public Double getLowerBound(int index) {
        
        return problem.getLowerBound(index);
    }

    @Override
    public Double getUpperBound(int index) {
        return problem.getUpperBound(index);  
    }

    @Override
    public void setObjective(int index, double value) {
       complete_solution.setObjective(index, value);
    }

    @Override
    public double getObjective(int index) {
        return complete_solution.getObjective(index);
    }

    @Override
    public Double getVariableValue(int index) {
        return variables[index];
    }

    @Override
    public void setVariableValue(int index, Double value) {
        variables[index] = value ;
    }

    @Override
    public String getVariableValueString(int index) {
        return getVariableValue(index).toString() ;
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
        return new DoubleSolutionSubcomponentDE(variables, problem, (DoubleSolution) complete_solution.copy());
    }

    @Override
    public void setAttribute(Object id, Object value) {
      complete_solution.setAttribute(id, value);
    }

    @Override
    public Object getAttribute(Object id) {
      
        return complete_solution.getAttribute(id);
    }

    public DoubleSolution getCompleteSolution() {
        return complete_solution;
    }

    public void setCompleteSolution(DoubleSolution complete_solution) {
        this.complete_solution = complete_solution;
    }
    
    
}
