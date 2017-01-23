package com.netforceinfotech.inti.expensesummary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expenselist.ExpenseListActivity;
import com.netforceinfotech.inti.expensereport.MyExpenseReportActivity;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.history.HistoryActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ExpenseSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    Toolbar toolbar;
    ImageView imageViewList;
    private Intent intent;
    RelativeLayout relativeLayoutSuper;
    String supervisorFlag = "4";
    public String eEmail;
    ExpenseCategoryAdapter adapter;
    final static String TAG = "AndroidSQLI Error: ";
    private ArrayList<ExpenseCategoryData> expenseCategoryDatas = new ArrayList<ExpenseCategoryData>();
    TextView textViewExpenseName, textViewDescriptionDetail, textViewfromDate, textViewtoDate, textViewStatus, textViewEmail, totalAmountTextView;
    String erName, erFromDate, erToDate, erDescription, erID, userType, erStatus, userID, customerID;
    UserSessionManager userSessionManager;
    DatabaseOperations dop;
    ArrayList<String> sortbycategoryid = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary);
        context = this;
        userSessionManager = new UserSessionManager(this);
        userSessionManager.checkLogin();

        HashMap<String, String> user = userSessionManager.getUserDetails();
        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        userType = user.get(UserSessionManager.KEY_USERTYPE);
        userID = user.get(UserSessionManager.KEY_USERID);
        customerID = user.get(UserSessionManager.KEY_CUSTOMERID);

        dop = new DatabaseOperations(this);

        if (eEmail.equalsIgnoreCase("3")) {
            supervisorFlag = "3";
        }


        initView();

        setupToolBar(getString(R.string.report));

        setupRecycler();


        //selectDummayDAta();


    }

    private void setupTotalAmountValue() {

        // send the query ....
        try {

            int eridlak = Integer.parseInt(erID);
            int useridlak = Integer.parseInt(userID);

            int totalamountlak = 0;

            Cursor cursor = dop.getTotalAmountByErIdAndUserId(dop, eridlak, useridlak);

            Log.d("TOTLLLL", DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {


                do {

                    totalamountlak = cursor.getInt(cursor.getColumnIndex("totalValue"));

                    Log.d("SDFSDAFSD", String.valueOf(totalamountlak));

                } while (cursor.moveToNext());


            }
            cursor.close();


            totalAmountTextView.setText(totalamountlak + "");

            Log.d("aSDFsdafsdfsd", totalAmountTextView.getText().toString());


        } catch (Exception ex) {

            ex.fillInStackTrace();
        }


    }

    private void InitDatas() {

        try {

            Bundle bundle = getIntent().getExtras();
            erID = bundle.getString("erID");

            // take expensesID from the server and get data..

            DatabaseOperations dop = new DatabaseOperations(this);

            // check if this is the first time....

            Cursor checkcursor = dop.CheckIsFirstTimeSummary(dop, erID);

            //dop.getListofExpensesCount(dop, eEmail, erID);

            Cursor cursor = dop.SelectAllDataSummary(dop, erID);

            Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));


            if (checkcursor.getCount() > 0) {

                if (cursor.moveToFirst()) {

                    do {


                        // erName = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME));
                        erName = URLDecoder.decode(cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME)), "UTF-8");
                        erDescription = URLDecoder.decode(cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_DESCRIPTION)), "UTF-8");
                        erFromDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_FROM_DATE));
                        erToDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_TO_DATE));
                        String id = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_ID));
                        erStatus = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_STATUS));
                        String currency = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CURRENCY_CODE));
                        // String date= cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.CREATEION_DATE));
                        //String draft= cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EXPENSE_DRAFT));
                        String total = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CONVERTED_AMOUNT));
                        String image = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_IMAGE_URL));

                        showMessage(currency);
                        showMessage(erName);


                    } while (cursor.moveToNext());
                }
                cursor.close();


            } else {

//                go to the add list expenses form...
                Intent intent = new Intent(ExpenseSummaryActivity.this, TextImageExpenseActivity.class);

                intent.putExtra("erID", erID);
                intent.putExtra("eEmail", eEmail);
                intent.putExtra("userType", userType);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }


        } catch (Exception ex) {

            ex.fillInStackTrace();

            Log.d("ERROR", String.valueOf(ex));
        }

        if (!supervisorFlag.equalsIgnoreCase("3")) {
            relativeLayoutSuper.setVisibility(View.GONE);
        }


        textViewExpenseName.setText(erName);
        textViewDescriptionDetail.setText(erDescription);
        textViewfromDate.setText(erFromDate);
        textViewtoDate.setText(erToDate);
        textViewStatus.setText(erStatus);
    }

    private void selectDummayDAta() {


        try {


            dop.SelectFromLISTOFANEXPENSETABLE(dop);

            Cursor cursor = dop.SelectFromLISTOFANEXPENSETABLE(dop);


            if (cursor.moveToFirst()) {

                do {
                    // String name = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.TITLE));
//                    String description = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_DESCRIPTION));
//                    String date = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_ID));
//                    String email = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EMPLOYEE_EMAIL));

                    //  showMessage("Hello Tashi"+name+"there is somethingfor your life"+email);


                    //  Log.d(TAG, "eName"+name+"eEIDeDescription"+description+"date"+ date+"email address"+email);


//                MyData myData = new MyData(id, name, description, date);
//                if (!myDatas.contains(myData)) {
//                    myDatas.add(myData);
//                }
                    // customerField.add(map);
                    // do what ever you want here
                } while (cursor.moveToNext());
            }
            cursor.close();
//        myAdapter.notifyDataSetChanged();


        } catch (Exception ex) {


            ex.fillInStackTrace();


            showMessage("Error" + ex);

            Log.d("ERROR", String.valueOf(ex));
        }


    }


    private void initView() {


        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewEmail = (TextView) findViewById(R.id.eEmailTextView);
        textViewEmail.setText(eEmail);

        textViewExpenseName = (TextView) findViewById(R.id.textViewExpenseName);
        textViewDescriptionDetail = (TextView) findViewById(R.id.textViewDescriptionDetails);
        textViewfromDate = (TextView) findViewById(R.id.textViewDateFrom);
        textViewtoDate = (TextView) findViewById(R.id.textViewDateTo);
        imageViewList = (ImageView) findViewById(R.id.imageViewList);
        findViewById(R.id.buttonListExpenses).setOnClickListener(this);
        setupListMenu(imageViewList);

        totalAmountTextView = (TextView) findViewById(R.id.totalAmountTextView);

        relativeLayoutSuper = (RelativeLayout) findViewById(R.id.relativeLayoutSuper);


    }

    private void setupListMenu(ImageView imageViewList) {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imageViewList);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.sync)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.request_approval)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.edit)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.delete)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.view_history)));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                switch (id) {
                    case 0:
                        SynDatatoServer();
                        break;
                    case 1:
                        RequestApproval();
                        break;
                    case 2:
                        Intent intent = new Intent(context, EditErActivity.class);
                        intent.putExtra("erID", erID);

                        // start activity for the result...
                        startActivityForResult(intent, 2);
                        //finish();
                        break;
                    case 3:
                        DeleteExpenseReport(erID);
                        break;
                    case 4:

                        Intent intentHistory = new Intent(context, HistoryActivity.class);
                        startActivity(intentHistory);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;


                }

            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void RequestApproval() {
    }

    private void SynDatatoServer() {

        String BaseUrl = getResources().getString(R.string.baseUrl);
        String erNameS;
        String erDescriptionS;
        String erFromDateS;
        String erToDateS;
        try {

            final Cursor requestCursor = dop.getExpenseReportByErID(dop, erID);
            Log.d("DATDADD",DatabaseUtils.dumpCursorToString(requestCursor));
            if (requestCursor.moveToFirst()) {

                do {

                    erNameS=requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME));
                    erDescriptionS =requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_DESCRIPTION));
                    erFromDateS =requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_FROM_DATE));
                    erToDateS =requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_TO_DATE));

                    String parameters = "exp_report&name=" + erNameS + "&discription=" + erDescriptionS + "&from_date=" + erFromDateS + "&to_date=" + erToDateS + "&customer_id=" + customerID + "&user_id=" + userID + "";
                    String MainUrl = BaseUrl + parameters;
                    Log.d("URLLAK", MainUrl);

                    Ion.with(this)
                            .load(MainUrl)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    Log.d("REsullllt", String.valueOf(result));

                                    if (result != null) {


                                        String status = result.get("status").getAsString();
                                        int erServerid = result.get("exp_report_no").getAsInt();
                                        dop.UpdateExpenseReportErServerID(dop,erServerid,erID);


                                    }

                                }
                            });


                } while (requestCursor.moveToNext());


            }
            requestCursor.close();


        } catch (SQLiteException ex) {
            ex.fillInStackTrace();

        }


    }

    private void setupRecycler() {


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        adapter = new ExpenseCategoryAdapter(context, expenseCategoryDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

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
                Intent intent = new Intent(ExpenseSummaryActivity.this, DashboardActivity.class);
                startActivity(intent);
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
                }
                if (id == 1) {

                    showMessage("my Profile clicked");
                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonListExpenses:

                intent = new Intent(context, ExpenseListActivity.class);
                Bundle bundle = getIntent().getExtras();
                String erListID = bundle.getString("erListID");
                intent.putExtra("erID", erID);
                intent.putExtra("erListID", erListID);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.imageViewList:
                intent = new Intent(context, CreateExpenseActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    private void SelectExpensesCategoryData() {


        sortbycategoryid.clear();

        int sum = 0;
        String catnamm = null;
        String BaseCurrency;
        int base_limit = 0;

        try {

            //        Get data by category wise....
            int userlak = Integer.parseInt(userID);

            Cursor cursor = dop.getCategoryDataByUserId(dop, userlak, erID);
            Log.d("SUmm", DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {

                do {

                    String catid = cursor.getString(cursor.getColumnIndex(TableData.SummaryTable.CATEGORY_ID));
                    if (!sortbycategoryid.contains(catid)) {
                        sortbycategoryid.add(catid);
                    }


                } while (cursor.moveToNext());

                // For loop for the case....


            }
            cursor.close();

            for (int i = 0; i < sortbycategoryid.size(); i++) {

                sum = 0;

                String categoryidlak = sortbycategoryid.get(i);
                Log.d("CATeGORy", String.valueOf(sortbycategoryid));

                Log.d("OMG", categoryidlak);
                Cursor summarycursor = dop.getSummaryByCatId(dop, categoryidlak, erID);
                Log.d("Summalal", DatabaseUtils.dumpCursorToString(summarycursor));

                if (summarycursor.moveToFirst()) {


                    do {

                        int value = summarycursor.getInt(summarycursor.getColumnIndex(TableData.SummaryTable.EL_CONVERTED_AMOUNT));
                        catnamm = summarycursor.getString(summarycursor.getColumnIndex(TableData.SummaryTable.CATEGORY_NAME));


                        Log.d("VAdd", String.valueOf(value));

                        sum = sum + value;
                        Log.d("Summddd", String.valueOf(sum));


                    } while (summarycursor.moveToNext());


                }
                summarycursor.close();

                Log.d("SummTotal", String.valueOf(sum));

                int customeridlak = Integer.parseInt(customerID);

                Cursor cursorbaselimit = dop.getBaseLimitByCatIdAndCustomerIdFromCategory(dop, categoryidlak, customeridlak);
                Log.d("BASE_LIMIT", DatabaseUtils.dumpCursorToString(cursorbaselimit));

                if (cursorbaselimit.moveToFirst()) {

                    do {
                        base_limit = cursorbaselimit.getInt(cursorbaselimit.getColumnIndex(TableData.CategoryTable.BASE_LIMIT));


                    } while (cursorbaselimit.moveToNext());

                }
                cursorbaselimit.close();

                //String categoryId, categoryName, currencyCode, currencySymbol, totalamount, policyamount;

                String ssss = String.valueOf(sum);
                String bbbbb = String.valueOf(base_limit);

//             Add data to the server side...
                ExpenseCategoryData data = new ExpenseCategoryData(categoryidlak, catnamm, "$", "Dol", ssss, bbbbb);

                expenseCategoryDatas.add(data);


            }

            adapter.notifyDataSetChanged();


        } catch (Exception ex) {

            ex.fillInStackTrace();
        }

//
//        ExpenseCategoryData data = new ExpenseCategoryData("Cate1", "Accomodation", "Dollar", "Dol", "250", "200");
//
//        expenseCategoryDatas.add(data);
//        data = new ExpenseCategoryData("Cate2", "Transportation", "Dollar", "Dol", "100", "200");
//        expenseCategoryDatas.add(data);
//        data = new ExpenseCategoryData("Cate1", "Food Allowance", "Dollar", "Dol", "300", "200");
//        expenseCategoryDatas.add(data);
//        data = new ExpenseCategoryData("Cate1", "Other", "Dollar", "Dol", "100", "200");
//        expenseCategoryDatas.add(data);
//        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {


            String Name = data.getStringExtra("erName");
            String description = data.getStringExtra("erDescription");
            String fromDate = data.getStringExtra("erFromDate");
            String toDate = data.getStringExtra("erToDate");
            //String erID =data.getStringExtra("erID");

            Log.d(TAG, "Name" + Name);
            Log.d(TAG, "Desc" + description);
            Log.d(TAG, "FromDate" + fromDate);
            Log.d(TAG, "ToDate" + toDate);
            Log.d(TAG, "ToDate" + erID);

//            DatabaseOperations databaseOperations = new DatabaseOperations(this);
//
//            databaseOperations.UpdateExpenseReport(databaseOperations,Name,fromDate,toDate,description,erID);
//
//            Cursor cursor =databaseOperations.SelectFromErTable(databaseOperations);
//
//            Log.d(TAG,DatabaseUtils.dumpCursorToString(cursor));

            // let try///


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        InitDatas();
        setupTotalAmountValue();
        SelectExpensesCategoryData();


    }


    private void DeleteExpenseReport(String erId) {

        DatabaseOperations dop = new DatabaseOperations(this);

        dop.DeleteData(dop, erId);

        Intent intent = new Intent(this, MyExpenseReportActivity.class);
        startActivity(intent);
        finish();
    }
}
