package org.uma.jmetal.operator;

import org.uma.jmetal.solution.Solution;


public abstract class Tweak <S extends Solution<?>>
{
    public abstract S tweak(S solution);
}