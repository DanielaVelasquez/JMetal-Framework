/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.algorithm.singleobjective.mos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.algorithm.singleobjective.mts.MultipleTrajectorySearchBuilder;
import org.uma.jmetal.problem.DoubleProblem;
/**
 *
 * @author danielavelasquezgarzon
 */
public class AbstractHRHMOSAlgorithmTest {
    
    private AbstractHRHMOSAlgorithm instance ;
    
    public AbstractHRHMOSAlgorithmTest() {
        String problemName = "co.edu.unicauca.problem.training_testing.Iris";
        DoubleProblem problem = null;
        HashMap<String, Object> mts_atributes = new MultipleTrajectorySearchBuilder(problem)
                                                .getConfiguration();
        MTSTecnique mts_exec = new MTSTecnique(mts_atributes);
        SolisAndWetsTecnique sw_exec = new SolisAndWetsTecnique(null);
        instance  =  new   MOSBuilder(problem)
                            .addTecnique(mts_exec)
                            .addTecnique(sw_exec)
                            .setFE(300)
                            .setMaxEvaluations(3000)
                            .build();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }



    /**
     * Test of updateProgress method, of class AbstractHRHMOSAlgorithm.
     */
    @Test
    public void testUpdateProgress()
    {
        System.out.println("updateProgress");
        instance.evaluations = 0;
        instance.updateProgress(300);
        assertEquals(300, instance.evaluations);
    }

    /**
     * Test of findBestQualityTecniques method, of class AbstractHRHMOSAlgorithm.
     */
    @Test
    public void testFindBestQualityTecniques() {
        System.out.println("findBestQualityTecniques");
        List<Double> quality_measures = new ArrayList<>();
        quality_measures.add(0.38);
        quality_measures.add(0.37);
        quality_measures.add(0.38);
        instance.n = quality_measures.size();
        instance.quality_measures = quality_measures;
        instance.findBestQualityTecniques();
        /**
         * Quality measures are {0.38, 0.37,0.38}
         * Best quality measures indexes: {0,2}
         */
        assertEquals(2, instance.best_tecniques_qualities.size());
        assertEquals(0, instance.best_tecniques_qualities.get(0));
        assertEquals(2, instance.best_tecniques_qualities.get(1));
        
        /**
         * Quality measures are {0.38, 0.37,0.38, 0.39}
         * Best quality measures indexes: {3}
         */
        
        quality_measures.add(0.39);
        instance.n = quality_measures.size();
        instance.quality_measures = quality_measures;
        instance.findBestQualityTecniques();
        assertEquals(1, instance.best_tecniques_qualities.size());
        assertEquals(3, instance.best_tecniques_qualities.get(0));
        
        
    }
    /**
     * Test of updateParticipationRatios method, of class AbstractHRHMOSAlgorithm.
     */
    @Test
    public void testUpdateParticipationRatios() {
        System.out.println("updateParticipationRatios");
        
        //---------------------------------------------
        //Más de un elemento en omega
        List<Double> quality_measures = new ArrayList<>();
        quality_measures.add(0.38);
        quality_measures.add(0.37);
        quality_measures.add(0.38);
        instance.n = quality_measures.size();
        instance.quality_measures = quality_measures;
        
        List<Double> participation_ratio = new ArrayList<>();
        participation_ratio.add((double)0.5);
        participation_ratio.add((double)0.25);
        participation_ratio.add((double)0.25);
        
        instance.participation_ratio = participation_ratio;
        
        double[] expResult = new double[3];
        expResult[0] = (0.500328947);
        expResult[1] = (0.249671053);
        expResult[2] = (0.25);
        
        double[] res = new double[3];
        
        List result = instance.updateParticipationRatios();
        int i = 0;
        for(Object o: result)
        {
            res[i] = Double.parseDouble(o.toString());
            i++;
        }
        assertArrayEquals(expResult, res, 0.1);
        
        //---------------------------------------------
        //Un elemento en omega
        quality_measures.add(0.39);
        instance.n = quality_measures.size();
        instance.quality_measures = quality_measures;
        
        participation_ratio = new ArrayList<>();
        participation_ratio.add((double)0.15);
        participation_ratio.add((double)0.35);
        participation_ratio.add((double)0.25);
        participation_ratio.add((double)0.25);
        
        instance.participation_ratio = participation_ratio;
        
        expResult = new double[4];
        expResult[0] = (0.149807692);
        expResult[1] = (0.349102564);
        expResult[2] = (0.249679487);
        expResult[3] = (0.251410256);
        
        res = new double[4];
        
        result = instance.updateParticipationRatios();
        i = 0;
        for(Object o: result)
        {
            res[i] = Double.parseDouble(o.toString());
            i++;
        }
        assertArrayEquals(expResult, res, 0.1);
        
        //---------------------------------------------
        //Todos en omega
        quality_measures = new ArrayList<>();
        quality_measures.add(0.38);
        quality_measures.add(0.38);
        instance.n = quality_measures.size();
        instance.quality_measures = quality_measures;
        
        participation_ratio = new ArrayList<>();
        participation_ratio.add((double)0.65);
        participation_ratio.add((double)0.35);
        
        instance.participation_ratio = participation_ratio;
        
        expResult = new double[2];
        expResult[0] = (0.65);
        expResult[1] = (0.35);
        
        res = new double[2];
        
        result = instance.updateParticipationRatios();
        i = 0;
        for(Object o: result)
        {
            res[i] = Double.parseDouble(o.toString());
            System.out.println(""+res[i]);
            i++;
        }
        assertArrayEquals(expResult, res, 0);
    }
   
}
