package com.netforceinfotech.inti.expenselist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.util.HashList;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.EditExpensesListActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.general.UserSessionManager;

import java.util.HashMap;

public class ExpenseListDetailActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextExchangeRate, editTextConvertedAmount, editTextRUC, editTextSeries, editTextNumberofDocs, editTextIGV;
    TextView textViewCostCenter, textViewDocType, textViewDraft, textViewTaxRate;
    CheckBox checkboxbillable;
    static final String TAG = "INTI_APP";
    int checkboxValue =0;
    TextView textViewDate, textViewCurrencyCode, textViewCategory, textViewProvider, editTextOriginalAmount, EditTextDescription, editTextTaxRate;
    Context context;
    Button buttonEdit,buttonDelete;
    ImageView imageViewChoose;
    UserSessionManager usersessionManager;

    String elId, customerId, eEmail, userId,erId;


    String elImageUrl, elDate, elCurrency, elOriginalAmount, elExchangeRate, elConvertedAmount, elDescription, elCat, elRUC, elProvider, elCostCenter, elDocType, elSeries, elNumofDocs, elDraft, elTaxRate, elIGV, elBillable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list_detail);
        context = this;

        usersessionManager = new UserSessionManager(this);


        try {

            Bundle bundle = getIntent().getExtras();
            elId = bundle.getString("elID");
            erId = bundle.getString("erID");
            showMessage("I m the id " + elId);

            Log.d(TAG, elId);


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }



        InitExpenseDatas();
        initView();
        setData();



    }

    private void setData() {

        textViewDate.setText(elDate);
        textViewCurrencyCode.setText(elCurrency);
        editTextOriginalAmount.setText(elOriginalAmount);
        editTextExchangeRate.setText(elExchangeRate);
        editTextConvertedAmount.setText(elConvertedAmount);
        EditTextDescription.setText(elDescription);
        textViewCategory.setText(elCat);
        editTextRUC.setText(elRUC);
        textViewProvider.setText(elProvider);
        textViewCostCenter.setText(elCostCenter);
        textViewDocType.setText(elDocType);
        editTextSeries.setText(elSeries);
        editTextNumberofDocs.setText(elNumofDocs);
        textViewDraft.setText(elDraft);
        editTextTaxRate.setText(elTaxRate);
        editTextIGV.setText(elIGV);

        Glide.with(this).load(elImageUrl).centerCrop().error(R.drawable.ic_barcode).into(imageViewChoose);
        int val =Integer.parseInt(elBillable);

        if(val!=checkboxValue){

            checkboxbillable.setChecked(true);
        }else{
            checkboxbillable.setChecked(false);
        }




    }

    private void InitExpenseDatas() {

        HashMap<String, String> user = usersessionManager.getUserDetails();

        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        customerId = user.get(UserSessionManager.KEY_CUSTOMERID);
        userId = user.get(UserSessionManager.KEY_USERID);

        DatabaseOperations dop = new DatabaseOperations(this);
        Cursor cursor = dop.SelectFromExpensesTable(dop, elId);

        Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

        if (cursor.moveToFirst()) {


            do {
                // erlistID,erListDes,erlistDate,currency,originalamount,category,imageurl,erlistCat;

                 elImageUrl = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_IMAGE_URL));
                 elDate = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DATE));
                elCurrency = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CURRENCY_CODE));
                elOriginalAmount = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_ORIGINAL_AMOUNT));
                elExchangeRate = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_EXCHANGE_RATE));
                elConvertedAmount = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CONVERTED_AMOUNT));
                elDescription = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DESCRIPTION));
                elCat = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_CATEGORY));
                elRUC = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_RUC));
                elProvider = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_PROVIDER));
                elCostCenter = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_COST_CENTER));
                elDocType = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DOCUMENT_TYPE));
                elSeries = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_SERIES));
                elNumofDocs = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_NUMBER_OF_DOCS));
                elDraft = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_DRAFT));
                elTaxRate = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_TAX_RATE));
                elIGV = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_IGV));
                elBillable = cursor.getString(cursor.getColumnIndex(TableData.ExpensesListTable.EL_BILLABLE));






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


    }

    private void initView() {

        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);


        textViewTaxRate = (TextView) findViewById(R.id.textViewTaxRate);
        checkboxbillable = (CheckBox) findViewById(R.id.checkboxbillable);
        editTextExchangeRate = (EditText) findViewById(R.id.editTextExchangeRate);
        editTextConvertedAmount = (EditText) findViewById(R.id.editTextConvertedAmount);
        editTextRUC = (EditText) findViewById(R.id.editTextRUC);
        editTextSeries = (EditText) findViewById(R.id.editTextSeries);
        editTextNumberofDocs = (EditText) findViewById(R.id.editTextNumberofDocs);

        editTextIGV = (EditText) findViewById(R.id.editTextIGV);
        textViewCostCenter = (TextView) findViewById(R.id.textViewCostCenter);

        textViewDocType = (TextView) findViewById(R.id.textViewDocType);
        textViewDraft = (TextView) findViewById(R.id.textViewDraft);

        EditTextDescription = (EditText) findViewById(R.id.EditTextDescription);
        editTextOriginalAmount = (TextView) findViewById(R.id.editTextOriginalAmount);

        textViewProvider = (TextView) findViewById(R.id.textViewProvider);

        textViewCategory = (TextView) findViewById(R.id.textViewCategory);

        textViewCurrencyCode = (TextView) findViewById(R.id.textViewCurrencyCode);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        imageViewChoose = (ImageView) findViewById(R.id.imageViewChoose);
        editTextTaxRate = (TextView) findViewById(R.id.textViewTaxRate);

        buttonEdit = (Button) findViewById(R.id.buttonEdit);

        buttonEdit.setOnClickListener(this);

        //




    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.buttonEdit:
                EditExpenses();
                break;
            case R.id.buttonDelete:

                DeleteExpensesList();
                break;
        }
    }

    private void DeleteExpensesList() {
        showMessage("Delete Button Clicked...");
    }

    private void EditExpenses() {


        Intent intent = new Intent(ExpenseListDetailActivity.this, EditExpensesListActivity.class);
        intent.putExtra("elID",elId);
        intent.putExtra("erID",erId);
        startActivity(intent);
        finish();

    }

    private void showMessage(String s) {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // call the call again....

    }
}
