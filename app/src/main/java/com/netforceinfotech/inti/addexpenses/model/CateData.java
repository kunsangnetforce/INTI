package com.netforceinfotech.inti.addexpenses.model;

/**
 * Created by Tenzin on 1/16/2017.
 */

public class CateData {
    String catname;
    String catid;

    public CateData(String catid,String catname) {
        this.catname = catname;
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public String getCatid() {
        return catid;
    }
}
