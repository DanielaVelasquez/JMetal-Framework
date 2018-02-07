package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import java.util.Comparator;
import org.uma.jmetal.algorithm.singleobjective.LocalOptimizer.OptSimulatedAnnealing;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Daniel Pusil
 */
public class MemeticEDBuilder implements AlgorithmBuilder
{
    
    private DoubleProblem problem;
    private int populationSize;
    private int maxEvaluations;
    private DifferentialEvolutionCrossover crossoverOperator;
    private DifferentialEvolutionSelection selectionOperator;
    private Comparator<DoubleSolution> comparator;
    private MutationOperator<DoubleSolution> mutationOperator;
    private JMetalRandom rand;

    //Añadir optimizador local
    private LocalSearchOperator localSearch;
    
    public MemeticEDBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.populationSize = 50;
        this.maxEvaluations = 2500;
        this.crossoverOperator = new DifferentialEvolutionCrossover(0.4, 0.5, "current-to-best/1/bin");//Default
        this.selectionOperator = new DifferentialEvolutionSelection();
        this.comparator = new ObjectiveComparator<DoubleSolution>(0,ObjectiveComparator.Ordering.ASCENDING);
        this.localSearch = new OptSimulatedAnnealing(10, null, comparator, problem);
        this.rand = JMetalRandom.getInstance();
        //this.mutationOperator = new UniformMutation(populationSize, populationSize)
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public MemeticEDBuilder setComparator(Comparator<DoubleSolution> comparator) {
        this.comparator = comparator;
        return this;
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
    
    
    
    public MemeticEDBuilder setLocalSearch(LocalSearchOperator ls) {
        this.localSearch = ls;
        return this;
    }
    
    public MemeticED build() {
        MemeticED m = new MemeticED(problem, maxEvaluations, populationSize, crossoverOperator,
                selectionOperator, localSearch,comparator);
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
    
 
    
    public MemeticEDBuilder setRandom(JMetalRandom rnd) {
        this.rand = rnd;
        return this;
    }
    
}
