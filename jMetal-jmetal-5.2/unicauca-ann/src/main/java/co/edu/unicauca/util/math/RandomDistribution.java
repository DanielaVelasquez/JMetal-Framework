package co.edu.unicauca.util.math;

import java.util.Random;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

public class RandomDistribution 
{
    private double nextNextGaussian;
    private boolean haveNextGaussian;
    
    private static RandomDistribution instance;
    
    public double nextGaussian(double mean, double sd)
    {
        return nextGaussian() * sd + mean;
        
    }
    public double nextGaussian()
    {
        JMetalRandom randomGenerator = JMetalRandom.getInstance();
        if(haveNextGaussian)
        {
            haveNextGaussian = false;
            return nextNextGaussian;
        }
        else
        {
            double v1, v2, s;
            do
            {
                v1 = 2 * randomGenerator.nextDouble() - 1;
                v2 = 2 * randomGenerator.nextDouble() - 1;
                s = v1 * v1 + v2 * v2;
            }while(s >= 1 || s == 0);
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
            nextNextGaussian = v2 * multiplier;
            haveNextGaussian = true;
            return v1 * multiplier;
        }
        
    }
    public double nextCauchy(double x0, double y)
    {
        JMetalRandom randomGenerator = JMetalRandom.getInstance();
        double x;
        double den;
        do
        {
            x = randomGenerator.nextDouble();
            den = Math.PI * (((x - x0)*(x - x0)) + (y * y));
        }while(den == 0);
        
        return y / den;
    }
    private RandomDistribution()
    {
        haveNextGaussian = false;
    }
    
    public static RandomDistribution getInstance()
    {
        if(instance == null)
        {
            instance = new RandomDistribution();
        }
        return instance;
    }
}
