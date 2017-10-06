package co.edu.unicauca.parameters;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolution;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolutionBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDE;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

public class DEParametersAdjust extends ParametersAdjust
{

    public DEParametersAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run() throws IOException
    {
        
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-DE");
        PrintWriter pw = new PrintWriter(fw);
        
        try {
            for(int i = 0; i < combinations; i++)
            {
                String line = "";
                double cr = -1 ;
                double f = -1;

                //for(int j = 0; j < values; j++)
                //{
                    cr = this.values[0][this.covering_array.getValue(i, 0)];
                    f = this.values[1][this.covering_array.getValue(i, 1)];
                //}
                line += ((cr+" "+f));

                double total_sum = 0;

                for(String ds:this.data_sets)
                {
                    String problemName =  "co.edu.unicauca.problem.training_testing."+ds;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

                    double train = 0;

                    for(int iterations = 0; iterations < total_iterations; iterations++)
                    {
                        DifferentialEvolutionCrossover crossoverOperator = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin");
                        DifferentialEvolution algorithm = new   DifferentialEvolutionBuilder(problem)
                                                            .setPopulationSize(50)
                                                            .setMaxEvaluations(60)
                                                            .setCrossover(crossoverOperator)
                                                            .build();
                        new AlgorithmRunner.Executor(algorithm)
                        .execute() ;

                        DoubleSolution solution = (DoubleSolution) algorithm.getResult();
                        train += (1-solution.getObjective(0));
                        total_sum += (1-solution.getObjective(0));
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
        DEParametersAdjust parameters = new DEParametersAdjust(5, 4, 10);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/de-params");
        parameters.getCovering_array().load("src/resources-params/de-ca");
        parameters.run();
    }
}
