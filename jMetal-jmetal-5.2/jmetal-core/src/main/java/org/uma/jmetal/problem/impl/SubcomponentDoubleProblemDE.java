package org.uma.jmetal.problem.impl;

import java.util.List;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DoubleSolutionSubcomponentDE;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class SubcomponentDoubleProblemDE implements DoubleProblem
{
    private static double LOWER_BOUND = 0;
    private static double UPPER_BOUND = 1;
    
    private List<Integer> index;
    private DoubleProblem original_problem;
    private DoubleSolution solution;

    
    public SubcomponentDoubleProblemDE(DoubleProblem original_problem)
    {
        
        this.original_problem = original_problem;
    }

    public List<Integer> getIndex() {
        return index;
    }
    
    public void setSolution(DoubleSolution solution) {
        this.solution = solution;
    }
    
    
    public void setIndex(List<Integer> index) {
        this.index = index;
    }
    
    public int getIndex(int i)
    {
        return this.index.get(i);
    }

    @Override
    public Double getLowerBound(int index) {
        return LOWER_BOUND;
    }

    @Override
    public Double getUpperBound(int index) {
       return UPPER_BOUND;
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
    
    public void multiply(DoubleSolution solution, DoubleSolution original_solution)
    {
        int j = 0;
        for(Integer i:index)
        {
            double value = original_solution.getVariableValue(i);
            double newValue = value * solution.getVariableValue(j);
            if(newValue>=original_problem.getLowerBound(i) && newValue<=original_problem.getUpperBound(j))
              original_solution.setVariableValue(i, newValue);
            j++;
        }
    }

    @Override
    public void evaluate(DoubleSolution solution)
    {
        DoubleSolutionSubcomponentDE s = (DoubleSolutionSubcomponentDE)solution;
        s.setCompleteSolution((DoubleSolution) this.solution.copy());
        DoubleSolution original_solution = s.getCompleteSolution();
        this.multiply(solution, original_solution);
        this.original_problem.evaluate(original_solution);
        
        
    }

    @Override
    public DoubleSolution createSolution() {
        JMetalRandom randomGenerator = JMetalRandom.getInstance();
        int size = index.size();
        double[] variables = new double[size];
        for(int i = 0; i < size; i++)
        {
            variables[i] = randomGenerator.nextDouble(LOWER_BOUND, UPPER_BOUND);
        }
        return new DoubleSolutionSubcomponentDE(variables,this, null);
    }
}
