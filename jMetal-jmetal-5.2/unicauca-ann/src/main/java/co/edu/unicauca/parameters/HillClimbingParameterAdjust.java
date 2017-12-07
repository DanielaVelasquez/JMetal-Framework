package co.edu.unicauca.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbing;
import org.uma.jmetal.algorithm.singleobjective.hill_climbing.HillClimbingBuilder;
import org.uma.jmetal.operator.impl.localsearch.BoundedUniformConvultion;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

public class HillClimbingParameterAdjust extends ParametersAdjust
{

    public HillClimbingParameterAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run(int inicio, int end) throws IOException
    {
        
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-HillClimbing-" + inicio + "-" + end);
        PrintWriter pw = new PrintWriter(fw);
        
        try 
        {
            for(int i = inicio; i < end; i++)
            {
                String line = "";
                double mean = -1;
                double standarDeviation = -1;
                
                mean = this.values[0][this.covering_array.getValue(i, 0)];
                standarDeviation = this.values[1][this.covering_array.getValue(i, 1)];
                line += (mean+" "+standarDeviation);

                double total_sum = 0;

                for(String ds:this.data_sets)
                {
                    String problemName = "co.edu.unicauca.problem.training_testing." + ds;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
                    elm = (AbstractELMEvaluator)problem;
                    double train = 0;

                    for(int iterations = 0; iterations < total_iterations; iterations++)
                    {
                        HillClimbing algorithm = new HillClimbingBuilder(problem)
                            .setTweak(new BoundedUniformConvultion(mean, standarDeviation))
                            .setMaxEvaluations(3000)
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
        HillClimbingParameterAdjust parameters = new HillClimbingParameterAdjust(5, 2, 30);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/HillClimbing-params");
        parameters.getCovering_array().load("src/resources-params/HillClimbing-ca");
        parameters.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
