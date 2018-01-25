package co.edu.unicauca.experiment;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.exec.experiment.Run;
import co.edu.unicauca.factory.algorithm.AlgorithmFactory;
import co.edu.unicauca.factory.parameters.DataBaseParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Experiment 
{
    private DataBaseConnection connection;
    
    private Run run;
    private int computer;
    
    public Experiment(Run run, int pc) throws Exception
    {
        this.run = run;
        this.computer = pc;
        this.correr();
    }

    public void correr() throws Exception
    {
        String rta = ""; 
        String problema;
        String algoritmo;
        int semilla;
        int runId;
        String tipo;
        
        while(run.canRun() && run.isExexuting() && !run.isOver())
        {    
            //System.out.println("Hi, this is from experiment");
            connection = DataBaseConnection.getInstancia();
            
            connection.modificacion("EXECUTE startTask @computerName = " + computer + ";");

            ResultSet resultado = connection.seleccion("" +
                    "       SELECT name_problem, name_algorithm, seed_executing, run_id_executing, name_type\n" +
                    "	FROM executing\n" +
                    "	INNER JOIN run ON run_id_executing = id_run\n" +
                    "	INNER JOIN algorithm ON algorithm_run = id_algorithm\n" +
                    "	INNER JOIN problem ON problem_run = id_problem\n" +
                    "	INNER JOIN type ON type_run = id_type\n" +
                    "	WHERE computer_executing = " + computer + " and executing_executing = 1;"); 



            if(resultado.next())
            {
                problema = resultado.getString(1);
                algoritmo = resultado.getString(2);
                semilla = Integer.parseInt(resultado.getString(3));
                runId = Integer.parseInt(resultado.getString(4));
                tipo = resultado.getString(5);
                AbstractELMEvaluator.EvaluatorType type = tipo.equals("cv")?AbstractELMEvaluator.EvaluatorType.CV:AbstractELMEvaluator.EvaluatorType.TT;
                String nombreProblema = "co.edu.unicauca.problem." + (tipo.equals("cv")? "cross_validation":"training_testing") + "." + problema;
                DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(nombreProblema);
                AlgorithmFactory factory = new AlgorithmFactory(new DataBaseParametersFactory());
                Algorithm auxAlg = factory.getAlgorithm(algoritmo, type, problem);
                System.out.println("Problema: "+problema+"\n"+
                                   "Algoritmo: "+algoritmo+"\n"+
                                   "Semilla: "+semilla+"\n"+
                                   "Run id: "+runId+"\n"+
                                   "Tipo: "+tipo);
                
                JMetalRandom rndm = JMetalRandom.getInstance();
                rndm.setSeed(semilla);
                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(auxAlg).execute() ;

                AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
                long computingTime = algorithmRunner.getComputingTime();
                DoubleSolution solution = (DoubleSolution) auxAlg.getResult();
                double resultadoExecTrain = ( solution.getObjective(0));
                double resultadoExecTest = p.test(solution);
                boolean inserted = false;
                while(!inserted)
                {
                    try 
                    {
                        String insertResultado = "INSERT INTO results VALUES(" + computingTime + ", " + resultadoExecTrain + ", " + resultadoExecTest + ", " + runId + ", " + semilla + ")";
                        connection.modificacion(insertResultado);
                        inserted = true;
                    } catch (Exception e) 
                    {
                        connection.reiniciarConexion();
                    }
                }
                connection.reiniciarConexion();   
            }
            
        }
    } 
}