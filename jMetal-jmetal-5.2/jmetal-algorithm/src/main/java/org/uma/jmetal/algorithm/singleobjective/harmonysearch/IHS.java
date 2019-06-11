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
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 */
public final class IHS extends AbstractHarmonySearch<DoubleSolution, DoubleSolution> {
	/**
	 * -------------------------------------------------------------------------
	 * Based On http://www.sciencedirect.com/science/article/pii/S0096300306015098
	 * M. Mahdavi, M. Fesanghary, E. Damangir. An improved harmony search algorithm
	 * for solving optimization problems, Applied mathematics and computation,
	 * 188(2007) 1567-1579.7
	 * -------------------------------------------------------------------------
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Parametters ------------
	 */
	private double hmcr;// Harmony memory Consideration Rate
	private double parMin;// pitch adjusting rate minimum
	private double parMax;// pitch adjusting rate minimum
	private double bwMin;// Bandwidth minimum
	private double bwMax;// Bandwidth maximun

	/**/
	public double c;
	private JMetalRandom randomGenerator;

	/**
	 * Contructor
	 *
	 * @param problem
	 * @param maxEvaluations
	 * @param hms
	 * @param PARMIN
	 * @param PARMAX
	 * @param bWMin
	 * @param bWMax
	 * @param hmcr
	 * @param evaluator
	 */
	public IHS(DoubleProblem problem, int maxEvaluations, int hms, double hmcr, double parMin, double parMax,
			double bWMin, double bWMax, SolutionListEvaluator<DoubleSolution> evaluator) {
		setProblem(problem);
		setMaxEvaluations(maxEvaluations);
		setHMS(hms);
		setEvaluations(0);
		setEvaluator(evaluator);
		Comparator<DoubleSolution> comparator = new FitnessNorma2Comparator<>();
		setComparator(comparator);
		this.parMin = parMin;
		this.parMax = parMax;

		this.bwMin = bWMin;
		this.bwMax = bWMax;

		this.hmcr = hmcr;
		if (randomGenerator == null) {
			randomGenerator = JMetalRandom.getInstance();
		}

		calculateC();
	}

	@Override
	public DoubleSolution improviceNewHarmony() {
		for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
			if (randomGenerator.nextDouble() < hmcr) {
				memoryConsideration(i);
				if (randomGenerator.nextDouble() < pAR(getEvaluations())) {
					pitchAjustmen(i, getEvaluations());/* Calcular una sola vez por generacion y no por variable */

				}
			} else {
				randomSelection(i);
			}
		}
		return NCHV;

	}

	@Override
	public String getName() {
		return "IHS";
	}

	@Override
	public String getDescription() {
		return "Improve Harmony search " + " HMS: " + getHMS() + " hmcr: " + hmcr + " PARMin: " + parMin + " PARMax: "
				+ parMax + " bwMin: " + bwMin + " bwMax: " + bwMax + "\n Evaluations:  " + getEvaluations();
	}

	/*---------------------------------- own methods----------------------------*/
	public void memoryConsideration(int varIndex) {
		int rand = randomGenerator.nextInt(0, getHMS() - 1);
		NCHV.setVariableValue(varIndex, getHarmonyMemory().get(rand).getVariableValue(varIndex));
	}

	public void randomSelection(int varIndex) {
		NCHV.setVariableValue(varIndex,
				randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
	}

	/**
	 * pitch adjusting rate for each generation
	 *
	 * @param t, current generation
	 * @return pAR, the new value of Pitch Ajuting Ratio
	 */
	public double pAR(int t) {
		return parMin + (((parMax - parMin) / getMaxEvaluations()) * t);

	}

	public double calculateC() {
		c = Math.log(bwMin / bwMax) / (getMaxEvaluations());
		return c;
	}

	/**
	 * Applies pitch adjustment based on adaptive bandWith
	 *
	 * @param i, current variable
	 * @param t, current iteration
	 */
	public void pitchAjustmen(int i, int t) {
		double bWDin = dynamicBW(t);
		double rand = randomGenerator.nextDouble();// random(0,1)
		if (rand <= 0.5) {
			bWDin = NCHV.getVariableValue(i) - randomGenerator.nextDouble() * bWDin;
		} else {
			bWDin = NCHV.getVariableValue(i) + randomGenerator.nextDouble() * bWDin;

		}
		/* Bounds control */
		if (bWDin < NCHV.getLowerBound(i)) {
			bWDin = NCHV.getLowerBound(i);
		} else {
			if (bWDin > NCHV.getUpperBound(i)) {
				bWDin = NCHV.getUpperBound(i);

			}
		}
		NCHV.setVariableValue(i, bWDin);
	}

	/**
	 * Calculate the BW for the current iteration (improvisation)
	 *
	 * @param t, Current iteration
	 * @return BW, the new value of BW for the current iteration
	 */
	public double dynamicBW(int t) {
		double exponente = c * (t);
		double exponencial = Math.pow(Math.E, exponente);
		return bwMax * exponencial;
	}

	/*----------------Set and Get-----------------------*/
	public double getHMCR() {
		return hmcr;
	}

	public void setHMCR(double hmcr) {
		this.hmcr = hmcr;
	}

	public double getParMin() {
		return parMin;
	}

	public void setParMin(double parMin) {
		this.parMin = parMin;
	}

	public double getParMax() {
		return parMax;
	}

	public void setParMax(double parMax) {
		this.parMax = parMax;
	}

	public double getBWMin() {
		return bwMin;
	}

	public void setBWMin(double bwMin) {
		this.bwMin = bwMin;
	}

	public double getBWMax() {
		return bwMax;
	}

	public void setBWMax(double bwMax) {
		this.bwMax = bwMax;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}
}
