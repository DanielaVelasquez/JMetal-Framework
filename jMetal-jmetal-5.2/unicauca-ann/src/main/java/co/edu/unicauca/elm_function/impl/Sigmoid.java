package co.edu.unicauca.elm_function.impl;

import co.edu.unicauca.elm_function.Function;


public class Sigmoid implements Function
{
    @Override
    public double evaluate(double x) 
    {
        double exp = Math.exp(-x) + 1;
        return 1 / exp;        
    }
}