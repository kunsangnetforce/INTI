package com.netforceinfotech.inti.expensereport;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseReportData {

    String erName,erFromDate,erToDate,erCurrencyCode,erListAmount,erListPolicyAmount,erStatus,erID;

    public ExpenseReportData(String erName, String erFromDate, String erToDate, String erCurrencyCode, String erListAmount, String erListPolicyAmount, String erStatus, String erID) {
        this.erName = erName;
        this.erFromDate = erFromDate;
        this.erToDate = erToDate;
        this.erCurrencyCode = erCurrencyCode;
        this.erListAmount = erListAmount;
        this.erListPolicyAmount = erListPolicyAmount;
        this.erStatus = erStatus;
        this.erID = erID;
    }

    public String getErName() {
        return erName;
    }

    public String getErFromDate() {
        return erFromDate;
    }

    public String getErToDate() {
        return erToDate;
    }

    public String getErCurrencyCode() {
        return erCurrencyCode;
    }

    public String getErListAmount() {
        return erListAmount;
    }

    public String getErListPolicyAmount() {
        return erListPolicyAmount;
    }

    public String getErStatus() {
        return erStatus;
    }

    public String getErID() {
        return erID;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpenseReportData)) {
            return false;
        }

        ExpenseReportData that = (ExpenseReportData) obj;
        return this.erID.equals(that.erID);
    }

}
