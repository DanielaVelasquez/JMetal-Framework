package co.edu.unicauca.experiment;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.random_search.RandomSearchBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DEFrobeniusBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaNSDEBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MOSBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSLS1Tecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.MTSTecnique;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsBuilder;
import org.uma.jmetal.algorithm.singleobjective.mos.SolisAndWetsTecnique;
import org.uma.jmetal.algorithm.singleobjective.mts.MTS_LS1Builder;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Experiment 
{
    private DataBaseConnection connection;
    
    public static void main(String[] args) {
        Experiment exp = new Experiment();
        exp.correr(78);
    }
    
    public void correr(int computador)
    {
        String rta = ""; 
        String problema;
        String algoritmo;
        int semilla;
        int runId = -1;
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
                    String nombreProblema = "co.edu.unicauca.problem." + (tipo.equals("cv")? "cross_validation":"training_testing") + "." + problema;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(nombreProblema);
                    Algorithm auxAlg = retornarAlgoritmo(algoritmo, tipo, problem);
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

    
    public Algorithm retornarAlgoritmo(String nombreAlgoritmo, String tipo, DoubleProblem problem)
    {
        Algorithm algAux = null;
        
        if(tipo.equals("cv"))
        {
            switch(nombreAlgoritmo)
            {
                case "DECC_G":
                    algAux = new DECC_GBuilder(problem)
                        .setMaxEvaluations(600)
                        .setPopulationSize(10)
                        .setSubcomponets(6)
                        .setFEs(30)
                        .setwFes(40)
                        .build();
                    break;
                    
                case "MemeticED":
                    algAux = new MemeticEDBuilder(problem)
                        .setMaxEvaluations(600)
                        .setPopulationSize(10)
                        .build();
                    break;
                    
                case "MOS":
                    MTSTecnique mts_exec = new MTSTecnique(new MultipleTrajectorySearchBuilder(problem)
                        .setLocalSearchTest(3)
                        .setLocalSearch(75)
                        .setNumberOfForeground(5)
                        .setPopulationSize(5)
                        .setLocalSearchBest(100)
                        .setBonus1(10)
                        .setBonus2(1));
                    
                    SolisAndWetsTecnique sw_exec = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem)
                        .setRho(0.5)
                        .setSizeNeighborhood(12));
                    
                    algAux = new MOSBuilder(problem)
                        .addTecnique(mts_exec)
                        .addTecnique(sw_exec)
                        .setFE(75)
                        .setE(0.15)
                        .setMaxEvaluations(600)
                        .build();                    
                    break;
                    
                case "SolisAndWets":
                    algAux = new SolisAndWetsBuilder(problem)
                        .setRho(0.5)
                        .setSizeNeighborhood(12)
                        .setMaxEvaluations(600)
                        .build();
                    break;
                    
                case "MTSLS1":
                    algAux = new MTS_LS1Builder(problem)
                        .setPopulationSize(10)
                        .setBonus1(10)
                        .setBonus2(1)
                        .setPenalizeValue(1)
                        .setMaxEvaluations(600)
                        .build();
                    break;
                    
                case "DE":
                    algAux = new DEFrobeniusBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(600)
                        .setPenalizeValue(1)
                        .setCrossover(new DifferentialEvolutionCrossover(0.5, 0.6, "rand/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .build();
                    break;
                    
                case "SaNSDE":
                    algAux = new SaNSDEBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(600)
                        .setCrossover(new DifferentialEvolutionCrossover(0.4, 0.6, "rand/1/bin"))
                        .setCrossoverOperator2(new DifferentialEvolutionCrossover(0.5, 0.4, "current-to-best/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .build();
                    break;
                    
                case "Random":
                    algAux = new RandomSearchBuilder<>(problem)
                        .setMaxEvaluations(600)
                        .build();
                    break;
                                   
                case "MTS":
                    algAux = new MultipleTrajectorySearchBuilder(problem)
                        .setLocalSearchTest(3)
                        .setLocalSearch(75)
                        .setNumberOfForeground(5)
                        .setPopulationSize(5)
                        .setLocalSearchBest(100)
                        .setMaxEvaluations(600)
                        .setBonus1(10)
                        .setBonus2(1)
                        .build();
                    break;
                    
                case "MOS1":
                    MTSLS1Tecnique mtsls1_exec = new MTSLS1Tecnique(new MTS_LS1Builder(problem)
                        .setPopulationSize(5)
                        .setBonus1(10)
                        .setBonus2(1));
                    
                    SolisAndWetsTecnique sw_exec1 = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem)
                        .setRho(0.5)
                        .setSizeNeighborhood(12));
                    
                    algAux = new MOSBuilder(problem)
                            .addTecnique(mtsls1_exec)
                            .addTecnique(sw_exec1)
                            .setFE(75)
                            .setE(0.15)
                            .setMaxEvaluations(600)
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
                        .setMaxEvaluations(3000)
                        .setPopulationSize(10)
                        .setSubcomponets(10)
                        .setFEs(70)
                        .setwFes(100)
                        .build();
                    break;
                    
                case "MemeticED":
                    algAux = new MemeticEDBuilder(problem)
                        .setMaxEvaluations(3000)
                        .setPopulationSize(10)
                        .build();
                    break;
                    
                 case "MOS":
                    MTSTecnique mts_exec = new MTSTecnique(new MultipleTrajectorySearchBuilder(problem)
                        .setLocalSearchTest(3)
                        .setLocalSearch(75)
                        .setNumberOfForeground(5)
                        .setPopulationSize(5)
                        .setLocalSearchBest(100)
                        .setBonus1(10)
                        .setBonus2(1));
                    
                    SolisAndWetsTecnique sw_exec = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem)
                        .setRho(0.5)
                        .setSizeNeighborhood(12));
                    
                    algAux = new MOSBuilder(problem)
                        .addTecnique(mts_exec)
                        .addTecnique(sw_exec)
                        .setFE(75)
                        .setE(0.15)
                        .setMaxEvaluations(3000)
                        .build();
                    break;
                    
                case "SolisAndWets":
                    algAux = new SolisAndWetsBuilder(problem)
                        .setRho(0.5)
                        .setSizeNeighborhood(12)
                        .setMaxEvaluations(3000)
                        .build();
                    break;
                    
                case "MTSLS1":
                    algAux = new MTS_LS1Builder(problem)
                        .setPopulationSize(10)
                        .setBonus1(10)
                        .setBonus2(1)
                        .setPenalizeValue(1)
                        .setMaxEvaluations(3000)
                        .build();
                    break;
                    
                case "DE":
                    algAux = new DEFrobeniusBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(3000)
                        .setPenalizeValue(1)
                        .setCrossover(new DifferentialEvolutionCrossover(0.5, 0.6, "rand/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .build();
                    break;
                    
                case "SaNSDE":
                    algAux = new SaNSDEBuilder(problem)
                        .setPopulationSize(10)
                        .setMaxEvaluations(3000)
                        .setCrossover(new DifferentialEvolutionCrossover(0.4, 0.6, "rand/1/bin"))
                        .setCrossoverOperator2(new DifferentialEvolutionCrossover(0.5, 0.4, "current-to-best/1/bin"))
                        .setSelection(new DifferentialEvolutionSelection())
                        .build();
                    break;
                    
                case "Random":
                    algAux = new RandomSearchBuilder(problem)
                        .setMaxEvaluations(3000)
                        .build();
                    break;
                                   
                case "MTS":
                    algAux = new MultipleTrajectorySearchBuilder(problem)
                        .setLocalSearchTest(3)
                        .setLocalSearch(75)
                        .setNumberOfForeground(5)
                        .setPopulationSize(5)
                        .setLocalSearchBest(100)
                        .setMaxEvaluations(3000)
                        .setBonus1(10)
                        .setBonus2(1)
                        .build();
                    break;
                    
                case "MOS1":
                    MTSLS1Tecnique mtsls1_exec = new MTSLS1Tecnique(new MTS_LS1Builder(problem)
                        .setPopulationSize(5)
                        .setBonus1(10)
                        .setBonus2(1));
                    
                    SolisAndWetsTecnique sw_exec1 = new SolisAndWetsTecnique(new SolisAndWetsBuilder(problem)
                        .setRho(0.5)
                        .setSizeNeighborhood(12));
                    
                    algAux =  new   MOSBuilder(problem)
                        .addTecnique(mtsls1_exec)
                        .addTecnique(sw_exec1)
                        .setFE(75)
                        .setE(0.15)
                        .setMaxEvaluations(3000)
                        .build();
                    break;
            }
        }
        
        return algAux;
    }
}