package com.netforceinfotech.inti.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.ChooseAddExpensesActivity;
import com.netforceinfotech.inti.filteredreport.FilteredReportActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DashboardActivity extends AppCompatActivity implements PieChartOnValueSelectListener, View.OnClickListener {
    PieChartView pieChartView;
    private PieChartData data;
    Context context;
    Toolbar toolbar;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        initGraph();
        setupToolBar(getString(R.string.dashboard));
        initView();
    }

    private void setupToolBar(String title) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageViewSetting = (ImageView) toolbar.findViewById(R.id.imageViewSetting);
        TextView textViewTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
        setupSettingMenu(imageViewSetting);

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

    private void initView() {

        findViewById(R.id.fabAddExpenseReport).setOnClickListener(this);
        findViewById(R.id.imageViewList).setOnClickListener(this);
        findViewById(R.id.relativeLayoutApproved).setOnClickListener(this);
        findViewById(R.id.relativeLayoutRejected).setOnClickListener(this);
        findViewById(R.id.relativeLayoutPaidout).setOnClickListener(this);
        findViewById(R.id.relativeLayoutInApproval).setOnClickListener(this);
    }

    private void initGraph() {
        pieChartView = (PieChartView) findViewById(R.id.chart);
        generateData();
        pieChartView.setOnValueTouchListener(this);
    }

    private void generateData() {

        List<SliceValue> values = new ArrayList<SliceValue>();
        SliceValue approvedValue = new SliceValue(20f, ContextCompat.getColor(context, R.color.green));
        values.add(approvedValue);
        SliceValue pendingValue = new SliceValue(21f, ContextCompat.getColor(context, R.color.yellow));
        values.add(pendingValue);
        SliceValue rejectValue = new SliceValue(22f, ContextCompat.getColor(context, R.color.red));
        values.add(rejectValue);
        SliceValue progressValue = new SliceValue(23f, ContextCompat.getColor(context, R.color.blue));
        values.add(progressValue);


        data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOutside(false);
        data.setHasLabels(true);
        data.setHasCenterCircle(true);

        pieChartView.setPieChartData(data);
    }

    @Override
    public void onValueSelected(int arcIndex, SliceValue value) {
        showMessage("index:" + arcIndex + "Selected. Method will be called..." + value);
    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayoutApproved:
                showMessage("approved method call");
                intent = new Intent(this, FilteredReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.relativeLayoutInApproval:
                showMessage("in approval method call");
                intent = new Intent(this, FilteredReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.relativeLayoutRejected:
                showMessage("reject method call");
                intent = new Intent(this, FilteredReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.relativeLayoutPaidout:
                showMessage("paid ouit method call");
                intent = new Intent(this, FilteredReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.imageViewList:
                showMessage("List will be shown");
                intent = new Intent(this, FilteredReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.fabAddExpenseReport:
                intent = new Intent(this, ChooseAddExpensesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }
}
