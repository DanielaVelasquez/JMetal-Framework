package org.uma.jmetal.algorithm.singleobjective.harmonySearch;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class HSBuilder {

    /**
     * Parameters
     */
    private DoubleProblem problem;
    private int maxEvaluations;
    private int HMS;//Harmonic memory Size
    private double PAR;//pitch adjusting rate
    private double BW;//Bandwidth
    private double HMCR;//Harmonic memory Consideration Rate
    private SolutionListEvaluator<DoubleSolution> evaluator;

    public HSBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.HMS = 10;
        this.maxEvaluations = 0;
        this.PAR = 0.4;
        this.BW = 0.1;
        this.HMCR = 0.8;
        this.evaluator = new SequentialSolutionListEvaluator<>();
    }

    /**
     * @return HarmonySearch algorithm
     */
    public HS build() {
        return new HS(problem, maxEvaluations, HMS, HMCR, PAR, BW, evaluator);
    }

    /*---------Set and get---------------*/
    public int getHMS() {
        return HMS;
    }

    public HSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public HSBuilder setHMS(int HMS) {
        this.HMS = HMS;
        return this;
    }

    public double getPAR() {
        return PAR;
    }

    public HSBuilder setPAR(double PAR) {
        this.PAR = PAR;
        return this;
    }

    public double getBW() {
        return BW;
    }

    public HSBuilder setBW(double BW) {
        this.BW = BW;
        return this;
    }

    public double getHMCR() {
        return HMCR;
    }

    public HSBuilder setHMCR(double HMCR) {
        this.HMCR = HMCR;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public HSBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public HSBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

}
