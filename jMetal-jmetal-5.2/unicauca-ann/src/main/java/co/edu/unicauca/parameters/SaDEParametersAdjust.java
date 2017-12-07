package co.edu.unicauca.parameters;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDE;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.FrobeniusComparator;

public class SaDEParametersAdjust extends ParametersAdjust
{

    public SaDEParametersAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run(int inicio, int end) throws IOException
    {
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-SaDE-" + inicio + "-" + end);
        PrintWriter pw = new PrintWriter(fw);
        
        try {
            for(int i = inicio; i < end; i++)
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
                        SaDE algorithm = new SaDEBuilder(problem)
                            .setCrossoverOperator(new DifferentialEvolutionCrossover(cr1, f1, "rand/1/bin"))
                            .setCrossoverOperator2(new DifferentialEvolutionCrossover(cr2, f2, "current-to-best/1/bin"))
                            .setPopulationSize(10)
                            .setMaxEvaluations(3000)
                            .setComparator(new FrobeniusComparator(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
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
        SaDEParametersAdjust parameters = new SaDEParametersAdjust(5, 4, 30);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/SaDE-params");
        parameters.getCovering_array().load("src/resources-params/SaDE-ca");
        parameters.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
