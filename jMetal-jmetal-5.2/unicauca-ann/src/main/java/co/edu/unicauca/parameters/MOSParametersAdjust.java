package co.edu.unicauca.parameters;

import co.edu.unicauca.factory.MOSFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MOS;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSLS1Tecnique;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.FrobeniusComparator;

public class MOSParametersAdjust extends ParametersAdjust
{

    public MOSParametersAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run(int inicio, int end) throws IOException
    {
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-MOS-" + inicio + "-" + end);
        PrintWriter pw = new PrintWriter(fw);
        
        try {
            for(int i = inicio; i < end; i++)
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
                        
                        System.out.println("iteration: "+iterations);
                        MOSFactory factory = new MOSFactory();
                        MOSBuilder builder  =  (MOSBuilder) factory.getAlgorithm("MOS", AbstractELMEvaluator.EvaluatorType.TT, problem);
                        
                        builder.setE(E)
                                .setFE(FE);
                        MOS algorithm = builder.build();
                        /*MTSLS1Tecnique mts_exec = new MTSLS1Tecnique(new MTS_LS1Builder(problem)
                        .setPopulationSize(5)
                        .setBonus1(10)
                        .setBonus2(1));
                        SolisAndWetsTecnique sw_exec = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem));
                        MOS algorithm = new   MOSBuilder(problem)
                                                .addTecnique(mts_exec)
                                                .addTecnique(sw_exec)
                                                .setFE(FE)
                                                .setE(E)
                                                .setMaxEvaluations(3000)
                                                .setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
                                                .build();*/
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
        MOSParametersAdjust parameters = new MOSParametersAdjust(5, 2, 30);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/MOS-params");
        parameters.getCovering_array().load("src/resources-"
                + "params/SolisAndWets-ca");
        parameters.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
