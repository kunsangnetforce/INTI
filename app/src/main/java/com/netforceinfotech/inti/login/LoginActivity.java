package com.netforceinfotech.inti.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.util.Debugger;
import com.netforceinfotech.inti.util.Validation;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import se.simbio.encryption.Encryption;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearLayoutLanguage;
    ImageView imageViewLogo;
    Context context;
    TextView textViewForgotPassword;
    CoordinatorLayout coordinateLayout;
    EditText etEmail, etPassword;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
    }

    private void initView() {
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        textViewForgotPassword.setOnClickListener(this);
        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        linearLayoutLanguage = (LinearLayout) findViewById(R.id.linearLayoutLanguage);
        setupLanguageDropDown(linearLayoutLanguage);
        imageViewLogo = (ImageView) findViewById(R.id.imageViewLogo);
        Glide.with(context).fromResource()
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 100)).load(R.drawable.logo).into(imageViewLogo);

    }

    private void setupLanguageDropDown(LinearLayout linearLayoutLanguage) {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(LoginActivity.this, linearLayoutLanguage);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem("English"))
                .addMenuItem(new DroppyMenuItem("Spain"));

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewForgotPassword:
                Intent intent = new Intent(context, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.buttonSignIn:
                if (!Validation.isEmailAddress(etEmail, validate)) {
                    showMessage("Email not valid");
                    return;
                }
                if (etPassword.getText().toString().trim().length() <= 0) {
                    showMessage("Input password");
                    return;
                }
                signIn(etEmail.getText().toString(), etPassword.getText().toString());
                break;
        }
    }

    private void signIn(String email, String password) {
        String key = "netforcekey";
        String salt = "servernetforce";
        byte[] iv = new byte[16];
        Encryption encryption = Encryption.getDefault(key, salt, iv);
        String encrypted = encryption.encryptOrNull(password);
        String decrypted = encryption.decryptOrNull(encrypted);
        Debugger.i("kunsang_enc_dec", password + " : \n" + encrypted + "\n" + decrypted);
        Intent intent=new Intent(context, DashboardActivity.class);
        startActivity(intent);


    }
}
