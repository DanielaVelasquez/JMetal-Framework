package org.uma.jmetal.algorithm.singleobjective.differentialevolution;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.impl.AbstractDifferentialEvolution;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.impl.RandomDistribution;


public class SaNSDE extends AbstractDifferentialEvolution<DoubleSolution>
{
    /**-------------------------------------------------------------------------z----------------
     * Constants
     *-----------------------------------------------------------------------------------------*/
    /**
     * Mean used for getting next gaussian value - it is use for calculating f value 
     */
    private final static double MEAN = 0.5;
    /**
     * Standar deviation used for getting next gaussian value - it is use for calculating f value 
     */
    private final static double STANDAR_DEVIATION = 0.5;
    /**
     * Location parameter for cauchy distribution - it is use for calculating f value 
     */
    private final static double X0 = 0;
    /**
     * Scale parameter for cauchy distribution - it is use for calculating f value 
     */
    private final static double Y_F = 1;
    /**
     * Scale parameter for cauchy distribution - it is use for calculating crossover rate value for an individual
     */
    private final static double Y_CR = 0.1;
    
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Probability to control wich mutation strategy to use
     */
    private double p;
    /**
     * Number of individuals using crossover operator 1 in a generation
     */
    private int ns1;
    /**
     * Number of individuals using crossover operator 2 in a generation
     */
    private int ns2;
    /**
     * Number of inidividual discarded using crossover operator 1 in a generation
     */
    private int nf1;
    /**
     * Number of individual discarded using crossover operator 2 in a generation
     **/
    private int nf2;
    /**
     * Probability to control wich distribution (Gaussian or Cauchy) use for calculating F in a generation
     */
    private double fp;
    /**
     * Number of individuals using gaussian distribution in a generation
     */
    private int fp_ns1;
    /**
     * Number of individuals using cauchy distribution in a generation
     */
    private int fp_ns2;
    /**
     * Number of individual discarded using gaussian distribution in a generation
     */
    private int fp_nf1;
    /**
     * Number of individual discarded using cauchy distribution in a generation
     */
    private int fp_nf2;
    /**
     * Populition's size
     */
    private int populationSize;
    /**
     * Maximun number of evaluations
     */
    private int maxEvaluations;

    /**
     * 
     */
    private SolutionListEvaluator<DoubleSolution> evaluator;
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    /**
     * Crossover operator using a diferent strategy 
     */
    private DifferentialEvolutionCrossover crossoverOperator2 ;
    /**
     * Actual number of evaluations
     */
    private int evaluations;
    /**
     * Crossover rate self adaption
     */
    private double CRm;
    /**
     * Succesful crossover rate used
     */
    private List CRrec;
    /**
     * Improvement of fitness value between an indivual from population an a new child in a generation
     * frec(k) = f(k) - f_new(k)
     */
    private List frec;
    /**
     * Total of improvement values in a generation
     */
    private double sum_frec;
    
    private JMetalRandom randomGenerator ;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    /**
     * Creates a new SaNSDE algorithm
     * @param problem algorithm's problem
     * @param maxEvaluations Maximun number of evaluations 
     * @param populationSize Population's size
     * @param crossoverOperator crossover operator with a crossover strategy 1
     * @param crossoverOperator2 crossover operator with a crossover strategy 2
     * @param selectionOperator operator for selection of individual's parent
     * @param evaluator
     * @param comparator Determines how a solution should be order
     * @param maxCycles Determines the maximun number of cycles to excecute
     */
    public SaNSDE(DoubleProblem problem, int maxEvaluations, int populationSize,
        DifferentialEvolutionCrossover crossoverOperator, DifferentialEvolutionCrossover crossoverOperator2, 
        DifferentialEvolutionSelection selectionOperator, SolutionListEvaluator<DoubleSolution> evaluator,
      Comparator<DoubleSolution> comparator)
    {
        setProblem(problem);
        this.maxEvaluations = maxEvaluations;
        this.populationSize = populationSize;
        this.crossoverOperator = crossoverOperator;
        this.selectionOperator = selectionOperator;
        this.evaluator = evaluator;
        this.crossoverOperator2 = crossoverOperator2;
        this.comparator = comparator;
        randomGenerator = JMetalRandom.getInstance();
        
        this.CRrec = new ArrayList();
        this.frec = new ArrayList();
        this.sum_frec = 0;
        
        this.initVariables();
    }
    /**
     * Initializes the algorithm's auto-adaptive variables
     */
    private void initVariables()
    {
        this.p = 0.5;
        this.fp = 0.5;
        this.CRm = 0.5;
        ns1 = 0;
        ns2 = 0;
        nf1 = 0;
        nf2 = 0;
        
        fp_ns1 = 0;
        fp_ns2 = 0;
        fp_nf1 = 0;
        fp_nf2 = 0;
        
    }
    /**
     * Updates p value according to the number of individuals using crossover 
     * operator 1 and 2 and indivuals discarded using crossover operator 1 and 2 
     */
    private void updateP()
    {
        int num = (ns1*(ns2+nf2));
        int den = (ns2*(ns1+nf1)+ns1*(ns2+nf2));
        //if(num!=0 && den != 0)
        //{
        p = (double) num / (double) den;
        //}
        
        ns1 = 0;
        ns2 = 0;
        nf1 = 0;
        nf2 = 0;
    }
    /**
     * Updates fp value according to the number of individuals using gaussian 
     * and cauchy distribution and indivuals discarded using gaussian and 
     * cauchy distribution
     */
    private void updateFP()
    {
        int num = (fp_ns1*(fp_ns2+fp_nf2));
        int den = (fp_ns2*(fp_ns1+fp_nf1)+fp_ns1*(fp_ns2+fp_nf2));
        /*if(num!=0 && den != 0)
        {*/
        fp = (double) num / (double) den;
        //}
        
        fp_ns1 = 0;
        fp_ns2 = 0;
        fp_nf1 = 0;
        fp_nf2 = 0;
    }
    /**
     * Calculates scale factor for an individual in the current generation
     * Counts if scale factor value was calculated using gaussian or cauchy
     * distribution
     * @return scale factor value for i-th indiviual
     */
    private double calculateF()
    {
        double u = randomGenerator.nextDouble(0, 1);
        RandomDistribution distributionRnd = RandomDistribution.getInstance();
        if(u < fp)
        {
            fp_ns1++;
            fp_nf2++;
            return distributionRnd.nextGaussian(MEAN, STANDAR_DEVIATION);
        }
        else
        {
            fp_ns2++;
            fp_nf1++;
            return distributionRnd.nextCauchy(X0, Y_F);
        }
    }
    /**
     * Calculates crossover rate for i-th individual in currrent generation
     * it use gaussian distribution between CRm value and 
     * @return crossover rate
     */
    private double calculateCR()
    {
        RandomDistribution distributionRnd = RandomDistribution.getInstance();
        return distributionRnd.nextGaussian(CRm, Y_CR);
    }
    /**
     * Updates crossover rate value according to the improvement of fitness recorded, its sum
     * and crossover rate values recorded
     */
    private void updateCR()
    {
        int size = CRrec.size();
        CRm = 0;
        for(int i = 0; i < size; i++)
        {
            double wk = (double) frec.get(i) /(double) sum_frec;
            CRm += wk * (double) CRrec.get(i);
        }
    }
    /**
     * Calculate sum of objectives value from a solution
     * @param s given solution
     * @return sum of solution's objectives
     */
    private double calculateSumObjectives(DoubleSolution s)
    {
        int size = s.getNumberOfObjectives();
        double sum = 0;
        for(int i = 0; i< size; i++)
        {
            sum += s.getObjective(i);
        }
        return sum;
    }
    /**
     * Updates values crossover rate and scale factor value for a crossover strategy
     * @param selected crossover srategy
     * @param f new scale factor for the selected crossover strategy
     * @param cr new crossover rate for the selected crossover strategy
     * @return Copy of the selected crossover rate strategy with the new scale factor and crossover rate
     */
    private DifferentialEvolutionCrossover updateValuesOf(DifferentialEvolutionCrossover selected,double f, double cr)
    {
        double k = selected.getK();
        String variant = selected.getVariant();
        return new DifferentialEvolutionCrossover(cr, f, k, variant);
    }
    @Override
    protected void initProgress() {
       evaluations = 1;
    }

    @Override
    protected void updateProgress() {
        evaluations += 1;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    @Override
    protected List<DoubleSolution> createInitialPopulation() {
        if(this.getPopulation()!=null)
            return this.getPopulation();
        List<DoubleSolution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
          DoubleSolution newIndividual = getProblem().createSolution();
          population.add(newIndividual);
        }
        return population;
    }

    @Override
    protected List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
        return evaluator.evaluate(population, getProblem());
    }

    @Override
    protected List<DoubleSolution> selection(List<DoubleSolution> population) {
        return population;
    }

    @Override
    protected List<DoubleSolution> reproduction(List<DoubleSolution> matingPopulation) 
    {
        List<DoubleSolution> offspringPopulation = new ArrayList<>();
        //Start improvement of fitness and crossoer rate values for current generation
        CRrec = new ArrayList();
        frec = new ArrayList();
        sum_frec = 0;
        
        for (int i = 0; i < populationSize; i++)
        {
          //Find parents for i-th individual
          selectionOperator.setIndex(i);
          List<DoubleSolution> parents = selectionOperator.execute(matingPopulation);
          
          //Find a random value between 0 and 1
          double u = randomGenerator.nextDouble(0, 1);
          
          List<DoubleSolution> children;
          //Calculates a scale factor and crossover rate for i-th indivual
          double f = this.calculateF();
          double cr = this.calculateCR();
          
          if(u < p)
          {
            crossoverOperator = updateValuesOf(crossoverOperator, f, cr);
            crossoverOperator.setCurrentSolution(matingPopulation.get(i));
            children = crossoverOperator.execute(parents);
            ns1++;
            nf2++;
         
          }
          else
          {
            crossoverOperator2 = updateValuesOf(crossoverOperator2, f, cr);
            crossoverOperator2.setCurrentSolution(matingPopulation.get(i));
            children = crossoverOperator2.execute(parents);
            ns2++;
            nf1++;
          }
          //Adds the best children
          offspringPopulation.add(children.get(0));
          //Calculates improvement of fitness between the i-th indiviual and its best child
          double frec_k = calculateSumObjectives(matingPopulation.get(i)) - calculateSumObjectives(children.get(0));
          frec.add(frec_k);
          sum_frec += frec_k;
          //Adds the crossover rate value used
          CRrec.add(cr);
        }
        //As P, FP and CR are auto adaptative they are re calculated accoring to the current generation 
        //values and perfomance
        this.updateP();
        this.updateFP();
        this.updateCR();
        return offspringPopulation;
    }
    @Override
    protected List<DoubleSolution> replacement(List<DoubleSolution> population, List<DoubleSolution> offspringPopulation) {
        List<DoubleSolution> pop = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
          if (comparator.compare(population.get(i), offspringPopulation.get(i)) < 0) {
            pop.add(population.get(i));
          } else {
            pop.add(offspringPopulation.get(i));
          }
        }

        //Collections.sort(pop, comparator) ;
        return pop;
    }

    @Override
    public DoubleSolution getResult() {
        Collections.sort(getPopulation(), comparator) ;
        return getPopulation().get(0);
    }

    @Override
    public String getName() {
        return "SaNSDE" ;
    }

    @Override
    public String getDescription() {
        return "Self-adaptative Differential Evolution with Neighborhood Search Algorithm" ;
    }
}
