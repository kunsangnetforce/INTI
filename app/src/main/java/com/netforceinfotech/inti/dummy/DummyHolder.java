package com.netforceinfotech.inti.dummy;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.netforceinfotech.inti.R;

/**
 * Created by Netforce on 11/8/2016.
 */

public class DummyHolder extends RecyclerView.ViewHolder {
    Button buttonIncrement,buttonDecrement;
    TextView textView,textViewPerValue;
    public DummyHolder(View itemView) {
        super(itemView);
        buttonIncrement= (Button) itemView.findViewById(R.id.buttonIncrement);
        buttonDecrement= (Button) itemView.findViewById(R.id.buttonDecrement);
        textView= (TextView) itemView.findViewById(R.id.textViewTotal);
        textViewPerValue= (TextView) itemView.findViewById(R.id.textViewPerValue);
    }
}
