package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.util.MOSTecniqueExec;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;


public class MOSHRH extends AbstractHRHMOSAlgorithm<DoubleSolution>
{

    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    
    public MOSHRH(List<MOSTecniqueExec> tecniques, Problem<DoubleSolution> problem, int maxEvaluations, double FE, Comparator<DoubleSolution> comparator, double E, double penalize_value) {
        super(tecniques, problem, maxEvaluations, FE, comparator, E, penalize_value);
    }
    @Override
    public String getName() {
        return "MOS HRH";
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected DoubleSolution createInitialPopulation() {
        List<DoubleSolution> population = new ArrayList<>();
        int i = 0;
        DoubleSolution pop = null;
        for(MOSTecniqueExec tecnique: tecniques)
        {
            int evaluations_j = (int) (FE * (double)this.participation_ratio.get(i));
            pop = (DoubleSolution) tecnique.evolve(evaluations_j, pop, this.problem, this.comparator);
            this.updateProgress(evaluations);
            i++;
        }
        return pop;
    }

    @Override
    protected List distributeParticipationTecniques() 
    {
        List<Double> pr = new ArrayList<>();
        for(int j = 0; j < this.n;j++)
        {
            pr.add((double)1/(double)n);
        }
        return pr;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return evaluations>maxEvaluations;
    }


    @Override
    protected List updateQualityOf(List<MOSTecniqueExec> tecniques) 
    {
        List<Double> quality = new ArrayList<>();
        for(MOSTecniqueExec tecnique: tecniques)
        {
            quality.add(tecnique.calculateAverageFitnessOffspringPopulationSize());
        }
        return quality;
    }

    @Override
    protected List updateParticipationRatios(List quality_values) 
    {
        List<Double> pr = new ArrayList<>();
        this.findBestQualityTecniques();
        double etha = this.calculateEtha();
        for(int k = 0; k < n; k++)
        {
            double actual_pr = (double) this.participation_ratio.get(k);
            if(this.best_tecniques_qualities.contains(k))
            {
                pr.add(actual_pr + etha);
            }
            else
            {
                pr.add(actual_pr - this.calculateDeltha(k));
            }
        }
        return pr;
    }

    @Override
    public DoubleSolution getResult() 
    {
        return this.individual;
    }
    /**
     * Represents the decrease in participation for a tecnique
     * @param tecnique tecnique 
     * @return the decrease in participation for a tecnique or null if the tecnique was selected as one of the best because of its quality
     */
    private Double calculateDeltha(int tecnique)
    {
        if(!this.best_tecniques_qualities.contains(tecnique))
        {
            double quality = (double) this.quality_measures.get(tecnique);
            double participation = (double) this.participation_ratio.get(tecnique);
            return this.E * ((this.quality_max - quality)/(this.quality_max)) * participation;
        }
        return null;
    }
    private double calculateEtha()
    {
        double sum = 0;
        int size = this.best_tecniques_qualities.size();
        for(int k = 0; k < n; k++)
        {
            if(!this.best_tecniques_qualities.contains(k))
            {
                sum += this.calculateDeltha(k);
            }
        }
        return sum/size;
    }

    @Override
    protected List combine(List<DoubleSolution> population, List<DoubleSolution> offspring_population) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
