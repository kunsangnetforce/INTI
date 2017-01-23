package com.netforceinfotech.inti.addexpenses.model;

/**
 * Created by Tenzin on 1/16/2017.
 */

public class SupplierData {

    int supplierid;
    String suppliername;

    public SupplierData(int supplierid, String suppliername) {
        this.supplierid = supplierid;
        this.suppliername = suppliername;
    }

    public int getSupplierid() {
        return supplierid;
    }

    public String getSuppliername() {
        return suppliername;
    }

}
