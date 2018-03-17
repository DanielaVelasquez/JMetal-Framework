package org.uma.jmetal.algorithm.singleobjective.harmonySearch;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class NGHSBuilder {

    private DoubleProblem problem;
    private int maxEvaluations;
    private int HMS;//Harmonic memory Size
    private double PM;
    private SolutionListEvaluator<DoubleSolution> evaluator;

    public NGHSBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.HMS = 10;
        this.maxEvaluations = 25000;
        this.evaluator = new SequentialSolutionListEvaluator<>();
    }

    public NGHS build() {
        NGHS hs = new NGHS(problem, maxEvaluations, HMS, PM, evaluator);
        return hs;
    }

    public int getHMS() {
        return HMS;
    }

    public NGHSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public NGHSBuilder setHMS(int HMS) {
        this.HMS = HMS;
        return this;
    }

    public double getPM() {
        return PM;
    }

    public NGHSBuilder setPM(double PM) {
        this.PM = PM;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public NGHSBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public NGHSBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public SolutionListEvaluator<DoubleSolution> getEvaluator() {
        return evaluator;
    }

    public NGHSBuilder setEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;

    }

}
