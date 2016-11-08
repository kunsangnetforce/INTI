package com.netforceinfotech.inti.expenselist;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseListData {
    String ern, name, description, date_from, date_to, status_description, status_code, currency_code, currency_symbol, total_amount, total_policy;

    ExpenseListData(String ern, String name, String description, String date_from, String date_to, String status_description, String status_code,
                    String currency_code, String currency_symbol, String total_amount, String total_policy) {
        this.ern = ern;
        this.name = name;
        this.description = description;
        this.date_from = date_from;
        this.date_to = date_to;
        this.status_description = status_description;
        this.status_code = status_code;
        this.currency_code = currency_code;
        this.currency_symbol = currency_symbol;
        this.total_amount = total_amount;
        this.total_policy = total_policy;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpenseListData)) {
            return false;
        }

        ExpenseListData that = (ExpenseListData) obj;
        return this.ern.equals(that.ern);
    }

}
