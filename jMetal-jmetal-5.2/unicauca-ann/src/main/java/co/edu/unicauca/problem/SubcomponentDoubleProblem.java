package co.edu.unicauca.problem;

import java.util.List;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;


public class SubcomponentDoubleProblem implements DoubleProblem
{
    private List<Integer> index;
    private DoubleProblem original_problem;

    
    public SubcomponentDoubleProblem(List<Integer> index)
    {
        this.index = index;
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
        this.original_problem.evaluate(solution);
    }

    @Override
    public DoubleSolution createSolution() {
        return original_problem.createSolution();
    }

    
}
