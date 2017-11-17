package org.uma.jmetal.algorithm.singleobjective.harmonySearch;

import java.util.Comparator;
import org.uma.jmetal.algorithm.impl.AbstractHarmonySearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.FitnessNorma2Comparator;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class GHS
        extends AbstractHarmonySearch<DoubleSolution, DoubleSolution> {

    /**
     * Global Best Harmony Search Based on
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
    private JMetalRandom randomGenerator;

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
    public GHS(DoubleProblem problem, int maxEvaluations, int hms,
            double HMCR, double PARMIN, double PARMAX, SolutionListEvaluator<DoubleSolution> evaluator) {
        setProblem(problem);
        setHMS(hms);
        setEvaluations(0);
        setMaxEvaluations(maxEvaluations);
        setEvaluator(evaluator);
      Comparator<DoubleSolution> comparator = new FitnessNorma2Comparator<>();
        setComparator(comparator);

        this.parMin = PARMIN;
        this.parMax = PARMAX;
        this.HMCR = HMCR;
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
        return "Global Best Harmony Search" + " HMS " + super.getHMS() + " HMCR " + this.HMCR + " ParMin: " + this.parMin + " ParMax " + this.parMax + "\n EFOS: " + getEvaluations();
    }

    @Override
    public DoubleSolution improviceNewHarmony() {
        for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
            if (randomGenerator.nextDouble() < HMCR) {
                memoryConsideration(i);
                if (randomGenerator.nextDouble() < PAR(getEvaluations())) {
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
    public double PAR(int t) {
        double paradjust = parMin + (((parMax - parMin) / getMaxEvaluations()) * t);
        return paradjust;
    }

    /**
     * @param varIndex, current dimention of vector
     */
    public void goTotheGlobalBest(int varIndex) {
        int columnRand = randomGenerator.nextInt(0, getProblem().getNumberOfVariables() - 1);//
        NCHV.setVariableValue(varIndex, getHarmonicMemory().get(getBestIndexHM()).getVariableValue(columnRand));
    }

    public void memoryConsideration(int varIndex) {
        int rand = randomGenerator.nextInt(0, getHMS() - 1);
        NCHV.setVariableValue(varIndex, getHarmonicMemory().get(rand).getVariableValue(varIndex));
    }

    public void randomSelection(int varIndex) {
        NCHV.setVariableValue(varIndex, randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
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

}
