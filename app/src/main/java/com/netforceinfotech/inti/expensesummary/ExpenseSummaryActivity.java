package com.netforceinfotech.inti.expensesummary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

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
    String erName, erFromDate, erToDate, erDescription, erID, userType, erStatus, userID, customerID, userClass;
    UserSessionManager userSessionManager;
    DatabaseOperations dop;
    ArrayList<String> sortbycategoryid = new ArrayList<>();
    int isOnline = 0;
    int erTotalAmount = 0;
    String userCurrencyCode, userCurrencyCodeSymbol;

    int erStatusInt = 5;

    TextView textViewCurrencySymbol;


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
        userClass = user.get(UserSessionManager.KEY_USERCLASS);
        userCurrencyCode = user.get(UserSessionManager.KEY_USERCURRENCY);
        userCurrencyCodeSymbol = user.get(UserSessionManager.KEY_USERCURRENCY_SYMBOL);


        dop = new DatabaseOperations(this);

        if (userType.equalsIgnoreCase("3")) {
            supervisorFlag = "3";
        }


        initView();

        setupToolBar(getString(R.string.report));

        setupRecycler();


        //selectDummayDAta();


    }

    private void setupTotalAmountValue() {

        if (isOnline != 1) {
            // send the query ....
            try {

                int eridlak = Integer.parseInt(erID);
                int useridlak = Integer.parseInt(userID);


                Cursor cursor = dop.getTotalAmountByErIdAndUserId(dop, eridlak, useridlak);

                Log.d("TOTLLLL", DatabaseUtils.dumpCursorToString(cursor));

                if (cursor.moveToFirst()) {


                    do {

                        erTotalAmount = cursor.getInt(cursor.getColumnIndex("totalValue"));

                        Log.d("SDFSDAFSD", String.valueOf(erTotalAmount));

                    } while (cursor.moveToNext());


                }
                cursor.close();


                totalAmountTextView.setText(erTotalAmount + "");

                Log.d("aSDFsdafsdfsd", totalAmountTextView.getText().toString());


            } catch (Exception ex) {

                ex.fillInStackTrace();
            }


        }


    }

    private void InitDatas() {

        Bundle bundle = getIntent().getExtras();
        erID = bundle.getString("erID");
        isOnline = bundle.getInt("isOnline");

        textViewCurrencySymbol.setText(userCurrencyCodeSymbol);

        Log.d("statusOnline", String.valueOf(isOnline));

        if (isOnline != 1) {

            erStatusInt = 5;

            try {

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

                    // add here....  something...
                    textViewExpenseName.setText(erName);
                    textViewDescriptionDetail.setText(erDescription);
                    textViewfromDate.setText(erFromDate);
                    textViewtoDate.setText(erToDate);
                    textViewStatus.setText(erStatus);


                } else {

//                go to the add list expenses form...
                    Intent intent = new Intent(ExpenseSummaryActivity.this, TextImageExpenseActivity.class);

                    intent.putExtra("erID", erID);
                    intent.putExtra("eEmail", eEmail);
                    intent.putExtra("userType", userType);
                    intent.putExtra("userID", userID);
                    intent.putExtra("isOnline", 0);
                    startActivity(intent);

                }


            } catch (Exception ex) {

                ex.fillInStackTrace();

                Log.d("ERROR", String.valueOf(ex));
            }

        } else {

            String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_data_by_report_num&customer_id=" + customerID + "&user_id=" + userID + "&user_class=" + userClass + "&exp_report_no=" + erID + "";

            Log.d("BASEONLINE", BaseUrl);
            Ion.with(context)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    erFromDate = result.get("from_date").getAsString();
                                    erToDate = result.get("to_date").getAsString();
                                    erName = result.get("name").getAsString();
                                    erDescription = result.get("description").getAsString();
                                    erTotalAmount = result.get("total_expense_amt").getAsInt();

                                    erStatusInt = result.get("er_status").getAsInt();
                                    if (erStatusInt == 0) {
                                        erStatus = getResources().getString(R.string.approved);

                                    } else if (erStatusInt == 1) {

                                        erStatus = getResources().getString(R.string.in_approval);
                                    } else if (erStatusInt == 2) {

                                        erStatus = getResources().getString(R.string.rejected);
                                    } else if (erStatusInt == 3) {

                                        erStatus = getResources().getString(R.string.paidout);
                                    }

                                    // Add Summary Data here..

                                    textViewExpenseName.setText(erName);
                                    textViewDescriptionDetail.setText(erDescription);
                                    textViewfromDate.setText(erFromDate);
                                    textViewtoDate.setText(erToDate);
                                    textViewStatus.setText(erStatus);
                                    totalAmountTextView.setText(erTotalAmount + "");

                                    JsonObject jsonObjectHotel = result.getAsJsonObject("hotel");
                                    if (!jsonObjectHotel.isJsonNull()) {


                                        int hotelAmount = jsonObjectHotel.get("total_hotel_amt").getAsInt();
                                        int hotelPolicyAmount = jsonObjectHotel.get("total_hotel_policy").getAsInt();
                                        if (hotelAmount == 0) {


                                        } else {


                                            String totalamountHotel = String.valueOf(hotelAmount);
                                            String policyAmountHotel = String.valueOf(hotelPolicyAmount);
                                            String catnamm = getResources().getString(R.string.cathotel);
                                            ExpenseCategoryData data = new ExpenseCategoryData("Hotel", catnamm, userCurrencyCode, userCurrencyCodeSymbol, totalamountHotel, policyAmountHotel);
                                            expenseCategoryDatas.add(data);
                                            adapter.notifyDataSetChanged();
                                        }

                                    }

                                    JsonObject jsonObjectMeal = result.getAsJsonObject("meals");
                                    if (!jsonObjectMeal.isJsonNull()) {

                                        int mealAmount = jsonObjectMeal.get("total_meals_amt").getAsInt();
                                        if (mealAmount == 0) {

                                        } else {

                                            String totalamountMeal = String.valueOf(mealAmount);
                                            String policyAmountMeal = jsonObjectMeal.get("total_meals_policy").getAsString();
                                            String catnamm = getResources().getString(R.string.catmeal);

                                            ExpenseCategoryData data = new ExpenseCategoryData("Meal", catnamm, userCurrencyCode, userCurrencyCodeSymbol, totalamountMeal, policyAmountMeal);

                                            expenseCategoryDatas.add(data);
                                            adapter.notifyDataSetChanged();
                                        }

                                    }
                                    JsonObject jsonObjectTransport = result.getAsJsonObject("transport");
                                    if (!jsonObjectTransport.isJsonNull()) {

                                        int transportAmount = jsonObjectTransport.get("total_transport_amt").getAsInt();

                                        if (transportAmount == 0) {

                                        } else {


                                            String totalamountTransport = String.valueOf(transportAmount);
                                            String policyAmountTransport = jsonObjectTransport.get("total_transport_policy").getAsString();
                                            String catnamm = getResources().getString(R.string.cattransport);

                                            ExpenseCategoryData data = new ExpenseCategoryData("Transport", catnamm, userCurrencyCode, userCurrencyCodeSymbol, totalamountTransport, policyAmountTransport);

                                            expenseCategoryDatas.add(data);
                                            adapter.notifyDataSetChanged();

                                        }


                                    }


                                } else if (status.equalsIgnoreCase("failed")) {

                                    int ErrorCode = result.get("Error Code").getAsInt();
                                    if (ErrorCode == 119) {

                                        showMessage(getString(R.string.thereisnodata));

                                        Intent intent = new Intent(ExpenseSummaryActivity.this, TextImageExpenseActivity.class);

                                        intent.putExtra("erID", erID);
                                        intent.putExtra("eEmail", eEmail);
                                        intent.putExtra("userType", userType);
                                        intent.putExtra("userID", userID);
                                        intent.putExtra("isOnline", 1);
                                        startActivity(intent);
                                    }


                                    showMessage(getResources().getString(R.string.tryAgain));
                                    finish();

                                }


                            } else {
                                showMessage(getString(R.string.serverError));
                                finish();
                            }

                        }
                    });


        }

        if (!supervisorFlag.equalsIgnoreCase("3")) {
            relativeLayoutSuper.setVisibility(View.GONE);
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

        textViewCurrencySymbol = (TextView) findViewById(R.id.textViewCurrencySymbol);
        findViewById(R.id.buttonListExpenses).setOnClickListener(this);


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
                        if (erStatusInt == 0 || erStatusInt == 3) {

                            showMessage(getString(R.string.youcantSyn));
                            break;

                        } else {
                            SynDatatoServer();
                            break;

                        }
                    case 1:

                        if (erStatusInt == 0 || erStatusInt == 3) {

                            showMessage(getString(R.string.youcantrequestforApproval));
                            break;

                        } else {
                            RequestApproval();
                            break;

                        }
                    case 2:

                        if (erStatusInt == 0 || erStatusInt == 3) {

                            showMessage(getString(R.string.youcantEdit));
                            break;

                        } else {

                            Intent intent = new Intent(context, EditErActivity.class);
                            intent.putExtra("erID", erID);
                            intent.putExtra("isOnline", isOnline);
                            // start activity for the result...
                            startActivityForResult(intent, 2);
                            //finish();
                            break;
                        }

                    case 3:
                        if (erStatusInt == 0 || erStatusInt == 3) {

                            showMessage(getString(R.string.youcantEditorDelete));
                            break;

                        } else {


                            DeleteExpenseReport(erID);
                            break;

                        }
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
            Log.d("DATDADD", DatabaseUtils.dumpCursorToString(requestCursor));
            if (requestCursor.moveToFirst()) {

                do {

                    erNameS = requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME));
                    erDescriptionS = requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_DESCRIPTION));
                    erFromDateS = requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_FROM_DATE));
                    erToDateS = requestCursor.getString(requestCursor.getColumnIndex(TableData.ExpenseReportTable.ER_TO_DATE));

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
                                        dop.UpdateExpenseReportErServerID(dop, erServerid, erID);

                                        //  http://netforce.biz/inti_expense/api/api.php?type=create_expenses&exp_report_no=10&expense_date=2016-01-12&currency_code=EUR&original_amt=500&exchange_rate=3.5201&functional_amt=4500&category_id=2&discription=test&supplier_id=12&supp_identifire=ZXZ&supplier_optional=zxzx&cost_center=ZAX&document_type=AA&serise=454&doc_number=55&project_code=ASA&type_of_tax=15&invoiceable=Y&customer_id=2&user_id=100&&attribute1=&attribute2=&attribute3=&attribute4=&attribute5=&attribute6=&attribute7=&attribute8=&attribute9=&attribute10=&attribute11=&attribute12=&attribute13=&attribute14=&attribute15=&attribute16=&attribute17=&attribute18=&attribute19=&attribute20=&customer_id=2&user_id=105;

                                        //  Add report Data....


                                        Cursor cursor = dop.getListofExpensesData(dop, erID);

                                        if (cursor.moveToFirst()) {

                                            do {
                                                try {

                                                    //  String imageUrl = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_IMAGE_URL)),"UTF-8");

                                                    String elDate = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DATE)), "UTF-8");
                                                    String elCurrency = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CURRENCY_CODE)), "UTF-8");

                                                    String elOriginalAmount = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_ORIGINAL_AMOUNT)), "UTF-8");
                                                    String elExchangeRate = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_EXCHANGE_RATE)), "UTF-8");

                                                    String elConvertedAmount = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CONVERTED_AMOUNT)), "UTF-8");
                                                    String elDescription = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DESCRIPTION)), "UTF-8");
                                                    String elCategory = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CATEGORY)), "UTF-8");

                                                    String elSupplier = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SUPPLIER)), "UTF-8");
                                                    String elSupplierIdentifier = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SUPPLIER_NAME)), "UTF-8");
                                                    String elSupplierName = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SUPPLIER_NAME)), "UTF-8");
                                                    String elCostCenter = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_COST_CENTER)), "UTF-8");

                                                    String elDoctype = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DOCUMENT_TYPE)), "UTF-8");
                                                    String elSeries = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SERIES)), "UTF-8");
                                                    String elNoDocs = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_NUMBER_OF_DOCS)), "UTF-8");
                                                    String elProject = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_PROJECT)), "UTF-8");
                                                    String elTaxRate = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_TAX_RATE)), "UTF-8");
                                                    String elTaxtAmount = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_TAX_AMOUNT)), "UTF-8");
                                                    String elCheckAble = URLEncoder.encode(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_BILLABLE)), "UTF-8");
                                                    //String imageUrl = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.));
                                                    //
                                                    // String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=create_expenses&exp_report_no=" + erServerid + "&expense_date=" + elDate + "&currency_code=" + elCurrency + "&original_amt=" + elOriginalAmount + "&exchange_rate=" + elExchangeRate + "&functional_amt=" + elConvertedAmount + "&category_id=" + elCategory + "&discription=" + elDescription + "&supplier_id=" + elSupplier + "&supp_identifire=" + elSupplierIdentifier + "&supplier_optional=" + elSupplierName + "&cost_center=" + elCostCenter + "&document_type=" + elDoctype + "&serise=" + elSeries + "&doc_number=" + elNoDocs + "&project_code=" + elProject + "&type_of_tax=" + elTaxRate + "&invoiceable=" + elCheckAble + "&customer_id=" + customerID + "&user_id=" + userID + "&&attribute1=&attribute2=&attribute3=&attribute4=&attribute5=&attribute6=&attribute7=&attribute8=&attribute9=&attribute10=&attribute11=&attribute12=&attribute13=&attribute14=&attribute15=&attribute16=&attribute17=&attribute18=&attribute19=&attribute20=";
                                                    String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=create_expenses&exp_report_no=" + erServerid + "&expense_date=" + elDate + "&currency_code=" + elCurrency + "&original_amt=" + elOriginalAmount + "&exchange_rate=" + elExchangeRate + "&functional_amt=" + elConvertedAmount + "&functional_tax_amt=" + 230 + "&category_id=" + elCategory + "&discription=" + elDescription + "&supplier_id=" + elSupplier + "&supp_identifire=" + elSupplierIdentifier + "&supplier_optional=" + elSupplierName + "&cost_center=" + elCostCenter + "&document_type=" + elDoctype + "&serise=" + elSeries + "&doc_number=" + elNoDocs + "&project_code=" + elProject + "&type_of_tax=" + elTaxRate + "&tax_amt=" + elTaxtAmount + "&invoiceable=" + elCheckAble + "&customer_id=" + customerID + "&user_id=" + userID + "&&attribute1=&attribute2=&attribute3=&attribute4=&attribute5=&attribute6=&attribute7=&attribute8=&attribute9=&attribute10=&attribute11=&attribute12=&attribute13=&attribute14=&attribute15=&attribute16=&attribute17=&attribute18=&attribute19=&attribute20=";

                                                    Log.d("EXPEURL", BaseUrl);
                                                    Ion.with(context)
                                                            .load(BaseUrl)
                                                            .asJsonObject()
                                                            .setCallback(new FutureCallback<JsonObject>() {
                                                                @Override
                                                                public void onCompleted(Exception e, JsonObject result) {

                                                                    if (result != null) {

                                                                        Log.d("TADFADSF", String.valueOf(result));
                                                                    } else {

                                                                        showMessage("There is something gooooo");
                                                                    }


                                                                }
                                                            });

                                                } catch (UnsupportedEncodingException e1) {
                                                    e1.printStackTrace();
                                                }

                                            } while (cursor.moveToNext());

                                        }
                                        cursor.close();


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
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
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
                intent.putExtra("isOnline", isOnline);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }
    }

    private void SelectExpensesCategoryData() {


        sortbycategoryid.clear();
        expenseCategoryDatas.clear();

        int sum = 0;
        String catnamm = null;
        String BaseCurrency;
        int base_limit = 0;

        if (isOnline != 1) {


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
                    ExpenseCategoryData data = new ExpenseCategoryData(categoryidlak, catnamm, userCurrencyCode, userCurrencyCodeSymbol, ssss, bbbbb);

                    expenseCategoryDatas.add(data);


                }

                adapter.notifyDataSetChanged();


            } catch (Exception ex) {

                ex.fillInStackTrace();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {


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

        setupListMenu(imageViewList);
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
