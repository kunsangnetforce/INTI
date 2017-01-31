package com.netforceinfotech.inti.addexpenses;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.addexpenses.model.CateData;
import com.netforceinfotech.inti.addexpenses.model.SupplierData;
import com.netforceinfotech.inti.addexpenses.model.TaxData;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expenselist.ExpenseListAdapter;
import com.netforceinfotech.inti.expenselist.ExpenseListData;
import com.netforceinfotech.inti.expensereport.MyExpenseReportActivity;
import com.netforceinfotech.inti.expensesummary.ExpenseSummaryActivity;
import com.netforceinfotech.inti.general.ConnectivityCheck;
import com.netforceinfotech.inti.general.DateCheck;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.myprofile.MyProfileActivity;
import com.netforceinfotech.inti.util.Debugger;
import com.netforceinfotech.inti.util.Validation;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class TextImageExpenseActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int CHOOSE_OPTION = 101;
    final ArrayList<CateData> categories = new ArrayList<CateData>();
    final ArrayList<String> currency = new ArrayList<>();
    final ArrayList<SupplierData> suppliers = new ArrayList<SupplierData>();
    final ArrayList<String> costcenters = new ArrayList<>();
    ArrayList<String> doctype = new ArrayList<>();
    Toolbar toolbar;
    Context context;
    ImageView imageViewList, imageViewChoose, imageViewAttached;
    private Intent intent;
    private MaterialDialog dialogDoB;
    TextView textViewDate, textViewCurrencyCode, textViewCategory, textViewSupplier, editTextOriginalAmount;
    LinearLayout linearLayoutDate;
    RelativeLayout relativeLayoutCurrency, relativeLayoutCategorymain, relativeLayoutTaxRate, relativeLayoutProject, relativeLayoutSupplierMain, costCenterRelativeLayout, RelativeLayoutDocType;
    private MaterialDialog dialogAddress;
    private EditText etOtherProvider;
    Button buttonSubmit;
    EditText EditTextDescription;
    ExpenseListAdapter myAdapter;
    ArrayList<ExpenseListData> expenseListDatas = new ArrayList<ExpenseListData>();
    CheckBox checkboxbillable;
    static final String TAG = "INTI_APP";
    String filePath, checkboxValue = "0";
    EditText editTextExchangeRate, editTextConvertedAmount, editTextSupplierIdentifier, editTextSupplierName, editTextSeries, editTextNumberofDocs, editTextTaxRate, editTextTaxAmount;
    TextView textViewCostCenter, textViewDocType, textViewProject, textViewTaxRate, eEmailTextView, erTitleTextView;

    ArrayList<TaxData> taxdatas = new ArrayList<TaxData>();
    ArrayList<String> project = new ArrayList<>();
    DatabaseOperations dop;
    String erName, eEmail, erID, userType,userClass;
    int userID, customerID;
    UserSessionManager userSessionManager;
    private String catid;
    ConnectivityCheck connectivityCheck;
    int isOnline =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_image_expense);
        context = this;


        setupToolBar(getString(R.string.text_image));
        userSessionManager = new UserSessionManager(this);
        userSessionManager.checkLogin();


        dop = new DatabaseOperations(this);
        connectivityCheck = new ConnectivityCheck(this);

        manageDatas();
        InitExpenseTableDatas();
        initView();
        InitExpenseData();






    }

    private void InitExpenseData() {


        showCurrencyPopUp();
        setupCategory();
        setupSupplier();
        setupCostCenter();
        setupDoctype();
        setupProject();
        setupTaxRate();
    }


    private void manageDatas() {

        HashMap<String, String> user = userSessionManager.getUserDetails();
        eEmail = user.get(UserSessionManager.KEY_EMAIL);
        userType = user.get(UserSessionManager.KEY_USERTYPE);
        userID = Integer.parseInt(user.get(UserSessionManager.KEY_USERID));
        customerID = Integer.parseInt(user.get(UserSessionManager.KEY_CUSTOMERID));
        userClass =user.get(UserSessionManager.KEY_USERCLASS);
    }

    private void InitExpenseTableDatas() {

        try {

            Bundle bundle = getIntent().getExtras();
            erID = bundle.getString("erID");

            if (isOnline != 0) {

            int eridlak = Integer.parseInt(erID);

            Cursor cursorER = dop.SelectDatafromExpenseReportTable(dop, eridlak, eEmail);
            Log.d("JUSTSHOWME", DatabaseUtils.dumpCursorToString(cursorER));


            if (cursorER.moveToFirst()) {


                do {

                    String ervalue = cursorER.getString(cursorER.getColumnIndex(TableData.ExpenseReportTable.ER_NAME));

                    try {

                        erName = URLDecoder.decode(ervalue, "UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                } while (cursorER.moveToNext());
            }
            cursorER.close();

        } else {

                String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=get_data_by_report_num&customer_id=" + customerID + "&user_id=" + userID + "&user_class=" + userClass + "&exp_report_no=" + erID + "";

                Log.d("BASEONLINE",BaseUrl);

                Ion.with(context)
                        .load(BaseUrl)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if(result!=null){

                                    String status = result.get("status").getAsString();

                                    if(status.equalsIgnoreCase("success")){

                                        erName = result.get("name").getAsString();

                                    }else {

                                        showMessage(getResources().getString(R.string.serverError));
                                    }
                                }

                            }
                        });




            }




        }catch (Exception ex){

            ex.fillInStackTrace();
        }






    }

    private void initView() {

        editTextSupplierName = (EditText) findViewById(R.id.editTextSupplierName);

        erTitleTextView = (TextView) findViewById(R.id.erTitleTextView);
        erTitleTextView.setText(erName);

        eEmailTextView = (TextView) findViewById(R.id.eEmailTextView);
        eEmailTextView.setText(eEmail);

        textViewTaxRate = (TextView) findViewById(R.id.textViewTaxRate);
        relativeLayoutTaxRate = (RelativeLayout) findViewById(R.id.relativeLayoutTaxRate);
        relativeLayoutProject = (RelativeLayout) findViewById(R.id.relativeLayoutProject);
        RelativeLayoutDocType = (RelativeLayout) findViewById(R.id.RelativeLayoutDocType);

        costCenterRelativeLayout = (RelativeLayout) findViewById(R.id.costCenterRelativeLayout);
        checkboxbillable = (CheckBox) findViewById(R.id.checkboxbillable);
        checkboxbillable.setOnClickListener(this);
        editTextExchangeRate = (EditText) findViewById(R.id.editTextExchangeRate);
        editTextConvertedAmount = (EditText) findViewById(R.id.editTextConvertedAmount);
        editTextSupplierIdentifier = (EditText) findViewById(R.id.editTextSupplierIdentifier);
        editTextSeries = (EditText) findViewById(R.id.editTextSeries);
        editTextNumberofDocs = (EditText) findViewById(R.id.editTextNumberofDocs);

        editTextTaxAmount = (EditText) findViewById(R.id.editTextTaxAmount);
        textViewCostCenter = (TextView) findViewById(R.id.textViewCostCenter);

        textViewDocType = (TextView) findViewById(R.id.textViewDocType);
        textViewProject = (TextView) findViewById(R.id.textViewProject);
        // ends here....

        EditTextDescription = (EditText) findViewById(R.id.EditTextDescription);
        editTextOriginalAmount = (TextView) findViewById(R.id.editTextOriginalAmount);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        imageViewAttached = (ImageView) findViewById(R.id.imageViewAttached);
        imageViewAttached.setVisibility(View.GONE);
        imageViewChoose = (ImageView) findViewById(R.id.imageViewChoose);
        textViewSupplier = (TextView) findViewById(R.id.textViewSupplier);
        relativeLayoutSupplierMain = (RelativeLayout) findViewById(R.id.relativeLayoutSupplierMain);
        relativeLayoutSupplierMain.setOnClickListener(this);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        relativeLayoutCategorymain = (RelativeLayout) findViewById(R.id.relativeLayoutCategoryMain);
        textViewCurrencyCode = (TextView) findViewById(R.id.textViewCurrencyCode);
        relativeLayoutCurrency = (RelativeLayout) findViewById(R.id.relativeLayoutCurrency);
    //    relativeLayoutCurrency.setOnClickListener(this);
        linearLayoutDate = (LinearLayout) findViewById(R.id.linearLayoutDate);
        linearLayoutDate.setOnClickListener(this);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        imageViewChoose.setOnClickListener(this);
        imageViewList = (ImageView) findViewById(R.id.imageViewList);
        Glide.with(context).fromResource()
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 100)).load(R.drawable.ic_toggle).into(imageViewList);
        imageViewList.setOnClickListener(this);

        DateTextWatcher();
        CurrencyCodeTextWatcher();
        OriginalAmountTextWatcher();
        CalcuteConvertedAmount();

        editTextSupplierIdentifier.setFocusable(false);
        editTextSupplierIdentifier.setFocusableInTouchMode(false);
        editTextSupplierName.setFocusableInTouchMode(false);


    }

    private void CalcuteConvertedAmount() {

        String oAmount = editTextOriginalAmount.getText().toString();
        if (!oAmount.equals("")) {
            Double orAmount = Double.parseDouble(oAmount);
            String exValue = editTextExchangeRate.getText().toString();
            Double exVal = Double.parseDouble(exValue);
            Double converted = orAmount * exVal;
            String finalVal = String.valueOf(converted);
            editTextConvertedAmount.setText(finalVal);
        }


    }

    private void CurrencyCodeTextWatcher() {


        textViewCurrencyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (textViewDate.getText().toString().isEmpty()) {
                    showMessage("Please Select the Date ");
                    textViewDate.requestFocus();

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    // expense date must be smaller than the current date....

                    String cvalue = textViewCurrencyCode.getText().toString();
                    String edate = textViewDate.getText().toString();
                    String baseCurrency = "USD";

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDate = simpleDate.format(c.getTime());

                    Date cDate = simpleDate.parse(currentDate);
                    Date eDate = simpleDate.parse(edate);

                    if (eDate.compareTo(cDate) < 0) {

                        String BaseUrl = "http://currencies.apps.grandtrunk.net/getrate/" + edate + "/" + baseCurrency + "/" + cvalue;
                        Log.d(TAG, BaseUrl);

                        Ion.with(context)
                                .load(BaseUrl)
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        try{


                                            if (!result.equalsIgnoreCase("False")) {

                                                editTextExchangeRate.setText(result);
                                                CalcuteConvertedAmount();
                                            }


                                        }catch (Exception ex){


                                        }


                                    }
                                });

                    } else {

                        textViewDate.requestFocus();
                        showMessage("Please Select a Valid Date...");
                        editTextExchangeRate.getText().clear();
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void DateTextWatcher() {

        textViewDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    // expense date must be smaller than the current date....


                    String cvalue = textViewCurrencyCode.getText().toString();
                    String edate = textViewDate.getText().toString();
                    String baseCurrency = "USD";

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDate = simpleDate.format(c.getTime());

                    Date cDate = simpleDate.parse(currentDate);
                    Date eDate = simpleDate.parse(edate);

                    if (eDate.compareTo(cDate) < 0) {


                        String BaseUrl = "http://currencies.apps.grandtrunk.net/getrate/" + edate + "/" + baseCurrency + "/" + cvalue;
                        Log.d(TAG, BaseUrl);

                        Ion.with(context)
                                .load(BaseUrl)
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {

                                        if (!result.equalsIgnoreCase("False")) {
                                            editTextExchangeRate.setText(result);
                                            CalcuteConvertedAmount();
                                        }


                                    }
                                });

                    } else {
                        editTextExchangeRate.getText().clear();
                        textViewDate.setText(" ");
                        textViewDate.setHint("Select a Valid Date");
                        textViewDate.requestFocus();
                        showMessage("Please Select a Valid Date...");

                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void OriginalAmountTextWatcher() {

        editTextOriginalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!textViewDate.getText().toString().isEmpty() && !textViewCurrencyCode.getText().toString().isEmpty() && !editTextExchangeRate.getText().toString().isEmpty()) {

                    CalcuteConvertedAmount();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                Intent intent = new Intent(context, MyExpenseReportActivity.class);
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
                    Intent intent = new Intent(TextImageExpenseActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

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
            case R.id.imageViewChoose:
                intent = new Intent(context, ChooseAddExpensesActivity.class);
                startActivityForResult(intent, CHOOSE_OPTION);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.linearLayoutDate:
                Calendar now = Calendar.getInstance();
                DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;

            case R.id.buttonSubmit:
                if(connectivityCheck.isOnline()&&isOnline==1){
                    ValidateAndSubmitDatas();
                }
                else if(isOnline==0){

                    ValidateAndSubmitDatasOffline();
                }


                //CallmeMR();
                break;
            case R.id.checkboxbillable:
                if (checkboxbillable.isChecked()) {
                    checkboxValue = "1";
                } else {
                    checkboxValue = "0";
                }
                break;

            case R.id.imageViewList:
                Intent intent = new Intent(TextImageExpenseActivity.this, MyExpenseReportActivity.class);
                intent.putExtra("eEmail", eEmail);
                intent.putExtra("userID", userID);
                startActivity(intent);
                finish();

                break;
        }
    }




    private void showCurrencyPopUp() {

        try{


            Cursor cursor = dop.getCurrency(dop, customerID);
            Log.d("CTA", DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {
                do {

                    // get values from the database...

                    String curree = cursor.getString(cursor.getColumnIndex(TableData.CurrencyTable.CURRENCY_NAME));
                    currency.add(curree);


                } while (cursor.moveToNext());
            }
            cursor.close();

            // Prepare the currency data .. get from the local database...


            final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutCurrency);
            for (int i = 0; i < currency.size(); i++) {

                droppyBuilder.addMenuItem(new DroppyMenuItem(currency.get(i)));

            }
// Set Callback handler
            droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
                @Override
                public void call(View v, int id) {

                    textViewCurrencyCode.setText(currency.get(id));
                }
            });

            DroppyMenuPopup droppyMenu = droppyBuilder.build();





        }catch (Exception ex){

            ex.fillInStackTrace();
        }




    }


    private void setupTaxRate() {
        try {
            taxdatas.clear();
            Cursor cursor = dop.getTaxByCustomerId(dop, customerID);

            Log.d("Tax", DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()) {
                do {
                    String taxratesss = cursor.getString(cursor.getColumnIndex(TableData.TaxTable.TAX_NAME));
                    int taxrateid = cursor.getInt(cursor.getColumnIndex(TableData.TaxTable.ID));
                    TaxData taxData = new TaxData(taxrateid, taxratesss);
                    taxdatas.add(taxData);


                } while (cursor.moveToNext());

            }
            cursor.close();


        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutTaxRate);

        for (int i = 0; i < taxdatas.size(); i++) {

            droppyBuilder.addMenuItem(new DroppyMenuItem(taxdatas.get(i).getTaxname()));

        }
        // Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {

                textViewTaxRate.setText(taxdatas.get(id).getTaxname());

                try {

                    int taxidlak = taxdatas.get(id).getTaxid();
                    Log.d("TaxId", String.valueOf(taxidlak));

                    Cursor cursor = dop.getTaxByTaxId(dop, taxidlak);
                    Log.d("TaxID", DatabaseUtils.dumpCursorToString(cursor));

                    if (cursor.moveToFirst()) {
                        do {
                            String gettaxrate = cursor.getString(cursor.getColumnIndex(TableData.TaxTable.TAX_RATE));

                            String amountvalue = editTextConvertedAmount.getText().toString();

                            Double trate = Double.parseDouble(gettaxrate);
                            Double amountva = Double.parseDouble(amountvalue);

                            Double totaltx = amountva * (trate / 100);

                            String finalAmountinBase = String.valueOf(totaltx);

                            editTextTaxAmount.setText(finalAmountinBase);


                        } while (cursor.moveToNext());

                    }
                    cursor.close();

                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }


            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();


    }

    private void setupProject() {
        try {
            project.clear();

            Cursor cursor = dop.getProjectByCustomerId(dop, customerID);
            Log.d("Project", DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {

                do {

                    String projectnam = cursor.getString(cursor.getColumnIndex(TableData.ProjectTable.PROJECT_NAME));

                    project.add(projectnam);

                } while (cursor.moveToNext());
            }
            cursor.close();


        } catch (Exception ex) {
            ex.fillInStackTrace();

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutProject);

        for (int i = 0; i < project.size(); i++) {

            droppyBuilder.addMenuItem(new DroppyMenuItem(project.get(i)));

        }
        // Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {

                textViewProject.setText(project.get(id));

            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void setupDoctype() {
        try {
            doctype.clear();

            Cursor cursor = dop.getDocTypeByCustomerId(dop, customerID);
            Log.d("Doctype", DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()) {

                do {

                    String doctypeName = cursor.getString(cursor.getColumnIndex(TableData.DocTypeTable.DOC_NAME));

                    // set cost nam
                    doctype.add(doctypeName);

                } while (cursor.moveToNext());
            }
            cursor.close();


        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, RelativeLayoutDocType);

        for (int i = 0; i < doctype.size(); i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem(doctype.get(i)));

        }
        // Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {

                textViewDocType.setText(doctype.get(id));

            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();


    }

    private void setupCostCenter() {


        try {
            costcenters.clear();

            Cursor cursor = dop.getCostCenterByCustomerId(dop, customerID);
            Log.d("CostCenter", DatabaseUtils.dumpCursorToString(cursor));


            if (cursor.moveToFirst()) {

                do {

                    String costcenternam = cursor.getString(cursor.getColumnIndex(TableData.CostCenterTable.COST_CENTER_NAME));

                    // set cost nam
                    costcenters.add(costcenternam);

                } while (cursor.moveToNext());
            }
            cursor.close();


        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, costCenterRelativeLayout);
        for (int i = 0; i < costcenters.size(); i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem(costcenters.get(i)));

        }
//        droppyBuilder.addMenuItem(new DroppyMenuItem("Others"));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                if (id == 100) {
                    showEditAddressPopup();
                } else {
                    textViewCostCenter.setText(costcenters.get(id));
                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }


    private void ValidateAndSubmitDatas() {


        String date = textViewDate.getText().toString().trim();
        String currencycode = textViewCurrencyCode.getText().toString().trim();
        String originalamount = editTextOriginalAmount.getText().toString().trim();
        String descriptions = EditTextDescription.getText().toString().trim();
        String category = textViewCategory.getText().toString().trim();
        String imageUrl = filePath;

        String textSupplier = textViewSupplier.getText().toString().trim();
        String exchangeRate = editTextExchangeRate.getText().toString().trim();
        String convertedAmount = editTextConvertedAmount.getText().toString().trim();
        String supplieridentifier = editTextSupplierIdentifier.getText().toString().trim();
        String series = editTextSeries.getText().toString().trim();

        String numofDocs = editTextNumberofDocs.getText().toString().trim();
        String taxRate = textViewTaxRate.getText().toString().trim();
        String taxamount = editTextTaxAmount.getText().toString().trim();
        String costcenter = textViewCostCenter.getText().toString().trim();
        String doctype = textViewDocType.getText().toString().trim();
        String project = textViewProject.getText().toString().trim();
        int checkValue = Integer.parseInt(checkboxValue);

        String suppliername = editTextSupplierName.getText().toString();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String currentdatess = df.format(c.getTime());


        Long tsLong = System.currentTimeMillis() / 1000;
        String creationDate = tsLong.toString();

        if (!date.isEmpty() && DateCheck.isDateGreater(date, currentdatess)) {

            if (!currencycode.isEmpty()) {

                if (!originalamount.isEmpty()) {

                    if (!descriptions.isEmpty()) {

                        if (!category.isEmpty()) {

                            if (!textSupplier.isEmpty()) {

                                if (!supplieridentifier.isEmpty()) {

                                    if (!suppliername.isEmpty()) {

                                        if (!costcenter.isEmpty()) {

                                            if (!doctype.isEmpty()) {

                                                if (!series.isEmpty()) {


                                                    if (!numofDocs.isEmpty()) {


                                                        if (!project.isEmpty()) {

                                                            if (!taxRate.isEmpty()) {

//

                                                                String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=create_expenses&exp_report_no=" + erID + "&expense_date=" + date + "&currency_code=" + currencycode + "&original_amt=" + originalamount + "&exchange_rate=" + exchangeRate + "&functional_amt=" + convertedAmount + "&functional_tax_amt=" + taxamount + "&category_id=" + category + "&discription=" + descriptions + "&supplier_id=" + suppliers + "&supp_identifire=" + supplieridentifier + "&supplier_optional=" + suppliername + "&cost_center=" + costcenter + "&document_type=" + doctype + "&serise=" + series + "&doc_number=" + numofDocs + "&project_code=" + project + "&type_of_tax=" + taxRate + "&tax_amt=" + taxamount + "&invoiceable=" + checkValue + "&customer_id=" + customerID + "&user_id=" + userID + "&&attribute1=&attribute2=&attribute3=&attribute4=&attribute5=&attribute6=&attribute7=&attribute8=&attribute9=&attribute10=&attribute11=&attribute12=&attribute13=&attribute14=&attribute15=&attribute16=&attribute17=&attribute18=&attribute19=&attribute20=";

                                                                Ion.with(context)
                                                                        .load(BaseUrl)
                                                                        .asJsonObject()
                                                                        .setCallback(new FutureCallback<JsonObject>() {
                                                                            @Override
                                                                            public void onCompleted(Exception e, JsonObject result) {

                                                                                     if(result!=null){

                                                                                         String status=result.get("status").getAsString();

                                                                                         if(status.equalsIgnoreCase("success")){

                                                                                             Intent intent = new Intent(TextImageExpenseActivity.this, ExpenseSummaryActivity.class);
                                                                                             intent.putExtra("eEmail", eEmail);
                                                                                             intent.putExtra("erID", erID);
                                                                                             intent.putExtra("elID", " ");

                                                                                             startActivity(intent);
                                                                                             finish();
                                                                                         }
                                                                                     }

                                                                            }
                                                                        });








                                                                // end here to if condition...


                                                            } else {
                                                                showMessage("Please select the Tax Rate.");
                                                            }

                                                        } else {

                                                            showMessage("Please select the Project ");
                                                        }

                                                    } else {

                                                        showMessage("Please enter the number of documents");
                                                    }
                                                } else {

                                                    showMessage("Please enter the Series");
                                                }
                                            } else {

                                                showMessage("Please select the Document Type");
                                            }

                                        } else {
                                            showMessage("Please Select the Cost Center");
                                        }


                                    } else {

                                        showMessage("Please enter the Supplier Name");
                                    }

                                } else {

                                    showMessage("Please enter the supplier Identifier");
                                }


                            } else {

                                showMessage("Please selec the Supplier");
                            }

                        } else {
                            showMessage("Please select the Category");
                        }

                    } else {
                        showMessage("Please enter the Description.");
                    }

                } else {

                    showMessage("Please enter the Original Amount");
                }


            } else {

                showMessage("Please select the Base Currency");
                textViewCurrencyCode.setError("Select Currency");
            }


        } else {

            showMessage("Please select a valid date");
            textViewDate.setText("");
            textViewDate.setHint("Please select a valid date");


        }


        // EditText editTextExchangeRate,editTextConvertedAmount,editTextRUC,editTextSeries,editTextNumberofDocs,editTextTaxRate,editTextIGV;
        //TextView textViewProvider,textViewCostCenter,textViewDocType,textViewDraft;


    }


    private void ValidateAndSubmitDatasOffline() {


        String date = textViewDate.getText().toString().trim();
        String currencycode = textViewCurrencyCode.getText().toString().trim();
        String originalamount = editTextOriginalAmount.getText().toString().trim();
        String descriptions = EditTextDescription.getText().toString().trim();
        String category = textViewCategory.getText().toString().trim();
        String imageUrl = filePath;

        String textSupplier = textViewSupplier.getText().toString().trim();
        String exchangeRate = editTextExchangeRate.getText().toString().trim();
        String convertedAmount = editTextConvertedAmount.getText().toString().trim();
        String supplieridentifier = editTextSupplierIdentifier.getText().toString().trim();
        String series = editTextSeries.getText().toString().trim();

        String numofDocs = editTextNumberofDocs.getText().toString().trim();
        String taxRate = textViewTaxRate.getText().toString().trim();
        String taxamount = editTextTaxAmount.getText().toString().trim();
        String costcenter = textViewCostCenter.getText().toString().trim();
        String doctype = textViewDocType.getText().toString().trim();
        String project = textViewProject.getText().toString().trim();
        int checkValue = Integer.parseInt(checkboxValue);

        String suppliername = editTextSupplierName.getText().toString();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String currentdatess = df.format(c.getTime());


        Long tsLong = System.currentTimeMillis() / 1000;
        String creationDate = tsLong.toString();

        if (!date.isEmpty() && DateCheck.isDateGreater(date, currentdatess)) {

            if (!currencycode.isEmpty()) {

                if (!originalamount.isEmpty()) {

                    if (!descriptions.isEmpty()) {

                        if (!category.isEmpty()) {

                            if (!textSupplier.isEmpty()) {

                                if (!supplieridentifier.isEmpty()) {

                                    if (!suppliername.isEmpty()) {

                                        if (!costcenter.isEmpty()) {

                                            if (!doctype.isEmpty()) {

                                                if (!series.isEmpty()) {


                                                    if (!numofDocs.isEmpty()) {


                                                        if (!project.isEmpty()) {

                                                            if (!taxRate.isEmpty()) {

//                                                                if all true then enter the data... in database...


                                                                // start if condition....


                                                                DatabaseOperations dop = new DatabaseOperations(this);
                                                                //dop.INSERT_LIST_OF_AN_EXPENSE_TABLE(dop, erID, userType, creationDate, eEmail, imageUrl, date, currencycode, originalamount, exchangeRate, convertedAmount,
                                                                //        descriptions, category, ruc, textProvider, costcenter, doctype, series, numofDocs, draft, taxRate, igv, erName, checkValue,erDescription,erFromDate,erToDate,erStatus,erlistID);

                                                                showMessage("Data entering.... ");


                                                                dop.AddExpensesList(dop, erID, creationDate, eEmail, userID, imageUrl, date, currencycode, originalamount, exchangeRate, convertedAmount, descriptions, category,textSupplier,supplieridentifier,suppliername,costcenter, doctype, series, numofDocs, project, taxRate, taxamount, checkValue);

// for dumping datas... start...
                                                                dop.getExpensesListData(dop);

                                                                Cursor cursor = dop.getExpensesListData(dop);
                                                                Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

                                                                // ends here...
                                                                // Enter the Summary Data..


                                                                dop.AddSummaryData(dop,userID,catid,category,erID,convertedAmount);
                                                                Cursor cu = dop.getSummary(dop);



                                                                Log.d("Summary",DatabaseUtils.dumpCursorToString(cu));



                                                                Intent intent = new Intent(TextImageExpenseActivity.this, ExpenseSummaryActivity.class);
                                                                intent.putExtra("eEmail", eEmail);
                                                                intent.putExtra("erID", erID);
                                                                intent.putExtra("elID", " ");

                                                                startActivity(intent);
                                                                finish();

                                                                // end here to if condition...


                                                            } else {
                                                                showMessage("Please select the Tax Rate.");
                                                            }

                                                        } else {

                                                            showMessage("Please select the Project ");
                                                        }

                                                    } else {

                                                        showMessage("Please enter the number of documents");
                                                    }
                                                } else {

                                                    showMessage("Please enter the Series");
                                                }
                                            } else {

                                                showMessage("Please select the Document Type");
                                            }

                                        } else {
                                            showMessage("Please Select the Cost Center");
                                        }


                                    } else {

                                        showMessage("Please enter the Supplier Name");
                                    }

                                } else {

                                    showMessage("Please enter the supplier Identifier");
                                }


                            } else {

                                showMessage("Please selec the Supplier");
                            }

                        } else {
                            showMessage("Please select the Category");
                        }

                    } else {
                        showMessage("Please enter the Description.");
                    }

                } else {

                    showMessage("Please enter the Original Amount");
                }


            } else {

                showMessage("Please select the Base Currency");
                textViewCurrencyCode.setError("Select Currency");
            }


        } else {

            showMessage("Please select a valid date");
            textViewDate.setText("");
            textViewDate.setHint("Please select a valid date");


        }


        // EditText editTextExchangeRate,editTextConvertedAmount,editTextRUC,editTextSeries,editTextNumberofDocs,editTextTaxRate,editTextIGV;
        //TextView textViewProvider,textViewCostCenter,textViewDocType,textViewDraft;


    }



    private void setupSupplier() {

        try {

            suppliers.clear();

            Cursor cursor = dop.getSupplier(dop, customerID);
            Log.d("SupplierList", DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()) {

                do {
                    String suppli = cursor.getString(cursor.getColumnIndex(TableData.SupplierTable.SUPPLIER_NAME));
                    int supid = cursor.getInt(cursor.getColumnIndex(TableData.SupplierTable.SUPPLIER_ID));

                    SupplierData supplierData = new SupplierData(supid, suppli);
                    suppliers.add(supplierData);


                } while (cursor.moveToNext());


            }
            cursor.close();

            SupplierData supplierData = new SupplierData(-100,getString(R.string.newSupplierText));
            suppliers.add(supplierData);





        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutSupplierMain);
        for (int i = 0; i < suppliers.size(); i++) {


            droppyBuilder.addMenuItem(new DroppyMenuItem(suppliers.get(i).getSuppliername()));

        }
//        droppyBuilder.addMenuItem(new DroppyMenuItem("Others"));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {

               int otherSupplierIndex = suppliers.size()-1;

                if (id ==otherSupplierIndex) {

                    editTextSupplierIdentifier.setFocusable(true);
                    editTextSupplierIdentifier.setFocusableInTouchMode(true);
                    editTextSupplierName.setFocusableInTouchMode(true);
                    editTextSupplierIdentifier.getText().clear();
                    editTextSupplierName.getText().clear();

                    showCustomeCostCenterPopup();


                } else {


                    textViewSupplier.setText(suppliers.get(id).getSuppliername());


                    Log.d("textViewSupplier",suppliers.get(id).getSuppliername());
                    int supplieridlak = suppliers.get(id).getSupplierid();
                    Log.d("SupplierId", String.valueOf(supplieridlak));
                    Cursor cursor = dop.getSupplierById(dop, supplieridlak);

                    Log.d("SuppDetail", DatabaseUtils.dumpCursorToString(cursor));

                    if (cursor.moveToFirst()) {

                        do {
                            String supplieridentifierss = cursor.getString(cursor.getColumnIndex(TableData.SupplierDetailTable.SUPPLIER_IDENTIFIER));
                            String suppliername = cursor.getString(cursor.getColumnIndex(TableData.SupplierDetailTable.SUPPLIER_NAME));

                            editTextSupplierIdentifier.setText(supplieridentifierss);
                            editTextSupplierName.setText(suppliername);


                        } while (cursor.moveToNext());
                    }
                    cursor.close();


                }


            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }


    private void showCustomeCostCenterPopup() {
     /*   getPermission();
        getLocation(0);
     */
        boolean wrapInScrollView = true;
        dialogAddress = new MaterialDialog.Builder(context)
                .title(R.string.otherprovider)
                .customView(R.layout.otherprovider, wrapInScrollView)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();


        etOtherProvider = (EditText) dialogAddress.findViewById(R.id.etOtherProvider);
        dialogAddress.findViewById(R.id.buttonAddressDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtherProvider.getText().length() <= 0) {
                    showMessage(getString(R.string.enter_costcenter));
                    return;
                }
                //textViewCostCenter.setText(etOtherProvider.getText().toString());
                textViewSupplier.setText(etOtherProvider.getText().toString());
                dialogAddress.dismiss();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_OPTION) {
            if (resultCode == RESULT_OK) {
                /*friendsidstring = data.getStringExtra("friendsid");//friendsname
                frindstring = data.getStringExtra("frindstring");
                friendslist.setText(frindstring);
                Debugger.i("kactivityresult", friendsidstring);*/
                String option = data.getStringExtra("option");
                if (option.equalsIgnoreCase("image")) {
                    filePath = data.getStringExtra("filepath");
                    Debugger.i("filepath", filePath);
                    File imageFile = savebitmap(filePath);
                    Glide.with(context).load(imageFile).error(R.drawable.ic_piechart).into(imageViewAttached);
                    imageViewAttached.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    private File savebitmap(String filePath) {
        File file = new File(filePath);
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            outStream = new FileOutputStream(file);
            if (extension.equalsIgnoreCase("png")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else {
                return null;
            }
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;


    }

    private void setupCategory() {

        try {

            categories.clear();

            Cursor cursor = dop.getCatByCustomerId(dop, customerID);
            Log.d("CTA_CAT", DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {
                do {
                    // get values from the database...
                    String catnam = cursor.getString(cursor.getColumnIndex(TableData.CategoryTable.CATEGORY_NAME));
                    String catid = cursor.getString(cursor.getColumnIndex(TableData.CategoryTable.CATEGORY_ID));
                    CateData cateData = new CateData(catid,catnam);
                    categories.add(cateData);



                } while (cursor.moveToNext());
            }
            cursor.close();


        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutCategorymain);

        for (int i = 0; i < categories.size(); i++) {

            droppyBuilder.addMenuItem(new DroppyMenuItem(categories.get(i).getCatname()));

        }

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                textViewCategory.setText(categories.get(id).getCatname());

                catid = categories.get(id).getCatid();


            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date2 = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            monthOfYear = monthOfYear + 1;
            date2 = date_format.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outDate = new SimpleDateFormat("yyyy-MM-dd");
        textViewDate.setText(outDate.format(date2));
    }

    private void showEditAddressPopup() {
     /*   getPermission();
        getLocation(0);
     */
        boolean wrapInScrollView = true;
        dialogAddress = new MaterialDialog.Builder(context)
                .title(R.string.otherprovider)
                .customView(R.layout.otherprovider, wrapInScrollView)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();


        etOtherProvider = (EditText) dialogAddress.findViewById(R.id.etOtherProvider);
        dialogAddress.findViewById(R.id.buttonAddressDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtherProvider.getText().length() <= 0) {
                    showMessage(getString(R.string.enter_providor));
                    return;
                }
                textViewSupplier.setText(etOtherProvider.getText().toString());
                dialogAddress.dismiss();
            }
        });


    }

//    private void showCountryPopUp() {
//        final CountryPicker picker = CountryPicker.newInstance("Select Country");
//        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
//        picker.setListener(new CountryPickerListener() {
//            @Override
//            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
//                // Implement your code here
//                try {
//                    Currency currency = picker.getCurrencyCode(code);
//                    String currencyCode = currency.getCurrencyCode();
//                    String currencySymbol = currency.getSymbol();
//                    textViewCurrencyCode.setText(currencySymbol + "   " + currencyCode);
//                } catch (Exception ex) {
//                    showMessage(getString(R.string.currency_not_found));
//                }
//                picker.dismiss();
//            }
//        });
//    }


//    private String addNumbers() {
//        int number1;
//        int number2;
//        if (editTextOriginalAmount.getText().toString() != "" && editTextOriginalAmount.getText().length() > 0) {
//            number1 = Integer.parseInt(editTextOriginalAmount.getText().toString());
//        } else {
//            number1 = 0;
//        }
//        if (editTextExchangeRate.getText().toString() != "" && editTextExchangeRate.getText().length() > 0) {
//            number2 = Integer.parseInt(editTextExchangeRate.getText().toString());
//        } else {
//            number2 = 0;
//        }
//
//        return Integer.toString(number1 * number2);
//    }
}
