package com.netforceinfotech.inti.addexpenses;

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
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expensereport.MyExpenseReportActivity;
import com.netforceinfotech.inti.expensesummary.ExpenseSummaryActivity;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.myprofile.MyProfileActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class CreateExpenseActivity extends AppCompatActivity implements View.OnClickListener {


    private int mYear, mMonth, mDay, mHour, mMinute;

    Toolbar toolbar;
    Context context;
    private TextView fromDateTextView, toDateTextView, eEmailTextView;
    private LinearLayout fromDatelayout, toDatelayout;
    private EditText etName, etDescription;
    public String eEmail, userType, userID, customerID, userName;
    String erStatus = "InApproval";
    String erName, erDescription, erID;
    UserSessionManager userSessionManager;
    MaterialDialog materialDialog;
    ImageView imageViewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        context = this;


        userSessionManager = new UserSessionManager(this);
        HashMap<String, String> users = userSessionManager.getUserDetails();

        eEmail = users.get(UserSessionManager.KEY_EMAIL);
        userType = users.get(UserSessionManager.KEY_USERTYPE);
        userID = users.get(UserSessionManager.KEY_USERID);
        customerID = users.get(UserSessionManager.KEY_CUSTOMERID);

        initView();
        setupToolBar(getString(R.string.create_expense));
        // SelectDatFromExpensesReport();


    }

    private void initView() {

        materialDialog = new MaterialDialog.Builder(this)
                .content(R.string.pleasewait)
                .progress(true, 0)
                .build();

        eEmailTextView = (TextView) findViewById(R.id.eEmailTextView);
        eEmailTextView.setText(eEmail);

        etName = (EditText) findViewById(R.id.editTextName);
        etDescription = (EditText) findViewById(R.id.editTextDescription);

        toDatelayout = (LinearLayout) findViewById(R.id.toDateLayout);
        toDatelayout.setOnClickListener(this);

        fromDateTextView = (TextView) findViewById(R.id.fromDateTextView);
        fromDatelayout = (LinearLayout) findViewById(R.id.fromDatelayout);
        fromDatelayout.setOnClickListener(this);
        toDateTextView = (TextView) findViewById(R.id.toDateTextView);


        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);
        imageViewList = (ImageView) findViewById(R.id.imageViewList);
        imageViewList.setOnClickListener(this);


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
                Intent intent = new Intent(CreateExpenseActivity.this, DashboardActivity.class);
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


                } else if (id == 1) {

                    Intent intent = new Intent(CreateExpenseActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    finish();
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
                Intent intent = new Intent(CreateExpenseActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;

            case R.id.imageViewList:
                intent = new Intent(CreateExpenseActivity.this, MyExpenseReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("eEmail", eEmail);
                bundle.putString("userID", userID);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }

//    private Boolean isSmallerDate(String fromDate,String toDate){
//
//        SimpleDateFormat dateformat = new SimpleDateFormat();
//         String frmDate = fromDate;
//        String tdate = toDate;
//        try {
//            Date date2 = dateformat.parse(tdate);
//            Date date1= dateformat.parse(frmDate);
//            if(date1.compareTo(date2)<0){
//
//                return true;
//            } else {
//
//                showMessage("To Date can't be smaller than from Date");
//
//                return false;
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//
//    }


    private void GetAllInputDatasandInsertinDB() {


//        insertExpensesData()

        DatabaseOperations dop = new DatabaseOperations(this);

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


                        materialDialog.dismiss();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String erCreationDate = sdf.format(new Date());
                        // insert datas additionall....



                        dop.AddExpenseReport(dop, erName, erFromDate, erToDate, erDescription, erStatus, erCreationDate, eEmail, userType, userID, customerID,1,0,0);

                        dop.getErIds(dop, userID);

                        Cursor cur = dop.getErIds(dop, userID);

                        if (cur.moveToLast()) {

                            erID = cur.getString(cur.getColumnIndex(TableData.ExpenseReportTable.ER_ID));
                            Log.d("IDDDDD", erID);
                        }
                        cur.close();


                        Intent intent = new Intent(context, ExpenseSummaryActivity.class);
                        intent.putExtra("erID", erID);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.enter, R.anim.exit);



                    } else {

                        showMessage("Please select the To date");
                    }

                } else {

                    showMessage("Please select the from Date");
                }


            } else {

                showMessage("Please enter the Descriptions");
            }

        } else {

            showMessage("Please Enter the Name");
        }

    }

    private boolean CheckNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = cm.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected() == true) {

            return true;

        } else {
            // do something...

            return false;
        }
    }

    private void getFromDate(String s) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        if (s.equalsIgnoreCase("fromdate")) {


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            // fromDateTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            //   fromDateTextView.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            fromDateTextView.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        } else if (s.equalsIgnoreCase("toDate")) {

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            toDateTextView.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();

        }


    }


}
