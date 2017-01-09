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
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.database.TableData;
import com.netforceinfotech.inti.expenselist.ExpenseListActivity;
import com.netforceinfotech.inti.expenselist.ExpenseListDetailActivity;
import com.netforceinfotech.inti.expensereport.MyExpenseReportActivity;
import com.netforceinfotech.inti.expensesummary.ExpenseSummaryActivity;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.myprofile.MyProfileActivity;
import com.netforceinfotech.inti.util.Debugger;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;

public class EditExpensesListActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private static final int CHOOSE_OPTION = 101;
    final ArrayList<String> categories = new ArrayList<>();
    final ArrayList<String> providers = new ArrayList<>();
    final ArrayList<String> costcenters = new ArrayList<>();
    ArrayList<String> doctype = new ArrayList<>();
    Toolbar toolbar;
    Context context;
    ImageView imageViewList, imageViewChoose, imageViewAttached;
    private Intent intent;
    private MaterialDialog dialogDoB;
    TextView textViewDate, textViewCurrencyCode, textViewCategory, textViewProvider, editTextOriginalAmount;
    LinearLayout linearLayoutDate;
    RelativeLayout relativeLayoutCurrency, relativeLayoutCategory, relativeLayoutTaxRate, relativeLayoutDraft, relativeLayoutProvider, costCenterRelativeLayout, RelativeLayoutDocType;
    private MaterialDialog dialogAddress;
    private EditText etOtherProvider;
    Button buttonSubmit, buttonDelete;

    CheckBox checkboxbillable;
    static final String TAG = "EditExpenseList";
    String filePath, checkboxValue = "0";
    int MycheckboxValue = 0;
    EditText editTextExchangeRate, EditTextDescription, editTextConvertedAmount, editTextRUC, editTextSeries, editTextNumberofDocs, editTextIGV;
    TextView textViewCostCenter, textViewDocType, textViewDraft, textViewTaxRate;

    ArrayList<String> taxrate = new ArrayList<>();
    ArrayList<String> draft = new ArrayList<>();
    String eEmail, userType, userId, elId, customerId, erID;
    DatabaseOperations dop;
    UserSessionManager userSessionManager;

    String elImageUrl, elDate, elCurrency, elOriginalAmount, elExchangeRate, elConvertedAmount, elDescription, elCat, elRUC, elProvider, elCostCenter, elDocType, elSeries, elNumofDocs, elDraft, elTaxRate, elIGV, elBillable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expenses_list);
        context = this;

        try {
            Bundle bundle = getIntent().getExtras();

            elId = bundle.getString("elID");
            erID = bundle.getString("erID");

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        dop = new DatabaseOperations(this);
        userSessionManager = new UserSessionManager(this);
        InitExpenseTableDatas();
        initView();
        setupToolBar(getString(R.string.editExpenses));
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
        textViewTaxRate.setText(elTaxRate);
        editTextIGV.setText(elIGV);

        Glide.with(this).load(elImageUrl).centerCrop().error(R.drawable.ic_barcode).into(imageViewChoose);
        int val = Integer.parseInt(elBillable);

        if (val != MycheckboxValue) {

            checkboxbillable.setChecked(true);
        } else {
            checkboxbillable.setChecked(false);
        }


    }


    private void InitExpenseTableDatas() {

        try {

            HashMap<String, String> user = userSessionManager.getUserDetails();
            eEmail = user.get(UserSessionManager.KEY_EMAIL);
            userId = user.get(UserSessionManager.KEY_USERID);
            userType = user.get(UserSessionManager.KEY_USERTYPE);
            customerId = user.get(UserSessionManager.KEY_CUSTOMERID);

            Cursor cursor = dop.SelectFromExpensesTable(dop, elId);
            Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {


                do {

                    // get values from the database...

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


                } while (cursor.moveToNext());
            }
            cursor.close();


        } catch (Exception ex) {

            Log.d("ERROR", "unable to get Datas" + ex);
        }

    }

    private void initView() {
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);

        textViewTaxRate = (TextView) findViewById(R.id.textViewTaxRate);
        relativeLayoutTaxRate = (RelativeLayout) findViewById(R.id.relativeLayoutTaxRate);
        relativeLayoutTaxRate.setOnClickListener(this);
        relativeLayoutDraft = (RelativeLayout) findViewById(R.id.relativeLayoutDraft);
        relativeLayoutDraft.setOnClickListener(this);
        RelativeLayoutDocType = (RelativeLayout) findViewById(R.id.RelativeLayoutDocType);
        RelativeLayoutDocType.setOnClickListener(this);

        costCenterRelativeLayout = (RelativeLayout) findViewById(R.id.costCenterRelativeLayout);
        costCenterRelativeLayout.setOnClickListener(this);
        checkboxbillable = (CheckBox) findViewById(R.id.checkboxbillable);
        checkboxbillable.setOnClickListener(this);
        editTextExchangeRate = (EditText) findViewById(R.id.editTextExchangeRate);
        editTextConvertedAmount = (EditText) findViewById(R.id.editTextConvertedAmount);
        editTextRUC = (EditText) findViewById(R.id.editTextRUC);
        editTextSeries = (EditText) findViewById(R.id.editTextSeries);
        editTextNumberofDocs = (EditText) findViewById(R.id.editTextNumberofDocs);

        editTextIGV = (EditText) findViewById(R.id.editTextIGV);
        textViewCostCenter = (TextView) findViewById(R.id.textViewCostCenter);

        textViewDocType = (TextView) findViewById(R.id.textViewDocType);
        textViewDraft = (TextView) findViewById(R.id.textViewDraft);
        // ends here....

        EditTextDescription = (EditText) findViewById(R.id.EditTextDescription);
        editTextOriginalAmount = (TextView) findViewById(R.id.editTextOriginalAmount);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        imageViewAttached = (ImageView) findViewById(R.id.imageViewAttached);
        imageViewAttached.setVisibility(View.GONE);
        imageViewChoose = (ImageView) findViewById(R.id.imageViewChoose);
        textViewProvider = (TextView) findViewById(R.id.textViewProvider);
        relativeLayoutProvider = (RelativeLayout) findViewById(R.id.relativeLayoutProvider);
        relativeLayoutProvider.setOnClickListener(this);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        relativeLayoutCategory = (RelativeLayout) findViewById(R.id.relativeLayoutCategory);
        relativeLayoutCategory.setOnClickListener(this);
        textViewCurrencyCode = (TextView) findViewById(R.id.textViewCurrencyCode);
        relativeLayoutCurrency = (RelativeLayout) findViewById(R.id.relativeLayoutCurrency);
        relativeLayoutCurrency.setOnClickListener(this);
        linearLayoutDate = (LinearLayout) findViewById(R.id.linearLayoutDate);
        linearLayoutDate.setOnClickListener(this);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        imageViewChoose.setOnClickListener(this);
        imageViewList = (ImageView) findViewById(R.id.imageViewList);

        Glide.with(context).fromResource()
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 100)).load(R.drawable.ic_toggle).into(imageViewList);
        imageViewList.setOnClickListener(this);
        try {

            CountryPicker picker = CountryPicker.newInstance("Select CountryData");
            Country countryData = picker.getUserCountryInfo(this);
            String countryCode = countryData.getCode();
            try {
                Currency currency = picker.getCurrencyCode(countryCode);
                String currencyCode = currency.getCurrencyCode();
                String currencySymbol = currency.getSymbol();
                textViewCurrencyCode.setText(currencySymbol + "   " + currencyCode);
            } catch (Exception ex) {
                showMessage(getString(R.string.currency_not_found));
            }
        } catch (Exception ex) {

        }

        editTextExchangeRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                editTextConvertedAmount.setText(addNumbers());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextOriginalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextConvertedAmount.setText(addNumbers());
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
                Intent intent = new Intent(context, ExpenseListDetailActivity.class);
                intent.putExtra("elID", elId);
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
                    Intent intent = new Intent(EditExpensesListActivity.this, MyProfileActivity.class);
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
            case R.id.relativeLayoutCurrency:
                showCountryPopUp();
                break;
            case R.id.relativeLayoutCategory:
                setupCategory();
                break;
            case R.id.relativeLayoutProvider:
                setupProvider();
                break;
            case R.id.costCenterRelativeLayout:
                setupCostCenter();
                break;
            case R.id.RelativeLayoutDocType:
                setupDoctype();
                break;
            case R.id.relativeLayoutDraft:
                setupDraft();
                break;
            case R.id.relativeLayoutTaxRate:
                setupTaxRate();
                break;
            case R.id.buttonSubmit:
                ValidateAndSubmitDatas();
                showMessage(" Submit Clicked");

                //CallmeMR();
                break;
            case R.id.checkboxbillable:
                if (checkboxbillable.isChecked()) {
                    checkboxValue = "1";
                } else {
                    checkboxValue = "0";
                }
                break;

            case R.id.buttonDelete:
                DeleteExpensesData(elId);
                break;

        }
    }

    private void DeleteExpensesData(String elid) {

        showMessage("Delete button Clicked....");
        dop.DeleteExpensesList(dop, elid);
        Intent intent = new Intent(EditExpensesListActivity.this, ExpenseListActivity.class);

        intent.putExtra("erID", erID);
        startActivity(intent);
        finish();
    }

    private void setupTaxRate() {
        try {
            taxrate.clear();

        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutTaxRate);

        for (int i = 0; i < 5; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("Tax Rate " + i));
            taxrate.add("Tax Rate " + i);
        }
        // Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {

                textViewTaxRate.setText(taxrate.get(id));


            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();


    }

    private void setupDraft() {
        try {
            draft.clear();

        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutDraft);

        for (int i = 0; i < 10; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("draft " + i));
            draft.add("draft " + i);
        }
        // Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {

                textViewDraft.setText(draft.get(id));

            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void setupDoctype() {
        try {
            doctype.clear();

        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, RelativeLayoutDocType);

        for (int i = 0; i < 10; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("DocType " + i));
            doctype.add("Doctype " + i);
        }
        // Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                if (id == 10) {
                    showEditAddressPopup();
                } else {
                    textViewDocType.setText(doctype.get(id));
                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();


    }

    private void setupCostCenter() {


        try {
            costcenters.clear();
        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, costCenterRelativeLayout);
        for (int i = 0; i < 10; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("costCenter " + i));
            costcenters.add("costcenter " + i);
        }
        droppyBuilder.addMenuItem(new DroppyMenuItem("Others"));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                if (id == 10) {
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

        String textProvider = textViewProvider.getText().toString().trim();
        String exchangeRate = editTextExchangeRate.getText().toString().trim();
        String convertedAmount = editTextConvertedAmount.getText().toString().trim();
        String ruc = editTextRUC.getText().toString().trim();
        String series = editTextSeries.getText().toString().trim();

        String numofDocs = editTextNumberofDocs.getText().toString().trim();
        String taxRate = textViewTaxRate.getText().toString().trim();
        String igv = editTextIGV.getText().toString().trim();
        String costcenter = textViewCostCenter.getText().toString().trim();
        String doctype = textViewDocType.getText().toString().trim();
        String draft = textViewDraft.getText().toString().trim();
        int checkValue = Integer.parseInt(checkboxValue);

        Long tsLong = System.currentTimeMillis() / 1000;
        String creationDate = tsLong.toString();

        int User_Id = Integer.parseInt(userId);


        showMessage("Data entering.... ");

//        Log.d(TAG, " "+ erID);
//        Log.d(TAG," "+ creationDate);
//        Log.d(TAG," "+ eEmail);
//        Log.d(TAG, " "+ String.valueOf(User_Id));
//        Log.d(TAG," "+ imageUrl);
//        Log.d(TAG," "+ date);
//        Log.d(TAG," "+ currencycode);
//        Log.d(TAG, " "+ originalamount);
//        Log.d(TAG," "+ exchangeRate);
//        Log.d(TAG," "+ convertedAmount);
//        Log.d(TAG," "+ descriptions);
//        Log.d(TAG, " "+ category);
//        Log.d(TAG," "+ ruc);
//        Log.d(TAG," "+ textProvider);
//        Log.d(TAG," "+ costcenter);
//        Log.d(TAG, " "+ doctype);
//        Log.d(TAG," "+ series);
//        Log.d(TAG," "+ numofDocs);
//        Log.d(TAG," "+ draft);
//        Log.d(TAG, " "+ taxRate);
//        Log.d(TAG," "+ igv);
//        Log.d(TAG, " "+ String.valueOf(checkValue));
//        Log.d(TAG," "+ elId);


        // ends here..

        dop.UpdateExpensesList(dop, erID, creationDate, eEmail, User_Id, imageUrl, date, currencycode, originalamount, exchangeRate, convertedAmount, descriptions, category, ruc, textProvider, costcenter, doctype, series, numofDocs, draft, taxRate, igv, checkValue, elId);

        dop.getExpensesListData(dop);

        Cursor cursor = dop.getExpensesListData(dop);
        Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

        Intent intent = new Intent(EditExpensesListActivity.this, ExpenseListActivity.class);
        intent.putExtra("elID", elId);
        intent.putExtra("erID", erID);
        startActivity(intent);
        finish();

    }


    private void setupProvider() {
        try {
            providers.clear();
        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutProvider);
        for (int i = 0; i < 10; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("providers " + i));
            providers.add("providers " + i);
        }
        droppyBuilder.addMenuItem(new DroppyMenuItem("Others"));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                if (id == 10) {
                    showCustomeCostCenterPopup();
                } else {
                    textViewProvider.setText(providers.get(id));
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
                textViewCostCenter.setText(etOtherProvider.getText().toString());
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
        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutCategory);
        for (int i = 0; i < 5; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("Category " + i));
            categories.add("Category " + i);
        }
// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                textViewCategory.setText(categories.get(id));


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
                textViewProvider.setText(etOtherProvider.getText().toString());
                dialogAddress.dismiss();
            }
        });


    }

    private void showCountryPopUp() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                try {
                    Currency currency = picker.getCurrencyCode(code);
                    String currencyCode = currency.getCurrencyCode();
                    String currencySymbol = currency.getSymbol();
                    textViewCurrencyCode.setText(currencySymbol + "   " + currencyCode);
                } catch (Exception ex) {
                    showMessage(getString(R.string.currency_not_found));
                }
                picker.dismiss();
            }
        });
    }


    private String addNumbers() {
        int number1;
        int number2;
        if (editTextOriginalAmount.getText().toString() != "" && editTextOriginalAmount.getText().length() > 0) {
            number1 = Integer.parseInt(editTextOriginalAmount.getText().toString());
        } else {
            number1 = 0;
        }
        if (editTextExchangeRate.getText().toString() != "" && editTextExchangeRate.getText().length() > 0) {
            number2 = Integer.parseInt(editTextExchangeRate.getText().toString());
        } else {
            number2 = 0;
        }

        return Integer.toString(number1 * number2);
    }
}
