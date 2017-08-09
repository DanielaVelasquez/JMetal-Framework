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
public class HarmonySearchBuilder {

    /**
     * Parameters
     */
    private DoubleProblem problem;
    private int maxEvaluations;
    private int HMS;//Harmonic memory Size
    private double PAR;//pitch adjusting rate
    private double BW;//Bandwidth
    private double HMCR;//Harmonic memory Consideration Rate
    private JMetalRandom randomGenerator;
    private SolutionListEvaluator<DoubleSolution> evaluator;

    public HarmonySearchBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.HMS = 10;
        this.maxEvaluations = 25000;
        this.PAR = 0.4;
        this.BW = 0.1;
        this.HMCR = 0.8;
        randomGenerator = JMetalRandom.getInstance();
        this.evaluator = new SequentialSolutionListEvaluator<>();
    }

    /**
     * @return HarmonySearch algorithm
     */
    public HarmonySearch build() {
        HarmonySearch hs = new HarmonySearch(problem, maxEvaluations, HMS, PAR, BW, HMCR, evaluator);
        hs.setRandonGenerator(randomGenerator);
        return hs;
    }

    /*---------Set and get---------------*/
    public int getHMS() {
        return HMS;
    }

    public HarmonySearchBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public HarmonySearchBuilder setHMS(int HMS) {
        this.HMS = HMS;
        return this;
    }

    public double getPAR() {
        return PAR;
    }

    public HarmonySearchBuilder setPAR(double PAR) {
        this.PAR = PAR;
        return this;
    }

    public double getBW() {
        return BW;
    }

    public HarmonySearchBuilder setBW(double BW) {
        this.BW = BW;
        return this;
    }

    public double getHMCR() {
        return HMCR;
    }

    public HarmonySearchBuilder setHMCR(double HMCR) {
        this.HMCR = HMCR;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public HarmonySearchBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public JMetalRandom getRandomGenerator() {
        return randomGenerator;
    }

    public HarmonySearchBuilder setRandomGenerator(JMetalRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public HarmonySearchBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

}
