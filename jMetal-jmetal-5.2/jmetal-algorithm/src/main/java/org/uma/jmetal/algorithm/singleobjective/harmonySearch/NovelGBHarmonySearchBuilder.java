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
public class NovelGBHarmonySearchBuilder {

    private DoubleProblem problem;
    private int maxEvaluations;
    private int HMS;//Harmonic memory Size
    private double PM;
    private JMetalRandom randomGenerator;
    private SolutionListEvaluator<DoubleSolution> evaluator;

    public NovelGBHarmonySearchBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.HMS = 10;
        this.maxEvaluations = 25000;
        randomGenerator = JMetalRandom.getInstance();
        this.evaluator = new SequentialSolutionListEvaluator<>();
    }

    public NovelGlobalBestHarmonySearch build() {
        NovelGlobalBestHarmonySearch hs = new NovelGlobalBestHarmonySearch(problem, maxEvaluations, HMS, PM, evaluator);
        hs.setRandonGenerator(randomGenerator);
        return hs;
    }

    public int getHMS() {
        return HMS;
    }

    public NovelGBHarmonySearchBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public NovelGBHarmonySearchBuilder setHMS(int HMS) {
        this.HMS = HMS;
        return this;
    }

    public double getPM() {
        return PM;
    }

    public NovelGBHarmonySearchBuilder setPM(double PM) {
        this.PM = PM;
        return this;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public NovelGBHarmonySearchBuilder setProblem(DoubleProblem problem) {
        this.problem = problem;
        return this;
    }

    public JMetalRandom getRandomGenerator() {
        return randomGenerator;
    }

    public NovelGBHarmonySearchBuilder setRandomGenerator(JMetalRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public NovelGBHarmonySearchBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public SolutionListEvaluator<DoubleSolution> getEvaluator() {
        return evaluator;
    }

    public NovelGBHarmonySearchBuilder setEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        return this;

    }

}
