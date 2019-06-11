package org.uma.jmetal.algorithm.singleobjective.harmonysearch;

import java.util.Comparator;
import java.util.List;

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
public class NGHS extends AbstractHarmonySearch<DoubleSolution, DoubleSolution> {
	/**
	 * -------------------------------------------------------------------------
	 * Based on "A novel global harmony search algorithm for task assignment
	 * problem" http://www.sciencedirect.com/science/article/pii/S0164121210001470
	 * -------------------------------------------------------------------------
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Parameters
	 */
	private final double pm;// Probability of mutation
	private JMetalRandom randomGenerator;

	/**
	 *
	 * @param problem
	 * @param maxEvaluations
	 * @param hms
	 * @param pm
	 * @param evaluator
	 */
	public NGHS(DoubleProblem problem, int maxEvaluations, int hms, double pm,
			SolutionListEvaluator<DoubleSolution> evaluator) {

		setProblem(problem);
		setMaxEvaluations(maxEvaluations);
		setHMS(hms);
		setEvaluations(0);
		setEvaluator(evaluator);
		Comparator<DoubleSolution> comparator = new FitnessNorma2Comparator<>();
		setComparator(comparator);

		this.pm = pm;
		if (randomGenerator == null) {
			randomGenerator = JMetalRandom.getInstance();
		}
	}

	@Override
	public String getName() {
		return "NovelGHS";
	}

	@Override
	public String getDescription() {
		return new StringBuilder().append("Novel Global Best Harmony Search ").append(" HMS: ").append(getHMS())
				.append(" pm: ").append(pm).append(" \nEvaluations : ").append(getEvaluations()).toString();
	}

	@Override
	public DoubleSolution improviceNewHarmony() {
		for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
			double xR = xR(i);
			positionUpdate(i, xR);
			double al = randomGenerator.nextDouble();
			if (al < pm) {
				randomSelection(i);
			}

		}
		return NCHV;
	}

	/**
	 * Update harmony memory
	 *
	 * @param NewHarmony to be considerate
	 * @return new HARMONY MEMORY
	 *
	 */
	@Override
	public List<DoubleSolution> updateHarmonyMemory(DoubleSolution newHarmony) {
		for (DoubleSolution sol : getHarmonyMemory()) {
			if (getComparator().compare(sol, newHarmony) == 0) {
				return getHarmonyMemory();
			}
		}
		getHarmonyMemory().remove(getWorstIndexHM());
		getHarmonyMemory().add((DoubleSolution) newHarmony.copy());
		updateWorstIndex();
		updateBestIndex();
		return getHarmonyMemory();
	}

	/*------------------------------------Own methods--------------------------*/
	public void randomSelection(int varIndex) {
		NCHV.setVariableValue(varIndex,
				randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
	}

	public double xR(int varIndex) {
		double xR = 2 * getHarmonyMemory().get(getBestIndexHM()).getVariableValue(varIndex)
				- getHarmonyMemory().get(getWorstIndexHM()).getVariableValue(varIndex);
		if (xR < NCHV.getLowerBound(varIndex)) {
			xR = NCHV.getLowerBound(varIndex);
		} else {
			if (xR > NCHV.getUpperBound(varIndex)) {
				xR = NCHV.getUpperBound(varIndex);
			}
		}
		return xR;
	}

	public void positionUpdate(int varIndex, double xR) {
		double pos = getHarmonyMemory().get(getWorstIndexHM()).getVariableValue(varIndex)
				+ (randomGenerator.nextDouble()
						* (xR - getHarmonyMemory().get(getWorstIndexHM()).getVariableValue(varIndex)));
		if (pos < NCHV.getLowerBound(varIndex)) {
			pos = NCHV.getLowerBound(varIndex);
		} else {
			if (pos > NCHV.getUpperBound(varIndex)) {
				pos = NCHV.getUpperBound(varIndex);
			}
		}
		NCHV.setVariableValue(varIndex, pos);
	}
}
