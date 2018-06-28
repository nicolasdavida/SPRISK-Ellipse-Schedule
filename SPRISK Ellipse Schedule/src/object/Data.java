/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author Armando
 */
public class Data {

    private String stock_code;
    private String stock_status;
    private String criticidad;
    
    public Data() {
    }

    public String getStock_code() {
        return stock_code;
    }

    public String getStock_status() {
        return stock_status;
    }

    public String getCriticidad() {
        return criticidad;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public void setCriticidad(String criticidad) {
        this.criticidad = criticidad;
    }
   
}
