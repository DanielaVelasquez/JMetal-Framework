package co.edu.unicauca.parameters;

import co.edu.unicauca.database.DataBaseConnection;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class Parameters
{
    private DataBaseConnection connection;
    private String nameProblem;
    private AbstractELMEvaluator.EvaluatorType type;
    private String typeName;
    private String algorithm;
    private String valuesConf;
    private int seed;
    private int algorithmId;
    private int idTask;
    
    
    public Parameters(int computer) throws Exception
    {
        connection = DataBaseConnection.getInstancia();
        
        boolean run = true;
        while(run)
        {
            connection.modificacion("EXECUTE requestTask @computerName = " + computer + ";");
            String query = 
            "SELECT nameType, nameProblem, nameAlg, valuesConf, seedTask,algorithmTask, idTask \n"+
            "FROM computers\n"+
            "INNER JOIN task ON idTask = taskPC\n"+
            "INNER JOIN type ON typeTask = idType\n"+
            "INNER JOIN problem ON problemTask = idProblem\n"+
            "INNER JOIN algorithm ON algorithmTask = idAlg\n"+
            "INNER JOIN configuration ON configurationTask = idConf\n"+
            "WHERE numberPC = "+computer+" AND isExecutingPC = 1";
            ResultSet result = connection.seleccion(query);
            run = result.next();
            
            if(run)
            {
                typeName = result.getString(1).trim();
                type = typeName.equals("cv")?AbstractELMEvaluator.EvaluatorType.CV:AbstractELMEvaluator.EvaluatorType.TT;
                nameProblem = "co.edu.unicauca.problem." + (typeName.equals("cv")? "cross_validation":"training_testing") + "." +result.getString(2).trim();
                algorithm = result.getString(3).trim();
                valuesConf = result.getString(4).trim();
                seed = result.getInt(5);
                algorithmId = result.getInt(6);
                idTask = result.getInt(7);
                
                DoubleProblem problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(nameProblem);
                Algorithm algorithm = ParamsFactory.getAlgorithm(algorithmId, connection, type, problem, this.algorithm, valuesConf);
                
                JMetalRandom rndm = JMetalRandom.getInstance();
                rndm.setSeed(seed);
                
                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute() ;
                AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
                long computingTime = algorithmRunner.getComputingTime();
                DoubleSolution solution = (DoubleSolution) algorithm.getResult();
                double train = ( solution.getObjective(0));
                double test = p.test(solution);
                
                String queryInsert = 
                "INSERT INTO results VALUES("+idTask+", "+test+", "+train+", "+ computingTime+")";
                connection.modificacion(queryInsert);
            }
            
        }
    }
    public static void main(String[] args) 
    {
        //int computer = Integer.parseInt(args[0]);
        while(true)
        {
            try 
            {
                new Parameters(1);
                //new Parameters(computer);
            }
            catch (Exception ex) 
            {
                Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
