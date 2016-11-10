package com.netforceinfotech.inti.expensesummary;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.expenselist.ExpenseListActivity;

import java.util.ArrayList;

/**
 * Created by Netforce on 11/8/2016.
 */

public class ExpenseCategoryAdapter extends RecyclerView.Adapter<ExpenseCategoryHolder> {
    private final LayoutInflater inflater;
    ArrayList<ExpenseCategoryData> expenseCategoryDatas;
    Context context;

    public ExpenseCategoryAdapter(Context context, ArrayList<ExpenseCategoryData> expenseCategoryDatas) {
        this.expenseCategoryDatas = expenseCategoryDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ExpenseCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_category_expense, parent, false);
        ExpenseCategoryHolder viewHolder = new ExpenseCategoryHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseCategoryHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExpenseListActivity.class);
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
