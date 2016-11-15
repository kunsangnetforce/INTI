package com.netforceinfotech.inti.dummy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;

import java.util.ArrayList;

/**
 * Created by Netforce on 11/8/2016.
 */

public class DummyAdapter extends RecyclerView.Adapter<DummyHolder> {
    private final LayoutInflater inflater;
    ArrayList<DummyData> dummyDatas;
    Context context;

    public DummyAdapter(Context context, ArrayList<DummyData> dummyDatas) {
        this.dummyDatas = dummyDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public DummyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_dummy, parent, false);
        DummyHolder viewHolder = new DummyHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DummyHolder holder, final int position) {
        holder.textViewPerValue.setText("" + dummyDatas.get(position).pervalue);
        holder.buttonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummyDatas.get(position).sum += dummyDatas.get(position).pervalue;
                holder.textView.setText("" + dummyDatas.get(position).sum);
                Dummy.total-=dummyDatas.get(position).pervalue;
                Dummy.textViw.setText(""+Dummy.total);

            }
        });
        holder.buttonDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dummyDatas.get(position).sum > 0) {
                    dummyDatas.get(position).sum -= dummyDatas.get(position).pervalue;
                    holder.textView.setText("" + dummyDatas.get(position).sum);
                    Dummy.total-=dummyDatas.get(position).pervalue;
                    Dummy.textViw.setText(""+Dummy.total);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dummyDatas.size();
    }
}
