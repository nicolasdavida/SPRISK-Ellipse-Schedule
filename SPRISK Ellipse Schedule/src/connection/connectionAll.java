/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import object.Data;
//import object.DataMS;

/**
 *
 * @author Armando
 */
public class connectionAll {
    
    static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
    
    public static Connection connectAll(){
            
        
        try{
            
            
            System.out.println("////////////////INICIO DEL PROCESO: " + tiempo() + "/////////////////////");
            System.out.println("INICIO conexion a Oracle:                       " + tiempo());
            
            Class.forName("oracle.jdbc.OracleDriver");
            Connection conOra = DriverManager.getConnection("jdbc:oracle:thin:@10.100.57.148:1522:ellrep","ellrep8","ellrep8");
            System.out.println("Conexion exitosa");
            
            System.out.println("INICIO query de Oracle:                         " + tiempo());
            
            Statement st = conOra.createStatement();
            
            // OJO TIENE LIMITE ROWNUM <=100
            // QUIERE DECIR QUE SOLO RETORNARA LAS PRIMERAS 100 FILAS, QUITARLO MAS ADELANTE
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
            "WHERE A.CLASS in ('2','A','E','R') and B.STOCK_TYPE in ('6','7','8') and A.STOCK_STATUS in ('X','A') and ROWNUM <= 100"+
            "and B.INVT_STAT_CODE in ('2111','3121','3131','3141','3151','4141','5201','5202','5203','5221','5222','5223','5231','5321','5401','5501','6200','6201','6203','6204','6205','6221') " +
            "GROUP BY B.STOCK_CODE, A.STOCK_STATUS, A.STOCK_SECTIONX1, A.ITEM_NAME, A.DESC_LINEX1, A.DESC_LINEX2, A.DESC_LINEX3, A.DESC_LINEX4, "+
            "B.UNIT_OF_ISSUE, B.INVENT_COST_PR, B.CLASS, B.STOCK_TYPE, B.ROQ, B.DUES_IN, B.IN_TRANSIT, B.CONSIGN_ITRANS, B.TOTAL_PICKED, "+
            "B.DUES_OUT, B.RESERVED";
            
            //String query2 = "SELECT * FROM ELLREP.MSF000_DC0003";
            
            //String query3 = "SELECT * FROM ELLREP.MRF8MW";
            
            int contador = 0;
            
            //st.setFetchSize(100);
            ResultSet rs = st.executeQuery(query);
            //ResultSetMetaData rsmd = rs.getMetaData();
            //int columnCount = rsmd.getColumnCount();
            System.out.println("FIN de query a Oracle:                          " + tiempo());
            //System.out.println(columnCount);
            
            System.out.println("INICIO query a List:                            " + tiempo());
            List<Data> dataList = new ArrayList<>();
            
            while(rs.next()){
                //contador++;
                // Creando el objeto y dandole los valores
                Data data = new Data();
                data.setStock_code(rs.getString(1));
                data.setStock_status(rs.getString(2));
                data.setCriticidad(rs.getString(3));
                data.setItem_name(rs.getString(4));
                data.setInvent_cost_pr(rs.getString(5));
                data.set_Class(rs.getString(6));
                data.setStock_type(rs.getString(7));
                data.setUnit_of_issue(rs.getString(8));
                data.setDs(rs.getString(9));
                data.setDs_large(rs.getString(10));
                data.setDues_in(rs.getString(11));
                data.setIn_transit(rs.getString(12));
                data.setConsign_itrans(rs.getString(13));
                data.setTotal_picked(rs.getString(14));
                data.setDues_out(rs.getString(15));
                data.setReserved(rs.getString(16));
                
                // Imprimiendo los valores
                /*
                System.out.println("N° objeto:          " + contador);
                System.out.println("Stock code:         " + data.getStock_code());
                System.out.println("Stock status:       " + data.getStock_status());
                System.out.println("Criticidad:         " + data.getCriticidad());
                System.out.println("Item Name:          " + data.getItem_name());
                System.out.println("Invent_cost_pr:     " + data.getInvent_cost_pr());
                System.out.println("Clase:              " + data.get_Class());
                System.out.println("Stock type:         " + data.getStock_type());
                System.out.println("Unit of Issue:      " + data.getUnit_of_issue());
                System.out.println("Descripcion:        " + data.getDs());
                System.out.println("Descripcion larga:  " + data.getDs_large());
                System.out.println("Dues in:            " + data.getDues_in());
                System.out.println("In transit:         " + data.getIn_transit());
                System.out.println("Consign:            " + data.getConsign_itrans());
                System.out.println("Total picked:       " + data.getTotal_picked());
                System.out.println("Dues out:           " + data.getDues_out());
                System.out.println("Reservado:          " + data.getReserved());
                System.out.println("-----------------------------------------");
                */
                dataList.add(data);
                System.out.println("Registro N°: " + contador++ + ", Hora : " + tiempo());
            }
            
            //System.out.println("FUNCIONA :D");
            //System.out.println("////////////////////////DEBUG///////////////////////");
            
            //List<DataMS> DataMSList = new ArrayList<>();
            //for(Data datahere : DataList)
            //{
                //DataMS dataMs = new DataMS();
                //dataMs.setStock_code(datahere.getStock_code());
                //dataMs.setStock_status(datahere.getStock_status());
                //dataMs.setCriticidad(datahere.getCriticidad());
                
                //DataMSList.add(dataMs);
                
                /*
                System.out.println("Datos del data MS");
                System.out.println("Stock code:     " + dataMs.getStock_code());
                System.out.println("Stock status:   " + dataMs.getStock_status());
                System.out.println("Criticidad:     " + dataMs.getCriticidad());
                System.out.println("---------------------------------------------");
                */
            //}
            
            System.out.println("INICIO conexion a MSSQL:                        " + tiempo());
            // Conexion a la Base de Datos MSSQL
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection("jdbc:sqlserver://CGS-DELL\\SQLEXPRESS:1433;databaseName=cgssa_sandbox","cgssa","123");
            System.out.println("Conexion a MSSQL exitosa");
            
            
            System.out.println("INICIO insert batch a MSSQL:                    " + tiempo());
            
            // Prepara la sentencia Batch, más adelante cambiar por UPDATE en vez de INSERT
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO dbo.TEST(stock_code, stock_status, criticidad, item_name, invent_cost_pr"
                        + ", class, stock_type, unit_of_issue, ds, ds_large, dues_in, in_transit"
                        + ", consign_itrans, total_picked, dues_out, reserved) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"       
            );
            
            // Por cada objeto en la lista DataMS, pasa los parametros correspondientes a la prepared statement
            for(Data data : dataList)
            {
                ps.setString(1, data.getStock_code());
                ps.setString(2, data.getStock_status());
                ps.setString(3, data.getCriticidad());
                ps.setString(4, data.getItem_name());
                ps.setString(5, data.getInvent_cost_pr());
                ps.setString(6, data.get_Class());
                ps.setString(7, data.getStock_type());
                ps.setString(8, data.getUnit_of_issue());
                ps.setString(9, data.getDs());
                ps.setString(10, data.getDs_large());
                ps.setString(11, data.getDues_in());
                ps.setString(12, data.getIn_transit());
                ps.setString(13, data.getConsign_itrans());
                ps.setString(14, data.getTotal_picked());
                ps.setString(15, data.getDues_out());
                ps.setString(16, data.getReserved());
                
                ps.addBatch();
            }
            
            // Ejecuta el Batch
            ps.executeBatch();
            
            // Guarda los cambios en la BD
            con.commit();   
            
            con.close();
            
            conOra.close();
            
            System.out.println("FIN insert batch a MSSQL:                       " + tiempo());
            System.out.println("////////////////FIN DEL PROCESO: " + tiempo() + "/////////////////////");
            
            //Fin
            return conOra;
            
        }catch(Exception ex)
        {
            System.out.println("Error, " + ex.toString());
        }
        return null;
    }

    private static String tiempo() {
        return format.format(new Date(System.currentTimeMillis()));
    }
}
