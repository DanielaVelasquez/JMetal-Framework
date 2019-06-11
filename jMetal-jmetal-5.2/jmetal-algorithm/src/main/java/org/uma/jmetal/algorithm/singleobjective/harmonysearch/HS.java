package org.uma.jmetal.algorithm.singleobjective.harmonysearch;

import java.util.Comparator;

import org.uma.jmetal.algorithm.impl.AbstractHarmonySearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.FitnessNorma2Comparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class HS extends AbstractHarmonySearch<DoubleSolution, DoubleSolution> {

	/**
	 * Harmony Search (HS) Based on
	 * http://www.sciencedirect.com/science/article/pii/S0045782504004682 L. G. A.
	 * Geem ZW, Kim JH, “A new heuristic optimization algorithm: Harmony search.
	 * Simulation,” Geem ZW, Kim JH, Loganathan GV. A. 2001.
	 * https://sites.google.com/a/hydroteq.com/www/
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parameters
	 */
	private double par;// pitch adjusting rate
	private double bw;// Bandwidth
	private double hmcr;// Harmony memory Consideration Rate
	private JMetalRandom randomGenerator;

	/**
	 *
	 * @param problem
	 * @param maxEvaluations ,Maximum number of evaluations of the objective
	 *                       function
	 * @param hms            ,Harmony memory size
	 * @param                par, Pitch Adjuting Rate
	 * @param bw             ,Bandwidth
	 * @param                hmcr, Harmony Memory Consideration Rate
	 * @param evaluator
	 */
	public HS(DoubleProblem problem, int maxEvaluations, int hms, double hmcr, double par, double bw,
			SolutionListEvaluator<DoubleSolution> evaluator) {

		setProblem(problem);
		setHMS(hms);
		setEvaluations(0);
		setMaxEvaluations(maxEvaluations);
		setEvaluator(evaluator);
		Comparator<DoubleSolution> comparator = new FitnessNorma2Comparator<>();
		setComparator(comparator);
		this.par = par;
		this.bw = bw;
		this.hmcr = hmcr;
		if (randomGenerator == null) {
			randomGenerator = JMetalRandom.getInstance();
		}

	}

	@Override
	public String getName() {
		return "HS";
	}

	@Override
	public String getDescription() {
		return new StringBuilder().append(" Harmony Search ").append(" HMS ").append(getHMS()).append(" hmcr: ")
				.append(hmcr).append(" par: ").append(par).append(" bw: ").append(bw).append("\n Evaluations ")
				.append(super.getEvaluations()).toString();
	}

	/**
	 * @return newSolution ,is a solution MUST BE CLONE IF IT IS BETTER THAN THE
	 *         WORST OF THE HARMONIC MEMORY
	 */
	@Override
	public DoubleSolution improviceNewHarmony() {
		for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
			if (randomGenerator.nextDouble() < hmcr) {
				memoryConsideration(i);
				if (randomGenerator.nextDouble() < par) {
					pitchAdjustment(i);
				}
			} else {
				randomSelection(i);
			}
		}
		return NCHV;
	}

	/* -----------Own methods------------- */
	/**
	 *
	 * @param varIndex
	 */
	public void memoryConsideration(int varIndex) {
		int rand = randomGenerator.nextInt(0, getHMS() - 1);
		NCHV.setVariableValue(varIndex, getHarmonyMemory().get(rand).getVariableValue(varIndex));
	}

	public void pitchAdjustment(int varIndex) {
		double temp = NCHV.getVariableValue(varIndex);

		double rand = randomGenerator.nextDouble();// random(0,1)
		if (rand <= 0.5) {
			temp -= randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)) * bw;
		} else {
			temp += randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)) * bw;
		}
		NCHV.setVariableValue(varIndex, temp);

		/**
		 * Limit control
		 */
		if (NCHV.getVariableValue(varIndex) > NCHV.getUpperBound(varIndex)) {
			NCHV.setVariableValue(varIndex, NCHV.getUpperBound(varIndex));
		} else {
			if (NCHV.getVariableValue(varIndex) < NCHV.getLowerBound(varIndex)) {
				NCHV.setVariableValue(varIndex, NCHV.getLowerBound(varIndex));
			}
		}

	}

	/* -------------------------Set And Get ---------------------------------- */
	public void randomSelection(int varIndex) {
		NCHV.setVariableValue(varIndex,
				randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
	}

	public void setPAR(double par) {
		this.par = par;
	}

	public void setHMCR(double hmcr) {
		this.hmcr = hmcr;
	}

	public void setBW(double bw) {
		this.bw = bw;
	}
}
