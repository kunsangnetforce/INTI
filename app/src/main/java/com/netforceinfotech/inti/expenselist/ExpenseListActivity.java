package com.netforceinfotech.inti.expenselist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

public class ExpenseListActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Toolbar toolbar;
    ImageView imageViewFilter, imageViewCloseFilter;
    TextView textViewStatus;
    RelativeLayout relativeLayoutFilter;
    private SwipyRefreshLayout swipyRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        context = this;
        initView();
        setupToolBar(getString(R.string.list));
        setupRecyclerView();
    }

    private void initView() {
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        relativeLayoutFilter = (RelativeLayout) findViewById(R.id.relativeLayoutFilter);
        imageViewCloseFilter = (ImageView) findViewById(R.id.imageCloseFilter);
        imageViewCloseFilter.setOnClickListener(this);
        imageViewFilter = (ImageView) findViewById(R.id.imageViewFilter);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyLayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                showMessage("refreshed");
                swipyRefreshLayout.setRefreshing(false);
            }
        });
        findViewById(R.id.fabAddExpenseReport).setOnClickListener(this);

        setupFilterDropDown();
    }

    private void setupRecyclerView() {
        findViewById(R.id.fabAddExpenseReport).setOnClickListener(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        ExpenseListAdapter adapter = new ExpenseListAdapter(context, null);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupFilterDropDown() {

        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imageViewFilter);

        for (int i = 0; i < 10; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("Category " + i));
        }

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                showMessage("Loading ...");
                relativeLayoutFilter.setVisibility(View.VISIBLE);
                textViewStatus.setText("Category " + id);
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();

    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void setupToolBar(String title) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageViewSetting = (ImageView) toolbar.findViewById(R.id.imageViewSetting);
        TextView textViewTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
        setupSettingMenu(imageViewSetting);
        ImageView imageViewBack = (ImageView) toolbar.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


    }

    private void setupSettingMenu(ImageView imageView) {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imageView);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.logout)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.profile)));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                showMessage("position: " + id + " clicked");
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAddExpenseReport:
                Intent intent = new Intent(context, TextImageExpenseActivity.class);
                startActivity(intent);
                break;
            case R.id.imageCloseFilter:
                relativeLayoutFilter.setVisibility(View.GONE);
                break;
        }
    }
}
