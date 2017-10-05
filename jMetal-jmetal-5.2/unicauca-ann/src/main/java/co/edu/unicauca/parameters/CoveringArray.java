package co.edu.unicauca.parameters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class CoveringArray 
{
    /**
     * Number of possible values
     */
    protected int v;
    /**
     * Number of combinations
     */
    private int n;

    /**
     * Number of column
     */
    private int k;
    /**
     * Covering array
     */
    private int[][] covering_array;

    public CoveringArray(int k, int v) {
        this.k = k;
        this.v = v;
    }
    
    
    
    public void load(String file) throws FileNotFoundException, IOException
    {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        this.n = Integer.parseInt(br.readLine());
        
        this.covering_array = new int[this.n][this.k];

       String actualLine;
        int i = 0;

        while ((actualLine = br.readLine()) != null) {
            String[] aux = actualLine.split(" ");

            for (int j = 0; j < this.k; j++)
            {
                int value = -1;
                try {
                    value = Integer.parseInt(aux[j]);
                } catch (Exception e) {
                }
                this.covering_array[i][j] = value;
            }
            i++;
        }
    }
    
    public int getValue(int i, int j)
    {
        int value = this.covering_array[i][j];
        if(value == -1)
        {
            value = JMetalRandom.getInstance().nextInt(0, this.v - 1);
        }
        return value;
    }

    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }
    
    
}
