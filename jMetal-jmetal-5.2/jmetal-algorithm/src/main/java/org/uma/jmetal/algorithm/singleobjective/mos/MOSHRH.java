package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.algorithm.tecnique.Tecnique;
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
    
    
    public MOSHRH(List<Tecnique> tecniques, Problem<DoubleSolution> problem, int maxEvaluations, int FE, Comparator<DoubleSolution> comparator, double E, double penalize_value) {
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
    protected DoubleSolution executeTecniques() {
        int i = 0;
        int total_evaluations = 0;
        for(Tecnique tecnique: tecniques)
        {
            int evaluations_j = (int) (FE * (double)this.participation_ratio.get(i));
            
            if(i == this.n - 1)
                evaluations_j = FE - total_evaluations; 
            
            //If the evalutions to perform is bigger than the number of maximun evalutions 
            if(this.evaluations + evaluations_j > maxEvaluations)
                evaluations_j = this.maxEvaluations - this.evaluations;
            
            //If there are not more availables evaluations then this can finish
            if(evaluations_j == 0)
                break;
            individual = (DoubleSolution) tecnique.evolve(evaluations_j, individual, this.problem, this.comparator);
            this.updateProgress(evaluations_j);
            total_evaluations +=evaluations_j;
            i++;
        }
        return individual;
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
        return evaluations>=maxEvaluations;
    }


    @Override
    protected List updateQualityOf(List<Tecnique> tecniques) 
    {
        List<Double> quality = new ArrayList<>();
        for(Tecnique tecnique: tecniques)
        {
            quality.add(tecnique.calculateAverageFitnessOffspringPopulationSize());
        }
        return quality;
    }

    @Override
    protected List updateParticipationRatios() 
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
            if(this.quality_max!= 0)
                return this.E * (Math.abs(this.quality_max - quality)/(this.quality_max)) * participation;
            else
                return this.E * (Math.abs(this.quality_max - quality)) * participation;
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
   
}
