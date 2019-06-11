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
public class HSBuilder implements AlgorithmBuilder<HS> {

	/**
	 * Parameters
	 */
	private DoubleProblem problem;
	private int maxEvaluations;
	private int hms;// Harmonic memory Size
	private double par;// pitch adjusting rate
	private double bw;// Bandwidth
	private double hmcr;// Harmonic memory Consideration Rate
	private SolutionListEvaluator<DoubleSolution> evaluator;

	public HSBuilder(DoubleProblem problem) {
		this.problem = problem;
		this.hms = 10;
		this.maxEvaluations = 0;
		this.par = 0.4;
		this.bw = 0.1;
		this.hmcr = 0.8;
		this.evaluator = new SequentialSolutionListEvaluator<>();
	}

	/**
	 * @return HarmonySearch algorithm
	 */
	@Override
	public HS build() {
		return new HS(problem, maxEvaluations, hms, hmcr, par, bw, evaluator);
	}

	/*---------Set and get---------------*/
	public int getHMS() {
		return hms;
	}

	public HSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
		this.evaluator = evaluator;
		return this;
	}

	public HSBuilder setHMS(int hms) {
		this.hms = hms;
		return this;
	}

	public double getPAR() {
		return par;
	}

	public HSBuilder setPAR(double par) {
		this.par = par;
		return this;
	}

	public double getBW() {
		return bw;
	}

	public HSBuilder setBW(double bw) {
		this.bw = bw;
		return this;
	}

	public double getHMCR() {
		return hmcr;
	}

	public HSBuilder setHMCR(double hmcr) {
		this.hmcr = hmcr;
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
