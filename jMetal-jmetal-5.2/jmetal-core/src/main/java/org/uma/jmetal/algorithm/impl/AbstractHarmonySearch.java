package org.uma.jmetal.algorithm.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public abstract class AbstractHarmonySearch<S extends Solution<?>, Result>
        implements Algorithm<Result> {

    /**
     * Abstract Harmony Search
     */
    /**
     * Parameters
     */
    public S NCHV;
    private List<S> HarmonicMemory;
    private Problem<S> problem;
    private int HMS;
    private int evaluations = 0;
    private int maxEvaluations;
    private int worstIndexHM = 0;
    private int bestIndexHM = 0;
    private SolutionListEvaluator<S> evaluator;
    private Comparator<S> comparator;
    private JMetalRandom randomGeneratorReference;

    //GENERAL CODE
    public void initProgress() {
        evaluations = getHMS();
    }

    public void updateProgress() {
        evaluations++;
    }

    public boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    public List<S> createInitialHarmonicMemory() {
        List<S> population = new ArrayList<>(getHMS());
        for (int i = 0; i < getHMS(); i++) {
            S newIndividual = getProblem().createSolution();
            population.add(newIndividual);
        }
        return population;
    }

    public List<S> evaluateHarmonicMemory() {
        return evaluator.evaluate(HarmonicMemory, getProblem());
    }

    public S evaluateHarmony(S harmonic) {
        List<S> lsttemp = new ArrayList<>();
        lsttemp.add(harmonic);
        return evaluator.evaluate(lsttemp, getProblem()).get(0);
    }

    /**
     * Update harmony memory
     *
     * @param NewHarmony to be considerate
     * @return new HARMONY MEMORY
     */
    public List<S> updateHarmonicMemory(S NewHarmony) {
        if (comparator.compare(getHarmonicMemory().get(getWorstIndexHM()), NewHarmony) > 0) {
            getHarmonicMemory().remove(getWorstIndexHM());
            getHarmonicMemory().add((S) NewHarmony.copy());
            updateWorstIndex();
            updateBestIndex();
        }
        return getHarmonicMemory();
    }

    /**
     * Find
     */
    public void updateWorstIndex() {
        setWorstIndexHM(0);
        for (int i = 0; i < getHMS(); i++) {
            if (getHarmonicMemory().get(i).getObjective(0) > getHarmonicMemory().get(getWorstIndexHM()).getObjective(0)) {//Minimize
                setWorstIndexHM(i);
            }
        }
    }

    public void updateBestIndex() {
        setBestIndexHM(0);
        for (int i = 0; i < getHMS(); i++) {
            if (getHarmonicMemory().get(i).getObjective(0) < getHarmonicMemory().get(getBestIndexHM()).getObjective(0)) {//minimize
                setBestIndexHM(i);
            }
        }

    }

    /**
     * Overwrite these methods with their own metaphor
     *
     * @return S solution
     */
    public abstract S improviceNewHarmony();

    /**
     * Main loop 
     * 1.Create Iniciacil HM 
     * 2.Valuate HM 
     * 3.Get best and worts harmony
     *
     */
    @Override
    public void run() {
        HarmonicMemory = createInitialHarmonicMemory();
        HarmonicMemory = evaluateHarmonicMemory();
        updateWorstIndex();
        updateBestIndex();

        initProgress();
        while (!isStoppingConditionReached()) {
            NCHV = improviceNewHarmony();
            NCHV = evaluateHarmony(NCHV);
            HarmonicMemory = updateHarmonicMemory(NCHV);
            updateProgress();
        }
        System.out.println("evaluations " + evaluations);
      
    }

    /* -------------------Setter and getters -----------*/
    public JMetalRandom getRandomGenerator() {
        if (randomGeneratorReference == null) {
            randomGeneratorReference = JMetalRandom.getInstance();
        }
        return randomGeneratorReference;
    }

    public void setRandomGeneratorReference(JMetalRandom randomGeneratorReference) {
        this.randomGeneratorReference = randomGeneratorReference;
    }

    public int getBestIndexHM() {
        return bestIndexHM;
    }

    public void setBestIndexHM(int bestIndexHM) {
        this.bestIndexHM = bestIndexHM;
    }

    public int getWorstIndexHM() {
        return worstIndexHM;
    }

    public void setWorstIndexHM(int worstIndexHM) {
        this.worstIndexHM = worstIndexHM;
    }

    public List<S> getHarmonicMemory() {
        return HarmonicMemory;
    }

    public void setHarmonicMemory(List<S> HM) {
        this.HarmonicMemory = HM;
    }

    public void setProblem(Problem<S> problem) {
        this.problem = problem;
    }

    public Problem<S> getProblem() {
        return problem;
    }

    public void setHMS(int hms) {
        this.HMS = hms;
    }

    public int getHMS() {
        return this.HMS;
    }

    public S getNCHV() {
        return NCHV;
    }

    public void setNCHV(S NCHV) {
        this.NCHV = NCHV;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(int evaluations) {
        this.evaluations = evaluations;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public SolutionListEvaluator<S> getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(SolutionListEvaluator<S> evaluator) {
        this.evaluator = evaluator;
    }

    public Comparator<S> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<S> comparator) {
        this.comparator = comparator;
    }

}