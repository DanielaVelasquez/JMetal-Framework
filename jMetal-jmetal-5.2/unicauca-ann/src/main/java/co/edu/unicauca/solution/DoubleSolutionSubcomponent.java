/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.solution;

import co.edu.unicauca.problem.SubcomponentDoubleProblem;
import java.util.List;
import java.util.Map;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

/**
 *
 * @author danielavelasquezgarzon
 */
public class DoubleSolutionSubcomponent implements DoubleSolution
{

    
    private double[] objectives;
    private double[] variables;
    private SubcomponentDoubleProblem problem;

    public DoubleSolutionSubcomponent(double[] objectives, double[] variables, SubcomponentDoubleProblem problem) {
        this.objectives = objectives;
        this.variables = variables;
        this.problem = problem;
    }
    
    
    
    @Override
    public Double getLowerBound(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getUpperBound(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        return variables[problem.getIndex(index)];
    }

    @Override
    public void setVariableValue(int index, Double value) {
        variables[problem.getIndex(index)] = value ;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
