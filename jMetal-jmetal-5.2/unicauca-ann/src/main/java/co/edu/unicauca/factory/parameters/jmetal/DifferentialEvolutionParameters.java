package co.edu.unicauca.factory.parameters.jmetal;

import co.edu.unicauca.factory.parameters.JMetalParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;

public class DifferentialEvolutionParameters extends AbstractJMetalParameters
{
     @Override
    public void load(JMetalParametersFactory factory)
    {
        this.factory = factory;
        this.loadDE();
    }
    
    private void loadDE()
    {
       //CV
       this.factory.putParameter("CR", AbstractELMEvaluator.EvaluatorType.CV, "DEUnicauca", 0);
       this.factory.putParameter("F", AbstractELMEvaluator.EvaluatorType.CV, "DEUnicauca", 0);
       //TT
       this.factory.putParameter("CR", AbstractELMEvaluator.EvaluatorType.TT, "DEUnicauca", 0);
       this.factory.putParameter("F", AbstractELMEvaluator.EvaluatorType.TT, "DEUnicauca", 0);
       
    }
}
