package co.edu.unicauca.problem;

import co.edu.unicauca.solution.DoubleSolutionSubcomponentDE;
import java.util.List;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;


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

    @Override
    public void evaluate(DoubleSolution solution)
    {
        DoubleSolutionSubcomponentDE s = (DoubleSolutionSubcomponentDE)solution;
        DoubleSolution original_solution = (DoubleSolution) this.solution.copy();
        int j = 0;
        for(Integer i:index)
        {
            double value = original_solution.getVariableValue(i);
            double newValue = value * solution.getVariableValue(j);
            original_solution.setVariableValue(i, newValue);
            j++;
        }
        this.original_problem.evaluate(original_solution);
        this.solution.setObjective(0, original_solution.getObjective(0));
    }

    @Override
    public DoubleSolution createSolution() {
        return original_problem.createSolution();
    }
}
