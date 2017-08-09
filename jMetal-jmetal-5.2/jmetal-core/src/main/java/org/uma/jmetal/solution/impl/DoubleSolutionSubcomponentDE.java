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
    private double[] objectives;
    private double[] variables;
    private SubcomponentDoubleProblemDE problem;

    public DoubleSolutionSubcomponentDE(double[] objectives, double[] variables, SubcomponentDoubleProblemDE problem) {
        this.objectives = objectives;
        this.variables = variables;
        this.problem = problem;
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
       objectives[index] = value ;
    }

    @Override
    public double getObjective(int index) {
        return objectives[index];
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
        return new DoubleSolutionSubcomponentDE(objectives, variables, problem);
    }

    @Override
    public void setAttribute(Object id, Object value) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getAttribute(Object id) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
