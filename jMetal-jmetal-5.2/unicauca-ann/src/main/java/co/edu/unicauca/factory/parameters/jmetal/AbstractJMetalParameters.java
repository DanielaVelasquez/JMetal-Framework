package co.edu.unicauca.factory.parameters.jmetal;

import co.edu.unicauca.factory.parameters.JMetalParametersFactory;

public abstract class AbstractJMetalParameters 
{
    protected JMetalParametersFactory factory;
    
    public abstract void load(JMetalParametersFactory factory);
}
