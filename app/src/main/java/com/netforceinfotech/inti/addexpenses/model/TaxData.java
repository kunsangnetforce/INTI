package com.netforceinfotech.inti.addexpenses.model;

/**
 * Created by Tenzin on 1/16/2017.
 */

public class TaxData {
    int taxid;
    String taxname;

    public TaxData(int taxid, String taxname) {
        this.taxid = taxid;
        this.taxname = taxname;
    }

    public String getTaxname() {
        return taxname;
    }

    public int getTaxid() {
        return taxid;
    }
}
