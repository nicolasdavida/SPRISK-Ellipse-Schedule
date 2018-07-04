/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

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
public class connectionAll {
    
    
    
    public static Connection connectAll(){

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
            "WHERE A.CLASS in ('2','A','E','R') and B.STOCK_TYPE in ('6','7','8') and A.STOCK_STATUS in ('X','A')" +
            "and B.INVT_STAT_CODE in ('2111','3121','3131','3141','3151','4141','5201','5202','5203','5221','5222','5223','5231','5321','5401','5501','6200','6201','6203','6204','6205','6221')";
            
            Statement st = conOra.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            //  Stock actualizado
            String query2 = 

            "SELECT a.STOCK_CODE, Sum(a.SOH) AS STOCK_TOTAL " +
            "FROM ellrep.MSF1HD a INNER JOIN ellrep.MSF1CS b ON a.CUSTODIAN_ID = b.CUSTODIAN_ID " +
            "WHERE (((a.HOLDING_TYPE)='F') AND ((b.CUSTODIAN_TYPE)='W') AND ((a.STK_OWNERSHP_IND)='O' Or (a.STK_OWNERSHP_IND)='C'))" +
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
            
            int contador = 0;
            //int count = 0;
            
            List<Data> dataList = new ArrayList<>();
            List<DataUpdate> dataList2 = new ArrayList<>();
            
            HashMap<String, Data> dataHM = new HashMap<>();
            
            while(rs.next())
            {
                Data data = new Data();
                
                System.out.println("Registro N°: " + contador++ + ", Hora : " + tiempo());
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

                dataList.add(data);
                //dataHM.put(rs.getString(1), data);
            }
            
            rs.close();
            
            //DBConnection.conMSSQL();
            Connection conMS = DBConnection.conMSSQL();
            
            String queryDelete = 
            "DELETE FROM dbo.tsrmes_rpto_oracle";
            
            Statement stMs = conMS.createStatement();
            stMs.execute(queryDelete);
            stMs.close();
            
            System.out.println("ELIMINADOS LOS REGISTROS ANTERIORES");
            
            // Prepara la sentencia Batch, más adelante cambiar por UPDATE en vez de INSERT
            PreparedStatement ps = conMS.prepareStatement(
                "INSERT INTO dbo.tsrmes_rpto_oracle(stock_code, stock_status, criticidad, item_name, invent_cost_pr"
                        + ", class, stock_type, unit_of_issue, ds, ds_large, dues_in, in_transit"
                        + ", consign_itrans, total_picked, dues_out, reserved, stock_code_act, stock_total_act) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"       
            );
            
            //final int batchSize = 1000;
            
            System.out.println("INICIO insert batch a MSSQL:                    " + tiempo());
            System.out.println("Insertando...");
            
            for(Data dataRs : dataList)
            {
                ps.setString(1, dataRs.getStock_code());
                ps.setString(2, dataRs.getStock_status());
                ps.setString(3, dataRs.getCriticidad());
                ps.setString(4, dataRs.getItem_name());
                ps.setString(5, dataRs.getInvent_cost_pr());
                ps.setString(6, dataRs.get_Class());
                ps.setString(7, dataRs.getStock_type());
                ps.setString(8, dataRs.getUnit_of_issue());
                ps.setString(9, dataRs.getDs());
                ps.setString(10, dataRs.getDs_large());
                ps.setString(11, dataRs.getDues_in());
                ps.setString(12, dataRs.getIn_transit());
                ps.setString(13, dataRs.getConsign_itrans());
                ps.setString(14, dataRs.getTotal_picked());
                ps.setString(15, dataRs.getDues_out());
                ps.setString(16, dataRs.getReserved());
                ps.setString(17, dataRs.getStock_code_actualizado());
                ps.setString(18, dataRs.getStock_total_actualizado());
                
                ps.addBatch();
                /*
                if(++count % batchSize == 0) {
                    System.out.println("Insert de " + batchSize + " filas a las:" + tiempo());
                    ps.executeBatch();
                }*/
                
                ps.executeBatch();
            }
            
            ps.close();
            conMS.commit(); 
            
            /*
            rs = st.executeQuery(query2);
            contador = 0;
            
            while(rs.next()){
                
                System.out.println("Campos a actualizar N°: " + contador++ + ", Hora : " + tiempo());
                DataUpdate data = new DataUpdate();
                data.setStock_code_actualizado(rs.getString(1));
                data.setStock_total_actualizado(rs.getString(2));
                
                dataList2.add(data);
            }
            
            conMS.setAutoCommit(false);
            ps = conMS.prepareStatement(
                "UPDATE dbo.tsrmes_rpto_oracle SET stock_total_act = ? WHERE stock_code = ?"     
            );
            
            for(DataUpdate dataUp : dataList2)
            {
                ps.setString(1, dataUp.getStock_total_actualizado());
                ps.setString(2, dataUp.getStock_code_actualizado());
                System.out.println("Code: " + dataUp.getStock_code_actualizado());
                System.out.println("Stock: " + dataUp.getStock_total_actualizado());
                ps.addBatch();
                ps.executeBatch();
            }
            
            System.out.println("CAMPOS ACTUALIZADOS");
            
            // Ejecuta el Batch
            //ps.executeBatch();
            // Guarda los cambios en la BD
            
            conMS.commit();
            */
            
            // TERCER QUERY: LEADTIME
            System.out.println("QUERY LEADTIME");
            rs.close(); // Fin rs del query2
            rs = st.executeQuery(query3);
            
            ps.close(); // Fin ps del query2
            ps = conMS.prepareStatement(
                "UPDATE dbo.tsrmes_rpto_oracle SET lead_time = ? WHERE stock_code = ?"     
            );
            
            while(rs.next())
            {
                System.out.println("Actualizando leadtime para el repuesto " + rs.getString(1));
                ps.setString(1, rs.getString(2)); // Asigna el valor de lead_time del query3 para el primer valor del update
                ps.setString(2, rs.getString(1)); // Asigna el valor de stock_code del query3 para el segundo valor del update
                ps.addBatch();
                ps.executeBatch();
            }
            
            conMS.commit();
            
            /*
            List<Leadtime> dataLeadtime = new ArrayList<>();
            while(rs.next()){
                
                System.out.println("Campos a actualizar N°: " + contador++ + ", Hora : " + tiempo());
                Leadtime leadtime = new Leadtime();
                leadtime.setStock_code(rs.getString(1));
                leadtime.setAvg_leadtime(rs.getString(2));
                dataLeadtime.add(leadtime);
            }
            */

            st.close();
            rs.close();
            
            //rs.close();
            //rs2.close();
            
            ps.close();
            
            conMS.close();
            conOra.close();
            
            System.out.println("FIN insert batch a MSSQL:                       " + tiempo());
            System.out.println("////////////////FIN DEL PROCESO: " + tiempo() + "/////////////////////");
            
            //Fin
            return conOra;
            
        }catch(Exception ex)
        {
            System.out.println("Error mensaje: " + ex.getMessage() + "\n" + "Error string: " + ex.toString());
        }
        return null;
    }

    static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
    
    private static String tiempo() {
        return format.format(new Date(System.currentTimeMillis()));
    }
}
