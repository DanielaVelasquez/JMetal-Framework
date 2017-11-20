package org.uma.jmetal.util.comparator;

import java.io.Serializable;
import java.util.Comparator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

public class FrobeniusComparator <S extends Solution<?>> implements Comparator<S>, Serializable
{
    public enum Ordering {ASCENDING, DESCENDING} ;
    
    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    /**
     * Ordering for objective
     */
    private FrobeniusComparator.Ordering orderObjective;
    /**
     * Ordering for frobenius norm
     */
    private FrobeniusComparator.Ordering orderFrobenius;
    /**
     * Objective of the index to compare
     */
    private int objectiveIndex;
    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    

    public FrobeniusComparator(int objectiveIndex) 
    {
        this.objectiveIndex = objectiveIndex;
        this.orderObjective = FrobeniusComparator.Ordering.ASCENDING;
        this.orderObjective = FrobeniusComparator.Ordering.DESCENDING;
    }

    public FrobeniusComparator(FrobeniusComparator.Ordering orderObjective, FrobeniusComparator.Ordering orderFrobenius, int objectiveIndex) 
    {
        this.orderObjective = orderObjective;
        this.orderFrobenius = orderFrobenius;
        this.objectiveIndex = objectiveIndex;
    }
    
    @Override
    public int compare(S solution1, S solution2)
    {
        int result ;
        if (solution1 == null)
        {
          if (solution2 == null) 
          {
            result = 0;
          } 
          else
          {
            result =  1;
          }
        } 
        else if (solution2 == null)
        {
          result =  -1;
        } 
        else if (solution1.getNumberOfObjectives() <= objectiveIndex) 
        {
          throw new JMetalException("The solution1 has " + solution1.getNumberOfObjectives()+ " objectives "
              + "and the objective to sort is " + objectiveIndex) ;
        } 
        else if (solution2.getNumberOfObjectives() <= objectiveIndex) 
        {
          throw new JMetalException("The solution2 has " + solution2.getNumberOfObjectives()+ " objectives "
              + "and the objective to sort is " + objectiveIndex) ;
        }
        else 
        {
            Double objective1 = solution1.getObjective(this.objectiveIndex);
            Double objective2 = solution2.getObjective(this.objectiveIndex);
            if (orderObjective == FrobeniusComparator.Ordering.ASCENDING) {
              result = Double.compare(objective1, objective2);
            } else {
              result = Double.compare(objective2, objective1);
            }
            
            if(result == 0)
            {
                try
                {
                    Double frobenius1 = (Double)solution1.getAttribute("B");
                    Double frobenius2 = (Double)solution2.getAttribute("B");
                    
                    if(orderFrobenius == FrobeniusComparator.Ordering.ASCENDING)
                    {
                      result = Double.compare(frobenius1, frobenius2);
                    }
                    else
                    {
                      result = Double.compare(frobenius2, frobenius1);
                    } 
                }
                catch(Exception e)
                {
                    
                }                
            }
        }
        return result;
    }    
}
