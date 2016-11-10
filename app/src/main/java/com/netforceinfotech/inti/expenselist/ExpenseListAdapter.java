package com.netforceinfotech.inti.expenselist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TextImageExpenseActivity.class);
                context.startActivity(intent);
                ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
