package org.uma.jmetal.algorithm.singleobjective.ihdels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.SaDEBuilder;
import org.uma.jmetal.algorithm.local_search.LocalSearch;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
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
    private SaDEBuilder SaDEbuilder;
    private double threshold;
    private int population_size;
    private double a;
    private double b;

    public IHDELSBuilder(DoubleProblem problem) {
        this.problem = problem;
        this.maxEvaluations = 3000;
        this.local_searches = new ArrayList<>();
        this.comparator = new FrobeniusComparator<>(FrobeniusComparator.Ordering.ASCENDING, FrobeniusComparator.Ordering.ASCENDING, 0);
        this.penalize_value = 1;
        this.reStart = 3;
        this.FE_DE = 100;
        this.FE_LS = 100;
        this.SaDEbuilder = new SaDEBuilder(problem);
        this.threshold = 0.01;
        this.population_size = 10;
    }
    
    @Override
    public IHDELS build() {
        if(local_searches.size() < 2)
            throw new JMetalException("You need at least two local searches: "+local_searches.size());
        return new IHDELS(maxEvaluations, local_searches, problem, comparator,
                penalize_value, reStart, population_size, FE_DE, FE_LS, SaDEbuilder,
                threshold, a, b);
    }

    public IHDELSBuilder setMaxEvaluations(int maxEvaluations) {
        if(maxEvaluations <1)
            throw new JMetalException("MaxEvaluations is negative: "+maxEvaluations);
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public IHDELSBuilder setLocal_searches(List<LocalSearch> local_searches) {
        if(local_searches == null)
            throw new JMetalException("Local searches are null");
        if(local_searches.isEmpty())
            throw new JMetalException("Local searches are empty");
        this.local_searches = local_searches;
        return this;
    }

    public IHDELSBuilder setProblem(DoubleProblem problem) {
        if(problem == null)
            throw new JMetalException("Problem is null");
        this.problem = problem;
        return this;
    }

    public IHDELSBuilder setComparator(Comparator<DoubleSolution> comparator) {
        if(comparator == null)
            throw new JMetalException("Comparator is null");
        this.comparator = comparator;
        return this;
    }

    public IHDELSBuilder setPenalize_value(double penalize_value) {
        this.penalize_value = penalize_value;
        return this;
    }

    public IHDELSBuilder setReStart(int reStart) {
        if(reStart < 0)
            throw new JMetalException("Re-start is negative : "+reStart);
        this.reStart = reStart;
        return this;
    }

    public IHDELSBuilder setFE_DE(int FE_DE) {
        if(FE_DE <= 0)
            throw new JMetalException("FE_DE is negative or zero:  "+FE_DE);
        this.FE_DE = FE_DE;
        return this;
    }

    public IHDELSBuilder setFE_LS(int FE_LS) {
        if(FE_LS <= 0)
            throw new JMetalException("FE_LS is negative: "+FE_LS);
        this.FE_LS = FE_LS;
        return this;
    }

    public IHDELSBuilder setSaDEBuilder(SaDEBuilder builder) {
        if(builder == null)
            throw new JMetalException("SaNSDE Builder is null");
        this.SaDEbuilder = builder;
        return this;
    }

    public IHDELSBuilder setThreshold(double threshold) {
        if(threshold > 1)
            throw new JMetalException("Threshold is greater than one: "+threshold);
        
        if(threshold < 0)
            throw new JMetalException("Threshold is negative: "+threshold);
        this.threshold = threshold;
        return this;
    }

    public IHDELSBuilder setPopulation_size(int population_size) {
        if(population_size < 0)
            throw new JMetalException("Population size is negative: "+population_size);
        this.population_size = population_size;
        return this;
    }

    public IHDELSBuilder setSearchDomain(double a, double b) {
        if(a > b)
            throw new JMetalException("Search domain first value: "+a+" is greater that last value: "+b);
        
        if(a == b)
            throw new JMetalException("There is no space on the search domain ["+a+", "+b+"]");
        this.a = a;
        this.b = b;
        return this;
    }
    
    public IHDELSBuilder addLocalSearch(LocalSearch ls)
    {
        if(ls == null)
            throw new JMetalException("Local search is null");
        this.local_searches.add(ls);
        return this;
    }
    
    public IHDELSBuilder removeLocalSearch(LocalSearch ls)
    {
        if(ls == null)
            throw new JMetalException("Local search is null");
        if(local_searches.contains(ls))
            throw new JMetalException("Local search is not on the list");
        this.local_searches.remove(ls);
        return this;
    }
    
    public IHDELSBuilder removeLocalSearches()
    {
        this.local_searches.clear();
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public List<LocalSearch> getLocal_searches() {
        return local_searches;
    }

    public DoubleProblem getProblem() {
        return problem;
    }

    public Comparator<DoubleSolution> getComparator() {
        return comparator;
    }

    public double getPenalize_value() {
        return penalize_value;
    }

    public int getReStart() {
        return reStart;
    }

    public int getFE_DE() {
        return FE_DE;
    }

    public int getFE_LS() {
        return FE_LS;
    }

    public SaDEBuilder getSaDEBuilder() {
        return SaDEbuilder;
    }

    public double getThreshold() {
        return threshold;
    }

    public int getPopulation_size() {
        return population_size;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

}
