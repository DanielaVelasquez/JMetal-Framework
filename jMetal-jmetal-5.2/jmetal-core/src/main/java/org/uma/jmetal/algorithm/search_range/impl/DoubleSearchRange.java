package org.uma.jmetal.algorithm.search_range.impl;

import org.uma.jmetal.algorithm.search_range.SearchRange;
import java.util.ArrayList;
import java.util.List;
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
        this.divide();
        for(int i = 0; i < this.n; i++)
        {
            double value = (double)search_range.get(i);
            if(value < 1.e-3)
            {
                value = (solution.getUpperBound(i) - solution.getLowerBound(i))*0.4;
                search_range.set(i, value);
            }
                
        }
    }

    @Override
    protected void divide() {
        for(int i = 0; i < this.n; i++)
        {
            double value = (double)search_range.get(i)/(double)2;
            search_range.set(i, value);
        }
    }

    @Override
    public SearchRange copy() {
        DoubleSearchRange sr = new DoubleSearchRange(problem);
        List<Double> ranges = new ArrayList<>();
        ranges.addAll(search_range);
        sr.setSearchRange(ranges);
        return sr;
    }
    
}
