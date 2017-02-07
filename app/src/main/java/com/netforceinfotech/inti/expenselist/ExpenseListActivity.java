package com.netforceinfotech.inti.expenselist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.myprofile.MyProfileActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EXPENSELIST";
    Context context;
    Toolbar toolbar;
    ImageView imageViewFilter, imageViewCloseFilter;
    TextView textViewStatus, eEmailTextView;
    RelativeLayout relativeLayoutFilter;
    private SwipyRefreshLayout swipyRefreshLayout;
    ArrayList<ExpenseListData> expenseListDatas = new ArrayList<ExpenseListData>();
    ExpenseListAdapter adapter;

    UserSessionManager userSessionManager;

    public String eName, eEmail, erID, erName, erFromDate, erToDate, erDescription,customerID,userID,BaseCurrencySymbol;
    int isOnline = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        context = this;

        userSessionManager = new UserSessionManager(this);
        userSessionManager.checkLogin();


        HashMap<String, String> user = userSessionManager.getUserDetails();
        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        customerID = user.get(UserSessionManager.KEY_CUSTOMERID);
        userID = user.get(UserSessionManager.KEY_USERID);
        BaseCurrencySymbol= user.get(UserSessionManager.KEY_USERCURRENCY_SYMBOL);

        Log.d("BASECURRENCYSYMBOL",BaseCurrencySymbol+"");

        try {

            Bundle bundle = getIntent().getExtras();
            erID = bundle.getString("erID");
            isOnline = bundle.getInt("isOnline");

            Log.d("IsOnline", String.valueOf(isOnline));


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        initView();
        setupToolBar(getString(R.string.list));
        setupRecyclerView();
        selectExpenseListDatas();

    }

    private void selectExpenseListDatas() {

        if (isOnline != 1) {

            try {

                DatabaseOperations dop = new DatabaseOperations(this);

                Cursor cursor = dop.getUserElDatas(dop, erID, eEmail);

                Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));


                if (cursor.moveToFirst()) {


                    do {


                        String erId = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.ER_ID));
                        String erListDes = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DESCRIPTION));
                        String erListDate = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DATE));
                        String erListCurrency = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CURRENCY_CODE));
                        String erListID = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_ID));
                        String erListOriginalAmount = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_ORIGINAL_AMOUNT));
                        String erListImageUrl = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_IMAGE_URL));
                        String erListCat = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CATEGORY));

                        int isOnline =0;


                        // String eEmail,userType,erListImageUrl,erID,erListID,erListDes,erListCat,erListAmount,erListCurrency,erListDate;
                        ExpenseListData expenseListData = new ExpenseListData(eEmail, erListImageUrl, erID, erListID, erListDes, erListCat, erListOriginalAmount, BaseCurrencySymbol, erListDate,isOnline);
                        expenseListDatas.add(expenseListData);


//
//                MyData myData = new MyData(id, name, description, date);
//                if (!myDatas.contains(myData)) {
//                    myDatas.add(myData);
//                }
//                     customerField.add(map);
//                     do what ever you want here
                    } while (cursor.moveToNext());

                }
                cursor.close();
                adapter.notifyDataSetChanged();

            } catch (Exception ex) {

                showMessage("Something is wrong.... ");
            }

        } else {

//            get data from the server...

            String BaseUrl ="http://161.202.19.38/inti_expense/api/api.php?type=get_all_expenses&customer_id="+customerID+"&user_id="+userID+"&exp_report_no="+erID+"";

            Log.d("ExpenseListURL",BaseUrl);

            Ion.with(context)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if(result!=null){

                                String status = result.get("status").getAsString();
                                if(status.equalsIgnoreCase("success")){

                                    JsonArray jsonArray =result.getAsJsonArray("data");

                                    if(!jsonArray.isJsonNull()){

                                        for(int i=0; i<jsonArray.size(); i++){

                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                            String erListImageUrl="";
                                            String erListID =jsonObject.get("EXPENSE_NUMBER").getAsString();
                                            String erListDes = jsonObject.get("DESCRIPTION").getAsString();
                                            String erListCat = jsonObject.get("CATEGORY_CLASS").getAsString();
                                           // String erListOriginalAmount=jsonObject.get("FUNCTIONAL_AMOUNT").getAsString();
                                            Double erlistOriginalAmt=jsonObject.get("FUNCTIONAL_AMOUNT").getAsDouble();


                                            String erListOriginalAmount=String.format("%.2f",erlistOriginalAmt);
                                            String erListDate=jsonObject.get("EXPENSE_DATE").getAsString();

                                            int isOnline =1;

                                            // String eEmail,userType,erListImageUrl,erID,erListID,erListDes,erListCat,erListAmount,erListCurrency,erListDate;
                                            ExpenseListData expenseListData = new ExpenseListData(eEmail, erListImageUrl, erID, erListID, erListDes, erListCat, erListOriginalAmount,BaseCurrencySymbol, erListDate,isOnline);
                                            expenseListDatas.add(expenseListData);
                                        }

                                        adapter.notifyDataSetChanged();

                                    }else {

                                        showMessage(getResources().getString(R.string.thereisnodata));
                                    }
                                }

                            }else {

                                showMessage(getResources().getString(R.string.serverError));
                                finish();
                            }


                        }
                    });

        }

    }

    private void initView() {


        eEmailTextView = (TextView) findViewById(R.id.eEmailTextView);
        eEmailTextView.setText(eEmail);
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
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        adapter = new ExpenseListAdapter(context, expenseListDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupFilterDropDown() {


        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imageViewFilter);

        droppyBuilder.addMenuItem(new DroppyMenuItem(getResources().getString(R.string.filterApproved)));
        droppyBuilder.addMenuItem(new DroppyMenuItem(getResources().getString(R.string.filterRejected)));
        droppyBuilder.addMenuItem(new DroppyMenuItem(getResources().getString(R.string.filterPaidOut)));
        droppyBuilder.addMenuItem(new DroppyMenuItem(getResources().getString(R.string.filterInApproval)));


// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                showMessage("Loading ...");
                relativeLayoutFilter.setVisibility(View.VISIBLE);
                textViewStatus.setText("Status " + id);
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
                if (id == 0) {
                    userSessionManager.logoutUser();
                    finish();
                } else if (id == 1) {
                    Intent intent = new Intent(ExpenseListActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fabAddExpenseReport:

                Intent intent = new Intent(ExpenseListActivity.this, TextImageExpenseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("eEmail", eEmail);
                bundle.putString("erID", erID);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

                break;
            case R.id.imageCloseFilter:
                relativeLayoutFilter.setVisibility(View.GONE);
                break;
        }
    }
}
