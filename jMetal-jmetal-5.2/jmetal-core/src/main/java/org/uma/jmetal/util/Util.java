package org.uma.jmetal.util;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

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
    /**
     * Fills an array with an specific value
     * @param array array to fill
     * @param value value to set
     */
    public static void fillArrayWith(int[][] array, int value)
    {
        int rows = array.length;
        int cols = array[0].length;
        for(int i = 0; i < rows;i++)
            for(int j = 0; j< cols;j++)
                array[i][j] = value;
    }
    /**
     * Finds row index where a column  in a array has an specfific value
     * @param value value to find
     * @param array collection where to look
     * @param col column where the row index will be selected
     * @return a row index where the array has the value
     */
    public static int getIndexWhere(int value, int[][] array, int col)
    {
        int row = -1;
        int rows = array.length - 1;
        do
        {
            row = randomGenerator.nextInt(0, rows);
        }while(array[row][col] != value);
        return row;
    }
}
