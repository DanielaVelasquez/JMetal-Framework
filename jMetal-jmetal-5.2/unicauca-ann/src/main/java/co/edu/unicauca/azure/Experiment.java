package co.edu.unicauca.azure;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_GBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.MemeticEDBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Experiment 
{
    private ConectaBD conectar;
    
    public static void main(String[] args) {
        Experiment exp = new Experiment();
        exp.correr(2);
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
                conectar = ConectaBD.getInstancia();
                conectar.modificacion("EXECUTE startTask @computerName=" + computador);
                ResultSet resultado = conectar.seleccion("" +
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
                    String nombreProblema = "co.edu.unicauca.problem." + tipo + "." + problema;
                    DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(nombreProblema);
                    Algorithm auxAlg = retornarAlgoritmo(algoritmo, tipo, problem);
                    JMetalRandom rndm = JMetalRandom.getInstance();
                    rndm.setSeed(semilla);
                    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(auxAlg).execute() ;
                    AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
                    long computingTime = algorithmRunner.getComputingTime();
                    DoubleSolution solution = (DoubleSolution) auxAlg.getResult();
                    double resultadoExecTrain = (1 - solution.getObjective(0));
                    double resultadoExecTest = p.test(solution);
                    String insertResultado = "INSERT INTO results VALUES(" + computingTime + ", " + resultadoExecTrain + ", " + resultadoExecTest + ", " + runId + ", " + semilla + ")";
                    conectar.modificacion(insertResultado);
                }
            }
            catch(Exception ex)
            {
                
            }
        }
    } 
    
    public Algorithm retornarAlgoritmo(String nombreAlgoritmo, String tipo, DoubleProblem problem)
    {
        Algorithm algAux = null;
        
        if(tipo.equals("cross_validation"))
        {
            switch(nombreAlgoritmo)
            {
                case "DECC_G":
                    algAux = new DECC_GBuilder(problem)
                        .setCycles(2)
                        .setPopulationSize(10)
                        .setSubcomponets(6)
                        .setFEs(2)
                        .setwFes(3)
                        .build();
                    break;
                case "MemeticED":
                    algAux = new MemeticEDBuilder(problem)
                        .setMaxEvaluations(3000)
                        .setPopulationSize(10)
                        .build();
                    break;
            }
        }
        else
        {
            switch(nombreAlgoritmo)
            {
                case "DECC_G":
                    algAux = new DECC_GBuilder(problem)
                        .setCycles(3)
                        .setPopulationSize(10)
                        .setSubcomponets(10)
                        .setFEs(6)
                        .setwFes(9)
                        .build();
                    break;
                case "MemeticED":
                    algAux = new MemeticEDBuilder(problem)
                        .setMaxEvaluations(3000)
                        .setPopulationSize(10)
                        .build();
                    break;
            }
        }
        
        return algAux;
    }
}