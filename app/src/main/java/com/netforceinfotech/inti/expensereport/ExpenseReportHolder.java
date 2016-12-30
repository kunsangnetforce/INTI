package com.netforceinfotech.inti.expensereport;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.netforceinfotech.inti.R;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseReportHolder extends RecyclerView.ViewHolder {

    TextView erName,erFromDate,erToDate,erStatus,erListConvertedAmount,erListPolicyAmount,erListCurrency;

    public ExpenseReportHolder(View itemView) {

        super(itemView);

        erName = (TextView) itemView.findViewById(R.id.erNameTextView);
        erFromDate = (TextView) itemView.findViewById(R.id.erFromDateTextView);
        erToDate = (TextView) itemView.findViewById(R.id.erToDateTextView);
        erListConvertedAmount = (TextView) itemView.findViewById(R.id.erListAmountTextView);
        erStatus = (TextView) itemView.findViewById(R.id.erStatusTextView);
        erListPolicyAmount = (TextView) itemView.findViewById(R.id.erListPolicyAmountTextView);
        erListCurrency = (TextView) itemView.findViewById(R.id.currencyCodeTextView);


    }
}
