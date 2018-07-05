/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import connection.DBConnection;
import static connection.DBConnection.tiempo;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import object.Data;
import object.DataUpdate;
import object.Leadtime;
//import object.DataMS;

/**
 *
 * @author Armando
 */
public class Respaldo {

    public static void respaldarRepuestos(){

        try{
            
            System.out.println("INICIO DEL PROCESO:                             " + tiempo());
            
            // DBConnection.conOracle();
            Connection conOra = DBConnection.conOracle();
            
            //  Tabla repuestos
            String query = 
                    
            "SELECT  B.STOCK_CODE, A.STOCK_STATUS, A.STOCK_SECTIONX1 AS CRITICIDAD, " +
            "A.ITEM_NAME, B.INVENT_COST_PR,  B.CLASS, B.STOCK_TYPE, B.UNIT_OF_ISSUE, " +
            "(A.DESC_LINEX1  || ' ' || A.DESC_LINEX2  || ' ' ||  A.DESC_LINEX3  || ' ' || A.DESC_LINEX4) as DS, " +
            "'' as DS_LARGE, " +
            "B.DUES_IN, B.IN_TRANSIT, B.CONSIGN_ITRANS, B.TOTAL_PICKED, B.DUES_OUT, B.RESERVED " +
            "FROM ellrep.MSF100 A " +
            "INNER JOIN ellrep.MSF170 B ON A.STOCK_CODE = B.STOCK_CODE " +
            "WHERE A.CLASS in ('2','A','E','R') and B.STOCK_TYPE in ('6','7','8') and A.STOCK_STATUS in ('X','A') " +
            "and B.INVT_STAT_CODE in ('2111','3121','3131','3141','3151','4141','5201','5202','5203','5221','5222','5223','5231','5321','5401','5501','6200','6201','6203','6204','6205','6221')";

            //  Stock actualizado
            String query2 = 

            "SELECT a.STOCK_CODE, Sum(a.SOH) AS STOCK_TOTAL " +
            "FROM ellrep.MSF1HD a INNER JOIN ellrep.MSF1CS b ON a.CUSTODIAN_ID = b.CUSTODIAN_ID " +
            "WHERE (((a.HOLDING_TYPE)='F') AND ((b.CUSTODIAN_TYPE)='W') AND ((a.STK_OWNERSHP_IND)='O' Or (a.STK_OWNERSHP_IND)='C')) " +
            "GROUP BY a.STOCK_CODE " +
            "ORDER BY a.STOCK_CODE";
            
            //  Calculo leadtime
            String query3 =
            
            "SELECT MSF170.STOCK_CODE, Avg(calculo_leadtime.leadtime) AS AvgOfleadtime, Min(calculo_leadtime.leadtime) AS MinOfleadtime, Max(calculo_leadtime.leadtime) AS MaxOfleadtime, STDDEV(calculo_leadtime.leadtime) AS StDevOfleadtime\n" +
            "FROM ellrep.MSF170 INNER JOIN (\n" +
            "    SELECT MSF221.PREQ_STK_CODE, to_date(trim(MSF220.CREATION_DATE),'YYYYMMDD') fe1, to_date(trim(MSF221.ONST_RCPT_DATE),'YYYYMMDD') fe2,\n" +
            "    to_date(trim(MSF221.ONST_RCPT_DATE),'YYYYMMDD')-to_date(trim(MSF220.CREATION_DATE),'YYYYMMDD') leadtime\n" +
            "    from ellrep.MSF221, ellrep.MSF220\n" +
            "    where MSF221.PO_NO = MSF220.PO_NO\n" +
            "    and trim(MSF221.ONST_RCPT_DATE) is not null) calculo_leadtime ON MSF170.STOCK_CODE=calculo_leadtime.PREQ_STK_CODE\n" +
            "GROUP BY MSF170.STOCK_CODE";
            // Fin queries
            
            // 1. QUERY TABLA REPUESTOS
            //DBConnection.conMSSQL();
            Connection conMS = DBConnection.conMSSQL();
            
            String queryDelete = 
            "DELETE FROM dbo.tsrmes_rpto_oracle";
            
            Statement stMS = conMS.createStatement();
            stMS.execute(queryDelete);
            stMS.close();
            
            System.out.println("REGISTROS ANTERIORES ELIMINADOS");
            
            PreparedStatement ps = conMS.prepareStatement(
                "INSERT INTO dbo.tsrmes_rpto_oracle(stock_code, stock_status, criticidad, item_name, invent_cost_pr"
                        + ", class, stock_type, unit_of_issue, ds, ds_large, dues_in, in_transit"
                        + ", consign_itrans, total_picked, dues_out, reserved) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"       
            );

            System.out.println("INICIO insert batch a MSSQL:                    " + tiempo());
            System.out.println("Insertando...");
            
            Statement st = conOra.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            int contador = 0;
            while(rs.next())
            {
                
                //System.out.println("Registro N°: " + contador++ + ", Hora : " + tiempo());
                ps.setString(1, rs.getString(1));
                ps.setString(2, rs.getString(2));
                ps.setString(3, rs.getString(3));
                ps.setString(4, rs.getString(4));
                ps.setString(5, rs.getString(5));
                ps.setString(6, rs.getString(6));
                ps.setString(7, rs.getString(7));
                ps.setString(8, rs.getString(8));
                ps.setString(9, rs.getString(9));
                ps.setString(10, rs.getString(10));
                ps.setString(11, rs.getString(11));
                ps.setString(12, rs.getString(12));
                ps.setString(13, rs.getString(13));
                ps.setString(14, rs.getString(14));
                ps.setString(15, rs.getString(15));
                ps.setString(16, rs.getString(16));
                //ps.setString(17, rs.getString(17));
                //ps.setString(18, rs.getString(18));
                ps.addBatch();
                contador++;
                if(contador%1000==0) {
                    ps.executeBatch();
                    ps.clearBatch();
                    System.out.println("Registro N°: " + contador + ", Hora :                    " + tiempo());
                }
            }
            if(contador%1000!=0) {
                ps.executeBatch();
                ps.clearBatch();
                System.out.println("Registro N°: " + contador + ", Hora :                    " + tiempo());
            }
            
            System.out.println("DATOS INSERTADOS                                " + tiempo());
            
            rs.close();
            ps.close();
            conMS.commit(); 
            
            // 2. QUERY STOCK
            conMS.setAutoCommit(false);
            ps = conMS.prepareStatement(
                "UPDATE dbo.tsrmes_rpto_oracle SET stock_total_act = ? WHERE stock_code = ?"     
            );
            
            rs = st.executeQuery(query2);
            
            System.out.println("INICIO update batch de stock                    " + tiempo());
            System.out.println("Actualizando...");
            
            contador = 0;
            while(rs.next()){
                
                //System.out.println("Campos a actualizar N°: " + contador++ + ", Hora : " + tiempo());
                ps.setString(1, rs.getString(2)); // El segundo resultado de rs retorna el stock
                ps.setString(2, rs.getString(1)); // El primer resultado de rs retorna el codigo
                //System.out.println("Code: " + rs.getString(1));
                //System.out.println("Stock: " + rs.getString(2));
                ps.addBatch();
                contador++;
                if(contador%100==0) {
                    ps.executeBatch();
                    ps.clearBatch();
                    System.out.println("Update N°: " + contador + ", Hora :                    " + tiempo());
                }
            }
            if(contador%100!=0) {
                    ps.executeBatch();
                    ps.clearBatch();
                    System.out.println("Update N°: " + contador + ", Hora :                    " + tiempo());
                }

            System.out.println("CAMPO STOCK ACTUALIZADO                         " + tiempo());
 
            rs.close(); // Fin rs query 2
            ps.close(); // Fin ps query 2
            conMS.commit();
            
            // 3. QUERY LEADTIME
            conMS.setAutoCommit(false);
            ps = conMS.prepareStatement(
                "UPDATE dbo.tsrmes_rpto_oracle SET lead_time = ? WHERE stock_code = ?"     
            );
            
            rs = st.executeQuery(query3);
            
            System.out.println("INICIO update batch de leadtime                 " + tiempo());
            System.out.println("Actualizando...");

            contador = 0;
            while(rs.next())
            {
                
                //System.out.println("Actualizando leadtime para el repuesto " + rs.getString(1));
                ps.setDouble(1, rs.getDouble(2)); // Asigna el valor de lead_time del query3 para el primer valor del update
                ps.setString(2, rs.getString(1)); // Asigna el valor de stock_code del query3 para el segundo valor del update
                ps.addBatch();
                contador++;
                if(contador%100==0) {
                    ps.executeBatch();
                    ps.clearBatch();
                    System.out.println("Update N°: " + contador + ", Hora :                    " + tiempo());
                }
            }
            if(contador%100!=0) {
                    ps.executeBatch();
                    ps.clearBatch();
                    System.out.println("Update N°: " + contador + ", Hora :                    " + tiempo());
                }
            
            System.out.println("CAMPO LEADTIME ACTUALIZADO                      " + tiempo());
            
            st.close();
            stMS.close();
            rs.close(); // Fin rs query 3
            ps.close(); // Fin ps query 3
            conMS.commit();

            conMS.close();
            conOra.close();
            
            System.out.println("FIN DEL PROCESO:                                " + tiempo());

        }catch(Exception ex)
        {
            System.out.println("Error mensaje: " + ex.getMessage() + "\n" + "Error string: " + ex.toString());
        }
    }
    
    public static void procedimientoAlmacenado()
    {
        try
        {
            
            Connection conMS = DBConnection.conMSSQL();
            System.out.println("EJECUTANDO PROCEDIMIENTO:                       " + tiempo());
            String query = "exec dbo.sp_respaldo";
            //String query = "exec dbo.sp_respaldo ?";
            CallableStatement cst = conMS.prepareCall(query);
            //cst.setInt(1, 0);
            //int resultado = cst.getInt("row_num");
            //cst.executeUpdate();
            //cst.executeQuery();
            //int resultado = cst.getInt(1);
            ResultSet rs = cst.executeQuery();
            while(rs.next()) //Posiciona el rs en el 1
            {
                int resultado = rs.getInt(1);
                System.out.println("FILAS MODIFICADAS: " + resultado);
            }
            //cst.execute();
            System.out.println("ACTUALIZANDO...");
            //int row_count = cst.getUpdateCount();
            cst.close();
            System.out.println("PROCEDIMIENTO EJECUTADO");
            //System.out.println("FILAS MODIFICADAS: " + resultado);
            //System.out.println("FILAS MODIFICADAS: " + row_count);
        }
        catch(Exception ex)
        {
            System.out.println("Error mensaje: " + ex.getMessage() + "\n" + "Error string: " + ex.toString());
        }
    }
}
