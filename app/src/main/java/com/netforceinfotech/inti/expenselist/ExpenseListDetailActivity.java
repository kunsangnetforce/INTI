package com.netforceinfotech.inti.expenselist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expensereport.MyExpenseReportActivity;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.myprofile.MyProfileActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class ExpenseListDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Context context;
    UserSessionManager userSessionManager;
    DatabaseOperations databaseoperations;
    String eEmail, userType;
    int userID, customerID;

    LinearLayout activity_expenselist_detail, linearLayoutDateDetail;
    RelativeLayout relativeLayoutTaxRateDetail, RelativeLayoutDocTypeDetail, costCenterRelativeLayoutDetail, relativeLayoutProviderDetail, relativeLayoutCurrencyDetail, relativeLayoutCategoryMainDetail, relativeLayoutSupplierMainDetail;
    EditText editTextTaxAmountDetail, editTextNumberofDocsDetail, editTextSeriesDetail, editTextSupplierNameDetail, editTextSupplierIdentifierDetail, editTextOriginalAmountDetail, editTextExchangeRateDetail, editTextConvertedAmountDetail, EditTextDescriptionDetail;
    TextView textViewTaxRateDetail, textViewProjectDetail, textViewDocTypeDetail, textViewCostCenterDetail, textViewSupplierDetail, erTitleTextViewDetail, textViewDateDetail, textViewCurrencyCodeDetail, textView3Detail, textViewCategoryDetail;
    ImageView imageViewChooseDetail, imageViewAttachedDetail,imageViewList;
    MaterialRippleLayout rippleDobDetail;
    CheckBox checkboxbillableDetail;
    String erID, elID;
    TextView eEmailTextView;
    String erName;
    int isOnline=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list_detail);
        context = this;
        setupToolBar(getString(R.string.elDetails));
        userSessionManager = new UserSessionManager(this);
        userSessionManager.checkLogin();
        databaseoperations = new DatabaseOperations(this);

        manageDatas();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetID();
        GetReadyAllData();
        InitExpenseTableDatas();

    }

    private void GetID() {

        try {

            Bundle bundle = getIntent().getExtras();
            erID = bundle.getString("erID");
            elID = bundle.getString("elID");
            isOnline =bundle.getInt("isOnline");


        } catch (Exception ex) {

            ex.fillInStackTrace();
        }

    }

    private void InitExpenseTableDatas() {

        try {

            Bundle bundle = getIntent().getExtras();
            erID = bundle.getString("erID");
            int eridlak = Integer.parseInt(erID);

            Cursor cursor = databaseoperations.SelectDatafromExpenseReportTable(databaseoperations, eridlak, eEmail);
            Log.d("ValueWali", DatabaseUtils.dumpCursorToString(cursor));


            if (cursor.moveToFirst()) {


                do {


                    String valueErname = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME));
                    try {

                        erName = URLDecoder.decode(valueErname,"UTF-8");

                    }catch (UnsupportedEncodingException ex){
                        ex.fillInStackTrace();
                    }





                } while (cursor.moveToNext());
            }
            cursor.close();

            erTitleTextViewDetail.setText(erName);


        } catch (Exception ex) {

            Log.d("ERROR", "unable to get Datas" + ex);
        }

    }


    private void initView() {


        imageViewList = (ImageView) findViewById(R.id.imageViewList);
        Glide.with(context).fromResource()
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 100)).load(R.drawable.ic_toggle).into(imageViewList);
        imageViewList.setOnClickListener(this);

        eEmailTextView = (TextView) findViewById(R.id.eEmailTextView);
        eEmailTextView.setText(eEmail);
        checkboxbillableDetail = (CheckBox) findViewById(R.id.checkboxbillableDetail);
        checkboxbillableDetail.setClickable(false);

        rippleDobDetail = (MaterialRippleLayout) findViewById(R.id.rippleDobDetail);
        imageViewChooseDetail = (ImageView) findViewById(R.id.imageViewChooseDetail);
        imageViewAttachedDetail = (ImageView) findViewById(R.id.imageViewAttachedDetail);
        textViewCategoryDetail = (TextView) findViewById(R.id.textViewCategoryDetail);
        textView3Detail = (TextView) findViewById(R.id.textView3Detail);
        textViewCurrencyCodeDetail = (TextView) findViewById(R.id.textViewCurrencyCodeDetail);
        textViewDateDetail = (TextView) findViewById(R.id.textViewDateDetail);
        erTitleTextViewDetail = (TextView) findViewById(R.id.erTitleTextViewDetail);
        textViewSupplierDetail = (TextView) findViewById(R.id.textViewSupplierDetail);

        textViewCostCenterDetail = (TextView) findViewById(R.id.textViewCostCenterDetail);
        textViewDocTypeDetail = (TextView) findViewById(R.id.textViewDocTypeDetail);
        textViewProjectDetail = (TextView) findViewById(R.id.textViewProjectDetail);
        textViewTaxRateDetail = (TextView) findViewById(R.id.textViewTaxRateDetail);
        EditTextDescriptionDetail = (EditText) findViewById(R.id.EditTextDescriptionDetail);
        EditTextDescriptionDetail.setFocusable(false);

        editTextConvertedAmountDetail = (EditText) findViewById(R.id.editTextConvertedAmountDetail);
        editTextConvertedAmountDetail.setFocusable(false);

        editTextExchangeRateDetail = (EditText) findViewById(R.id.editTextExchangeRateDetail);
        editTextExchangeRateDetail.setFocusable(false);

        editTextOriginalAmountDetail = (EditText) findViewById(R.id.editTextOriginalAmountDetail);
        editTextOriginalAmountDetail.setFocusable(false);

        editTextSupplierIdentifierDetail = (EditText) findViewById(R.id.editTextSupplierIdentifierDetail);
        editTextSupplierIdentifierDetail.setFocusable(false);
        editTextSupplierNameDetail = (EditText) findViewById(R.id.editTextSupplierNameDetail);
        editTextSupplierNameDetail.setFocusable(false);

        editTextSeriesDetail = (EditText) findViewById(R.id.editTextSeriesDetail);
        editTextSeriesDetail.setFocusable(false);

        editTextNumberofDocsDetail = (EditText) findViewById(R.id.editTextNumberofDocsDetail);
        editTextNumberofDocsDetail.setFocusable(false);

        editTextTaxAmountDetail = (EditText) findViewById(R.id.editTextTaxAmountDetail);
        editTextTaxAmountDetail.setFocusable(false);
        relativeLayoutSupplierMainDetail = (RelativeLayout) findViewById(R.id.relativeLayoutSupplierMainDetail);
        relativeLayoutCategoryMainDetail = (RelativeLayout) findViewById(R.id.relativeLayoutCategoryMainDetail);

        relativeLayoutCurrencyDetail = (RelativeLayout) findViewById(R.id.relativeLayoutCurrencyDetail);
        activity_expenselist_detail = (LinearLayout) findViewById(R.id.activity_expenselist_detail);
        linearLayoutDateDetail = (LinearLayout) findViewById(R.id.linearLayoutDateDetail);
        relativeLayoutTaxRateDetail = (RelativeLayout) findViewById(R.id.relativeLayoutTaxRateDetail);
        relativeLayoutProviderDetail = (RelativeLayout) findViewById(R.id.relativeLayoutProviderDetail);
        RelativeLayoutDocTypeDetail = (RelativeLayout) findViewById(R.id.RelativeLayoutDocTypeDetail);
        costCenterRelativeLayoutDetail = (RelativeLayout) findViewById(R.id.costCenterRelativeLayoutDetail);

        setupListMenu(imageViewList);


    }


    private void setupListMenu(ImageView imageViewList) {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imageViewList);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.edit)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.delete)))
                .addMenuItem(new DroppyMenuItem(getString(R.string.calcel)));


// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {

                if (id == 0) {

                    Intent intent = new Intent(context,EditExpensesListActivity.class);
                    intent.putExtra("erID", erID);
                    intent.putExtra("elID",elID);
                    intent.putExtra("isOnline",isOnline);

                    // start activity for the result...
                    startActivityForResult(intent, 1959);
                    //finish();


                }
                if (id ==1) {

                   DeleteELDadaUsingelID();


                }
                if (id == 2) {

                    ClickOnCancel();

                }



            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void ClickOnCancel() {

        Intent intent = new Intent(ExpenseListDetailActivity.this,ExpenseListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("erID",erID);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    private void DeleteELDadaUsingelID() {


        if(databaseoperations.DeleteExpensesList(databaseoperations,elID)){

            Intent intent = new Intent(ExpenseListDetailActivity.this,ExpenseListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("erID",erID);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        }







    }


    private void GetReadyAllData() {
        if (isOnline != 1) {

        Cursor cursor = databaseoperations.SelectFromExpensesTable(databaseoperations, erID, elID);
        Log.d("ExpensesListAll", DatabaseUtils.dumpCursorToString(cursor));

        if (cursor.moveToFirst()) {

            do {

                textViewDateDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DATE)));
                textViewCurrencyCodeDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CURRENCY_CODE)));
                editTextOriginalAmountDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_ORIGINAL_AMOUNT)));
                editTextExchangeRateDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_EXCHANGE_RATE)));
                editTextConvertedAmountDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CONVERTED_AMOUNT)));
                EditTextDescriptionDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DESCRIPTION)));
                textViewCategoryDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CATEGORY)));
                textViewSupplierDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SUPPLIER)));
                editTextSupplierIdentifierDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SUPPLIER_IDENTIFIER)));
                editTextSupplierNameDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SUPPLIER_NAME)));

                textViewCostCenterDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_COST_CENTER)));
                textViewDocTypeDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DOCUMENT_TYPE)));
                editTextSeriesDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SERIES)));
                editTextNumberofDocsDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_NUMBER_OF_DOCS)));
                textViewProjectDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_PROJECT)));
                textViewTaxRateDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_TAX_RATE)));
                editTextTaxAmountDetail.setText(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_TAX_AMOUNT)));

                int checvalue = cursor.getInt(cursor.getColumnIndex(TableData.ExpensesListTable.EL_BILLABLE));

                if (checvalue == 1) {

                    checkboxbillableDetail.setChecked(true);
                } else {

                    checkboxbillableDetail.setChecked(false);
                }

                Glide.with(this).load(cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_IMAGE_URL))).placeholder(R.drawable.ic_barcode).into(imageViewChooseDetail);


            } while (cursor.moveToNext());

        }
        cursor.close();

    } else{


            String BaseUrl ="http://161.202.19.38/inti_expense/api/api.php?type=get_expense_detail&customer_id="+customerID+"&user_id="+userID+"&expense_no="+elID+"&exp_report_no="+erID+"";


            Log.d("adsfsadfsd",BaseUrl);
            Ion.with(context)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {


                            if(result!=null){

                                String status = result.get("status").getAsString();
                                if(status.equalsIgnoreCase("success")){

                                    String imagePath = result.get("IMAGE_URL").getAsString();


                                    JsonArray jsonArray = result.getAsJsonArray("data");
                                    JsonObject jsonObject=jsonArray.get(0).getAsJsonObject();

                                    textViewDateDetail.setText(jsonObject.get("EXPENSE_DATE").getAsString());
                                    textViewCurrencyCodeDetail.setText(jsonObject.get("EXPENSE_CURRENCY_CODE").getAsString());
                                    editTextOriginalAmountDetail.setText(jsonObject.get("EXPENSE_BASE_AMOUNT").getAsString());
                                    editTextExchangeRateDetail.setText(jsonObject.get("EXCHANGE_RATE").getAsString());
                                    editTextConvertedAmountDetail.setText(jsonObject.get("FUNCTIONAL_AMOUNT").getAsString());
                                    EditTextDescriptionDetail.setText(jsonObject.get("DESCRIPTION").getAsString());
                                    textViewCategoryDetail.setText(jsonObject.get("CATEGORY_CLASS").getAsString());
                                    textViewSupplierDetail.setText(jsonObject.get("SUPPLIER_OPTIONAL").getAsString());
                                    editTextSupplierIdentifierDetail.setText(jsonObject.get("VENDOR_ERP_IDENTIFIER").getAsString());
                                    editTextSupplierNameDetail.setText(jsonObject.get("SUPPLIER_OPTIONAL").getAsString());
                                    textViewCostCenterDetail.setText(jsonObject.get("COST_CENTER").getAsString());
                                    textViewDocTypeDetail.setText(jsonObject.get("DOCUMENT_TYPE_NAME").getAsString());
                                    editTextSeriesDetail.setText(jsonObject.get("SERISE").getAsString());
                                    editTextNumberofDocsDetail.setText(jsonObject.get("DOC_NUMBER").getAsString());
                                    textViewProjectDetail.setText(jsonObject.get("PROJECT_CODE").getAsString());
                                    textViewTaxRateDetail.setText(jsonObject.get("TAX_RATE_NAME").getAsString());
                                    editTextTaxAmountDetail.setText(jsonObject.get("FUNCTIONAL_TAX_AMOUNT").getAsString());


                                    String checvalue = jsonObject.get("BILLABLE_FLG").getAsString();

                                    if (checvalue.equalsIgnoreCase("Y")) {

                                        checkboxbillableDetail.setChecked(true);
                                    } else {

                                        checkboxbillableDetail.setChecked(false);
                                    }

                                  Glide.with(context).load(imagePath).centerCrop().placeholder(R.drawable.ic_barcode).into(imageViewChooseDetail);






                                }else if(status.equalsIgnoreCase("")){


                                }
                            }



                        }
                    });

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
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExpenseListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("erID",erID);
                intent.putExtras(bundle);
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
                    Intent intent = new Intent(ExpenseListDetailActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }


    private void manageDatas() {

        HashMap<String, String> user = userSessionManager.getUserDetails();
        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        userType = user.get(UserSessionManager.KEY_USERTYPE);
        userID = Integer.parseInt(user.get(UserSessionManager.KEY_USERID));
        customerID = Integer.parseInt(user.get(UserSessionManager.KEY_CUSTOMERID));



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {




        }
    }



    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


}
