package co.edu.unicauca.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDE;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

public class SaNSDEParametersAdjust extends ParametersAdjust
{

    public SaNSDEParametersAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run() throws IOException
    {
        
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-SaNSDE");
        PrintWriter pw = new PrintWriter(fw);
        
        try {
            for(int i = 0; i < combinations; i++)
            {
                String line = "";
                double cr1 = -1 ;
                double cr2 = -1;
                double f1 = -1;
                double f2 = -1;

                //for(int j = 0; j < values; j++)
                //{
                    cr1 = this.values[0][this.covering_array.getValue(i, 0)];
                    cr2 = this.values[1][this.covering_array.getValue(i, 1)];
                    f1 =  this.values[2][this.covering_array.getValue(i, 2)];
                    f2 =  this.values[3][this.covering_array.getValue(i, 3)];
                //}
                line += ((cr1+" "+cr2+" "+f1+" "+f2));

                double total_sum = 0;

                for(String ds:this.data_sets)
                {
                    String problemName =  "co.edu.unicauca.problem.training_testing."+ds;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
                    elm = (AbstractELMEvaluator)problem;
                    double train = 0;

                    for(int iterations = 0; iterations < total_iterations; iterations++)
                    {
                        SaNSDE algorithm = new   SaNSDEBuilder(problem,cr1,cr2,f1,f2)
                                                            .setPopulationSize(50)
                                                            .setMaxEvaluations(60)
                                                            .build();
                        new AlgorithmRunner.Executor(algorithm)
                        .execute() ;

                        DoubleSolution solution = (DoubleSolution) algorithm.getResult();
                        double evaluation = elm.test(solution);;
                        train += evaluation;
                        total_sum += evaluation;
                    }

                   line += (" "+(train/this.total_iterations));    
 
                }
                line +=(" " +(total_sum/(this.data_sets.size()*this.total_iterations)));
                line += "\n";
                pw.print(line);
                System.out.println(""+line);
            }
            pw.close();
            
        } catch (Exception e) {
            pw.close();
        }
        
        
    }
    
    public static void main(String[] args) throws Exception{
        SaNSDEParametersAdjust parameters = new SaNSDEParametersAdjust(5, 4, 10);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/SaNSDE-params");
        parameters.getCovering_array().load("src/resources-params/SaNSDE-ca");
        parameters.run();
    }
}
