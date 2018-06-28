package main;


import connection.connectionOra;
import connection.connectionMS;
import java.sql.Connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Armando
 */
public class Main {
    
    public static void main(String[] args)
    {
        // Conexion con MSSQL
        connectionMS.MSConn();
        
        // Conexion con Oracle remoto
        connectionOra.OraConn();
    }
}
