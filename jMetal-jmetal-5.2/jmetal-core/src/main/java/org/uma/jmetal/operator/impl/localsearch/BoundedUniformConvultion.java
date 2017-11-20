package org.uma.jmetal.operator.impl.localsearch;

import org.uma.jmetal.operator.Tweak;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class BoundedUniformConvultion extends Tweak<DoubleSolution>
{
    private double probability;
    private double radius;
    private JMetalRandom random;

    public BoundedUniformConvultion(double probability, double radius) {
        this.probability = probability;
        this.radius = radius;
        random = JMetalRandom.getInstance();
    }
    
    @Override
    public void tweak(DoubleSolution solution) 
    {
        int n = solution.getNumberOfVariables();
        
        for(int i = 0; i < n; i++)
        {
            if (probability >= random.nextDouble(0, 1))
            {
                double value = solution.getVariableValue(i);
                double x = 0;
                
                do
                {
                    x = random.nextDouble(-radius, radius);
                }while((value + x <= solution.getLowerBound(i)) && (value + x >= solution.getUpperBound(i)));
                
                solution.setVariableValue(i, value + x);
            }
        }        
    }
    
}
