package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.util.MOSTecniqueExec;
import org.uma.jmetal.solution.DoubleSolution;


public class MOSHRH extends AbstractHRHMOSAlgorithm<DoubleSolution>
{
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    @Override
    public String getName() {
        return "MOS HRH";
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List initializeSteps() {
        List<Double> FE = new ArrayList<>();
        for(int i = 0; i < this.n; i++)
            FE.add((double)0);
        return FE;
    }

    @Override
    protected List updateSteps() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<DoubleSolution> createInitialPopulation() {
        //TO_DO si se hace así podría hacerse que la participación dependa
        //de la cantidad que cada uno genere
        List<DoubleSolution> population = new ArrayList<>();
        
        int size = this.populationSize / this.n;
        int missing = this.populationSize % this.n;
        boolean done = false;
        
        int i = 0;
        for(MOSTecniqueExec tecnique: tecniques)
        {
            List<DoubleSolution> p = tecnique.evolve(size, null, this.problem, this.comparator);
            //AQUí YA SE CUANTOS HIZO CADA TECNICA, ALGUNA HACEN UNA MAS QUE OTRA
            //DE AQUI SE PUEDE SACAR EL PARTICIPATION RATIO
            population.addAll(p);
            i++;
            if(!done && this.n - i == missing)
            {
                size++;
                done = true;
            }
        }
        return population;
    }

    @Override
    protected List distributeParticipationTecniques() {
        List<Double> participation_ratio = new ArrayList<>();
        for(int i = 0; i < this.n;i++)
        {
            participation_ratio.add((double)1/(double)n);
        }
        return participation_ratio;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean isTecniqueStoppingConditionReached(int j) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void evaluatePopulation(List<DoubleSolution> population) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List updateQualityOf(List tecniques) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List updateParticipationRatios(List quality_values) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List evolve(Algorithm tecnique, int FE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List combine(List<DoubleSolution> population, List<DoubleSolution> offspring_population) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DoubleSolution getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
