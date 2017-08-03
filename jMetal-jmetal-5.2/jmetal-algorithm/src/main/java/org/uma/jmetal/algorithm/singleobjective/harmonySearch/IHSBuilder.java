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
public class IHSBuilder {

    private DoubleProblem problem;
    private int maxEvaluations;
    private int HMS;//Harmonic memory Size
    private double PARMAX;//pitch adjusting rate maximum
    private double PARMIN;//pitch adjusting rate minimum
    private double BWMIN;//Bandidth minimum
    private double BWMAX;//Bandwidth maximum

    private double HMCR;//Harmonic memory Consideration Rate
    private JMetalRandom randomGenerator;
    private SolutionListEvaluator<DoubleSolution> evaluator;

    public IHSBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.HMS = 10;
        this.maxEvaluations = 250008888;
        this.PARMIN = 0.1;
        this.PARMAX = 0.5;

        this.BWMIN = 0.01;
        this.BWMAX = 0.4;

        this.HMCR = 0.8;
        randomGenerator = JMetalRandom.getInstance();
        this.evaluator = new SequentialSolutionListEvaluator<>();
    }

    public ImprovedHarmonySearch build() {
        ImprovedHarmonySearch hs = new ImprovedHarmonySearch(problem, maxEvaluations, HMS, PARMIN, PARMAX, BWMIN, BWMAX, HMCR, evaluator);
        hs.setRandonGenerator(randomGenerator);
        return hs;
    }

    public int getHMS() {
        return HMS;
    }

    public IHSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public IHSBuilder setHMS(int HMS) {
        this.HMS = HMS;
        return this;
    }

    public double getHMCR() {
        return HMCR;
    }

    public IHSBuilder setHMCR(double HMCR) {
        this.HMCR = HMCR;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public IHSBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public JMetalRandom getRandomGenerator() {
        return randomGenerator;
    }

    public IHSBuilder setRandomGenerator(JMetalRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public IHSBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public double getPARMAX() {
        return PARMAX;
    }

    public IHSBuilder setPARMAX(double PARMAX) {
        this.PARMAX = PARMAX;
        return this;
    }

    public double getPARMIN() {
        return PARMIN;
    }

    public IHSBuilder setPARMIN(double PARMIN) {
        this.PARMIN = PARMIN;
        return this;
    }

    public SolutionListEvaluator<DoubleSolution> getEvaluator() {
        return evaluator;
    }

    public IHSBuilder setEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;

    }

    public double getBWMIN() {
        return BWMIN;
    }

    public IHSBuilder setBWMIN(double BWMIN) {
        this.BWMIN = BWMIN;
        return this;
    }

    public double getBWMAX() {
        return BWMAX;
    }

    public IHSBuilder setBWMAX(double BWMAX) {
        this.BWMAX = BWMAX;
        return this;

    }

}
