package com.netforceinfotech.inti.expenselist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netforceinfotech.inti.R;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseListHolder extends RecyclerView.ViewHolder {

    public TextView textViewCategory, textviewDescription,textViewExpenDate,textViewCurrencyCode,textViewAmount;
    public ImageView imageViewExpenses;
    public ExpenseListHolder(View itemView) {

        super(itemView);

        textViewAmount = (TextView) itemView.findViewById(R.id.textViewAmount);
        textViewCategory= (TextView) itemView.findViewById(R.id.textViewCategory);
        textviewDescription = (TextView) itemView.findViewById(R.id.textviewDescription);
        textViewExpenDate = (TextView) itemView.findViewById(R.id.textViewExpenDate);
        textViewCurrencyCode = (TextView) itemView.findViewById(R.id.textViewCurrencyCode);
        imageViewExpenses = (ImageView) itemView.findViewById(R.id.imageViewExpenses);


    }
}
