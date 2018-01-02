/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.database;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniela
 */
public class TestConnection {

    public static void main(String[] args) {
        try
        {
            DataBaseConnection connection = DataBaseConnection.getInstancia();
            System.out.println("Connection is OK.");
        } catch (Exception ex) 
        {
            System.out.println("Connection failed.");
            System.out.println(ex.getMessage());
        }
    }
    
}
