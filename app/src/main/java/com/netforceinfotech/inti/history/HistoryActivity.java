package com.netforceinfotech.inti.history;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netforceinfotech.inti.R;

public class HistoryActivity extends AppCompatActivity {

    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context = this;
        setupRecycler();
        setupToolBar(getString(R.string.report));
    }

    private void setupRecycler() {
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        HistroyAdapter adapter = new HistroyAdapter(context, null);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
    }
    private void setupToolBar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageViewSetting= (ImageView) toolbar.findViewById(R.id.imageViewSetting);
        TextView textViewTitle= (TextView) toolbar.findViewById(R.id.textViewTitle);
        imageViewSetting.setVisibility(View.INVISIBLE);
        textViewTitle.setText(title);
        ImageView imageViewBack= (ImageView) toolbar.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

    }
}
