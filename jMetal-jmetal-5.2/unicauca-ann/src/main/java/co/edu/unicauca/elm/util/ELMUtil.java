package co.edu.unicauca.elm.util;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm.ELM;
import co.edu.unicauca.elm.ELM.ELMType;


public class ELMUtil 
{
    /**
     * Obtains elm type given a data set
     * @param data_set collection of data
     * @return ELM type according to data set provided
     */
    public static ELMType getELMType(DataSet data_set)
    {
        return data_set.getNumber_classes() == 1?ELM.ELMType.REGRESSION:ELM.ELMType.CLASSIFICATION;
    }
}
