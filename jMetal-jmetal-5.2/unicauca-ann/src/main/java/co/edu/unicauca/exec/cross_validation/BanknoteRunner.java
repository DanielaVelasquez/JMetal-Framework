package co.edu.unicauca.exec.cross_validation;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.util.Comparator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DECC_G;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class BanknoteRunner 
{
    public static void main(String[] args) throws Exception 
    {
        JMetalRandom rnd = JMetalRandom.getInstance();
        rnd.setSeed(1);
        DoubleProblem problem;
        Algorithm<DoubleSolution> algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;
        SolutionListEvaluator<DoubleSolution> evaluator ;
        String problemName ;
        String referenceParetoFront = "" ;
        Comparator<DoubleSolution> comparator;
        if (args.length == 1) {
          problemName = args[0];
        } else if (args.length == 2) {
          problemName = args[0] ;
          referenceParetoFront = args[1] ;
        } else {
          problemName = "co.edu.unicauca.problem.cross_validation.Banknote";
        }
        evaluator = new SequentialSolutionListEvaluator<DoubleSolution>() ;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        comparator = new ObjectiveComparator<DoubleSolution>(0,ObjectiveComparator.Ordering.ASCENDING);
       
        /*double result[] = new double[]{-0.8644811116014093,0.482980034535944,-1.0,-1.0,0.9693794184155992,-0.6966032329569359,0.29491871745966697,0.5174827338123161,-0.915489247034809,-0.7829404115403313,0.21532418070938986,0.6453706265910443,-0.9656326548227394,-1.0,-1.0,-0.8573846664773566,-1.0,-0.9743262421676628};
        
        
        
        TrainingTestingEvaluator tt = (TrainingTestingEvaluator) problem;
        tt.getInputWeightsBiasFrom(result);
        System.out.println("Accuracy training: "+tt.train());*/
        
        double cr = 0.5 ;
        double f = 0.5 ;
        crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin") ;

        selection = new DifferentialEvolutionSelection() ;

        algorithm = new DECC_G(2,7,50,50,problem,10,evaluator, comparator);
        

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
          .execute() ;

        DoubleSolution solution = algorithm.getResult() ;
        long computingTime = algorithmRunner.getComputingTime() ;

        System.out.println("Total execution time: " + computingTime + "ms");
        System.out.println("Objective "+(1-solution.getObjective(0)));
        
        AbstractELMEvaluator p = (AbstractELMEvaluator)problem;
        System.out.println("Testing: "+p.test(solution));
        /*double a = ((TrainingTestingEvaluator)problem).test(solution);
        
        System.out.println("Accuracy "+a);

        /*printFinalSolutionSet(population);
        if (!referenceParetoFront.equals("")) {
          printQualityIndicators(population, referenceParetoFront) ;
        }*/
    }        
}