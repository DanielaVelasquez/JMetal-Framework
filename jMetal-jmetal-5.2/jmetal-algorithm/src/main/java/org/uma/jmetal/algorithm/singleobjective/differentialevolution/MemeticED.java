package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.impl.AbstractDifferentialEvolution;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.impl.RandomDistribution;

public class MemeticED extends AbstractDifferentialEvolution<DoubleSolution> {

    /**
     * ------------------------------------------------------------------------
     * Based on
     * http://www.sciencedirect.com/science/article/pii/S003132031630036X ****
     * Y. Zhang, J. Wu, Z. Cai, P. Zhang, and L. Chen, “Memetic Extreme Learning
     * Machine,” Pattern Recognit., vol. 58, pp. 135–148, 2016.
     * -------------------------------------------------------------------------
     */
    /**
     * Atributes
     */
    private final int populationSize;
    private final int maxEvaluations;
    private final Comparator<DoubleSolution> comparator;

    private int evaluations;
    public double CR;
    public double F;
    public double CRm;
    public double CRmSigma;
    public double Fm;
    public double FmSigma;

    public List<DoubleSolution> archiveLst;
    public ArrayList CRlst;
    public ArrayList Flst;

    private final double C;
    private final double P;

    int generationCounter = 0;

    private final LocalSearchOperator localSearch;

    private JMetalRandom randomGenerator;

    /**
     * Constructor
     * @param problem Problem to solve
     * @param maxEvaluations Maximum number of evaluations to perform
     * @param populationSize
     * @param crossoverOperator
     * @param selectionOperator
     * @param Ls, Local Optimizer
     * @param comparator
     */
    public MemeticED(DoubleProblem problem, int maxEvaluations, int populationSize,
            DifferentialEvolutionCrossover crossoverOperator,
            DifferentialEvolutionSelection selectionOperator, LocalSearchOperator Ls,
            Comparator<DoubleSolution> comparator) {
        setProblem(problem);
        this.maxEvaluations = maxEvaluations;
        this.populationSize = populationSize;
        this.crossoverOperator = crossoverOperator;
        this.selectionOperator = selectionOperator;
        this.localSearch = Ls;
        this.comparator = comparator;

        //Default values
        this.P = 0.2;
        this.C = 1.0 / 10;
        this.CRm = 0.9;
        this.CRmSigma = 0.1;
        this.Fm = 0.5;
        this.FmSigma = 0.1;
        this.archiveLst = new ArrayList<>();
        this.Flst = new ArrayList();
        this.CRlst = new ArrayList();
        if (randomGenerator == null) {
            randomGenerator = JMetalRandom.getInstance();
        }
    }

    @Override
    protected void initProgress() {
        evaluations = populationSize;
    }

    @Override
    protected void updateProgress() {
        evaluations++;
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
        for (int i = 0; i < population.size(); i++) {
            try {
                getProblem().evaluate(population.get(i));
                updateProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return population;
    }

    /*------------------ Overwriting method to add Local-search(Simulated Annealing), ------------*/
    @Override
    public void run() {
        List<DoubleSolution> offspringPopulation;
        List<DoubleSolution> matingPopulation;
        this.evaluations = 0;
        setPopulation(createInitialPopulation());
        setPopulation(evaluatePopulation(getPopulation()));
        initProgress();
        while (!isStoppingConditionReached()) {

            matingPopulation = selection(getPopulation());
            offspringPopulation = reproduction(matingPopulation);
            offspringPopulation = evaluatePopulation(offspringPopulation);
            setPopulation(replacement(getPopulation(), offspringPopulation));
            setPopulation(localOptimization(getPopulation()));
            generationCounter++;
        }
    }

    @Override
    protected List<DoubleSolution> selection(List<DoubleSolution> population) {
        return population;
    }

    @Override
    protected List<DoubleSolution> reproduction(List<DoubleSolution> matingPopulation) {

        List<DoubleSolution> offspringPopulation = new ArrayList<>();
        CRm = updateCrm(CRm, generationCounter);
        Fm = updateFm(Fm, generationCounter);

        CR = getCR(CRm, CRmSigma);
        F = getF(Fm, FmSigma);

        int evaluationTemp = evaluations;
        for (int i = 0; i < populationSize; i++) {
            if (evaluationTemp <= maxEvaluations) {
                selectionOperator.setIndex(i);
                List<DoubleSolution> parents = selectionOperator.execute(matingPopulation);//Con esto puede ser el mismo el padre->SOLO NESECITO UNO
                DoubleSolution children = mutation(i, parents);
                offspringPopulation.add(children);
                evaluationTemp++;
            } else {
                break;
            }
        }

        CRlst.add(CR);
        Flst.add(F);

        return offspringPopulation;
    }

    
    @Override
    protected List<DoubleSolution> replacement(List<DoubleSolution> population, List<DoubleSolution> offspringPopulation) {
        List<DoubleSolution> pop = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            if (i < offspringPopulation.size()) {
                if (comparator.compare(offspringPopulation.get(i), population.get(i)) < 0) {
                    pop.add(offspringPopulation.get(i));

                } else {
                    pop.add(population.get(i));
                }
            } else {
                pop.add(population.get(i));
            }
        }
        Collections.sort(pop, comparator);
        generationCounter++;
        return pop;
    }

    public List<DoubleSolution> localOptimization(List<DoubleSolution> solutions) {
        List<DoubleSolution> pop = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            if (!isStoppingConditionReached() && (maxEvaluations - evaluations) > 70) {
                DoubleSolution S = (DoubleSolution) localSearch.execute(solutions.get(i));
                pop.add(S);
                evaluations += localSearch.getEvaluations();
            } else {
                pop.add(solutions.get(i));
            }
        }
        return pop;
    }

    /**
     * @return Returns the best individual
     */
    @Override
    public DoubleSolution getResult() {
        Collections.sort(getPopulation(), comparator);
        return getPopulation().get(0);
    }

    @Override
    public String getName() {
        return "MemeticDE";
    }

    @Override
    public String getDescription() {
        return "Memetic Differential Evolution Algorithm with Simulated A";
    }

    protected DoubleSolution mutation(int current, List<DoubleSolution> vSeleccionados) {
        DoubleSolution wi = getProblem().createSolution();

        DoubleSolution pBestSolution = getBestfrom100p(P);
        DoubleSolution PUA = getXR2(getPopulation(), archiveLst);
        for (int i = 0; i < wi.getNumberOfVariables(); i++) {
            //== == == == == == == == == == == == == == == Mutation == == == == == == == == == == == == ==
            double value = getPopulation().get(current).getVariableValue(i)
                    + F * (pBestSolution.getVariableValue(i) - getPopulation().get(current).getVariableValue(i))
                    + F * (vSeleccionados.get(0).getVariableValue(i) - PUA.getVariableValue(i));

            wi.setVariableValue(i, value);
            if (wi.getVariableValue(i) < wi.getLowerBound(i)) {
                wi.setVariableValue(i, wi.getLowerBound(i));
            }
            if (wi.getVariableValue(i) > wi.getUpperBound(i)) {
                wi.setVariableValue(i, wi.getUpperBound(i));
            }
        }
        return wi;
    }

    /*=========================Crossover=========================================*/
    protected DoubleSolution fatherCrossover(int i, DoubleSolution vPerturbado) {
        int rndb = randomGenerator.nextInt(0, vPerturbado.getNumberOfVariables() - 1);

        for (int d = 0; d < vPerturbado.getNumberOfVariables(); d++) {
            if ((randomGenerator.nextInt(0, 1) < CR) || (d == rndb)) {
                vPerturbado.setVariableValue(d, vPerturbado.getVariableValue(d));
            } else {
                vPerturbado.setVariableValue(d, getPopulation().get(i).getVariableValue(d));
            }
        }

        return vPerturbado;
    }

    /*-------------------------------Own methdos--------------------------------------*/
    public void defaultStart() {
        this.CRm = 0.9;
        this.CRmSigma = 0.1;
        this.Fm = 0.5;
        this.FmSigma = 0.1;
        this.archiveLst = new ArrayList<>();
        this.Flst = new ArrayList();
        this.CRlst = new ArrayList();

    }

    /**
     * generate the best of 100p% population
     *
     * @param p Percentage of population (0,1]
     * @return
     */
    public DoubleSolution getBestfrom100p(double p) {
        int max = (int) (populationSize * p);//the population is sorted
        int bestRand = randomGenerator.nextInt(0, (max - 1));
        DoubleSolution b = getPopulation().get(bestRand);
        return b;
    }

    /**
     * ----------------------------------------------------------------------
     * JADE: Adaptive Differential Evolution with Optional External archiveLst
     * B.Parameter Adaptation Base on J. Zhang,A.Sanderson,Jade:adaptive
     * differential evolution with optional externalarchive,IEEE
     * Trans.Evol.Comput.13(5)(2009)945–958.
     * -----------------------------------------------------------------------
     */
    /**
     * Generate CR according to a normal distribution with mean CRm,and sigma
     * "CRsigma" and std 0.1 (scale parameter)
     * @param CRm initialite at 0.9
     * @param CRsigma initiaize at 0.1 (std)
     * @return CR
     */
    public double getCR(double CRm, double CRsigma) {

        RandomDistribution distributionRnd = RandomDistribution.getInstance();
        double cr = CRm + CRsigma * distributionRnd.nextGaussian(); //->>>>* randGen.nextGaussian();
        if (cr < 0) {
            cr = 0.0;
        } else {
            if (cr > 1) {
                cr = 1.0d;
            }
        }
        return cr;
    }

    /**
     * Generate F according to a cauchy distribution with location parameter Fm,
     * and Fsigma scale parameter
     * @param uF initialite a 0.5
     * @param Fsigma
     * @return F According to a cauchy distribution
     */
    public double getF(double uF, double Fsigma) {
        
        double f = 0.0;
        while (f == 0.0) {
            f = randCauchy(uF, Fsigma);
        }
        if (f > 1) {
            f = 1.0;
        }
        return f;
    }

    /**
     * xr2 from P U A
     * @param P Current population
     * @param A archiveLst population
     * @return
     */
    public DoubleSolution getXR2(List<DoubleSolution> P, List<DoubleSolution> A) {
        ArrayList<DoubleSolution> tmp = new ArrayList<>();
        P.stream().forEach((p) -> {
            tmp.add(p);
        });
        A.stream().forEach((a) -> {//for 1 iteration It's nothing 
            tmp.add(a);
        });
        return tmp.get(randomGenerator.nextInt(0, (tmp.size() - 1)));
    }

    public double updateCrm(double CRm, int currentGen) {
        if (currentGen > 1) {
            CRm = (1.0 - C) * CRm + C * media(CRlst);
        }
        return CRm;
    }

    public double updateFm(double Fm, int currentGen) {
        if (currentGen > 1) {
            Fm = (1.0 - C) * Fm + C * mediaLehmermean(Flst);
        }
        return Fm;
    }

    public void Archive(List<DoubleSolution> hst, DoubleSolution a) {
        if (hst.size() >= populationSize) {
            hst.remove(randomGenerator.nextInt(0, (hst.size() - 1)));
        }
        hst.add(a);
    }

    /**
     * Lehmer mean
     * @param lst need be Doubles
     * @return Lehmer mean
     */
    public double mediaLehmermean(ArrayList lst) {

        double addSquare = 0.0;
        double add = 0.0;
        for (Object square : lst) {
            double tmp = (double) square;
            addSquare += tmp * tmp;

        }
        for (Object sumaN : lst) {
            add += (double) sumaN;
        }
        double media = 0.0;
        if (add != 0 && addSquare != 0) {
            media = addSquare / add;
        }
        return media;
    }

    public double media(ArrayList lst) {
        double add = 0.0;
        for (Object numb : lst) {
            add += (double) numb;
        }
        if (!lst.isEmpty()) {
            add = add / lst.size();
        }
        return add;
    }
    
    public double randCauchy(double mu, double delta) {//% Cauchy distribution: cauchypdf = @(x, mu, delta) 1/pi*delta./((x-mu).^2+delta^2) -->MA-ELM
        double val = mu + delta * Math.tan(Math.PI * (randomGenerator.nextDouble() - 0.5));
        return val;
    }

    /*----------Set and get---------*/
    public int getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(int evaluations) {
        this.evaluations = evaluations;
    }

    public JMetalRandom getRandomGenerator() {
        return randomGenerator;
    }

    public void setRandomGenerator(JMetalRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

}
