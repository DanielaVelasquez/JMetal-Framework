package co.edu.unicauca.exec.training_testing;


public class IrisRunner 
{
    
    /*public static void main(String[] args) throws Exception {
        JMetalRandom rnd = JMetalRandom.getInstance();
        rnd.setSeed(1);
        DoubleProblem problem;
        Algorithm<DoubleSolution> algorithm;
        DifferentialEvolutionSelection selection;
        DifferentialEvolutionCrossover crossover;
        SolutionListEvaluator<DoubleSolution> evaluator ;
        String problemName ;
        String referenceParetoFront = "" ;
        if (args.length == 1) {
          problemName = args[0];
        } else if (args.length == 2) {
          problemName = args[0] ;
          referenceParetoFront = args[1] ;
        } else {
          problemName = "co.edu.unicauca.problem.training_testing.Iris";
        }
        evaluator = new SequentialSolutionListEvaluator<DoubleSolution>() ;
        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);
        
        
        /*double result[] = new double[]{-0.8644811116014093,0.482980034535944,-1.0,-1.0,0.9693794184155992,-0.6966032329569359,0.29491871745966697,0.5174827338123161,-0.915489247034809,-0.7829404115403313,0.21532418070938986,0.6453706265910443,-0.9656326548227394,-1.0,-1.0,-0.8573846664773566,-1.0,-0.9743262421676628};
        
        
        
        TrainingTestingEvaluator tt = (TrainingTestingEvaluator) problem;
        tt.getInputWeightsBiasFrom(result);
        System.out.println("Accuracy training: "+tt.train());
        System.out.println("Accuracy testing: "+tt.test2());*/
        
     /*   double cr = 0.5 ;
        double f = 0.5 ;
        crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin") ;

        selection = new DifferentialEvolutionSelection() ;

        algorithm = new SaNSDE(problem, 100000, 100,
        crossover, new DifferentialEvolutionCrossover(cr, f, "current-to-best/1/bin"), 
        selection, evaluator,
        new ObjectiveComparator<DoubleSolution>(0));
        

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
          .execute() ;

        DoubleSolution solution = algorithm.getResult() ;
        long computingTime = algorithmRunner.getComputingTime() ;

        System.out.println("Total execution time: " + computingTime + "ms");
        double a = ((TrainingTestingEvaluator)problem).test(solution);
        
        System.out.println("Accuracy "+a);

        /*printFinalSolutionSet(population);
        if (!referenceParetoFront.equals("")) {
          printQualityIndicators(population, referenceParetoFront) ;
        }*/
    //}
    
}
