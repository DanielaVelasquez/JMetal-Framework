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
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class GlobalBestHarmonySearch
        extends AbstractHarmonySearch<DoubleSolution, DoubleSolution> {

    /**
     * Based on
     * http://www.sciencedirect.com/science/article/pii/S0096300307009320 -----
     * M. G.H. Omran and M. Mahdavi, “Global-best harmony search,” Appl. Math.
     * Comput., vol. 198, no. 2, pp. 643–656, 2008.
     */
    /**
     * Parameters
     */
    public double HMCR;//Harmonic memory Consideration Rate
    public double parMin;//pitch adjuting rate minimum
    public double parMax;//pitch adjuting rate maximum

    /**
     *
     * @param problem
     * @param maxEvaluations
     * @param hms
     * @param PARMIN
     * @param PARMAX
     * @param HMCR
     * @param evaluator
     */
    public GlobalBestHarmonySearch(DoubleProblem problem, int maxEvaluations, int hms,
            double PARMIN, double PARMAX, double HMCR, SolutionListEvaluator<DoubleSolution> evaluator) {
        setProblem(problem);
        setHMS(hms);
        setEvaluations(0);
        setMaxEvaluations(maxEvaluations);
        setEvaluator(evaluator);
        Comparator<DoubleSolution> comparator = new ObjectiveComparator<>(0);
        setComparator(comparator);

        this.parMax = PARMAX;
        this.parMin = PARMIN;
        this.HMCR = HMCR;
        NCHV = problem.createSolution();//just to reserve memory
    }

    @Override
    public String getName() {
        return "GHS";
    }

    @Override
    public String getDescription() {
        return "Global Best Harmony Search" + " ParMin: " + this.parMin + " ParMax " + this.parMax + " HMCR " + this.HMCR + " HMS " + super.getHMS() + "\n EFOS: " + getEvaluations();
    }

    @Override
    public DoubleSolution improviceNewHarmony() {
        for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
            if (getRandomGenerator().nextDouble() < HMCR) {
                memoryConsideration(i);
                if (getRandomGenerator().nextDouble() < PAR(getEvaluations())) {
                    goTotheGlobalBest(i);
                }
            } else {
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

    /*--------------------------------------Own Methods----------------*/
    /**
     *
     * @param t current iteration (Generation)
     * @return parAjust, the new value of PAR
     */
    public double PAR(int t) {
        double paradjust = parMin + (((parMax - parMin) / getMaxEvaluations()) * t);
        return paradjust;
    }

    /**
     * @param varIndex, current dimention of vector
     */
    public void goTotheGlobalBest(int varIndex) {
        int columnRand = getRandomGenerator().nextInt(0, getProblem().getNumberOfVariables() - 1);//
        NCHV.setVariableValue(varIndex, getHarmonicMemory().get(getBestIndexHM()).getVariableValue(columnRand));
    }

    public void memoryConsideration(int varIndex) {
        int rand = getRandomGenerator().nextInt(0, getHMS() - 1);
        NCHV.setVariableValue(varIndex, getHarmonicMemory().get(rand).getVariableValue(varIndex));
    }

    public void randomSelection(int varIndex) {
        NCHV.setVariableValue(varIndex, getRandomGenerator().nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
    }

    /*---------------------------Set and Get-----------------------*/
    public double getHMCR() {
        return HMCR;
    }

    public void setHMCR(double HMCR) {
        this.HMCR = HMCR;
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

    public void setRandonGenerator(JMetalRandom rnd) {
        setRandomGeneratorReference(getRandomGenerator());
    }
}
