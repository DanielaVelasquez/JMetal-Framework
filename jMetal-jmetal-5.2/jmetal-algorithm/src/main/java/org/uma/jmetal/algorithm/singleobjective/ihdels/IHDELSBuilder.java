package org.uma.jmetal.algorithm.singleobjective.ihdels;

import com.sun.jmx.remote.internal.ArrayQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.util.LocalSearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.comparator.FrobeniusComparator;


public class IHDELSBuilder implements AlgorithmBuilder<IHDELS>
{
    private int maxEvaluations;
    private List<LocalSearch> local_searches;
    private DoubleProblem problem ;
    private Comparator<DoubleSolution> comparator;
    private double penalize_value;
    private int reStart;
    private int FE_DE;
    private int FE_LS;
    private SaDEBuilder builder;
    private double threshold;
    private int population_size;

    public IHDELSBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.maxEvaluations = 3000;
        this.local_searches = new ArrayList<>();
        this.comparator = new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
        this.penalize_value = 1;
        this.reStart = 3;
        this.FE_DE = 100;
        this.FE_LS = 100;
        this.builder = new SaDEBuilder(problem);
        this.threshold = 0.01;
        this.population_size = 10;
    }
    
    @Override
    public IHDELS build() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
