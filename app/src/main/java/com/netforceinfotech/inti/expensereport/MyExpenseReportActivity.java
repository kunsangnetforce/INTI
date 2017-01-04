package com.netforceinfotech.inti.expensereport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expenselist.ExpenseListData;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyExpenseReportActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="MyExpenseReport" ;
    SwipyRefreshLayout swipyRefreshLayout;
    Toolbar toolbar;
    ImageView imageViewCloseFilter, imageViewFilter;
    Context context;
    TextView textViewStatus;
    RelativeLayout relativeLayoutFilter;
    int click = 5;
    ArrayList<ExpenseReportData> expenseReportDatas = new ArrayList<ExpenseReportData>();
    ExpenseReportAdapter erAdapter;
    String eEmail,userID;
    TextView textViewEmail;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_report);
        context = this;
        try {
            Bundle bundle = getIntent().getExtras();
            click = bundle.getInt("click");
//            eEmail = bundle.getString("eEmail");
//            userID = bundle.getString("userID");




        } catch (Exception ex) {
            ex.printStackTrace();
        }


        initView();
        setupToolBar(getString(R.string.my_expense_reports));
        setupRecyclerView();
        initErDatas();
        JustDump();

    }

    private void JustDump() {


        DatabaseOperations databaseOperations = new DatabaseOperations(this);
        Cursor cursor = databaseOperations.SelectAllData(databaseOperations,userID);

        Log.d("Sala",DatabaseUtils.dumpCursorToString(cursor));


    }

    private void initErDatas() {

        // get all the pass datas from network...

//        if(CheckNetworkInfo()){
//
//          //  String extraParameters =eEmail+"&password="+userPass+"&name="+erName+"&discription="+erDescription+"&from_date="+erFromDate+"&to_date="+erToDate+"&customer_id="+customerID+"&user_id="+userID;
//
//            String BaseUrl ="http://netforce.biz/inti_expense/api/api.php?type=exp_report&email=";
//
//            Log.d("Goooo",BaseUrl);
//
//            Ion.with(this)
//                    .load(BaseUrl)
//                    .asJsonObject()
//                    .setCallback(new FutureCallback<JsonObject>() {
//                        @Override
//                        public void onCompleted(Exception e, JsonObject result) {
//
//                            if(result!=null){
//
//                                String status = result.get("status").getAsString();
//                                String _expenseID=result.get("exp_report_no").getAsString();
//
//                                if(status.equalsIgnoreCase("success")){
//
//
//                                }
//                            }
//
//                        }
//                    });
//
//        }


        DatabaseOperations dop = new DatabaseOperations(this);

        Cursor cursor = dop.getMyExpenseReports(dop,eEmail);

        Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));



            if (cursor.moveToFirst()) {



                do {


                    String erName = null;
                    try {
                        erName = URLDecoder.decode(cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME)),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }



                    // erDescription = URLDecoder.decode(cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_DESCRIPTION)),"UTF-8");

                    String erFromDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_FROM_DATE));
                    String erToDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_TO_DATE));
                    String erID = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_ID));
                    String erStatus = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_STATUS));
                    String erListCurrencyCode = "$";
                    String erAmount = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_TOTAL_COST));



                  //  String erName,erFromDate,erToDate,erCurrencyCode,erListAmount,erListPolicyAmount,erStatus,erID;


                    ExpenseReportData data= new ExpenseReportData(erName,erFromDate,erToDate,erListCurrencyCode,erAmount,"345",erStatus,erID);

                    expenseReportDatas.add(data);




                } while (cursor.moveToNext());
                erAdapter.notifyDataSetChanged();
            }
            cursor.close();


    }

    private boolean CheckNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = cm.getActiveNetworkInfo();

        if(networkinfo!=null && networkinfo.isConnected()==true){

            return true;

        }else {
            // do something...

            return false;
        }
    }

    private void initView() {

        userSessionManager = new UserSessionManager(this);
        if(userSessionManager.checkLogin())
            finish();
        HashMap<String,String> user= userSessionManager.getUserDetails();

        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        userID = user.get(UserSessionManager.KEY_USERID);


        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewEmail.setText(eEmail);

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
        erAdapter = new ExpenseReportAdapter(this, expenseReportDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(erAdapter);
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
