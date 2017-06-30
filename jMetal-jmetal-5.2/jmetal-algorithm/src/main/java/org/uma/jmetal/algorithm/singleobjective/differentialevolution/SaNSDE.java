package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import co.edu.unicauca.util.math.RandomDistribution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.impl.AbstractDifferentialEvolution;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class SaNSDE extends AbstractDifferentialEvolution<DoubleSolution>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Probability to control wich mutation strategy to use
     */
    private double p;
    private List mutationStrategy;
    private int ns1;
    private int ns2;
    private int nf1;
    private int nf2;
    private double fp;
    private int fp_ns1;
    private int fp_ns2;
    private int fp_nf1;
    private int fp_nf2;
    
    private int populationSize;
    private int maxEvaluations;
    private SolutionListEvaluator<DoubleSolution> evaluator;
    private Comparator<DoubleSolution> comparator;
    private DifferentialEvolutionCrossover crossoverOperator2 ;
    private DifferentialEvolutionSelection selectionOperator2 ;
    private int evaluations;
    
    private double CRm;
    private List wk;
    private List CRrec;
    private List frec;
    private double sum_frec;
    
    private JMetalRandom randomGenerator ;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    /**
     * 
     * @param problem
     * @param maxEvaluations
     * @param populationSize
     * @param crossoverOperator
     * @param crossoverOperator2
     * @param selectionOperator
     * @param selectionOperator2
     * @param evaluator
     * @param comparator 
     */
    public SaNSDE(DoubleProblem problem, int maxEvaluations, int populationSize,
        DifferentialEvolutionCrossover crossoverOperator, DifferentialEvolutionCrossover crossoverOperator2, 
      DifferentialEvolutionSelection selectionOperator,DifferentialEvolutionSelection selectionOperator2, SolutionListEvaluator<DoubleSolution> evaluator,
      Comparator<DoubleSolution> comparator)
    {
        setProblem(problem);
        this.maxEvaluations = maxEvaluations;
        this.populationSize = populationSize;
        this.crossoverOperator = crossoverOperator;
        this.selectionOperator = selectionOperator;
        this.evaluator = evaluator;
        this.crossoverOperator2 = crossoverOperator2;
        this.selectionOperator2 = selectionOperator2;
        this.comparator = comparator;
        randomGenerator = JMetalRandom.getInstance();
        this.cleanVariables();
        
        CRrec = new ArrayList();
        wk = new ArrayList();
        frec = new ArrayList();
        sum_frec = 0;
    }
    
    private void cleanVariables()
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
    private void updateP()
    {
        int num = (ns1*(ns2+nf2));
        int den = (ns2*(ns1+nf1)+ns1*(ns2+nf2));
        if(num!=0 && den != 0)
        {
            p = (double) num / (double) den;
        }
        
        ns1 = 0;
        ns2 = 0;
        nf1 = 0;
        nf2 = 0;
    }
    private void updateFP()
    {
        int num = (fp_ns1*(fp_ns2+fp_nf2));
        int den = (fp_ns2*(fp_ns1+fp_nf1)+fp_ns1*(fp_ns2+fp_nf2));
        if(num!=0 && den != 0)
        {
            fp = (double) num / (double) den;
        }
        
        fp_ns1 = 0;
        fp_ns2 = 0;
        fp_nf1 = 0;
        fp_nf2 = 0;
    }
    private double calculateF()
    {
        double u = randomGenerator.nextDouble(0, 1);
        RandomDistribution distributionRnd = RandomDistribution.getInstance();
        if(u < fp)
        {
            fp_ns1++;
            fp_nf2++;
            return distributionRnd.nextGaussian(0.5, 0.5);
        }
        else
        {
            fp_ns2++;
            fp_nf1++;
            return distributionRnd.nextCauchy(0, 1);
        }
    }
    private double calculateCR()
    {
        RandomDistribution distributionRnd = RandomDistribution.getInstance();
        return distributionRnd.nextGaussian(CRm, 0.1);
    }
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
    @Override
    protected void initProgress() {
       evaluations = populationSize;
    }

    @Override
    protected void updateProgress() {
        evaluations += populationSize;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    @Override
    protected List<DoubleSolution> createInitialPopulation() {
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
    protected List<DoubleSolution> reproduction(List<DoubleSolution> matingPopulation) {
        List<DoubleSolution> offspringPopulation = new ArrayList<>();
        
        CRrec = new ArrayList();
        wk = new ArrayList();
        frec = new ArrayList();
        sum_frec = 0;
        
        for (int i = 0; i < populationSize; i++)
        {
          selectionOperator.setIndex(i);
          List<DoubleSolution> parents = selectionOperator.execute(matingPopulation);
          
          double u = randomGenerator.nextDouble(0, 1);
          
          List<DoubleSolution> children;
          double f = this.calculateF();
          double cr = this.calculateCR();
          
          if(u < p)
          {
            crossoverOperator = updateF(crossoverOperator, f, cr);
            crossoverOperator.setCurrentSolution(matingPopulation.get(i));
            children = crossoverOperator.execute(parents);
            ns1++;
            nf2++;
         
          }
          else
          {
            crossoverOperator2 = updateF(crossoverOperator2, f, cr);
            crossoverOperator2.setCurrentSolution(matingPopulation.get(i));
            children = crossoverOperator.execute(parents);
            ns2++;
            nf1++;
          }
          
          offspringPopulation.add(children.get(0));
          double frec_k = calculateSumObjetives(matingPopulation.get(i)) - calculateSumObjetives(children.get(0));
          frec.add(frec_k);
          sum_frec += frec_k;
          
          CRrec.add(cr);
        }
        this.updateP();
        this.updateFP();
        this.updateCR();
        return offspringPopulation;
    }
    private double calculateSumObjetives(DoubleSolution s)
    {
        int size = s.getNumberOfObjectives();
        double sum = 0;
        for(int i = 0; i< size; i++)
        {
            sum += s.getObjective(i);
        }
        return sum;
    }
    private DifferentialEvolutionCrossover updateF(DifferentialEvolutionCrossover selected,double f, double cr)
    {
        double k = selected.getK();
        String variant = selected.getVariant();
        return new DifferentialEvolutionCrossover(cr, f, k, variant);
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

        Collections.sort(pop, comparator) ;
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
