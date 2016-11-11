package com.netforceinfotech.inti.history;

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

public class HistroyAdapter extends RecyclerView.Adapter<HistoryHolder> {
    private final LayoutInflater inflater;
    ArrayList<HistoryData> historyDatas;
    Context context;

    public HistroyAdapter(Context context, ArrayList<HistoryData> historyDatas) {
        this.historyDatas = historyDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_history, parent, false);
        HistoryHolder viewHolder = new HistoryHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
           }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
