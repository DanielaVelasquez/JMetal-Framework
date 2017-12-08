package co.edu.unicauca.experiment;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.factory.AlgorithmFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import java.util.Comparator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.random_search.RandomSearchBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DEUnicaucaBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSLS1Tecnique;
import org.uma.jmetal.algorithm.singleobjective.solis_and_wets.SolisAndWetsBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.FrobeniusComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Experiment 
{
     //Sansde
    private final static double CR1_SANSDE = 0.3;
    private final static double F1_SANSDE = 0.6;
    private final static double CR2_SANSDE = 0.7;
    private final static double F2_SANSDE = 0.3;
    //DE
    private final static double CR_DE = 0.7;
    private final static double F_DE = 0.5;
    //Sade
    private final static double CR1_SADE = 0.6;
    private final static double F1_SADE = 0.5;
    private final static double CR2_SADE = 0.5;
    private final static double F2_SADE = 0.7;
    
    //solis
    private final static double RHO = 0.4;
    private final static int NEIGHBORHOOD = 12;
    
    private DataBaseConnection connection;
    
    
    
    public static void main(String[] args) 
    {
        Experiment exp = new Experiment();
        exp.correr(Integer.parseInt(args[0]));
    }
    
    public void correr(int computador)
    {
        String rta = ""; 
        String problema;
        String algoritmo;
        int semilla;
        int runId;
        String tipo;
        
        while(!rta.equals("FINISHED"))
        {            
            try
            {
                connection = DataBaseConnection.getInstancia();
                System.out.println("voy a pedir");
                connection.modificacion("EXECUTE startTask @computerName = " + computador + ";");
                System.out.println("reservé");
                
                ResultSet resultado = connection.seleccion("" +
                        "       SELECT name_problem, name_algorithm, seed_executing, run_id_executing, name_type\n" +
                        "	FROM executing\n" +
                        "	INNER JOIN run ON run_id_executing = id_run\n" +
                        "	INNER JOIN algorithm ON algorithm_run = id_algorithm\n" +
                        "	INNER JOIN problem ON problem_run = id_problem\n" +
                        "	INNER JOIN type ON type_run = id_type\n" +
                        "	WHERE computer_executing = " + computador + " and executing_executing = 1;"); 
                                
                
                if(!resultado.next())
                {
                    rta = "FINISHED";
                }
                
                if(!rta.equals("FINISHED"))
                {
                    problema = resultado.getString(1);
                    algoritmo = resultado.getString(2);
                    semilla = Integer.parseInt(resultado.getString(3));
                    runId = Integer.parseInt(resultado.getString(4));
                    tipo = resultado.getString(5);
                    AbstractELMEvaluator.EvaluatorType type = tipo.equals("cv")?AbstractELMEvaluator.EvaluatorType.CV:AbstractELMEvaluator.EvaluatorType.TT;
                    String nombreProblema = "co.edu.unicauca.problem." + (tipo.equals("cv")? "cross_validation":"training_testing") + "." + problema;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(nombreProblema);
                    Algorithm auxAlg = AlgorithmFactory.getAlgorithm(algoritmo, type, problem);
                    System.out.println("-----"+problema+"-----"+algoritmo+"-----"+semilla+"-----"+runId+"-----"+tipo);
                    JMetalRandom rndm = JMetalRandom.getInstance();
                    rndm.setSeed(semilla);
                    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(auxAlg).execute() ;

                    AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
                    long computingTime = algorithmRunner.getComputingTime();
                    DoubleSolution solution = (DoubleSolution) auxAlg.getResult();
                    double resultadoExecTrain = (1 - solution.getObjective(0));
                    double resultadoExecTest = p.test(solution);
                    String insertResultado = "INSERT INTO results VALUES(" + computingTime + ", " + resultadoExecTrain + ", " + resultadoExecTest + ", " + runId + ", " + semilla + ")";
                    connection.modificacion(insertResultado);
                }
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    } 

    
    /*public Algorithm retornarAlgoritmo(String nombreAlgoritmo, String tipo, DoubleProblem problem)
    {
        Algorithm algAux = null;
        Comparator<DoubleSolution> comparator = new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
        int evaluations_cv = 300;
        int evaluations_tt = 3000;
        double penalize_value = 0;
       
        if(tipo.equals("cv"))
        {
            switch(nombreAlgoritmo)
            {
                case "DECC_G":
                    algAux = new DECC_GBuilder(problem)
                        .setMaxEvaluations(evaluations_cv)
                        .setPopulationSize(10)
                        .setSubcomponets(6)
                        .setFEs(30)
                        .setwFes(40)
                        .setPenalizeValue(penalize_value)
                        .setComparator(comparator)
                        .setDEBuilder(new DEUnicaucaBuilder(problem)
                                      .setCrossover(new DifferentialEvolutionCrossover(CR_DE, F_DE, "rand/1/bin"))
                                      .setSelection(new DifferentialEvolutionSelection()))
                        .setSaNSDEBuilder(new SaNSDEBuilder(problem)
                                          .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                                          .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin")))
                        .build();
                    break;
                case "DE":
                    algAux = new DEUnicaucaBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(evaluations_cv)
                        .setPenalizeValue(penalize_value)
                        .setCrossover(new DifferentialEvolutionCrossover(CR_DE, F_DE, "rand/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .setComparator(comparator)
                        .build();
                    break;
                case "SaNSDE":
                    algAux = new SaNSDEBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(evaluations_cv)
                        .setPenalizeValue(penalize_value)
                        .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                        .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .setComparator(comparator)
                        .build();
                    break;
                case "MOS":
                    MTSLS1Tecnique mtsls1_exec = new MTSLS1Tecnique(new MTS_LS1Builder(problem)
                        .setPopulationSize(5)
                        .setBonus1(10)
                        .setBonus2(1));
                    
                    SolisAndWetsTecnique sw_exec1 = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem)
                        .setRho(RHO)
                        .setSizeNeighborhood(NEIGHBORHOOD));
                    
                    algAux = new MOSBuilder(problem)
                            .addTecnique(mtsls1_exec)
                            .addTecnique(sw_exec1)
                            .setFE(75)
                            .setE(0.15)
                            .setMaxEvaluations(evaluations_cv)
                            .setComparator(comparator)
                            .setPenalizeValue(penalize_value)
                            .build();
                    break;
                case "MTSLS1":
                    algAux = new MTS_LS1Builder(problem)
                        .setPopulationSize(10)
                        .setBonus1(10)
                        .setBonus2(1)
                        .setPenalizeValue(penalize_value)
                        .setMaxEvaluations(evaluations_cv)
                        .setComparator(comparator)
                        .build();
                    break;
                case "SolisAndWets":
                    algAux = new SolisAndWetsBuilder(problem)
                        .setRho(RHO)
                        .setSizeNeighborhood(NEIGHBORHOOD)
                        .setPenalizeValue(penalize_value)
                        .setMaxEvaluations(evaluations_cv)
                        .setComparator(comparator)
                        .build();
                    break;
                    
                case "MemeticED":
                    algAux = new MemeticEDBuilder(problem)
                        .setMaxEvaluations(evaluations_cv)
                        .setPopulationSize(10)
                        .build();
                    break;
                      
                case "Random":
                    algAux = new RandomSearchBuilder<>(problem)
                        .setMaxEvaluations(evaluations_cv)
                        .setComparator(comparator)
                        .build();
                    break;
                
                case "SaDE":
                    algAux = new SaDEBuilder(problem)
                                .setPopulationSize(10)
                                .setMaxEvaluations(evaluations_cv)
                                .setCrossoverOperator(new DifferentialEvolutionCrossover(CR1_SADE, F1_SADE, "rand/1/bin"))
                                .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SADE, F2_SADE, "current-to-best/1/bin"))
                                .setSelectionOperator(new DifferentialEvolutionSelection())
                                .setComparator(comparator)
                                .setPenalizeValue(penalize_value)
                                .build();
                    break;
                    
                
                    
                    //NO PONER 3000 sino 600
               
            }
        }
        else
        {
            switch(nombreAlgoritmo)
            {
                case "DECC_G":
                    algAux = new DECC_GBuilder(problem)
                        .setMaxEvaluations(evaluations_tt)
                        .setPopulationSize(10)
                        .setSubcomponets(10)
                        .setFEs(70)
                        .setwFes(100)
                        .setPenalizeValue(penalize_value)
                        .setDEBuilder(new DEUnicaucaBuilder(problem)
                                      .setCrossover(new DifferentialEvolutionCrossover(CR_DE, F_DE, "rand/1/bin"))
                                      .setSelection(new DifferentialEvolutionSelection()))
                        .setSaNSDEBuilder(new SaNSDEBuilder(problem)
                                          .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                                          .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin")))
                        .build();
                    break;
                case "SaDE":
                    algAux = new SaDEBuilder(problem)
                                .setPopulationSize(10)
                                .setMaxEvaluations(evaluations_tt)
                                .setPenalizeValue(penalize_value)
                                .setCrossoverOperator(new DifferentialEvolutionCrossover(CR1_SADE, F1_SADE, "rand/1/bin"))
                                .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SADE, F2_SADE, "current-to-best/1/bin"))
                                .setSelectionOperator(new DifferentialEvolutionSelection())
                                .setComparator(comparator)
                                 .build();
                    break;
                    
                case "MemeticED":
                    algAux = new MemeticEDBuilder(problem)
                        .setMaxEvaluations(evaluations_tt)
                        .setPopulationSize(10)
                        .build();
                    break;
                    
                case "SolisAndWets":
                    algAux = new SolisAndWetsBuilder(problem)
                        .setRho(RHO)
                        .setSizeNeighborhood(NEIGHBORHOOD)
                        .setMaxEvaluations(evaluations_tt)
                        .setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
                        .setPenalizeValue(penalize_value)
                        .build();
                    break;
                    
                case "MTSLS1":
                    algAux = new MTS_LS1Builder(problem)
                        .setPopulationSize(10)
                        .setBonus1(10)
                        .setBonus2(1)
                        .setPenalizeValue(penalize_value)
                        .setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
                        .setMaxEvaluations(evaluations_tt)
                        .build();
                    break;
                    
                case "DE":
                    algAux = new DEUnicaucaBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(evaluations_tt)
                        .setPenalizeValue(penalize_value)
                        .setCrossover(new DifferentialEvolutionCrossover(0.7, 0.5, "rand/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .build();
                    break;
                    
                case "SaNSDE":
                    algAux = new SaNSDEBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(evaluations_tt)
                        .setCrossover(new DifferentialEvolutionCrossover(CR1_SANSDE, F1_SANSDE, "rand/1/bin"))
                        .setCrossoverOperator2(new DifferentialEvolutionCrossover(CR2_SANSDE, F2_SANSDE, "current-to-best/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .setPenalizeValue(penalize_value)
                        .build();
                    break;
                    
                case "Random":
                    algAux = new RandomSearchBuilder(problem)
                        .setMaxEvaluations(evaluations_tt)
                        .build();
                    break;
                             
                    
                case "MOS":
                    MTSLS1Tecnique mtsls1_exec = new MTSLS1Tecnique(new MTS_LS1Builder(problem)
                        .setPopulationSize(5)
                        .setBonus1(10)
                        .setComparator(new FrobeniusComparator<>(FrobeniusComparator.Ordering.DESCENDING, FrobeniusComparator.Ordering.ASCENDING, 0))
                        .setBonus2(1));
                    
                    SolisAndWetsTecnique sw_exec1 = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem)
                        .setRho(RHO)
                        .setSizeNeighborhood(NEIGHBORHOOD));
                    
                    algAux =  new   MOSBuilder(problem)
                        .addTecnique(mtsls1_exec)
                        .addTecnique(sw_exec1)
                        .setFE(75)
                        .setE(0.15)
                        .setMaxEvaluations(evaluations_tt)
                        .setPenalizeValue(penalize_value)
                        .build();
                    break;
            }
        }
        
        return algAux;
    }*/
}