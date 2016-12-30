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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expensesummary.ExpenseSummaryActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class CreateExpenseActivity extends AppCompatActivity implements View.OnClickListener {



    private int mYear, mMonth, mDay, mHour, mMinute;

    Toolbar toolbar;
    Context context;
    private  TextView fromDateTextView,toDateTextView,eEmailTextView;
    private LinearLayout fromDatelayout,toDatelayout;
    private EditText etName,etDescription;
    public String eEmail,userType,userID,customerID,userPass,userName;
    String erStatus="InApproval";
    String erName,erDescription ,erID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        context=this;

        try{
            Bundle bundle=getIntent().getExtras();
            eEmail = bundle.getString("eEmail");
            userType = bundle.getString("userType");
            userID = bundle.getString("userID");
            customerID =bundle.getString("customerID");
            userPass =bundle.getString("userPass");
            userName = bundle.getString("userName");




        }catch (Exception ex){

            ex.fillInStackTrace();
        }
        initView();
        setupToolBar(getString(R.string.create_expense));
       // SelectDatFromExpensesReport();


    }

    private void initView() {

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


    }

    private void setupToolBar(String title) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageViewSetting = (ImageView) toolbar.findViewById(R.id.imageViewSetting);
        TextView textViewTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
        setupSettingMenu(imageViewSetting);
        ImageView imageViewBack= (ImageView) toolbar.findViewById(R.id.imageViewBack);
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
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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



    private void GetAllInputDatasandInsertinDB() {

        erID= UUID.randomUUID().toString();

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


        String erFromDate=fromDateTextView.getText().toString().trim();
        String erToDate=toDateTextView.getText().toString().trim();

        if(!erName.isEmpty()){

            if(!erDescription.isEmpty()){

                if(!erFromDate.isEmpty()){

                    if(!erToDate.isEmpty()){

                        //Generate the current creating date of Expenses Report...

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        // This erID should be unique...



                        // check if net is available or not... if yes insert into the server or store in local...

                        if(CheckNetworkInfo()){
                            String extraParameters =eEmail+"&password="+userPass+"&name="+erName+"&discription="+erDescription+"&from_date="+erFromDate+"&to_date="+erToDate+"&customer_id="+customerID+"&user_id="+userID;

                            String BaseUrl ="http://netforce.biz/inti_expense/api/api.php?type=exp_report&email="+extraParameters;

                            Log.d("Goooo",BaseUrl);

                            Ion.with(this)
                                    .load(BaseUrl)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {

                                            if(result!=null){

                                                String status = result.get("status").getAsString();
                                                String _expenseID=result.get("exp_report_no").getAsString();

                                                if(status.equalsIgnoreCase("success")){



                                                   Intent intent = new Intent(CreateExpenseActivity.this,ExpenseSummaryActivity.class);

                                                    intent.putExtra("eEmail",eEmail);
                                                    intent.putExtra("erID",erID);
                                                    intent.putExtra("userType",userType);
                                                    intent.putExtra("_expenseID",_expenseID);
                                                    startActivity(intent);
                                                    finish();
                                                   }
                                            }

                                        }
                                    });

                        }else {

//                            no net then code here...

                            dop.insertExpensesDatas(dop,erID,erFromDate,erToDate,currentDateandTime,erStatus,erName,erDescription,eEmail,userType,userID,customerID);

                            Intent intent=new Intent(context,ExpenseSummaryActivity.class);

                            intent.putExtra("eEmail",eEmail);
                            intent.putExtra("erID",erID);
                            intent.putExtra("userType",userType);

                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.enter, R.anim.exit);

                        }





                    }else {

                        showMessage("Please select the To date");
                    }

                }else {

                    showMessage("Please select the from Date");
                }


            }else {

                showMessage("Please enter the Descriptions");
            }

        }else {

            showMessage("Please Enter the Name");
        }

    }

    private boolean CheckNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = cm.getActiveNetworkInfo();

        if(networkinfo!=null && networkinfo.isConnected()==true){

              return true;

        }else {
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
