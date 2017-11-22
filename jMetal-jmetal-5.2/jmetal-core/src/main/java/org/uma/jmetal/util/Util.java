package org.uma.jmetal.util;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * 
 * @author danielavelasquezgarzon
 */
public class Util 
{
    private static JMetalRandom randomGenerator = JMetalRandom.getInstance();
    
    /**
     * Creates a random pemutation from 0 to n-1
     * @param n maximun value to create permutacion
     * @return List with a random permutation with numbers from 0 to n-1
     */
    public static List<Integer> createRandomPermutation(int n)
    {
        List<Integer> list = new ArrayList<>();
        while(list.size() != n)
        {
            int value = randomGenerator.nextInt(0, n - 1);
            if(!list.contains(value))
                list.add(value);
        }
        return list;
    }
}
