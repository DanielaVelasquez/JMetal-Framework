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
public class IHSBuilder implements AlgorithmBuilder<IHS> {

	private DoubleProblem problem;
	private int maxEvaluations;
	private int hms;// Harmonic memory Size
	private double parMax;// pitch adjusting rate maximum
	private double parMin;// pitch adjusting rate minimum
	private double bwMin;// Bandidth minimum
	private double bwMax;// Bandwidth maximum

	private double hmcr;// Harmonic memory Consideration Rate
	private SolutionListEvaluator<DoubleSolution> evaluator;

	public IHSBuilder(DoubleProblem problem) {
		this.problem = problem;
		this.hms = 10;
		this.maxEvaluations = 250008888;
		this.parMin = 0.1;
		this.parMax = 0.5;

		this.bwMin = 0.01;
		this.bwMax = 0.4;

		this.hmcr = 0.8;
		this.evaluator = new SequentialSolutionListEvaluator<>();
	}

	@Override
	public IHS build() {
		return new IHS(problem, maxEvaluations, hms, hmcr, parMin, parMax, bwMin, bwMax, evaluator);

	}

	public int getHMS() {
		return hms;
	}

	public IHSBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
		this.evaluator = evaluator;
		return this;
	}

	public IHSBuilder setHMS(int hms) {
		this.hms = hms;
		return this;
	}

	public double getHMCR() {
		return hmcr;
	}

	public IHSBuilder setHMCR(double hmcr) {
		this.hmcr = hmcr;
		return this;
	}

	public DoubleProblem getProblem() {
		return problem;
	}

	public IHSBuilder setProblem(DoubleProblem problem) {
		this.problem = problem;
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
		return parMax;
	}

	public IHSBuilder setPARMAX(double parMax) {
		this.parMax = parMax;
		return this;
	}

	public double getPARMIN() {
		return parMin;
	}

	public IHSBuilder setPARMIN(double parMin) {
		this.parMin = parMin;
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
		return bwMin;
	}

	public IHSBuilder setBWMIN(double bwMin) {
		this.bwMin = bwMin;
		return this;
	}

	public double getBWMAX() {
		return bwMax;
	}

	public IHSBuilder setBWMAX(double bwMax) {
		this.bwMax = bwMax;
		return this;
	}
}
