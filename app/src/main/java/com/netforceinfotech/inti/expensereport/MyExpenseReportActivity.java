package com.netforceinfotech.inti.expensereport;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

public class MyExpenseReportActivity extends AppCompatActivity implements View.OnClickListener {
    SwipyRefreshLayout swipyRefreshLayout;
    Toolbar toolbar;
    ImageView imageViewCloseFilter, imageViewFilter;
    Context context;
    TextView textViewStatus;
    RelativeLayout relativeLayoutFilter;
    int click = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_report);
        context = this;
        try {
            Bundle bundle = getIntent().getExtras();
            click = bundle.getInt("click");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initView();
        setupToolBar(getString(R.string.my_expense_reports));
        setupRecyclerView();

    }

    private void initView() {
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        relativeLayoutFilter = (RelativeLayout) findViewById(R.id.relativeLayoutFilter);
        imageViewCloseFilter = (ImageView) findViewById(R.id.imageCloseFilter);
        imageViewCloseFilter.setOnClickListener(this);
        imageViewFilter = (ImageView) findViewById(R.id.imageViewFilter);
        findViewById(R.id.imageViewPiechart).setOnClickListener(this);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyLayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                showMessage("refreshed");
                swipyRefreshLayout.setRefreshing(false);
            }
        });
        findViewById(R.id.fabAddExpenseReport).setOnClickListener(this);
        switch (click) {
            case 0:
                textViewStatus.setText(getString(R.string.approve));
                break;
            case 1:
                textViewStatus.setText(getString(R.string.in_approval));
                break;
            case 2:
                textViewStatus.setText(getString(R.string.rejected));
                break;
            case 3:
                textViewStatus.setText(getString(R.string.paidout));
                break;
            case 5:
                relativeLayoutFilter.setVisibility(View.GONE);
                break;
        }
        setupFilterDropDown();
    }

    private void setupFilterDropDown() {

        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imageViewFilter);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.approve)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.in_approval)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.rejected)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.paidout)));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                showMessage("Loading ...");
                relativeLayoutFilter.setVisibility(View.VISIBLE);
                switch (id) {
                    case 0:
                        textViewStatus.setText(getString(R.string.approve));
                        break;
                    case 1:
                        textViewStatus.setText(getString(R.string.in_approval));
                        break;
                    case 2:
                        textViewStatus.setText(getString(R.string.rejected));
                        break;
                    case 3:
                        textViewStatus.setText(getString(R.string.paidout));
                        break;
                    case 5:
                        relativeLayoutFilter.setVisibility(View.GONE);
                        break;
                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();

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

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        ExpenseReportAdapter adapter = new ExpenseReportAdapter(this, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewPiechart:
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
            case R.id.fabAddExpenseReport:
                intent = new Intent(this, CreateExpenseActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.imageCloseFilter:
                relativeLayoutFilter.setVisibility(View.GONE);
                showMessage("Loading...");
                break;
        }
    }
}
