package com.netforceinfotech.inti.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.util.Validation;

public class ForgotPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button buttonRecoverPassword;
    EditText etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setupToolBar("Forgot Password");
        buttonRecoverPassword= (Button) findViewById(R.id.buttonRecoverPassword);
        etEmail= (EditText) findViewById(R.id.etEmail);
        buttonRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Validation.isEmailAddress(etEmail,true)){
                    showMessage("Email not valid");
                    return;
                }
                forgotPassword(etEmail.getText().toString().trim());
            }
        });
    }

    private void forgotPassword(String trim) {
        showMessage("forgot password method called");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void setupToolBar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageViewSetting= (ImageView) toolbar.findViewById(R.id.imageViewSetting);
        TextView textViewTitle= (TextView) toolbar.findViewById(R.id.textViewTitle);
        imageViewSetting.setVisibility(View.INVISIBLE);
        textViewTitle.setText(title);

    }
}
