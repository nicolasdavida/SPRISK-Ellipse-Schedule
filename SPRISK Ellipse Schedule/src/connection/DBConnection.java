/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;
import static connection.connectionAll.format;
import java.sql.*;

/**
 *
 * @author Armando
 */
public class DBConnection {
    
    public static String tiempo() {
        return format.format(new java.util.Date(System.currentTimeMillis()));
    }
    
    public static Connection conOracle()
    {
        try
        {
            System.out.println("INICIO conexion a Oracle:                       " + tiempo());
            
            Class.forName("oracle.jdbc.OracleDriver");
            Connection conOra = DriverManager.getConnection("jdbc:oracle:thin:@10.100.57.148:1522:ellrep","ellrep8","ellrep8");
            
            System.out.println("Conexion Oracle exitosa");
            
            return conOra;
        }
        catch(Exception ex)
        {
            System.out.println("Error mensaje: " + ex.getMessage() + "\n" + "Error string: " + ex.toString());
        }
        return null;
    }
    
    public static Connection conMSSQL()
    {
        try
        {
            System.out.println("INICIO conexion a MSSQL:                        " + tiempo());
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conMS = DriverManager.getConnection("jdbc:sqlserver://CGS-DELL\\SQLEXPRESS:1433;databaseName=cgssa_sandbox","cgssa","123");
            
            System.out.println("Conexion a MSSQL exitosa");
            
            return conMS;
        }
        catch(Exception ex)
        {
            System.out.println("Error mensaje: " + ex.getMessage() + "\n" + "Error string: " + ex.toString());
        }
        return null;
    }
    
}
