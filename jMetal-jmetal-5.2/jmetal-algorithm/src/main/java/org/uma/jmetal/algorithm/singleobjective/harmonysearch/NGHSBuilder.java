package org.uma.jmetal.algorithm.singleobjective.harmonysearch;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class NGHSBuilder implements AlgorithmBuilder<NGHS> {

	private DoubleProblem problem;
	private int maxEvaluations;
	private int hms;// Harmonic memory Size
	private double pm;
	private SolutionListEvaluator<DoubleSolution> evaluator;

	public NGHSBuilder(DoubleProblem problem) {
		this.problem = problem;
		this.hms = 10;
		this.maxEvaluations = 25000;
		this.evaluator = new SequentialSolutionListEvaluator<>();
	}

	@Override
	public NGHS build() {
		return new NGHS(problem, maxEvaluations, hms, pm, evaluator);
	}

	public int getHMS() {
		return hms;
	}

	public NGHSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
		this.evaluator = evaluator;
		return this;
	}

	public NGHSBuilder setHMS(int hms) {
		this.hms = hms;
		return this;
	}

	public double getPM() {
		return pm;
	}

	public NGHSBuilder setPM(double pm) {
		this.pm = pm;
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
