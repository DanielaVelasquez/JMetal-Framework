package co.edu.unicauca.parameters;

import co.edu.unicauca.elm.ELM;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class ParametersAdjust 
{
    protected static int SPACES = 30;
    protected static int DECIMALS = 3;
    /**
     * Possibles values for every entry
     * row - entry
     * col - values for i-th entry
     */
    protected double[][] values;
    /**
     * Data sets to evaluate
     */
    protected List<String> data_sets;
    /**
     * Number of possible values
     */
    protected int v;
    /**
     * Number of columns 
     */
    protected int k;
    /**
     * Combinations to be made
     */
    protected CoveringArray covering_array;
    
    protected AbstractELMEvaluator elm;
    
    protected  int total_iterations;

    public ParametersAdjust(int v, int k, int total_iterations) {
        this.v = v;
        this.k = k;
        this.total_iterations = total_iterations;
        this.covering_array = new CoveringArray(k,v);
    }
    
    
    
    public void load(String file) throws FileNotFoundException, IOException
    {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        
        this.values = new double[this.k][this.v];

       String actualLine;
        int i = 0;

        while ((actualLine = br.readLine()) != null) {
            String[] aux = actualLine.split(" ");

            for (int j = 0; j < this.v; j++)
            {
                this.values[i][j] = Double.parseDouble(aux[j]);
            }
            i++;
        }
    }
    public void readDataSets(String file) throws FileNotFoundException, IOException
    {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        
        this.data_sets = new ArrayList<>();
        String actualLine;
        int i = 0;

        while ((actualLine = br.readLine()) != null) {
            this.data_sets.add(actualLine);
        }
    }
    
    protected void printDataSets()
    {
        for(String ds: this.data_sets)
        {
            System.out.printf("%"+SPACES+".s", ds);    
        }
    }

    public CoveringArray getCovering_array() {
        return covering_array;
    }
    
    public abstract void run() throws Exception;
}
