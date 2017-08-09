package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import org.uma.jmetal.algorithm.singleobjective.LocalOptimizer.OptSimulatedAnnealing;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil
 */
public class MemeticEDBuilder {
    
    private DoubleProblem problem;
    private int populationSize;
    private int maxEvaluations;
    private DifferentialEvolutionCrossover crossoverOperator;
    private DifferentialEvolutionSelection selectionOperator;
    private SolutionListEvaluator<DoubleSolution> evaluator;
    private JMetalRandom rand;

    //Añadir optimizador local
    private LocalSearchOperator localSearch;
    
    public MemeticEDBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.populationSize = 50;
        this.maxEvaluations = 2500;
        this.crossoverOperator = new DifferentialEvolutionCrossover(0.4, 0.5, "current-to-best/1/bin");//Default
        this.selectionOperator = new DifferentialEvolutionSelection();
        this.evaluator = new SequentialSolutionListEvaluator<>();
        this.localSearch = new OptSimulatedAnnealing(10, null, null, problem);
        this.rand = JMetalRandom.getInstance();
        
    }
    
    public MemeticEDBuilder setPopulationSize(int populationSize) {
        if (populationSize < 0) {
            throw new JMetalException("Population size is negative: " + populationSize);
        }
        
        this.populationSize = populationSize;
        
        return this;
    }
    
    public MemeticEDBuilder setMaxEvaluations(int maxEvaluations) {
        if (maxEvaluations < 0) {
            throw new JMetalException("MaxEvaluations is negative: " + maxEvaluations);
        }
        
        this.maxEvaluations = maxEvaluations;
        
        return this;
    }
    
    public MemeticEDBuilder setCrossover(DifferentialEvolutionCrossover crossover) {
        this.crossoverOperator = crossover;
        
        return this;
    }
    
    public MemeticEDBuilder setSelection(DifferentialEvolutionSelection selection) {
        this.selectionOperator = selection;
        
        return this;
    }
    
    public MemeticEDBuilder setSolutionListEvaluator(SolutionListEvaluator<DoubleSolution> evaluator) {
        this.evaluator = evaluator;
        
        return this;
    }
    
    public MemeticEDBuilder setLocalSearch(LocalSearchOperator ls) {
        this.localSearch = ls;
        return this;
    }
    
    public MemeticED build() {
        MemeticED m = new MemeticED(problem, maxEvaluations, populationSize, crossoverOperator,
                selectionOperator, evaluator, localSearch);
        m.setRandomGenerator(this.rand);
        return m;
    }

    /* Getters */
    public DoubleProblem getProblem() {
        return problem;
    }
    
    public int getPopulationSize() {
        return populationSize;
    }
    
    public int getMaxEvaluations() {
        return maxEvaluations;
    }
    
    public DifferentialEvolutionCrossover getCrossoverOperator() {
        return crossoverOperator;
    }
    
    public DifferentialEvolutionSelection getSelectionOperator() {
        return selectionOperator;
    }
    
    public SolutionListEvaluator<DoubleSolution> getSolutionListEvaluator() {
        return evaluator;
    }
    
    public MemeticEDBuilder setRandom(JMetalRandom rnd) {
        this.rand = rnd;
        return this;
    }
    
}
