package org.uma.jmetal.algorithm.singleobjective.harmonysearch;

import java.util.Comparator;

import org.uma.jmetal.algorithm.impl.AbstractHarmonySearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.FitnessNorma2Comparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class GHS extends AbstractHarmonySearch<DoubleSolution, DoubleSolution> {

	/**
	 * Global Best Harmony Search Based on
	 * http://www.sciencedirect.com/science/article/pii/S0096300307009320 ----- M.
	 * G.H. Omran and M. Mahdavi, “Global-best harmony search,” Appl. Math. Comput.,
	 * vol. 198, no. 2, pp. 643–656, 2008.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Parameters
	 */
	private double hmcr;// Harmony memory Consideration Rate
	private double parMin;// pitch adjuting rate minimum
	private double parMax;// pitch adjuting rate maximum
	private JMetalRandom randomGenerator;

	/**
	 *
	 * @param problem
	 * @param maxEvaluations
	 * @param hms
	 * @param PARMIN
	 * @param PARMAX
	 * @param hmcr
	 * @param evaluator
	 */
	public GHS(DoubleProblem problem, int maxEvaluations, int hms, double hmcr, double parMin, double parMax,
			SolutionListEvaluator<DoubleSolution> evaluator) {
		setProblem(problem);
		setHMS(hms);
		setEvaluations(0);
		setMaxEvaluations(maxEvaluations);
		setEvaluator(evaluator);
		Comparator<DoubleSolution> comparator = new FitnessNorma2Comparator<>();
		setComparator(comparator);

		this.parMin = parMin;
		this.parMax = parMax;
		this.hmcr = hmcr;
		if (randomGenerator == null) {
			randomGenerator = JMetalRandom.getInstance();
		}
	}

	@Override
	public String getName() {
		return "GHS";
	}

	@Override
	public String getDescription() {
		return new StringBuilder().append("Global Best Harmony Search").append(" HMS ").append(super.getHMS())
				.append(" hmcr ").append(this.hmcr).append(" ParMin: ").append(this.parMin).append(" ParMax ")
				.append(this.parMax).append("\n EFOS: ").append(getEvaluations()).toString();
	}

	@Override
	public DoubleSolution improviceNewHarmony() {
		for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
			if (randomGenerator.nextDouble() < hmcr) {
				memoryConsideration(i);
				if (randomGenerator.nextDouble() < par(getEvaluations())) {
					goTotheGlobalBest(i);
				}
			} else {
				randomSelection(i);
			}
		}
		return NCHV;
	}

	/*--------------------------------------Own Methods----------------*/
	/**
	 *
	 * @param t current iteration (Generation)
	 * @return parAjust, the new value of PAR
	 */
	public double par(int t) {
		return parMin + (((parMax - parMin) / getMaxEvaluations()) * t);
	}

	/**
	 * @param varIndex, current dimention of vector
	 */
	public void goTotheGlobalBest(int varIndex) {
		int columnRand = randomGenerator.nextInt(0, getProblem().getNumberOfVariables() - 1);//
		NCHV.setVariableValue(varIndex, getHarmonyMemory().get(getBestIndexHM()).getVariableValue(columnRand));
	}

	public void memoryConsideration(int varIndex) {
		int rand = randomGenerator.nextInt(0, getHMS() - 1);
		NCHV.setVariableValue(varIndex, getHarmonyMemory().get(rand).getVariableValue(varIndex));
	}

	public void randomSelection(int varIndex) {
		NCHV.setVariableValue(varIndex,
				randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
	}

	/*---------------------------Set and Get-----------------------*/
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
}
