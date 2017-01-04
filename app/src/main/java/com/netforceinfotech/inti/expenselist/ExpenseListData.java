package com.netforceinfotech.inti.expenselist;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseListData {


    String eEmail,erListImageUrl,erID,erListID,erListDes,erListCat,erListAmount,erListCurrency,erListDate;

    public ExpenseListData(String eEmail, String erListImageUrl, String erID, String erListID, String erListDes, String erListCat, String erListAmount, String erListCurrency, String erListDate) {
        this.eEmail = eEmail;

        this.erListImageUrl = erListImageUrl;
        this.erID = erID;
        this.erListID = erListID;
        this.erListDes = erListDes;
        this.erListCat = erListCat;
        this.erListAmount = erListAmount;
        this.erListCurrency = erListCurrency;
        this.erListDate = erListDate;
    }

    public String geteEmail() {
        return eEmail;
    }



    public String getErListImageUrl() {
        return erListImageUrl;
    }

    public String getErID() {
        return erID;
    }

    public String getErListID() {
        return erListID;
    }

    public String getErListDes() {
        return erListDes;
    }

    public String getErListCat() {
        return erListCat;
    }

    public String getErListAmount() {
        return erListAmount;
    }

    public String getErListCurrency() {
        return erListCurrency;
    }

    public String getErListDate() {
        return erListDate;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpenseListData)) {
            return false;
        }

        ExpenseListData that = (ExpenseListData) obj;
        return this.erListID.equals(that.erListID);
    }

}
