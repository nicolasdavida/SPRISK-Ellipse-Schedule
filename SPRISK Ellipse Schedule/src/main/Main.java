package main;


import source.Respaldo;
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
        // Conectar todo
        Respaldo.respaldarRepuestos();
        
        // Conexion con MSSQL
        //connectionMS.MSConn();
        
        // Conexion con Oracle remoto
        //connectionOra.OraConn();
    }
}
