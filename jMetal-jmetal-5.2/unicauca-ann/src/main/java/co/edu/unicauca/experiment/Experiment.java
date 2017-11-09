package co.edu.unicauca.experiment;

import co.edu.unicauca.database.DataBaseConnection;
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
    private DataBaseConnection connection;
    
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
                connection = DataBaseConnection.getInstancia();
                connection.modificacion("EXECUTE startTask @computerName = " + computador + ";");
                ResultSet resultado = connection.seleccion("" +
                        "       SELECT d.dat_name, a.alg_name, s.see_value, r.run_id, dt.dat_type_name\n" +
                        "	FROM run r\n" +
                        "		INNER JOIN algorithm a ON a.alg_id = r.alg_id\n" +
                        "		INNER JOIN dataset d ON d.dat_id = r.dat_id\n" +
                        "		INNER JOIN seed s ON s.see_id = r.see_id\n" +
                        "		INNER JOIN datasettype dt ON dt.dat_type_id = r.dat_type_id\n" +
                        "	WHERE r.run_pc = " + computador + " AND r.run_status = 1;");
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
                    JMetalRandom rndm = JMetalRandom.getInstance();
                    rndm.setSeed(semilla);
                    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(auxAlg).execute() ;
                    AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
                    long computingTime = algorithmRunner.getComputingTime();
                    DoubleSolution solution = (DoubleSolution) auxAlg.getResult();
                    double resultadoExecTrain = (1 - solution.getObjective(0));
                    double resultadoExecTest = p.test(solution);
                    
                    String consulta = "SELECT * FROM results WHERE run_id = " + runId;
                    ResultSet result = connection.seleccion(consulta);
                    if(!result.next())
                    {
                        String insertResultado = "INSERT INTO results VALUES(" + runId + ", " + computingTime + ", " + resultadoExecTrain + ", " + resultadoExecTest + ");"
                                               + "UPDATE run SET run_status = 2 WHERE run_id = " + runId + ";";
                        connection.modificacion(insertResultado);
                    }
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