package co.edu.unicauca.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSHRH;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSTecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

public class MOSParametersAdjust extends ParametersAdjust
{

    public MOSParametersAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run() throws IOException
    {
        
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-MOS");
        PrintWriter pw = new PrintWriter(fw);
        
        try {
            for(int i = 0; i < combinations; i++)
            {
                String line = "";
                int FE = -1 ;
                double E = -1;

                FE = (int) this.values[0][this.covering_array.getValue(i, 0)];
                E = this.values[1][this.covering_array.getValue(i, 1)];
                
                line += ((FE+" "+E));

                double total_sum = 0;

                for(String ds:this.data_sets)
                {
                    String problemName =  "co.edu.unicauca.problem.training_testing."+ds;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
                    
                    elm = (AbstractELMEvaluator)problem;
                    
                    double train = 0;

                    for(int iterations = 0; iterations < total_iterations; iterations++)
                    {
                        HashMap<String, Object> mts_atributes = new MultipleTrajectorySearchBuilder(problem)
                                                .setLocalSearchTest(3)
                                                .setLocalSearch(75)
                                                .setNumberOfForeground(5)
                                                .setPopulationSize(5)
                                                .setLocalSearchBest(100)
                                                .getConfiguration();
                        
                        HashMap<String, Object> saw_atributes =new SolisAndWetsBuilder(problem, null)
                                                               .getConfiguration();
                
                        MTSTecnique mts_exec = new MTSTecnique(mts_atributes);
                        SolisAndWetsTecnique sw_exec = new SolisAndWetsTecnique(saw_atributes);
                        MOSHRH algorithm = new   MOSBuilder(problem)
                                                                    .addTecnique(mts_exec)
                                                                    .addTecnique(sw_exec)
                                                                    .setFE(FE)
                                                                    .setE(E)
                                                                    .setMaxEvaluations(3000)
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
        MOSParametersAdjust parameters = new MOSParametersAdjust(5, 2, 10);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/MOS-params");
        parameters.getCovering_array().load("src/resources-"
                + "params/SolisAndWets-ca");
        parameters.run();
    }
}
