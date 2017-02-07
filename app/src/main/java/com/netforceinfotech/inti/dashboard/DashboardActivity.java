package com.netforceinfotech.inti.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.expenselist.ExpenseListActivity;
import com.netforceinfotech.inti.expensereport.MyExpenseReportActivity;
import com.netforceinfotech.inti.general.ConnectivityCheck;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.myprofile.MyProfileActivity;
import com.netforceinfotech.inti.supervisor_expensereport.SupervisorExpenseReportActivity;
import com.netforceinfotech.inti.util.CurrencyClass;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DashboardActivity extends AppCompatActivity implements PieChartOnValueSelectListener, View.OnClickListener {
    private static final String TAG = "Dash";
    PieChartView pieChartView;
    private PieChartData data;
    Context context;
    Toolbar toolbar;
    Intent intent;
    LinearLayout linearLayoutAssigned;
    String supervisorFlag = "5";

    Bundle bundle;
    public String eEmail, userType, userID, customerID;
    TextView eEmailTextView;
    UserSessionManager sessionManager;
    DatabaseOperations dop;
    MaterialDialog materialDialog;
    ConnectivityCheck connectivityCheck;
    TextView textViewInApproval, textViewApproved, textViewOffline, textViewRejected, textViewPaidout;
    String ApprovedPercentage,InApprovedPercentage,RejectedPercentage,PaidOutPercentage,PendingPercentage,OfflinePercentage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        sessionManager = new UserSessionManager(getApplicationContext());
        sessionManager.checkLogin();
        dop = new DatabaseOperations(this);
        connectivityCheck = new ConnectivityCheck(this);


        setDatas();

        setupToolBar(getString(R.string.dashboard));
        initView();
        InitDashboardData();
        initGraph();

        AddCategoryData();
        AddCurrencyData();
        AddSupervisorData();
        AddSupplierData();
        AddSupplierDetailData();
        AddCostCenterData();
        AddDocTypeData();
        AddProjectData();
        AddTaxData();








    }

    private void InitDashboardData() {

        if (connectivityCheck.isOnline()) {

            //
            getUserApprovedData();
            getUserInApprovalData();
            getUserRejectedData();
            getUserPaidData();
            getUserOfflineData();

            if (!userType.equalsIgnoreCase("4")) {

                getUserPendingData();

            }


        } else {

            showMessage(getString(R.string.nointernetconnection));

        }


    }

    private void getUserPendingData() {


    }

    private void getUserOfflineData() {

        Cursor cursor = dop.getMyExpenseReports(dop, eEmail);
        int erCount = cursor.getCount();

        if (erCount > 0) {

            OfflinePercentage = String.valueOf(erCount);

            textViewOffline.setText(OfflinePercentage);

        }


    }

    private void getUserPaidData() {


        try {


            String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_paidout_percentage&customer_id=" + customerID + "&user_id=" + userID + "";

            Ion.with(this)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                     PaidOutPercentage = result.get("paidout_percentage").getAsString();
                                    textViewRejected.setText(PaidOutPercentage + "%");


                                } else {

                                    showMessage(getResources().getString(R.string.nouserexist));

                                }
                            }

                        }
                    });

        } catch (Exception ex) {

            ex.fillInStackTrace();
        }


    }

    private void getUserRejectedData() {




            String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_rejected_percentage&customer_id=" + customerID + "&user_id=" + userID + "";

            Ion.with(this)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                  RejectedPercentage = result.get("rejected_percentage").getAsString();
                                    textViewRejected.setText(RejectedPercentage + "%");


                                } else {

                                    showMessage(getResources().getString(R.string.nouserexist));

                                }
                            }

                        }
                    });


    }

    private void getUserInApprovalData() {

        String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_inapproval_percentage&customer_id=" + customerID + "&user_id=" + userID + "";

        Ion.with(this)
                .load(BaseUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            String status = result.get("status").getAsString();

                            if (status.equalsIgnoreCase("success")) {

                                InApprovedPercentage = result.get("inapproval_percentage").getAsString();
                                textViewInApproval.setText(InApprovedPercentage + "%");


                            } else {

                                showMessage(getResources().getString(R.string.nouserexist));

                            }
                        }

                    }
                });


    }


    private void getUserApprovedData() {


        String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_approved_percentage&customer_id=" + customerID + "&user_id=" + userID + "";

        Ion.with(this)
                .load(BaseUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            String status = result.get("status").getAsString();

                            if (status.equalsIgnoreCase("success")) {

                                 ApprovedPercentage = result.get("approval_percentage").getAsString();
                                textViewApproved.setText(ApprovedPercentage + "%");


                            } else {

                                showMessage(getResources().getString(R.string.nouserexist));

                            }
                        }

                    }
                });


    }

    private void AddTaxData() {


        try {

            String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_tax_name&customer_id=" + customerID;

            // Add Category
            Ion.with(this)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    JsonArray jsonArray = result.getAsJsonArray("data");
                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        try {

                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            String taxrate = jsonObject.get("TAX_RATE").getAsString();
                                            String taxname = jsonObject.get("TAX_RATE_NAME").getAsString();
                                            int taxid = jsonObject.get("id").getAsInt();
                                            int customeridlak = Integer.parseInt(customerID);

                                            dop.AddTax(dop, taxid, customeridlak, taxrate, taxname);

                                            Cursor cursor = dop.getTax(dop);
                                            Log.d("TAX", DatabaseUtils.dumpCursorToString(cursor));


                                        } catch (Exception ex) {


                                        }

                                    }


                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");
                                }

                            } else {

                                showMessage("there may have some server error.");
                            }

                        }
                    });


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    private void AddProjectData() {
        try {

            // Add Project Table...

            String BaseUrl6 = "http://161.202.19.38/inti_expense/api/api.php?type=get_project&customer_id=" + customerID;

            // Add Category
            Ion.with(this)
                    .load(BaseUrl6)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    JsonArray jsonArray = result.getAsJsonArray("data");
                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        try {


                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            // there is some error int eh segment data....

                                            String projectName = jsonObject.get("SEGMENT_NAME").getAsString();
                                            int projectid = jsonObject.get("id").getAsInt();


                                            int customeridlak = Integer.parseInt(customerID);

                                            dop.AddProject(dop, projectid, customeridlak, projectName);

                                            Cursor cursor = dop.getProject(dop);
                                            Log.d("Project", DatabaseUtils.dumpCursorToString(cursor));


                                        } catch (Exception ex) {


                                        }

                                    }


                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");
                                }

                            } else {

                                showMessage("there may have some server error.");
                            }

                        }
                    });


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    private void AddDocTypeData() {

        try {
            // Add Doctype Data....

            String BaseUrl5 = "http://161.202.19.38/inti_expense/api/api.php?type=get_doc_type&customer_id=" + customerID;

            // Add Category
            Ion.with(this)
                    .load(BaseUrl5)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    JsonArray jsonArray = result.getAsJsonArray("data");
                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        try {


                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            String doctypeName = jsonObject.get("DOCUMENT_TYPE_NAME").getAsString();
                                            int docid = jsonObject.get("id").getAsInt();

                                            int useridlak = Integer.parseInt(userID);
                                            int customeridlak = Integer.parseInt(customerID);

                                            dop.AddDoctype(dop, docid, customeridlak, doctypeName);

                                            Cursor cursor = dop.getDoctype(dop);
                                            Log.d("DocType", DatabaseUtils.dumpCursorToString(cursor));


                                        } catch (Exception ex) {


                                        }

                                    }


                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");
                                }

                            } else {

                                showMessage("there may have some server error.");
                            }

                        }
                    });


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    private void AddCostCenterData() {

        // Add Cost Center Data....
        try {

            String BaseUrl3 = "http://161.202.19.38/inti_expense/api/api.php?type=get_cost_center&customer_id=" + customerID;

            Ion.with(this)
                    .load(BaseUrl3)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    JsonArray jsonArray = result.getAsJsonArray("data");
                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        try {


                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            // segment value must be equal to the cost center name....
                                            String costcentername = jsonObject.get("SEGMENT_VALUE").getAsString();
                                            int costid = jsonObject.get("id").getAsInt();


                                            int customeridlak = Integer.parseInt(customerID);

                                            dop.AddCostCenter(dop, costid, customeridlak, costcentername);

                                            Cursor cursor = dop.getCostCenter(dop);
                                            Log.d("CostCenter", DatabaseUtils.dumpCursorToString(cursor));


                                        } catch (Exception ex) {


                                        }

                                    }

                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");
                                }

                            } else {

                                showMessage("there may have some server error.");
                            }

                        }
                    });


        } catch (Exception ex) {

            ex.fillInStackTrace();
        }
    }

    private void AddSupplierDetailData() {

        // Add the supplier Detail datas here...

        try {

            String BaseUrl7 = "http://161.202.19.38/inti_expense/api/api.php?type=get_supplier&customer_id=" + customerID;

            // Add Category
            Ion.with(this)
                    .load(BaseUrl7)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    JsonArray jsonArray = result.getAsJsonArray("data");

                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        try {


                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            // there is some error int eh segment data....

                                            String suppliername = jsonObject.get("VENDOR_NAME").getAsString();
                                            String supplieridentifier = jsonObject.get("VENDOR_ERP_IDENTIFIER").getAsString();
                                            String supplierid = jsonObject.get("SUPPLIER_ID").getAsString();


                                            int supplieridlak = Integer.parseInt(supplierid);
                                            int customeridlak = Integer.parseInt(customerID);

                                            dop.AddSupplierDetail(dop, customeridlak, supplieridlak, supplieridentifier, suppliername);


                                            Cursor cursor = dop.getSupplierDetails(dop);
                                            Log.d("Supp", DatabaseUtils.dumpCursorToString(cursor));


                                        } catch (Exception ex) {


                                        }

                                    }

                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");
                                }

                            } else {

                                showMessage("there may have some server error.");
                            }

                        }
                    });


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }


    private void AddSupplierData() {
        try {

            // Add the Supplier Data....
            String BaseUrl2 = "http://161.202.19.38/inti_expense/api/api.php?type=get_supplier&customer_id=" + customerID;

            // Add Category
            Ion.with(this)
                    .load(BaseUrl2)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    int useridlak = Integer.parseInt(userID);
                                    int customeridlak = Integer.parseInt(customerID);

                                    JsonArray jsonArray = result.getAsJsonArray("data");


                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        try {
                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            String suppliername = jsonObject.get("VENDOR_NAME").getAsString();
                                            String supplierid = jsonObject.get("SUPPLIER_ID").getAsString();
                                            int supplieridlak = Integer.parseInt(supplierid);
                                            dop.AddSupplier(dop, useridlak, customeridlak, supplieridlak, suppliername);

                                            Cursor cursor = dop.getSupplier(dop);
                                            Log.d("Supp", DatabaseUtils.dumpCursorToString(cursor));

                                        } catch (Exception ex) {

                                        }

                                    }


                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");
                                }

                            } else {

                                showMessage("there may have some server error.");
                            }

                        }
                    });


        } catch (Exception ex) {

            ex.fillInStackTrace();
        }
    }

    private void AddSupervisorData() {

        try {


            // Supervisor data comes here...


        } catch (Exception ex) {

            ex.fillInStackTrace();

        }
    }

    private void AddCurrencyData() {
        // Add All the Currency Data....

        try {

            String BaseUrl1 = "http://161.202.19.38/inti_expense/api/api.php?type=get_currency&customer_id=" + customerID;

            Ion.with(this)
                    .load(BaseUrl1)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    JsonArray jsonArray = result.getAsJsonArray("data");
                                    for (int i = 0; i < jsonArray.size(); i++) {

                                        try {


                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            String currencyName = jsonObject.get("CURRENCY_CODE").getAsString();
                                            int currencyid = jsonObject.get("id").getAsInt();

                                            int useridlak = Integer.parseInt(userID);
                                            int customeridlak = Integer.parseInt(customerID);

                                            dop.AddCurrency(dop, currencyid, useridlak, customeridlak, currencyName);

                                            Cursor cursor = dop.getCurrency(dop);
                                            Log.d("Currency", DatabaseUtils.dumpCursorToString(cursor));


                                        } catch (Exception ex) {


                                        }

                                    }

                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");
                                }

                            } else {

                                showMessage("there may have some server error.");
                            }

                        }
                    });


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

    }

    private void AddCategoryData() {

        // Add the Category Data...
        try {

            String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_category&customer_id=" + customerID;

            Ion.with(this)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    JsonArray jsonArray = result.getAsJsonArray("data");
                                    if (jsonArray.size() > 0) {

                                        for (int i = 0; i < jsonArray.size(); i++) {

                                            try {
                                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                                String catCode = jsonObject.get("CATEGORY_CODE").getAsString();
                                                String catName = jsonObject.get("CATEGORY_NAME").getAsString();
                                                int baselimit = jsonObject.get("BASE_LIMIT").getAsInt();

                                                int id = jsonObject.get("id").getAsInt();
                                                int useridlak = Integer.parseInt(userID);
                                                int customeridlak = Integer.parseInt(customerID);

                                                dop.AddCategory(dop, id, useridlak, customeridlak, catCode, catName, baselimit);

                                                Cursor cursor = dop.getCat(dop);
                                                Log.d("Category", DatabaseUtils.dumpCursorToString(cursor));


                                            } catch (Exception ex) {


                                            }

                                        }
                                    } else {

                                        showMessage(getResources().getString(R.string.thereisnodata));
                                    }


                                } else if (status.equalsIgnoreCase("failed")) {

                                    showMessage(" failed da..");

                                }

                            } else {

                                showMessage(getResources().getString(R.string.serverError));
                            }

                        }
                    });
        } catch (Exception ex) {

            ex.fillInStackTrace();
        }


    }


    private void setDatas() {

        HashMap<String, String> users = sessionManager.getUserDetails();
        eEmail = users.get(UserSessionManager.KEY_EMAIL);
        userType = users.get(UserSessionManager.KEY_USERTYPE);
        userID = users.get(UserSessionManager.KEY_USERID);
        customerID = users.get(UserSessionManager.KEY_CUSTOMERID);

        if (userType.equalsIgnoreCase("3")) {

            supervisorFlag = "3";

        } else if (userType.equalsIgnoreCase("2")) {

            supervisorFlag = "2";

        } else if (userType.equalsIgnoreCase("1")) {

            supervisorFlag = "1";

        } else if (userType.equalsIgnoreCase("4")) {

            supervisorFlag = "4";
        }

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

                if (id == 0) {

                    sessionManager.logoutUser();
                    finish();


                } else if (id == 1) {

                    Intent intent = new Intent(DashboardActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void initView() {

        materialDialog = new MaterialDialog.Builder(this)
                .title(getResources().getString(R.string.pleasewait))
                .content(getResources().getString(R.string.dataloading))
                .build();


        eEmailTextView = (TextView) findViewById(R.id.eEmailTextView);
        eEmailTextView.setText(eEmail);
        textViewInApproval = (TextView) findViewById(R.id.textViewInApproval);
        textViewApproved = (TextView) findViewById(R.id.textViewApproved);
        textViewOffline = (TextView) findViewById(R.id.textViewOffline);
        textViewRejected = (TextView) findViewById(R.id.textViewRejected);
        textViewPaidout = (TextView) findViewById(R.id.textViewPaidout);

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
        findViewById(R.id.relativeLayoutOffline).setOnClickListener(this);
    }

    private void initGraph() {
        pieChartView = (PieChartView) findViewById(R.id.chart);
        generateData();
        pieChartView.setOnValueTouchListener(this);
    }

    private void generateData() {

//        float perct = Float.parseFloat(ApprovedPercentage);


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
                bundle.putInt("erStatus", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;

            case 1:
                showMessage("In Approval method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("erStatus", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case 2:
                showMessage("Rejected method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("erStatus", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case 3:
                showMessage("Paid out method call");
                intent = new Intent(this, MyExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("erStatus", 3);
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

                getUserDataByStatus(0);

                break;
            case R.id.relativeLayoutInApproval:

                getUserDataByStatus(1);


                break;
            case R.id.relativeLayoutRejected:
                showMessage("reject method call");

                getUserDataByStatus(2);

                break;
            case R.id.relativeLayoutPaidout:
                showMessage("paid ouit method call");

                getUserDataByStatus(3);
                break;

            case R.id.relativeLayoutPendingReport:

                intent = new Intent(this, SupervisorExpenseReportActivity.class);
                bundle = new Bundle();
                bundle.putInt("erStatus", 4);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.relativeLayoutOffline:

                getUserDataByStatus(5);
                break;


            case R.id.imageViewList:
                showMessage("List will be shown");
                getUserDataByStatus(6);
                break;

            case R.id.fabAddExpenseReport:
                intent = new Intent(this, CreateExpenseActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }
    }

    private void getUserDataByStatus(final int statusId) {


        ConnectivityCheck Netcheck = new ConnectivityCheck(this);

        if (Netcheck.isOnline()) {

            intent = new Intent(context, MyExpenseReportActivity.class);
            bundle = new Bundle();
            bundle.putInt("erStatus", statusId);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {

            sessionManager.logoutUser();
            finish();
            showMessage(getResources().getString(R.string.nointernetconnection));
        }
    }


}
