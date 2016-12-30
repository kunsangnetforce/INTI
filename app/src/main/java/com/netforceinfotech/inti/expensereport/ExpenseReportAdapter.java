package com.netforceinfotech.inti.expensereport;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.expensesummary.ExpenseSummaryActivity;

import java.util.ArrayList;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseReportAdapter extends RecyclerView.Adapter<ExpenseReportHolder> {
    private final LayoutInflater inflater;
    ArrayList<ExpenseReportData> expenseReportDatas;
    Context context;

    public ExpenseReportAdapter(Context context, ArrayList<ExpenseReportData> expenseReportDatas) {
        this.expenseReportDatas = expenseReportDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ExpenseReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_expense_report, parent, false);
        ExpenseReportHolder viewHolder = new ExpenseReportHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseReportHolder holder, int position) {


        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGrey));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        try {

            ExpenseReportData exReport= expenseReportDatas.get(position);

            holder.erName.setText(exReport.getErName());
            holder.erFromDate.setText(exReport.getErFromDate());
            holder.erFromDate.setText(exReport.getErFromDate());
            holder.erToDate.setText(exReport.getErToDate());
            holder.erListConvertedAmount.setText(exReport.getErListAmount());
            holder.erListCurrency.setText(exReport.getErCurrencyCode());
            holder.erListPolicyAmount.setText(exReport.getErListPolicyAmount());
            holder.erStatus.setText(exReport.getErStatus());

        }catch (Exception ex){

            ExpenseReportData exReport= expenseReportDatas.get(position);

            holder.erName.setText("Tashi Nyima");
            holder.erFromDate.setText("FromDate");
            holder.erFromDate.setText("FromDate");
            holder.erToDate.setText("FromDate");
            holder.erListConvertedAmount.setText("FromDate");
            holder.erListCurrency.setText("FromDate");
            holder.erListPolicyAmount.setText("FromDate");
            holder.erStatus.setText("FromDate");
            showMessage("I am in the wrong place...");
        }





    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return expenseReportDatas.size();
    }
}
