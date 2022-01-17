/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hibernatefxml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diegu
 */
public class Conexion {
    public static Connection con;

    static {
        String url = "jdbc:mysql://localhost:3308/comandas?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";

        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión realizada con éxito.");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Connection getCon() {
        return con;
    }
    
    

   

}
