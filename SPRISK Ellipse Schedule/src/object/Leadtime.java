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
public class Leadtime {

    public Leadtime() {
    }

    public String getStock_code() {
        return stock_code;
    }

    public String getAvg_leadtime() {
        return avg_leadtime;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public void setAvg_leadtime(String avg_leadtime) {
        this.avg_leadtime = avg_leadtime;
    }
    
    private String stock_code;
    private String avg_leadtime;
    
}
