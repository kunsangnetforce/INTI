package com.netforceinfotech.inti.dummy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.netforceinfotech.inti.R;

import java.util.ArrayList;

public class Dummy extends AppCompatActivity {

    public static TextView textViw;
    RecyclerView recyclerView;
    public static int total = 0;
    ArrayList<DummyData> dummyDatas = new ArrayList<>();
    private DummyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        initView();
        initRecycler();

    }

    private void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DummyAdapter(this, dummyDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        setupDummyData();
    }

    private void setupDummyData() {
        try {
            dummyDatas.clear();
        } catch (Exception ex) {

        }
        for (int i = 0; i < 5; i++) {
            int pervalue = (1 + i) * 3;
            dummyDatas.add(new DummyData(0, pervalue));
        }
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        textViw = (TextView) findViewById(R.id.textView);
    }
}
