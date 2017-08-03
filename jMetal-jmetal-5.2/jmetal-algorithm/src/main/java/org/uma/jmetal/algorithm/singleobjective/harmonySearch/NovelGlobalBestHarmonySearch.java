package org.uma.jmetal.algorithm.singleobjective.harmonySearch;

import java.util.Collections;
import java.util.Comparator;
import org.uma.jmetal.algorithm.impl.AbstractHarmonySearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class NovelGlobalBestHarmonySearch
        extends AbstractHarmonySearch<DoubleSolution, DoubleSolution> {

    /**
     * -------------------------------------------------------------------------
     * Based on "A novel global harmony search algorithm for task assignment
     * problem"
     * http://www.sciencedirect.com/science/article/pii/S0164121210001470
     * -------------------------------------------------------------------------
     */
    /**
     * Parameters
     */
    private final double PM;//Probability of mutation
    private JMetalRandom randomGenerator;

    /**
     *
     * @param problem
     * @param maxEvaluations
     * @param hms
     * @param PM
     * @param evaluator
     */
    public NovelGlobalBestHarmonySearch(DoubleProblem problem, int maxEvaluations, int hms,
            double PM, SolutionListEvaluator<DoubleSolution> evaluator) {

        setProblem(problem);
        setMaxEvaluations(maxEvaluations);
        setHMS(hms);
        setEvaluations(0);
        setEvaluator(evaluator);
        Comparator<DoubleSolution> comparator = new ObjectiveComparator<>(0);
        setComparator(comparator);

        super.NCHV = problem.createSolution();//just to reserve memory
        this.PM = PM;
    }

    @Override
    public String getName() {
        return "NovelGHS";
    }

    @Override
    public String getDescription() {
        return "Novel Global Best Harmony Search " + " PM: " + PM + " \nEvaluations : " + getEvaluations();
    }

    @Override
    public DoubleSolution improviceNewHarmony() {
        for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
            double xR = xR(i);
            positionUpdate(i, xR);
            double al = randomGenerator.nextDouble();
            if (al < PM) {
                randomSelection(i);
            }
        }
        return NCHV;
    }

    @Override
    public DoubleSolution getResult() {
        Collections.sort(getHarmonicMemory(), getComparator());
        return getHarmonicMemory().get(0);
    }

    /*------------------------------------Own methods--------------------------*/
    public void randomSelection(int varIndex) {
        NCHV.setVariableValue(varIndex, randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
    }

    public double xR(int varIndex) {
        double xR = 2 * getHarmonicMemory().get(getBestIndexHM()).getVariableValue(varIndex)
                - getHarmonicMemory().get(getWorstIndexHM()).getVariableValue(varIndex);
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
        double pos = getHarmonicMemory().get(getWorstIndexHM()).getVariableValue(varIndex)
                + (randomGenerator.nextDouble() * (xR - getHarmonicMemory().get(getWorstIndexHM()).getVariableValue(varIndex)));
        NCHV.setVariableValue(varIndex, pos);
    }

    /*-------------------- Set and Get -------------------------*/
    public void setRandonGenerator(JMetalRandom rnd) {
        this.randomGenerator = rnd;
    }

}
