package org.uma.jmetal.algorithm.singleobjective.harmonySearch;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class GHSBuilder {

    private DoubleProblem problem;
    private int maxEvaluations;//Maximum number of evaluations of the objective function
    private int HMS;//Harmonic memory Size
    private double HMCR;//Harmonic memory Consideration Rate

    private double PARMAX;//pitch adjusting rate
    private double PARMIN;//pitch adjusting rate

    private SolutionListEvaluator<DoubleSolution> evaluator;

    /**
     *
     * @param problem, With default parameters (The values recommended in the
     * literature)
     */
    public GHSBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.HMS = 10;
        this.maxEvaluations = 25000;
        this.PARMAX = 0.8;
        this.PARMIN = 0.01;
        this.HMCR = 0.8;
        this.evaluator = new SequentialSolutionListEvaluator<>();
    }

    public GHS build() {
        GHS hs = new GHS(problem, maxEvaluations, HMS, HMCR, PARMIN, PARMAX, evaluator);
        return hs;
    }

    public int getHMS() {
        return HMS;
    }

    public GHSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public GHSBuilder setHMS(int HMS) {
        this.HMS = HMS;
        return this;
    }

    public double getHMCR() {
        return HMCR;
    }

    public GHSBuilder setHMCR(double HMCR) {
        this.HMCR = HMCR;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public GHSBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public GHSBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public double getPARMAX() {
        return PARMAX;
    }

    public GHSBuilder setPARMAX(double PARMAX) {
        this.PARMAX = PARMAX;
        return this;
    }

    public double getPARMIN() {
        return PARMIN;
    }

    public GHSBuilder setPARMIN(double PARMIN) {
        this.PARMIN = PARMIN;
        return this;
    }

    public SolutionListEvaluator<DoubleSolution> getEvaluator() {
        return evaluator;
    }

    public GHSBuilder setEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;

    }

}
