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

    private String item_name;
    private String invent_cost_pr;
    private String _class;
    private String stock_type;
    private String unit_of_issue;
    private String ds;
    private String ds_large;
    private String dues_in;
    private String in_transit;
    private String consign_itrans;
    private String total_picked;
    private String dues_out;
    private String reserved;

    private String stock_code_actualizado;
    private String stock_total_actualizado;
    ////////////////////////////////////////////////////////////////////////////
    
    public Data() {
    }
    
    public String getStock_code_actualizado() {
        return stock_code_actualizado;
    }

    public void setStock_code_actualizado(String stock_code_actualizado) {
        this.stock_code_actualizado = stock_code_actualizado;
    }
    
    public String getStock_total_actualizado() {
        return stock_total_actualizado;
    }

    public void setStock_total_actualizado(String stock_total_actualizado) {
        this.stock_total_actualizado = stock_total_actualizado;
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
    
    public String getItem_name() {
        return item_name;
    }

    public String getInvent_cost_pr() {
        return invent_cost_pr;
    }

    public String get_Class() {
        return _class;
    }

    public String getStock_type() {
        return stock_type;
    }

    public String getUnit_of_issue() {
        return unit_of_issue;
    }

    public String getDs() {
        return ds;
    }

    public String getDs_large() {
        return ds_large;
    }

    public String getDues_in() {
        return dues_in;
    }

    public String getIn_transit() {
        return in_transit;
    }

    public String getConsign_itrans() {
        return consign_itrans;
    }

    public String getTotal_picked() {
        return total_picked;
    }

    public String getDues_out() {
        return dues_out;
    }

    public String getReserved() {
        return reserved;
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

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setInvent_cost_pr(String invent_cost_pr) {
        this.invent_cost_pr = invent_cost_pr;
    }

    public void set_Class(String _class) {
        this._class = _class;
    }

    public void setStock_type(String stock_type) {
        this.stock_type = stock_type;
    }

    public void setUnit_of_issue(String unit_of_issue) {
        this.unit_of_issue = unit_of_issue;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public void setDs_large(String ds_large) {
        this.ds_large = ds_large;
    }

    public void setDues_in(String dues_in) {
        this.dues_in = dues_in;
    }

    public void setIn_transit(String in_transit) {
        this.in_transit = in_transit;
    }

    public void setConsign_itrans(String consign_itrans) {
        this.consign_itrans = consign_itrans;
    }

    public void setTotal_picked(String total_picked) {
        this.total_picked = total_picked;
    }

    public void setDues_out(String dues_out) {
        this.dues_out = dues_out;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
   
}
