package com.netforceinfotech.inti.expensesummary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.addexpenses.TextImageExpenseActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expenselist.ExpenseListActivity;
import com.netforceinfotech.inti.history.HistoryActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.util.ArrayList;
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
    final static String TAG="AndroidSQLI Error: ";
    private ArrayList<ExpenseCategoryData> expenseCategoryDatas = new ArrayList<ExpenseCategoryData>();
    private TextView textViewExpenseName, textViewDescriptionDetail, textViewfromDate, textViewtoDate,textViewStatus,textViewEmail;
    String erName, erFromDate, erToDate,erDescription,erID,userType,erStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary);
        context = this;

            try {

                Bundle bundle = getIntent().getExtras();
                eEmail = bundle.getString("eEmail");
                erID = bundle.getString("erID");
                userType = bundle.getString("userType");

                // take expensesID from the server and get data..

                DatabaseOperations dop = new DatabaseOperations(this);

                dop.getListofExpensesCount(dop, eEmail, erID);

                Cursor cursor = dop.getListofExpensesCount(dop, eEmail, erID);
                Log.d(TAG,DatabaseUtils.dumpCursorToString(cursor));


                if (cursor.getCount() > 0) {

                    if (cursor.moveToFirst()) {

                        do {

                            erName = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.TITLE));
                            erDescription = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.ER_DESCRIPTION));
                            erFromDate = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.ERFROMDATE));
                            erToDate = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.ERTODATE));
                            String id = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_ID));
                            erStatus = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.ERSTATUS));
                            String currency= cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.CURRENCY_CODE));
                            String date= cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.CREATEION_DATE));
                            String draft= cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSE_DRAFT));

                            String total= cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.CONVERTED_AMOUNT));

                            String image= cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_IMAGE_URL));



                            Log.d(TAG, erDescription);
                            Log.d(TAG, erFromDate);
                            Log.d(TAG, erToDate);
                            Log.d(TAG, erName);
                            Log.d(TAG,id);
                            Log.d(TAG,currency);
                            Log.d(TAG,draft);
                            Log.d(TAG,total);
                            Log.d(TAG,image);


                        } while (cursor.moveToNext());
                    }
                    cursor.close();


                } else {

//                go to the add list expenses form...

                    Intent intent = new Intent(ExpenseSummaryActivity.this, TextImageExpenseActivity.class);
                    intent.putExtra("erID",erID);
                    intent.putExtra("eEmail",eEmail);
                    intent.putExtra("userType",userType);
                    startActivity(intent);

                }


            } catch (Exception ex) {

                ex.fillInStackTrace();

                Log.d("ERROR", String.valueOf(ex));
            }


        if (eEmail.equalsIgnoreCase("3")) {
                supervisorFlag = "3";
            }


        initView();
        setupToolBar(getString(R.string.report));

        setupRecycler();

       SelectExpensesCategoryData();


        //selectDummayDAta();
    }

    private void selectDummayDAta() {


        try{

            DatabaseOperations dop = new DatabaseOperations(this);
            dop.SelectFromLISTOFANEXPENSETABLE(dop);

            Cursor cursor = dop.SelectFromLISTOFANEXPENSETABLE(dop);



            if (cursor.moveToFirst()) {

                do {
                    String name = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_DESCRIPTION));
                    String date = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EXPENSES_ID));
                    String email = cursor.getString(cursor.getColumnIndex(TableData.ListofAnExpensesTable.EMPLOYEE_EMAIL));

                    showMessage("Hello Tashi"+name+"there is somethingfor your life"+email);


                    Log.d(TAG, "eName"+name+"eEIDeDescription"+description+"date"+ date+"email address"+email);


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


        }catch (Exception ex){


            ex.fillInStackTrace();


            showMessage("Error"+ex);

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


        relativeLayoutSuper = (RelativeLayout) findViewById(R.id.relativeLayoutSuper);
        if (!supervisorFlag.equalsIgnoreCase("3")) {
            relativeLayoutSuper.setVisibility(View.GONE);
        }
        findViewById(R.id.buttonListExpenses).setOnClickListener(this);
        imageViewList = (ImageView) findViewById(R.id.imageViewList);
        setupListMenu(imageViewList);

        textViewExpenseName.setText(erName);
        textViewDescriptionDetail.setText(erDescription);
        textViewfromDate.setText(erFromDate);
        textViewtoDate.setText(erToDate);
        textViewStatus.setText(erStatus);

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
                if (id == 4) {
                    Intent intent = new Intent(context, HistoryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonListExpenses:

                intent = new Intent(context, ExpenseListActivity.class);
                Bundle bundle = getIntent().getExtras();
                String eEmail = bundle.getString("eEmail");
                String erID = bundle.getString("erID");
                String erListID = bundle.getString("erListID");
                intent.putExtra("eEmail",eEmail);
                intent.putExtra("erID",erID);
                intent.putExtra("erListID",erListID);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.imageViewList:
                intent = new Intent(context, CreateExpenseActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    private void SelectExpensesCategoryData() {


        ExpenseCategoryData data = new ExpenseCategoryData("Cate1", "Accomodation", "Dollar", "Dol", "250", "200");

        expenseCategoryDatas.add(data);
        data = new ExpenseCategoryData("Cate2", "Transportation", "Dollar", "Dol", "100", "200");
        expenseCategoryDatas.add(data);
        data = new ExpenseCategoryData("Cate1", "Food Allowance", "Dollar", "Dol", "300", "200");
        expenseCategoryDatas.add(data);
        data = new ExpenseCategoryData("Cate1", "Other", "Dollar", "Dol", "100", "200");
        expenseCategoryDatas.add(data);
        adapter.notifyDataSetChanged();



    }
}
