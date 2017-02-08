package com.netforceinfotech.inti.expensesummary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.netforceinfotech.inti.R;

import org.w3c.dom.Text;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseCategoryHolder extends RecyclerView.ViewHolder {

    TextView categoryTextView,originalCurrencysymbol,hashTextView,startTextViewBracket,endTextViewBracket,policyCurrencySybmbol,originalAmountTextView,policyAmountTextView;

    public ExpenseCategoryHolder(View itemView) {

        super(itemView);

        categoryTextView = (TextView) itemView.findViewById(R.id.textViewCategory);
        originalCurrencysymbol = (TextView) itemView.findViewById(R.id.textViewCurrencySymbol);
        policyCurrencySybmbol = (TextView) itemView.findViewById(R.id.textViewCurrencySymbol1);
        originalAmountTextView = (TextView) itemView.findViewById(R.id.orignalamountTextView);
        policyAmountTextView= (TextView) itemView.findViewById(R.id.policyamountTextView);
        startTextViewBracket = (TextView) itemView.findViewById(R.id.startTextViewBracket);
        endTextViewBracket = (TextView) itemView.findViewById(R.id.endTextViewBracket);
        hashTextView = (TextView) itemView.findViewById(R.id.hashTextView);




    }
}
