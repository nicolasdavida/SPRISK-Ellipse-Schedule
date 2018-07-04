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
public class DataUpdate {

    public DataUpdate() {
    }

    public String getStock_code_actualizado() {
        return stock_code_actualizado;
    }

    public String getStock_total_actualizado() {
        return stock_total_actualizado;
    }

    public void setStock_code_actualizado(String stock_code_actualizado) {
        this.stock_code_actualizado = stock_code_actualizado;
    }

    public void setStock_total_actualizado(String stock_total_actualizado) {
        this.stock_total_actualizado = stock_total_actualizado;
    }
    
    private String stock_code_actualizado;
    private String stock_total_actualizado;
    
}
