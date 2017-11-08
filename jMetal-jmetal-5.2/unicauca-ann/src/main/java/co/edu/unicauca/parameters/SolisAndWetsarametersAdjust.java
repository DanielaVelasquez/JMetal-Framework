package co.edu.unicauca.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWets;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

public class SolisAndWetsarametersAdjust extends ParametersAdjust
{

    public SolisAndWetsarametersAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run() throws IOException
    {
        
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-SolisAndWets");
        PrintWriter pw = new PrintWriter(fw);
        
        try 
        {
            for(int i = 0; i < combinations; i++)
            {
                String line = "";
                double rho = -1;
                int sizeNeighborhood = -1;
                
                rho = this.values[0][this.covering_array.getValue(i, 0)];
                sizeNeighborhood = (int)(this.values[1][this.covering_array.getValue(i, 1)]);
                line += (rho+" "+sizeNeighborhood);

                double total_sum = 0;

                for(String ds:this.data_sets)
                {
                    String problemName = "co.edu.unicauca.problem.training_testing." + ds;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
                    elm = (AbstractELMEvaluator)problem;
                    double train = 0;

                    for(int iterations = 0; iterations < total_iterations; iterations++)
                    {
                        SolisAndWets algorithm = new SolisAndWetsBuilder(problem, new ObjectiveComparator<DoubleSolution>(0,ObjectiveComparator.Ordering.ASCENDING))
                                                            .setPopulationSizeNeighborhoodSize(sizeNeighborhood)
                                                            .setRho(rho)
                                                            .setNumEFOs(3000)
                                                            .build();
                        
                        new AlgorithmRunner.Executor(algorithm).execute() ;
                        DoubleSolution solution = (DoubleSolution) algorithm.getResult();
                        double evaluation = elm.test(solution);
                        train += evaluation;
                        total_sum += evaluation;
                    }

                   line += (" " + (train / this.total_iterations));
                }
                
                line += (" " + (total_sum / (this.data_sets.size() * this.total_iterations)));
                line += "\n";
                pw.print(line);
                System.out.println("" + line);
            }
            pw.close();            
        }
        catch (Exception e) 
        {
            pw.close();
        }
    }
    
    public static void main(String[] args) throws Exception{
        SolisAndWetsarametersAdjust parameters = new SolisAndWetsarametersAdjust(5, 2, 10);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/SolisAndWets-params");
        parameters.getCovering_array().load("src/resources-params/SolisAndWets-ca");
        parameters.run();
    }
}
