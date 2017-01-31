package com.netforceinfotech.inti.expensereport;

import android.app.ProgressDialog;
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

import com.google.gson.JsonArray;
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
import com.netforceinfotech.inti.myprofile.MyProfileActivity;
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
    private static final String TAG = "MyExpenseReport";
    SwipyRefreshLayout swipyRefreshLayout;
    Toolbar toolbar;
    ImageView imageViewCloseFilter, imageViewFilter;
    Context context;
    TextView textViewStatus;
    RelativeLayout relativeLayoutFilter;
    int erStatus = 6;
    ArrayList<ExpenseReportData> expenseReportDatas = new ArrayList<ExpenseReportData>();
    ExpenseReportAdapter erAdapter;
    String eEmail, userID, customerId;
    TextView textViewEmail;
    UserSessionManager userSessionManager;
    DatabaseOperations dop;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_report);
        context = this;

        userSessionManager = new UserSessionManager(this);
        userSessionManager.checkLogin();
        dop = new DatabaseOperations(this);
        try {
            Bundle bundle = getIntent().getExtras();
            erStatus = bundle.getInt("erStatus");


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.dataloading));

        initView();
        setupToolBar(getString(R.string.my_expense_reports));
        setupRecyclerView();
        initErDatas();

    }


    private void initErDatas() {

        switch (erStatus) {
            case 0:
                showErDataByErStatus(0);
                break;
            case 1:
                showErDataByErStatus(1);
                break;
            case 2:
                showErDataByErStatus(2);
                break;
            case 3:
                showErDataByErStatus(3);
                break;
            case 5:
                showErDataByErStatus(5);
                break;
            case 6:
                showErDataByErStatus(6);
                break;


        }


    }


    private void showErDataByErStatus(final int erStatus) {

        switch (erStatus) {
            case 0:
                //  get approved data from the server....
                getApprovedData();
                break;
            case 1:
                getInApprovedData();
                break;
            case 2:
                getRejectedData();
                break;

            case 3:
                getPaidOutData();
                break;

            case 5:
                getOfflineData();
                break;
            case 6:
                getDataWithoutFilter();
                break;


        } // Switch Ends....


    }

    private void getApprovedData() {

        pd.show();
        String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_all_approved&customer_id=" + customerId + "&user_id=" + userID + "";
        Log.d("BaseUrl", BaseUrl);
        Ion.with(context)
                .load(BaseUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            String status = result.get("status").getAsString();

                            if (status.equalsIgnoreCase("success")) {

                                JsonArray jsonArray = result.getAsJsonArray("data");

                                if (jsonArray.size() != 0) {

                                    pd.dismiss();

                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                        String tripdays = jsonObject.get("TRIP_DAYS").getAsString();
                                        String expenseReportId = jsonObject.get("EXPENSE_REPORT_NUMBER").getAsString();
                                        String erFromDate = jsonObject.get("FROM_DATE").getAsString();
                                        String erToDate = jsonObject.get("TO_DATE").getAsString();
                                        String erDescription = jsonObject.get("DESCRIPTION").getAsString();
                                        String erName = jsonObject.get("NAME").getAsString();
                                        String erStatus = jsonObject.get("STATUS").getAsString();
                                        String erCurrencyCode = jsonObject.get("CURRENCY_CODE").getAsString();

                                        ExpenseReportData data = new ExpenseReportData(erName, erFromDate, erToDate, erCurrencyCode, "100", "200", erStatus, expenseReportId, 1);

                                        expenseReportDatas.add(data);


                                    }
                                    erAdapter.notifyDataSetChanged();

                                }
                                pd.dismiss();


                            } else {
                                pd.dismiss();

                                showMessage("somsdfsdaf ");
                            }
                        }
                    }
                });

    }

    private void getInApprovedData() {


        pd.show();
        String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_all_inapproved&customer_id=" + customerId + "&user_id=" + userID + "";
        Log.d("BaseUrl", BaseUrl);
        Ion.with(context)
                .load(BaseUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            String status = result.get("status").getAsString();

                            if (status.equalsIgnoreCase("success")) {

                                JsonArray jsonArray = result.getAsJsonArray("data");

                                if (jsonArray.size() != 0) {

                                    pd.dismiss();

                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                        String tripdays = jsonObject.get("TRIP_DAYS").getAsString();
                                        String expenseReportId = jsonObject.get("EXPENSE_REPORT_NUMBER").getAsString();
                                        String erFromDate = jsonObject.get("FROM_DATE").getAsString();
                                        String erToDate = jsonObject.get("TO_DATE").getAsString();
                                        String erDescription = jsonObject.get("DESCRIPTION").getAsString();
                                        String erName = jsonObject.get("NAME").getAsString();
                                        String erStatus = jsonObject.get("STATUS").getAsString();
                                        String erCurrencyCode = jsonObject.get("CURRENCY_CODE").getAsString();

                                        ExpenseReportData data = new ExpenseReportData(erName, erFromDate, erToDate, erCurrencyCode, "100", "200", erStatus, expenseReportId, 1);

                                        expenseReportDatas.add(data);


                                    }
                                    erAdapter.notifyDataSetChanged();

                                }
                                pd.dismiss();


                            } else {
                                pd.dismiss();

                                showMessage("somsdfsdaf ");
                            }
                        }
                    }
                });


    }

    private void getRejectedData() {


        pd.show();
        String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_all_rejected&customer_id=" + customerId + "&user_id=" + userID + "";
        Log.d("BaseUrl", BaseUrl);
        Ion.with(context)
                .load(BaseUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            String status = result.get("status").getAsString();

                            if (status.equalsIgnoreCase("success")) {

                                JsonArray jsonArray = result.getAsJsonArray("data");

                                if (jsonArray.size() != 0) {

                                    pd.dismiss();

                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                        String tripdays = jsonObject.get("TRIP_DAYS").getAsString();
                                        String expenseReportId = jsonObject.get("EXPENSE_REPORT_NUMBER").getAsString();
                                        String erFromDate = jsonObject.get("FROM_DATE").getAsString();
                                        String erToDate = jsonObject.get("TO_DATE").getAsString();
                                        String erDescription = jsonObject.get("DESCRIPTION").getAsString();
                                        String erName = jsonObject.get("NAME").getAsString();
                                        String erStatus = jsonObject.get("STATUS").getAsString();
                                        String erCurrencyCode = jsonObject.get("CURRENCY_CODE").getAsString();

                                        ExpenseReportData data = new ExpenseReportData(erName, erFromDate, erToDate, erCurrencyCode, "100", "200", erStatus, expenseReportId, 1);

                                        expenseReportDatas.add(data);


                                    }
                                    erAdapter.notifyDataSetChanged();

                                }
                                pd.dismiss();


                            } else {
                                pd.dismiss();

                                showMessage("somsdfsdaf ");
                            }
                        }
                    }
                });


    }

    private void getPaidOutData() {


    }

    private void getOfflineData() {

        expenseReportDatas.clear();

        Cursor cursor = dop.getMyExpenseReports(dop, eEmail);

        Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));


        if (cursor.moveToFirst()) {

            do {


                String erName = null;
                try {
                    erName = URLDecoder.decode(cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME)), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String erFromDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_FROM_DATE));
                String erToDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_TO_DATE));
                String erID = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_ID));
                String erStatuss = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_STATUS));
                String erListCurrencyCode = "$";
                int useridlak = Integer.parseInt(userID);
                int eridlak = Integer.parseInt(erID);

                int totalexpensesAmount = 0;
                int policyAmount = 0;

                Cursor totalexpenseCursor = dop.getTotalAmountByErIdAndUserId(dop, eridlak, useridlak);
                Log.d("TotalExpensecursor", DatabaseUtils.dumpCursorToString(totalexpenseCursor));

                if (totalexpenseCursor.moveToFirst()) {
                    do {

                        totalexpensesAmount = totalexpenseCursor.getInt(totalexpenseCursor.getColumnIndex("totalValue"));


                    } while (totalexpenseCursor.moveToNext());


                }
                totalexpenseCursor.close();

                int customeridlak = Integer.parseInt(customerId);

                Cursor policycursor = dop.getPolicyTotalAmountByErIdAndUserId(dop, customeridlak);
                Log.d("PolicyAmount", DatabaseUtils.dumpCursorToString(policycursor));


                if (policycursor.moveToFirst()) {

                    do {

                        policyAmount = policycursor.getInt(policycursor.getColumnIndex("baselimitTotal"));

                        Log.d("Psadfsdafsad", String.valueOf(policyAmount));


                    } while (policycursor.moveToNext());
                }
                policycursor.close();


                //  String erName,erFromDate,erToDate,erCurrencyCode,erListAmount,erListPolicyAmount,erStatus,erID;

                String totalamountlak = String.valueOf(totalexpensesAmount);
                String policyamountlak = String.valueOf(policyAmount);


                ExpenseReportData data = new ExpenseReportData(erName, erFromDate, erToDate, erListCurrencyCode, totalamountlak, policyamountlak, erStatuss, erID, 0);

                expenseReportDatas.add(data);


            } while (cursor.moveToNext());
            erAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    private void getDataWithoutFilter() {


// fuck you thousand of times...
        getApprovedData();
        getInApprovedData();
        getRejectedData();
        getOfflineData();


    }


    private void initView() {


        HashMap<String, String> user = userSessionManager.getUserDetails();

        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        userID = user.get(UserSessionManager.KEY_USERID);
        customerId = user.get(UserSessionManager.KEY_CUSTOMERID);


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
        switch (erStatus) {
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
                textViewStatus.setText(getString(R.string.offlineReport));
            case 6:
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
                .addMenuItem(new DroppyMenuItem(getString(R.string.paidout)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.offlineReport)));

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
                        textViewStatus.setText(getString(R.string.offlineReport));
                    case 6:
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
                if (id == 0) {
                    userSessionManager.logoutUser();
                    finish();

                } else if (id == 1) {
                    Intent intent = new Intent(MyExpenseReportActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
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
