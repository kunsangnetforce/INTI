package com.netforceinfotech.inti.addexpenses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.netforceinfotech.inti.R;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

public class TextImageExpenseActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int CHOOSE_OPTION = 101;
    final ArrayList<String> categories = new ArrayList<>();
    final ArrayList<String> providers = new ArrayList<>();
    Toolbar toolbar;
    Context context;
    ImageView imageViewList;
    private Intent intent;
    private MaterialDialog dialogDoB;
    TextView textViewDate, textViewCurrencyCode, textViewCategory, textViewProvider;
    LinearLayout linearLayoutDate;
    RelativeLayout relativeLayoutCurrency, relativeLayoutCategory, relativeLayoutProvider;
    private MaterialDialog dialogAddress;
    private EditText etOtherProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_image_expense);
        context = this;
        setupToolBar(getString(R.string.text_image));
        initView();
    }

    private void initView() {
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
        findViewById(R.id.imageViewChoose).setOnClickListener(this);
        imageViewList = (ImageView) findViewById(R.id.imageViewList);
        Glide.with(context).fromResource()
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 100)).load(R.drawable.ic_toggle).into(imageViewList);
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
        }
    }

    private void setupProvider() {
        try {
            providers.clear();
        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutProvider);
        for (int i = 0; i < 10; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem("providers " + i));
            categories.add("providers " + i);
        }
        droppyBuilder.addMenuItem(new DroppyMenuItem("Others"));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                if (id == 10) {
                    showEditAddressPopup();
                } else {
                    textViewProvider.setText(providers.get(id));
                }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
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
            }
        }
    }

    private void setupCategory() {
        try {
            categories.clear();
        } catch (Exception ex) {

        }
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, relativeLayoutCategory);
        for (int i = 0; i < 10; i++) {
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
}
