package com.netforceinfotech.inti.supervisor_expensereport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;
import com.netforceinfotech.inti.expensesummary.ExpenseSummaryActivity;

import java.util.ArrayList;

/**
 * Created by Netforce on 11/8/2016.
 */

public class SupervisorERAdapter extends RecyclerView.Adapter<SupervisorERHolder> {
    private final LayoutInflater inflater;
    ArrayList<SupervisorERData> supervisorERDatas;
    Context context;

    public SupervisorERAdapter(Context context, ArrayList<SupervisorERData> supervisorERDatas) {
        this.supervisorERDatas = supervisorERDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public SupervisorERHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_supervisor_er, parent, false);
        SupervisorERHolder viewHolder = new SupervisorERHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SupervisorERHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Coming Soon");

//
//                Intent intent = new Intent(context, ExpenseSummaryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("from", "super");
//                intent.putExtras(bundle);
//                context.startActivity(intent);
            }
        });
    }

    private void showMessage(String s)
    {

        Toast.makeText(context,s,Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
