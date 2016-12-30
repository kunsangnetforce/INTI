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

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.util.ArrayList;

public class ExpenseListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="EXPENSELIST" ;
    Context context;
    Toolbar toolbar;
    ImageView imageViewFilter, imageViewCloseFilter;
    TextView textViewStatus;
    RelativeLayout relativeLayoutFilter;
    private SwipyRefreshLayout swipyRefreshLayout;
    ArrayList<ExpenseListData> expenseListDatas = new ArrayList<ExpenseListData>();
    ExpenseListAdapter adapter;

    public String eName, eEmail, erID,erName,erFromDate,erToDate,erDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        context = this;
        try {


            Bundle bundle = getIntent().getExtras();
            eEmail = bundle.getString("eEmail");
            erID= bundle.getString("erID");


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        initView();
        setupToolBar(getString(R.string.list));
        setupRecyclerView();
        selectExpenseListDatas();

    }

    private void selectExpenseListDatas() {


        try{

            DatabaseOperations dop = new DatabaseOperations(this);
            dop.DummDatas(dop,eEmail);
            Cursor cursor =dop.DummDatas(dop,eEmail);
            Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));
            showMessage(DatabaseUtils.dumpCursorToString(cursor));


            if (cursor.moveToFirst()) {


                do {
                    // erlistID,erListDes,erlistDate,currency,originalamount,category,imageurl,erlistCat;

                        String erlistName = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.TITLE));
                        String erListDes = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_DESCRIPTION));
                        String erListDate = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.CREATEION_DATE));
                        String erListCurrency = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.CURRENCY_CODE));
                        String erListID = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.ERLISTID));
                        String erListOriginalAmount = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.ORIGINAL_AMOUNT));
                        String erListImageUrl = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_IMAGE_URL));
                        String erListCat = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_CATEGORY));
                        String userType =cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.USER_TYPE));

                        //String eEmail,userType,erListImageUrl,erID,erListID,erListDes,erListCat,erListAmount,erListCurrency,erListDate;
                        ExpenseListData expenseListData = new ExpenseListData(eEmail,userType,erListImageUrl,erID,erListID,erListDes,erListCat,erListOriginalAmount,erListCurrency,erListDate);
                        expenseListDatas.add(expenseListData);







//                MyData myData = new MyData(id, name, description, date);
//                if (!myDatas.contains(myData)) {
//                    myDatas.add(myData);
//                }
                    // customerField.add(map);
                    // do what ever you want here
                } while (cursor.moveToNext());

            }
            cursor.close();
            adapter.notifyDataSetChanged();

        }catch (Exception ex){

            showMessage("Something is wrong.... ");
        }


//
//
//
//        // String user_id,user_type,img, expensesId, description,expense_date,expense_currency,expenses_amount,expenses_category;
//
//        ExpenseListData expensData = new ExpenseListData("tash11", "3", " null", "expensID12", "this is just a simple tibet", " 12-23-2016", "INR", "300", "travel");
//
//        expenseListDatas.add(expensData);
//
//        expensData = new ExpenseListData("Kunsang1", "3", " null", "expensID32", "this is just a simple india", " 12-3-2016", "INR", "500", "travel");
//
//        expenseListDatas.add(expensData);
//
//        expensData = new ExpenseListData("Thinlay12", "3", " null", "expensID92", "this is just a simple china", " 11-23-2016", "INR", "700", "travel");
//
//        expenseListDatas.add(expensData);
//        expensData = new ExpenseListData("Choephel", "3", " null", "expensID92", "this is just a simple china", " 11-23-2016", "INR", "700", "travel");
//
//        expenseListDatas.add(expensData);
//        expensData = new ExpenseListData("Nyima tashi", "3", " null", "expensID92", "this is just a simple china", " 11-23-2016", "INR", "700", "travel");
//
//        expenseListDatas.add(expensData);
//
//        adapter.notifyDataSetChanged();


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
        adapter = new ExpenseListAdapter(context, expenseListDatas);
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

                Intent intent = new Intent(ExpenseListActivity.this, TextImageExpenseActivity.class);
                intent.putExtra("eEmail",eEmail);
               intent.putExtra("erID",erID);
//
                startActivity(intent);
                break;
            case R.id.imageCloseFilter:
                relativeLayoutFilter.setVisibility(View.GONE);
                break;
        }
    }
}
