package com.netforceinfotech.inti.expenselist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;
import com.netforceinfotech.inti.expensesummary.ExpenseSummaryActivity;

import java.util.ArrayList;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListHolder> {
    private final LayoutInflater inflater;
    ArrayList<ExpenseListData> expenseListDatas;
    Context context;

    public ExpenseListAdapter(Context context, ArrayList<ExpenseListData> expenseListDatas) {
        this.expenseListDatas = expenseListDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ExpenseListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_expense_list, parent, false);
        ExpenseListHolder viewHolder = new ExpenseListHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseListHolder holder, int position) {
        final ExpenseListData expData= expenseListDatas.get(position);

        holder.textviewDescription.setText(expData.getErListDes());
        holder.textViewExpenDate.setText(expData.getErListDate());
        holder.textViewCurrencyCode.setText(expData.getErListCurrency());
        holder.textViewAmount.setText(expData.getErListAmount());
        holder.textViewCategory.setText(expData.getErListCat());
        Glide.with(context).load(expData.getErListImageUrl()).error(R.drawable.ic_barcode).placeholder(R.drawable.ic_barcode).into(holder.imageViewExpenses);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,ExpenseListDetailActivity.class);
                intent.putExtra("elID",expData.getErListID());
                intent.putExtra("erID",expData.getErID());
                context.startActivity(intent);

            }
        });


        // wait coming here soon....



    }

    @Override
    public int getItemCount() {

        return expenseListDatas.size();
    }

}
