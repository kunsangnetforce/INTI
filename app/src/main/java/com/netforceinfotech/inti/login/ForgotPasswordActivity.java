package com.netforceinfotech.inti.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.util.Validation;

public class ForgotPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button buttonRecoverPassword;
    EditText etEmail;
    Context context;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setupToolBar("Forgot Password");
        context = this;
        pd = new ProgressDialog(this);
        pd.setMessage("Recovering Password...");
        buttonRecoverPassword= (Button) findViewById(R.id.buttonRecoverPassword);
        etEmail= (EditText) findViewById(R.id.etEmail);
        buttonRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Validation.isEmailAddress(etEmail,true)){
                    showMessage("Email not valid");
                    return;
                }
                pd.show();
                forgotPassword(etEmail.getText().toString().trim());
            }
        });
    }

    private void forgotPassword(String trim) {

        try{
            String BaseUrl = "http://netforce.biz/inti_expense/api/api.php?type=forgetpwd&email="+trim;

            Ion.with(this)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error


                            if (result!=null){

                                String status =result.get("status").getAsString();

                                showMessage(status);


                                if(status.equalsIgnoreCase("success")){

                                    pd.dismiss();
                                    Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                                    startActivity(intent);


                                }else {
                                    pd.dismiss();
                                    etEmail.getText().clear();
                                    etEmail.requestFocus();
                                    showMessage("Enter a correct email...");
                                    InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
//Hide:
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//Show

                                }




                            }else {
                                pd.dismiss();
                                etEmail.getText().clear();
                                etEmail.requestFocus();

                                showMessage("Please enter a correct email..");

                            }

                        }
                    });

        }catch (Exception ex){

            showMessage("Enter a Correct Email...");
        }




    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void setupToolBar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageViewSetting= (ImageView) toolbar.findViewById(R.id.imageViewSetting);
        TextView textViewTitle= (TextView) toolbar.findViewById(R.id.textViewTitle);
        imageViewSetting.setVisibility(View.INVISIBLE);
        textViewTitle.setText(title);
        ImageView imageViewBack= (ImageView) toolbar.findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

    }
}
