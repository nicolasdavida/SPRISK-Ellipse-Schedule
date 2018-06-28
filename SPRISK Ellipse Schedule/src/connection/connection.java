package connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import object.Data;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Armando
 */
public class connection {
    
    public static Connection JCConn(){
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@10.100.57.148:1522:ellrep","ellrep8","ellrep8");
            System.out.println("Conexion exitosa");
            
            Statement st = con.createStatement();
            
            String query = 
                    
            "SELECT  B.STOCK_CODE, A.STOCK_STATUS, A.STOCK_SECTIONX1 AS CRITICIDAD, "+
            "A.ITEM_NAME, B.INVENT_COST_PR,  B.CLASS, B.STOCK_TYPE, B.UNIT_OF_ISSUE, "+
            "(A.DESC_LINEX1  || ' ' || A.DESC_LINEX2  || ' ' ||  A.DESC_LINEX3  || ' ' || A.DESC_LINEX4) as DS, "+       
            "(SELECT b1.CAT_EXT_DATA "+
            "FROM ellrep.MSF100 a1 INNER JOIN ellrep.MSF1CC b1 ON a1.CAT_EXT_UUID = b1.CAT_EXT_UUID "+
            "and a1.STOCK_CODE=B.STOCK_CODE "+
            ") as DS_LARGE, "+
            "B.DUES_IN, B.IN_TRANSIT, B.CONSIGN_ITRANS, B.TOTAL_PICKED, B.DUES_OUT, B.RESERVED "+
            "FROM ellrep.MSF100 A INNER JOIN ellrep.MSF170 B ON A.STOCK_CODE = B.STOCK_CODE "+
            "WHERE A.CLASS in ('2','A','E','R') and B.STOCK_TYPE in ('6','7','8') and A.STOCK_STATUS in ('X','A') "+
            "and B.INVT_STAT_CODE in ('2111','3121','3131','3141','3151','4141','5201','5202','5203','5221','5222','5223','5231','5321','5401','5501','6200','6201','6203','6204','6205','6221') " +
            "GROUP BY B.STOCK_CODE, A.STOCK_STATUS, A.STOCK_SECTIONX1, A.ITEM_NAME, A.DESC_LINEX1, A.DESC_LINEX2, A.DESC_LINEX3, A.DESC_LINEX4, "+
            "B.UNIT_OF_ISSUE, B.INVENT_COST_PR, B.CLASS, B.STOCK_TYPE, B.ROQ, B.DUES_IN, B.IN_TRANSIT, B.CONSIGN_ITRANS, B.TOTAL_PICKED, "+
            "B.DUES_OUT, B.RESERVED";
            
            String query2 = "SELECT * FROM ELLREP.MSF000_DC0003";
            
            String query3 = "SELECT * FROM ELLREP.MRF8MW";
            
            int contador = 0;
            
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            
            //System.out.println(columnCount);
            
            
            List<Data> DataList = new ArrayList<>();
            
            while(rs.next()){
                contador++;
                Data data = new Data();
                data.setStock_code(rs.getString(1));
                data.setStock_status(rs.getString(2));
                data.setCriticidad(rs.getString(3));
                System.out.println("N° objeto:    " + contador);
                System.out.println("Stock code:   " + data.getStock_code());
                System.out.println("Stock status: " + data.getStock_status());
                System.out.println("Criticidad:   " + data.getCriticidad());
                System.out.println("-----------------------------------------");
                DataList.add(data);
            }
            
            System.out.println("FUNCIONA :D");
            System.out.println("////////////////////////DEBUG///////////////////////");
            for(Data datahere : DataList)
            {
                System.out.println(datahere.getStock_code());
                System.out.println(datahere.getStock_status());
                System.out.println(datahere.getCriticidad());
            }
            
            con.close();
            return con;
            
        }catch(Exception ex)
        {
            System.out.println("Error " + ex.toString());
        }
        return null;
    }
}
