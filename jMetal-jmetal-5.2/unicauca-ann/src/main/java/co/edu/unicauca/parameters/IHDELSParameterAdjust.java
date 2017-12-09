package co.edu.unicauca.parameters;

import co.edu.unicauca.factory.IHDELSFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.uma.jmetal.algorithm.singleobjective.ihdels.IHDELS;
import org.uma.jmetal.algorithm.singleobjective.ihdels.IHDELSBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

public class IHDELSParameterAdjust extends ParametersAdjust
{

    public IHDELSParameterAdjust(int v, int k, int total_iterations) {
        super(v, k, total_iterations);
    }

    @Override
    public void run(int inicio, int end) throws IOException
    {        
        int combinations = this.covering_array.getN();
        int values = this.covering_array.getK();
        
        FileWriter fw = new FileWriter("results-IHDELS-" + inicio + "-" + end);
        PrintWriter pw = new PrintWriter(fw);
        
        try 
        {
            for(int i = inicio; i < end; i++)
            {
                String line = "";
                int fe_de = -1;
                int fe_ls = -1;
                double a = -1;
                double b = -1;
                int restart = -1;
                double threshold = -1;
                
                fe_de = (int)(this.values[0][this.covering_array.getValue(i, 0)]);
                fe_ls = (int) (this.values[1][this.covering_array.getValue(i, 1)]);
                a = this.values[1][this.covering_array.getValue(i, 2)];
                b = this.values[1][this.covering_array.getValue(i, 3)];
                restart = (int) (this.values[1][this.covering_array.getValue(i, 4)]);
                threshold = this.values[1][this.covering_array.getValue(i, 5)];
                line += (fe_de+" "+fe_ls+" "+a+" "+b+" "+restart+" "+threshold);

                double total_sum = 0;

                for(String ds:this.data_sets)
                {
                    String problemName = "co.edu.unicauca.problem.training_testing." + ds;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
                    elm = (AbstractELMEvaluator)problem;
                    double train = 0;
                    
                    /*HillClimbingBuilder hcBuilder = new HillClimbingBuilder(problem)
                        .setTweak(new BoundedUniformConvultion(0.55, 0.3));
                    LocalSearch hillClimbing = new LSHillClimbing(hcBuilder);

                    MTS_LS1Builder mtsls1Builder = new MTS_LS1Builder(problem);
                    LocalSearch mtsls1 = new LSMTS_LS1(mtsls1Builder);*/
                    
                    //SaDEBuilder builder = new SaDEBuilder(problem);

                    for(int iterations = 0; iterations < total_iterations; iterations++)
                    {
                        IHDELSFactory factory = new IHDELSFactory();
                        IHDELSBuilder builder = (IHDELSBuilder) factory.getAlgorithm("IHDELS", AbstractELMEvaluator.EvaluatorType.TT, problem);
                        builder.setFE_DE(fe_de)
                               .setFE_LS(fe_ls)
                               .setPopulation_size(10)
                               .setReStart(restart)
                               .setSearchDomain(a, b)
                               .setThreshold(threshold);
                        IHDELS algorithm = builder.build();
                        /*IHDELS algorithm = new IHDELSBuilder(problem)
                            .addLocalSearch(mtsls1)
                            .addLocalSearch(hillClimbing)
                            .setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
                            .setMaxEvaluations(3000)
                            .setFE_DE(fe_de)
                            .setFE_LS(fe_ls)
                            .setPopulation_size(10)
                            .setReStart(restart)
                            .setSearchDomain(a, b)
                            .setThreshold(threshold)
                            .setSaDEBuilder(builder)
                             
                            .build();*/
                        
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
        IHDELSParameterAdjust parameters = new IHDELSParameterAdjust(3, 6, 30);
        parameters.readDataSets("src/resources-params/mts-datasets");
        parameters.load("src/resources-params/HillClimbing-params");
        parameters.getCovering_array().load("src/resources-params/HillClimbing-ca");
        parameters.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
