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
public class GHSBuilder implements AlgorithmBuilder<GHS> {

	private DoubleProblem problem;
	private int maxEvaluations;// Maximum number of evaluations of the objective function
	private int hms;// Harmonic memory Size
	private double hmcr;// Harmonic memory Consideration Rate

	private double parMax;// pitch adjusting rate
	private double parMin;// pitch adjusting rate

	private SolutionListEvaluator<DoubleSolution> evaluator;

	/**
	 *
	 * @param problem, With default parameters (The values recommended in the
	 *        literature)
	 */
	public GHSBuilder(DoubleProblem problem) {
		this.problem = problem;
		this.hms = 10;
		this.maxEvaluations = 25000;
		this.parMax = 0.8;
		this.parMin = 0.01;
		this.hmcr = 0.8;
		this.evaluator = new SequentialSolutionListEvaluator<>();
	}

	@Override
	public GHS build() {
		return new GHS(problem, maxEvaluations, hms, hmcr, parMin, parMax, evaluator);
	}

	public int getHMS() {
		return hms;
	}

	public GHSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
		this.evaluator = evaluator;
		return this;
	}

	public GHSBuilder setHMS(int hms) {
		this.hms = hms;
		return this;
	}

	public double getHMCR() {
		return hmcr;
	}

	public GHSBuilder setHMCR(double hmcr) {
		this.hmcr = hmcr;
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
		return parMax;
	}

	public GHSBuilder setPARMAX(double parMax) {
		this.parMax = parMax;
		return this;
	}

	public double getPARMIN() {
		return parMin;
	}

	public GHSBuilder setPARMIN(double parMin) {
		this.parMin = parMin;
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
