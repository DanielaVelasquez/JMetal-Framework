package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.uma.jmetal.algorithm.impl.AbstractDifferentialEvolution;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.impl.RandomDistribution;


public class SaNSDE extends AbstractDifferentialEvolution<DoubleSolution>
{
    /**
     * ------------------------------------------------------------------------
     * Based on
     * http://ieeexplore.ieee.org/document/4630935/ ****
     * Z. Yang, K. Tang and X. Yao, “Self-adaptive Differential Evolution with 
     * Neighborhood Search,” 2008 IEEE Congress on Evolutionary 
     * Computation, 2008.
     * -------------------------------------------------------------------------
     */
    
    /**------------------------------------------------------------------------------------------
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
     * Probability to control which mutation strategy to use
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
     * Population's size
     */
    private int populationSize;
    /**
     * Maximun number of evaluations
     */
    private int maxEvaluations;
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    /**
     * Crossover operator using a diferent strategy 
     */
    private DifferentialEvolutionCrossover crossoverOperator2;
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
    /**
     * Value to penalize a solution which doesn't reach to be evaluated
     */
    private double penalize_value;
    /**
     * Array to save which mutation strategy generate new individual
     * true for first strategie
     * false other wise
     */     
    private boolean[] reproValues;    
    /**
     * Array to save which distribution generate new value of scale factor
     * true for normal distribution
     * false for Cauchy distribution
     */     
    private boolean[] scaleFactorValues;
    
    private final JMetalRandom randomGenerator;
    
    
    
    
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
     * @param penalize_value value to penalize a solution if evaluation run out
     * @param comparator Determines how a solution should be order
     */
    public SaNSDE(DoubleProblem problem, int maxEvaluations, int populationSize,
        DifferentialEvolutionCrossover crossoverOperator, DifferentialEvolutionCrossover crossoverOperator2, 
        DifferentialEvolutionSelection selectionOperator, 
        Comparator<DoubleSolution> comparator,double penalize_value)
    {
        setProblem(problem);
        this.maxEvaluations = maxEvaluations;
        this.populationSize = populationSize;
        this.crossoverOperator = crossoverOperator;
        this.selectionOperator = selectionOperator;
        this.crossoverOperator2 = crossoverOperator2;
        this.comparator = comparator;
        randomGenerator = JMetalRandom.getInstance();
        this.penalize_value = penalize_value;
        this.CRrec = new ArrayList();
        this.frec = new ArrayList();
        this.sum_frec = 0;
        this.reproValues = new boolean[populationSize];
        this.scaleFactorValues = new boolean[populationSize];
        
        this.initVariables();
    }    
    
    @Override
    protected void initProgress() {
       //iteration = iteration;
    }

    @Override
    protected void updateProgress() {
        //iteration += 1;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    @Override
    protected List<DoubleSolution> createInitialPopulation() {
        this.evaluations = 0;
        
        if(this.getPopulation() != null)
        {
            return this.getPopulation();
        }
        
        List<DoubleSolution> population = new ArrayList<>(populationSize);
        
        for (int i = 0; i < populationSize; i++) 
        {
            DoubleSolution newIndividual = getProblem().createSolution();
            population.add(newIndividual);
        }
        
        return population;
    }
    
    @Override
    protected List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
        int i = 0;
        int populationSize = population.size();
        
        while(!isStoppingConditionReached() && i < populationSize)
        {
            DoubleSolution solution = population.get(i);
            this.getProblem().evaluate(solution);
            i++;
            this.evaluations++;
        }
        
        for(int j = i; j < populationSize; j++)
        {
            DoubleSolution solution = population.get(j);
            this.penalize(solution);
        }
        
        return population;
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
            double f = this.calculateF(i);
            double cr = this.calculateCR();

            if(u < p)
            {
                crossoverOperator = updateValuesOf(crossoverOperator, f, cr);
                crossoverOperator.setCurrentSolution(matingPopulation.get(i));
                children = crossoverOperator.execute(parents);
                reproValues[i] = true;
            }
            else
            {
                crossoverOperator2 = updateValuesOf(crossoverOperator2, f, cr);
                crossoverOperator2.setCurrentSolution(matingPopulation.get(i));
                children = crossoverOperator2.execute(parents);
                reproValues[i] = false;
            }
            //Adds the best children
            offspringPopulation.add(children.get(0));
            //Calculates improvement of fitness between the i-th indiviual and its best child
            double frec_k = matingPopulation.get(i).getObjective(0) - children.get(0).getObjective(0);
            frec.add(frec_k);
            sum_frec += frec_k;
            //Adds the crossover rate value used
            CRrec.add(cr);
        }
        
        return offspringPopulation;
    }
    
    @Override
    protected List<DoubleSolution> replacement(List<DoubleSolution> population, List<DoubleSolution> offspringPopulation) {
        List<DoubleSolution> pop = new ArrayList<>();
        
        for (int i = 0; i < populationSize; i++)
        {
            DoubleSolution p = population.get(i);
            DoubleSolution o = offspringPopulation.get(i);
            DoubleSolution s = this.getBest(p, o);
            DoubleSolution add;
            
            if(!this.inPopulation(pop, s))
            {
                add = s;
            }
            else
            {
                if(s == o)
                {
                    add = p;
                }
                else
                {
                    add = o;
                }
            }
            
            //for update auto-adaptive parameters
            if (add == o)
            {
                //update p
                if (reproValues[i])
                {
                    ns1++;
                }
                else
                {
                    ns2++;
                }
                
                //update fp
                if (scaleFactorValues[i])
                {
                    fp_ns1++;
                }
                else
                {
                    fp_ns2++;
                }
                
            }
            else
            {
                //update p
                if (reproValues[i])
                {
                    nf1++;
                }
                else
                {
                    nf2++;
                }
                
                //update fp
                if (scaleFactorValues[i])
                {
                    fp_nf1++;
                }
                else
                {
                    fp_nf2++;
                }
            }
            
            pop.add(add);
        }
        
        //As P, FP and CR are auto adaptative they are re calculated accoring to the current generation 
        //values and perfomance
        this.updateP();
        this.updateFP();
        this.updateCR();
        
        return pop;
    }
    
    /**
     * @return best individual in population
     */
    @Override
    public DoubleSolution getResult() 
    {
        DoubleSolution best = getPopulation().get(0);
        
        for(int i = 1; i < populationSize; i++)
        {
            DoubleSolution s = this.getPopulation().get(i);
            best = this.getBest(best, s);
        }
        
        return best;
    }

    @Override
    public String getName() {
        return "SaNSDE" ;
    }

    @Override
    public String getDescription() {
        return "Self-adaptive Differential Evolution with Neighborhood Search Algorithm" ;
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
        
        this.evaluations = 0;        
    }
    
    /**
     * Updates p value according to the number of individuals using crossover 
     * operator 1 and 2 and indivuals discarded using crossover operator 1 and 2 
     */
    private void updateP()
    {
        int num = (ns1*(ns2+nf2));
        int den = (ns2*(ns1+nf1)+ns1*(ns2+nf2));
        
        p = (double) num / (double) den;
        
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
        
        fp = (double) num / (double) den;
        
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
    private double calculateF(int position)
    {
        double u = randomGenerator.nextDouble(0, 1);
        RandomDistribution distributionRnd = RandomDistribution.getInstance();
        
        if(u < fp)
        {
            scaleFactorValues[position] = true;
            return distributionRnd.nextGaussian(MEAN, STANDAR_DEVIATION);
        }
        else
        {
            scaleFactorValues[position] = false;
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
     * Updates values crossover rate and scale factor value for a crossover strategy
     * @param selected crossover srategy
     * @param f new scale factor for the selected crossover strategy
     * @param cr new crossover rate for the selected crossover strategy
     * @return Copy of the selected crossover rate strategy with the new scale factor and crossover rate
     */
    private DifferentialEvolutionCrossover updateValuesOf(DifferentialEvolutionCrossover selected, double f, double cr)
    {
        double k = selected.getK();
        String variant = selected.getVariant();
        return new DifferentialEvolutionCrossover(cr, f, k, variant);
    }
    
    /**
     * Sets an individual function value to a penalization value given by 
     * the user
     * @param solution solution to penalize with a bad function value
     */
    protected void penalize(DoubleSolution solution){
        solution.setObjective(0, this.penalize_value);
    }
    
    /**
     * Determines if an individual with the same values already exists on a
     * population
     * @param population population to search
     * @param individual individual to compare with individuals in population
     * @return 
     */
    private boolean inPopulation(List<DoubleSolution> population, DoubleSolution individual)
    {
        for(DoubleSolution s: population)
        {
            if(s != individual)
            {
                int n = this.getProblem().getNumberOfVariables();
                
                for(int i = 0; i < n; i++)
                {
                    double sValue = s.getVariableValue(i);
                    double individualValue = individual.getVariableValue(i);
                    
                    if(sValue == individualValue)
                    {
                        if(i == n-1)
                        {
                            return true;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    private DoubleSolution getBest(DoubleSolution s1, DoubleSolution s2)
    {
        int comparison = comparator.compare(s1, s2);
        
        if(comparison <= 0)
        {
            return s1;
        }
        else
        {
            return s2;
        }
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getFp() {
        return fp;
    }

    public void setFp(double fp) {
        this.fp = fp;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
    }

    public DifferentialEvolutionCrossover getCrossoverOperator2() {
        return crossoverOperator2;
    }

    public void setCrossoverOperator2(DifferentialEvolutionCrossover crossoverOperator2) {
        this.crossoverOperator2 = crossoverOperator2;
    }
    
    public double getPenalize_value() {
        return penalize_value;
    }

    public void setPenalize_value(double penalize_value) {
        this.penalize_value = penalize_value;
    }    
}