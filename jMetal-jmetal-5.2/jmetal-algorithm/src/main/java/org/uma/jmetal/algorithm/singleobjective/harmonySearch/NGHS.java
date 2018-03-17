package org.uma.jmetal.algorithm.singleobjective.harmonySearch;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.impl.AbstractHarmonySearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.FitnessNorma2Comparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class NGHS
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
    public NGHS(DoubleProblem problem, int maxEvaluations, int hms,
            double PM, SolutionListEvaluator<DoubleSolution> evaluator) {

        setProblem(problem);
        setMaxEvaluations(maxEvaluations);
        setHMS(hms);
        setEvaluations(0);
        setEvaluator(evaluator);
        Comparator<DoubleSolution> comparator = new FitnessNorma2Comparator<>();
        setComparator(comparator);

        this.PM = PM;
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
        return "Novel Global Best Harmony Search " + " HMS: " + getHMS() + " PM: " + PM + " \nEvaluations : " + getEvaluations();
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

    /*SOBRE ESCRIBIR LA ACTUALIZACION DE MEMORIA ARMONICA*/
    /**
     * Update harmony memory
     *
     * @param NewHarmony to be considerate
     * @return new HARMONY MEMORY
     *
     */
    @Override
    public List<DoubleSolution> updateHarmonicMemory(DoubleSolution NewHarmony) {
        for (DoubleSolution sol : getHarmonicMemory()) {
            if (getComparator().compare(sol, NewHarmony) == 0) {
                return getHarmonicMemory();
            }
        }
        getHarmonicMemory().remove(getWorstIndexHM());
        getHarmonicMemory().add((DoubleSolution) NewHarmony.copy());
        updateWorstIndex();
        updateBestIndex();
        return getHarmonicMemory();
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
        if (pos < NCHV.getLowerBound(varIndex)) {//el control de los limites es necesario, Aunque en el paper originar no lo consideren
            pos = NCHV.getLowerBound(varIndex);
        } else {
            if (pos > NCHV.getUpperBound(varIndex)) {
                pos = NCHV.getUpperBound(varIndex);
            }
        }
        NCHV.setVariableValue(varIndex, pos);
    }

}
