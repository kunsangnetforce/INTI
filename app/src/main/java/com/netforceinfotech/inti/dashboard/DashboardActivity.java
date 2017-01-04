package com.netforceinfotech.inti.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.expenselist.ExpenseListActivity;
import com.netforceinfotech.inti.expensereport.MyExpenseReportActivity;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.supervisor_expensereport.SupervisorExpenseReportActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DashboardActivity extends AppCompatActivity implements PieChartOnValueSelectListener, View.OnClickListener {
    private static final String TAG ="Dash";
    PieChartView pieChartView;
    private PieChartData data;
    Context context;
    Toolbar toolbar;
    Intent intent;
    LinearLayout linearLayoutAssigned;
     String supervisorFlag ="4";
    Bundle bundle;
    public String eEmail,userType, userID,customerID,userPass,userName;
    TextView eEmailTextView;
    UserSessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        sessionManager = new UserSessionManager(this);
        try {
            Bundle bundle = getIntent().getExtras();
            eEmail = bundle.getString("eEmail");
            userType = bundle.getString("userType");
            userID = bundle.getString("userID");
            customerID =bundle.getString("customerID");
            userPass =bundle.getString("userPass");
          //  userName =bundle.getString("userName");


            if (userType.equalsIgnoreCase("3")) {
                supervisorFlag = "3";
            }else if(userType.equalsIgnoreCase("2")){
                supervisorFlag ="2";
            }

        } catch (Exception ex) {

        }
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
        ImageView imageViewBack = (ImageView) toolbar.findViewById(R.id.imageViewBack);
        imageViewBack.setVisibility(View.GONE);
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

    private void initView() {

        if(sessionManager.checkLogin())
            finish();

        HashMap<String,String> user= sessionManager.getUserDetails();

        String emairr= user.get(UserSessionManager.KEY_EMAIL);
        String ct = user.get(UserSessionManager.KEY_USERTYPE);
        String uid = user.get(UserSessionManager.KEY_USERID);
        String cid = user.get(UserSessionManager.KEY_CUSTOMERID);

        Log.d(TAG," "+ emairr);
        Log.d(TAG," "+ ct);
        Log.d(TAG," "+ uid);
        Log.d(TAG," "+ cid);



        eEmailTextView= (TextView) findViewById(R.id.eEmailTextView);
        eEmailTextView.setText(eEmail);

        linearLayoutAssigned = (LinearLayout) findViewById(R.id.linearLayoutAssigned);
        if (supervisorFlag.equalsIgnoreCase("4")) {

            linearLayoutAssigned.setVisibility(View.GONE);

        }

        findViewById(R.id.fabAddExpenseReport).setOnClickListener(this);
        findViewById(R.id.imageViewList).setOnClickListener(this);
        findViewById(R.id.relativeLayoutApproved).setOnClickListener(this);
        findViewById(R.id.relativeLayoutRejected).setOnClickListener(this);
        findViewById(R.id.relativeLayoutPaidout).setOnClickListener(this);
        findViewById(R.id.relativeLayoutInApproval).setOnClickListener(this);
        findViewById(R.id.relativeLayoutPendingReport).setOnClickListener(this);
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

        if (supervisorFlag.equalsIgnoreCase("3")) {
            SliceValue pendingReport = new SliceValue(23f, ContextCompat.getColor(context, R.color.colorAccent));
            values.add(pendingReport);
        }
        data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOutside(false);
        data.setHasLabels(true);
        data.setHasCenterCircle(true);

        pieChartView.setPieChartData(data);
    }

    @Override
    public void onValueSelected(int arcIndex, SliceValue value) {
        switch (arcIndex) {
            case 0:
                showMessage("Approved method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case 1:
                showMessage("In Approval method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 1);

                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case 2:
                showMessage("Rejected method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case 3:
                showMessage("Paid out method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 3);
                intent.putExtras(bundle);

                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case 4:
                showMessage("Pending method call");
                intent = new Intent(this, SupervisorExpenseReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);


                break;

        }
        showMessage("index:" + arcIndex + "Selected. Method will be called..." + value);
    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayoutApproved:
                showMessage("approved method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.relativeLayoutInApproval:
                showMessage("in approval method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.relativeLayoutRejected:
                showMessage("reject method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.relativeLayoutPaidout:
                showMessage("paid ouit method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 3);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.relativeLayoutPendingReport:
                intent = new Intent(this, SupervisorExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 4);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.imageViewList:
                showMessage("List will be shown");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("click", 5);
                bundle.putString("eEmail",eEmail);
                bundle.putString("userID",userID);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.fabAddExpenseReport:
                intent = new Intent(this, CreateExpenseActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }
    }
}
