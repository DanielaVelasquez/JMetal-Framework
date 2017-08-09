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
 * @author Daniel Pusil<danielpusil@unicauca.edu.co>
 */
public final class ImprovedHarmonySearch extends
        AbstractHarmonySearch<DoubleSolution, DoubleSolution> {

    /**
     * -------------------------------------------------------------------------
     * Based On
     * http://www.sciencedirect.com/science/article/pii/S0096300306015098 M.
     * Mahdavi, M. Fesanghary, E. Damangir. An improved harmony search algorithm
     * for solving optimization problems, Applied mathematics and computation,
     * 188(2007) 1567-1579.7
     * -------------------------------------------------------------------------
     */
    /**
     * Parametters ------------
     */
    public double HMCR;//Harmonic memory Consideration Rate
    public double parMin;//pitch adjusting rate minimum
    public double parMax;//pitch adjusting rate minimum
    public double BWMin;//Bandwidth minimum
    public double BWMax;//Bandwidth maximun

    /**/
    public double C;
    private JMetalRandom randomGenerator;

    /**
     * Contructor
     *
     * @param problem
     * @param maxEvaluations
     * @param hms
     * @param PARMIN
     * @param PARMAX
     * @param BWMIN
     * @param BWMAX
     * @param HMCR
     * @param evaluator
     */
    public ImprovedHarmonySearch(DoubleProblem problem, int maxEvaluations, int hms,
            double PARMIN, double PARMAX,
            double BWMIN, double BWMAX,
            double HMCR, SolutionListEvaluator<DoubleSolution> evaluator) {
        setProblem(problem);
        setMaxEvaluations(maxEvaluations);
        setHMS(hms);
        setEvaluations(0);
        setEvaluator(evaluator);
        Comparator<DoubleSolution> comparator = new ObjectiveComparator<>(0);
        setComparator(comparator);

        super.NCHV = problem.createSolution();//just to reserve memory
        this.parMin = PARMIN;
        this.parMax = PARMAX;

        this.BWMin = BWMIN;
        this.BWMax = BWMAX;

        this.HMCR = HMCR;
        calculateC();
    }

    @Override
    public DoubleSolution improviceNewHarmony() {
        for (int i = 0; i < getProblem().getNumberOfVariables(); i++) {
            if (randomGenerator.nextDouble() < HMCR) {
                memoryConsideration(i);
                if (randomGenerator.nextDouble() < pAR(getEvaluations())) {
                    pitchAjustmen(i, getEvaluations());/*Calcular una sola vez por generacion y no por variable */

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

    @Override
    public String getName() {
        return "IHS";
    }

    @Override
    public String getDescription() {
        return "Improve Harmony search " + " HMCR: " + HMCR + " PARMin: " + parMin + " PARMax: " + parMax + " BWMin: " + BWMin + " BWMax: " + BWMax + "\n Evaluations:  " + getEvaluations();
    }

    /*---------------------------------- own methods----------------------------*/
    public void memoryConsideration(int varIndex) {
        int rand = randomGenerator.nextInt(0, getHMS() - 1);
        NCHV.setVariableValue(varIndex, getHarmonicMemory().get(rand).getVariableValue(varIndex));
    }

    public void randomSelection(int varIndex) {
        NCHV.setVariableValue(varIndex, randomGenerator.nextDouble(NCHV.getLowerBound(varIndex), NCHV.getUpperBound(varIndex)));
    }

    /**
     * pitch adjusting rate for each generation
     *
     * @param t, current generation
     * @return pAR, the new value of Pitch Ajuting Ratio
     */
    public double pAR(int t) {
        double paradjust = parMin + (((parMax - parMin) / getMaxEvaluations()) * t);
        return paradjust;
    }

    public double calculateC() {
        C = Math.log(BWMin / BWMax)
                / (getMaxEvaluations() - 1);
        return C;
    }

    /**
     * Applies pitch adjustment based on adaptive bandWith
     *
     * @param i, current variable
     * @param t, current iteration
     */
    public void pitchAjustmen(int i, int t) {
        double value = dynamicBW(t);
        double rand = randomGenerator.nextDouble();//random(0,1)
        if (rand <= 0.5) {
            value = NCHV.getVariableValue(i) + randomGenerator.nextDouble() * value;

        } else {
            value = NCHV.getVariableValue(i) - randomGenerator.nextDouble() * value;

        }
        /*Bounds control*/
        if (value < NCHV.getLowerBound(i)) {
            value = NCHV.getLowerBound(i);
        } else {
            if (value > NCHV.getUpperBound(i)) {
                value = NCHV.getUpperBound(i);

            }
        }
        NCHV.setVariableValue(i, value);
    }

    /**
     * Calculate the BW for the current iteration (improvisation)
     *
     * @param t, Current iteration
     * @return BW, the new value of BW for the current iteration
     */
    public double dynamicBW(int t) {
        double exponente = C * (t - 1);
        double exponencial = Math.pow(Math.E, exponente);
        double BW = BWMax * exponencial;
        return BW;
    }

    public void setRandonGenerator(JMetalRandom rnd) {
        this.randomGenerator = rnd;
    }

    /*----------------Set and Get-----------------------*/
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

    public double getBWMin() {
        return BWMin;
    }

    public void setBWMin(double BWMin) {
        this.BWMin = BWMin;
    }

    public double getBWMax() {
        return BWMax;
    }

    public void setBWMax(double BWMax) {
        this.BWMax = BWMax;
    }

    public double getC() {
        return C;
    }

    public void setC(double C) {
        this.C = C;
    }

    public void setRandomGenerator(JMetalRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

}
