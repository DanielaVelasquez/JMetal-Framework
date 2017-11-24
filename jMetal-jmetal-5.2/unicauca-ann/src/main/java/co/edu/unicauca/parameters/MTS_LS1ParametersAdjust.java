package co.edu.unicauca.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearch;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

public class MTS_LS1ParametersAdjust extends ParametersAdjust
{

    public MTS_LS1ParametersAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run(int inicio, int end) throws IOException
    {
        
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-mts-" + inicio + "-" + end);
        PrintWriter pw = new PrintWriter(fw);
        
        try {
            for(int i = inicio; i < end; i++)
            {
                String line = "";
                int local_search_test = -1 ;
                int local_search = -1;
                int number_of_foregrounds = -1;
                int population_size = -1;
                int local_search_best = -1;

                //for(int j = 0; j < values; j++)
                //{
                    local_search_test = (int) this.values[0][this.covering_array.getValue(i, 0)];
                    local_search = (int) this.values[1][this.covering_array.getValue(i, 1)];
                    number_of_foregrounds = (int) this.values[2][this.covering_array.getValue(i, 2)];
                    population_size = (int) this.values[3][this.covering_array.getValue(i, 3)];
                    local_search_best = (int) this.values[4][this.covering_array.getValue(i, 4)];
                //}
                line += ((local_search_test+" "+local_search+" "+number_of_foregrounds
                                                                +" "+population_size+" "+local_search_best));

                double total_sum = 0;

                for(String ds:this.data_sets)
                {
                    String problemName =  "co.edu.unicauca.problem.training_testing."+ds;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

                    elm = (AbstractELMEvaluator)problem;
                    double train = 0;

                    for(int iterations = 0; iterations < total_iterations; iterations++)
                    {
                        System.out.println("----------------------------------------------");
                        System.out.println("Problem: "+problemName+ "i: "+iterations);
                        
                        MultipleTrajectorySearch algorithm = new   MultipleTrajectorySearchBuilder(problem)
                                                            .setMaxEvaluations(3000)
                                                            .setPopulationSize(population_size)
                                                            .setLocalSearch(local_search)
                                                            .setLocalSearchBest(local_search_best)
                                                            .setNumberOfForeground(number_of_foregrounds)
                                                            .setLocalSearchTest(local_search_test)
                                                            .build();
                        new AlgorithmRunner.Executor(algorithm)
                        .execute() ;

                        DoubleSolution solution = (DoubleSolution) algorithm.getResult();
                        double evaluation = elm.test(solution);
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
        MTS_LS1ParametersAdjust parameters = new MTS_LS1ParametersAdjust(3, 5, 30);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/mts-params");
        parameters.getCovering_array().load("src/resources-params/mts-ca");
        parameters.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
