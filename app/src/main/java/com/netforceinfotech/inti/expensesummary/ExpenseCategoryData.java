package com.netforceinfotech.inti.expensesummary;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseCategoryData {



    String categoryId, categoryName, currencyCode, currencySymbol, totalamount, policyamount;

    ExpenseCategoryData(String categoryId, String categoryName, String currencyCode, String currencySymbol, String totalamount, String policyamount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.totalamount = totalamount;
        this.policyamount = policyamount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public String getPolicyamount() {
        return policyamount;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpenseCategoryData)) {
            return false;
        }

        ExpenseCategoryData that = (ExpenseCategoryData) obj;
        return this.categoryId.equals(that.categoryId);
    }

}
