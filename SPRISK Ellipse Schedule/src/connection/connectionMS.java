/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.*;

/**
 *
 * @author Armando
 */
public class connectionMS {
    
    public static Connection MSConn(){
    
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection("jdbc:sqlserver://CGS-DELL\\SQLEXPRESS:1433;databaseName=cgssa_sandbox","cgssa","123");
            System.out.println("Conexion exitosa");
            
            String query = "SELECT * FROM dbo.chains";
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                System.out.println("Primer valor:          " + rs.getString(1));
            }
            con.close();
            return con;
        }
        catch(Exception ex){
            System.out.println("Error. " + ex.toString());
        }
        return null;
    }
     
}
