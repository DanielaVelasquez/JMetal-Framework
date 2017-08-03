package org.uma.jmetal.algorithm.singleobjective.harmonySearch;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class GBHarmonySearchBuilder {

    private DoubleProblem problem;
    private int maxEvaluations;//Maximum number of evaluations of the objective function
    private int HMS;//Harmonic memory Size

    private double PARMAX;//pitch adjusting rate
    private double PARMIN;//pitch adjusting rate

    private double HMCR;//Harmonic memory Consideration Rate
    private JMetalRandom randomGenerator;
    private SolutionListEvaluator<DoubleSolution> evaluator;

    /**
     *
     * @param problem, With default parameters (The values recommended in the
     * literature)
     */
    public GBHarmonySearchBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.HMS = 10;
        this.maxEvaluations = 25000;
        this.PARMAX = 0.8;
        this.PARMIN = 0.01;
        this.HMCR = 0.8;
        randomGenerator = JMetalRandom.getInstance();
        this.evaluator = new SequentialSolutionListEvaluator<>();
    }

    public GlobalBestHarmonySearch build() {
        GlobalBestHarmonySearch hs = new GlobalBestHarmonySearch(problem, maxEvaluations, HMS, PARMIN, PARMAX, HMCR, evaluator);
        hs.setRandonGenerator(randomGenerator);
        return hs;
    }

    public int getHMS() {
        return HMS;
    }

    public GBHarmonySearchBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public GBHarmonySearchBuilder setHMS(int HMS) {
        this.HMS = HMS;
        return this;
    }

    public double getHMCR() {
        return HMCR;
    }

    public GBHarmonySearchBuilder setHMCR(double HMCR) {
        this.HMCR = HMCR;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public GBHarmonySearchBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public JMetalRandom getRandomGenerator() {
        return randomGenerator;
    }

    public GBHarmonySearchBuilder setRandomGenerator(JMetalRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public GBHarmonySearchBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public double getPARMAX() {
        return PARMAX;
    }

    public GBHarmonySearchBuilder setPARMAX(double PARMAX) {
        this.PARMAX = PARMAX;
        return this;
    }

    public double getPARMIN() {
        return PARMIN;
    }

    public GBHarmonySearchBuilder setPARMIN(double PARMIN) {
        this.PARMIN = PARMIN;
        return this;
    }

    public SolutionListEvaluator<DoubleSolution> getEvaluator() {
        return evaluator;
    }

    public GBHarmonySearchBuilder setEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;

    }

}
