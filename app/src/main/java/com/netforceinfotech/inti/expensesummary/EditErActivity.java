package com.netforceinfotech.inti.expensesummary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.CreateExpenseActivity;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditErActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView fromDateTextView, toDateTextView, eEmailTextView;
    private LinearLayout fromDatelayout, toDatelayout;
    private EditText etName, etDescription;
    String erID, eEmail, userType, userID;
    UserSessionManager usersessionmanager;
    Toolbar toolbar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Context context;
    MaterialDialog materialDialog;
    String erName, erDescription, erFromDate, erToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_er);

        usersessionmanager = new UserSessionManager(this);
        HashMap<String, String> user = usersessionmanager.getUserDetails();
        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        userType = user.get(UserSessionManager.KEY_USERTYPE);
        userID = user.get(UserSessionManager.KEY_USERID);

        try {

            Bundle bundle = getIntent().getExtras();
            erID = bundle.getString("erID");


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

        setupDatas();

        initView();
        setupToolBar(getString(R.string.editreport));


    }

    private void setupDatas() {

        DatabaseOperations dop = new DatabaseOperations(this);

        Cursor cursor = dop.getEditExpensesReportData(dop, erID);

        if (cursor.moveToFirst()) {

            do {


                // erName = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME));
                try {
                    erName = URLDecoder.decode(cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_NAME)), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    erDescription = URLDecoder.decode(cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_DESCRIPTION)), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                erFromDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_FROM_DATE));
                erToDate = cursor.getString(cursor.getColumnIndex(TableData.ExpenseReportTable.ER_TO_DATE));


            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    private void initView() {

        materialDialog = new MaterialDialog.Builder(this)
                .content(R.string.pleasewait)
                .progress(true, 0)
                .build();

        eEmailTextView = (TextView) findViewById(R.id.eEmailTextView);
        eEmailTextView.setText(eEmail);

        etName = (EditText) findViewById(R.id.editTextName);
        etName.setText(erName, TextView.BufferType.EDITABLE);

        etDescription = (EditText) findViewById(R.id.editTextDescription);
        etDescription.setText(erDescription, TextView.BufferType.EDITABLE);

        toDatelayout = (LinearLayout) findViewById(R.id.toDateLayout);
        toDatelayout.setOnClickListener(this);

        fromDateTextView = (TextView) findViewById(R.id.fromDateTextView);
        fromDateTextView.setText(erFromDate, TextView.BufferType.EDITABLE);
        fromDatelayout = (LinearLayout) findViewById(R.id.fromDatelayout);
        fromDatelayout.setOnClickListener(this);
        toDateTextView = (TextView) findViewById(R.id.toDateTextView);
        toDateTextView.setText(erToDate, TextView.BufferType.EDITABLE);

        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromDatelayout:

                getFromDate("fromDate");

                break;
            case R.id.toDateLayout:

                getFromDate("toDate");
                break;
            case R.id.buttonSave:

                GetAllInputDatasandInsertinDB();


                break;
            case R.id.buttonCancel:

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
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
                Intent intent = new Intent(EditErActivity.this, ExpenseSummaryActivity.class);
                intent.putExtra("erID", erID);
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
                showMessage("position: " + id + " clicked");
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void showMessage(String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    private void GetAllInputDatasandInsertinDB() {
        materialDialog.show();


        try {
            erName = URLEncoder.encode(etName.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            erDescription = URLEncoder.encode(etDescription.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String erFromDate = fromDateTextView.getText().toString().trim();
        String erToDate = toDateTextView.getText().toString().trim();

        if (!erName.isEmpty()) {

            if (!erDescription.isEmpty()) {

                if (!erFromDate.isEmpty()) {

                    if (!erToDate.isEmpty()) {

                       Intent intent= new Intent();

                        Bundle bundle = new Bundle();
                        bundle.putString("erName",erName);
                        bundle.putString("erDescription",erDescription);
                        bundle.putString("erFromDate",erFromDate);
                        bundle.putString("erToDate",erToDate);
                        intent.putExtra("erID", erID);
                        setResult(2, intent);
                        finish();






                } else {

                    showMessage("Please select the To date");
                }

            } else {

                showMessage("Please select the from Date");
            }


        } else {

            showMessage("Please enter the Descriptions");
        }

    }

    else

    {

        showMessage("Please Enter the Name");
    }


}




    private void getFromDate(String s) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        if(s.equalsIgnoreCase("fromdate")){


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            // fromDateTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            //   fromDateTextView.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            fromDateTextView.setText( year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        } else if(s.equalsIgnoreCase("toDate")){

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            toDateTextView.setText( year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();

        }




    }

}
