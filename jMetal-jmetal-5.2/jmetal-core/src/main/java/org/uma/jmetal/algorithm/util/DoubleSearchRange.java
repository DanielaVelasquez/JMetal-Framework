package org.uma.jmetal.algorithm.util;

import java.util.ArrayList;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class DoubleSearchRange extends SearchRange<DoubleSolution, DoubleProblem>
{

    public DoubleSearchRange(DoubleProblem problem) {
        super(problem);
    }

    @Override
    public void create(DoubleSolution solution) 
    {
        this.search_range = new ArrayList();
        for(int i = 0; i < this.n; i++)
        {
            double value = (solution.getUpperBound(i) - solution.getLowerBound(i))/2;
            search_range.add(value);
        }
    }

    @Override
    public void update(DoubleSolution solution)
    {
        this.search_range.clear();
        for(int i = 0; i < this.n; i++)
        {
            double value = (solution.getUpperBound(i) - solution.getLowerBound(i))*0.4;
            search_range.add(value);
        }
    }
    
}
