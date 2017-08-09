package org.uma.jmetal.util.pseudorandom.impl;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class RandomDistribution 
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Next gaussian value stored
     */
    private double nextNextGaussian;
    /**
     * Determines if there is a next gaussian value stored
     */
    private boolean haveNextGaussian;
    
    private static RandomDistribution instance;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    /**
     * Creates a new RandomDistribution
     */
    private RandomDistribution()
    {
        haveNextGaussian = false;
    }
    /**
     * Obtains the unique instace object
     * @return random distribution object
     */
    public static RandomDistribution getInstance()
    {
        if(instance == null)
        {
            instance = new RandomDistribution();
        }
        return instance;
    }
    
    /**
     * Finds next gaussian with a given mean and standar deviation
     * @param mean desired mean
     * @param sd desired standar deviation 
     * @return next gaussian
     */
    public double nextGaussian(double mean, double sd)
    {
        return nextGaussian() * sd + mean;
        
    }
    /**
     * Finds next gaussian with mean 0 and standar deviation 1
     * @return next gaussian value
     */
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
    /**
     * Finds cauchy value according to the given values 
     * @param x0 location parameter, specifying the location of the peak of the distribution
     * @param y  scale parameter which specifies the half-width at half-maximum 
     * @return cauchy value 
     */
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
    
}
