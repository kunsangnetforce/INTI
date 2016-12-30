package com.netforceinfotech.inti.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.netforceinfotech.inti.util.Debugger;
import com.netforceinfotech.inti.util.Validation;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import se.simbio.encryption.Encryption;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearLayoutLanguage;
    ImageView imageViewLogo;
    Context context;
    TextView textViewForgotPassword;
    CoordinatorLayout coordinateLayout;
    EditText etEmail, etPassword;
    private boolean validate = true;
    String userType = "normal",eEmail,userPass;
    ProgressDialog pd;
    UserSessionManager userSessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
    }

    private void initView() {
        userSessionManager = new UserSessionManager(this);
        pd = new ProgressDialog(context);
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
                if (etPassword.getText().toString().trim().length() <= 4) {
                    showMessage("Input password");
                    return;
                }
                signIn(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                break;
        }
    }


    private void signIn(final String email, String password) {

         userPass =password;

        pd.setMessage("Logging In....");
        pd.show();

        // check if internet connection or not...
        ConnectivityManager cm = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()==true){

            String additionalparameter= email+"&password="+password;

            String BaseUrl ="http://netforce.biz/inti_expense/api/api.php?type=login&email="+additionalparameter;

//        JsonObject json=new JsonObject();
//        json.addProperty("type", "login");
//        json.addProperty("email_id",email);
//        json.addProperty("password",password);

            Ion.with(context)
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



                                    JsonObject json = result.getAsJsonObject("data");

                                    eEmail = json.get("email_id").getAsString();
                                    userType= json.get("user_type").getAsString();
                                    String userID = json.get("user_id").getAsString();
                                    String customerID = json.get("customer_id").getAsString();
                                   // String userName = json.get("user_name").getAsString();
                                    userSessionManager.createUserLoginSession("INTI",eEmail);

                                    userSessionManager.setKeyEmail(eEmail);
                                    userSessionManager.setKeyCustomerid(customerID);
                                    userSessionManager.setKeyUserid(userID);

                                    Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                                    intent.putExtra("eEmail",eEmail);
                                    intent.putExtra("userType",userType);
                                    intent.putExtra("userID",userID);
                                    intent.putExtra("customerID",customerID);
                                    intent.putExtra("userPass",userPass);
                                   // intent.putExtra("userName",userName);
                                    startActivity(intent);
                                    finish();

                                }




                            }else {

                             showMessage("No Data.");

                            }

                        }
                    });

        }else{
            pd.dismiss();

            Snackbar snackbar = Snackbar
                    .make(coordinateLayout,getResources().getString(R.string.nointernetconnection), Snackbar.LENGTH_LONG);
            snackbar.show();
        }








//        setupSelfSSLCert();
//
//
//        JsonObject js=new JsonObject();
//        js.addProperty("type", "login");
//        js.addProperty("vEmail",email);
//        js.addProperty("vPassword",password);
//        // change the login url...
//
//        String Webservice_login_url=getResources().getString(R.string.webservice_api_url);
//        Log.e("Webservice_login_url",Webservice_login_url);
//        Ion.with(context)
//                .load(Webservice_login_url)
//                .setJsonObjectBody(js)
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//
//                        if(result!=null){
//
//                            Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
//                            intent.putExtra("eEMAIL",email);
//                            intent.putExtra("userType",userType);
//                            startActivity(intent);
//
//
//
//                        }else {
//
//                            showMessage("Something is wrong... ");
////                            Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
////                            intent.putExtra("eEMAIL",email);
////
////                            startActivity(intent);
//
//                        }
//
//                    }
//                });

//        // Pass the urls with credientials..
//        String key = "netforcekey";
//        String salt = "servernetforce";
//        byte[] iv = new byte[16];
//
//        //use email address to find the flags of the user.......
//
//
//        Encryption encryption = Encryption.getDefault(key, salt, iv);
//        String encrypted = encryption.encryptOrNull(password);
//        String decrypted = encryption.decryptOrNull(encrypted);
//        Debugger.i("kunsang_enc_dec", password + " : \n" + encrypted + "\n" + decrypted);
//        Intent intent = new Intent(context, DashboardActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("email", email);
//        intent.putExtras(bundle);
//        startActivity(intent);


    }



    private static class Trust implements X509TrustManager {

        /**
         * {@inheritDoc}
         */
        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType)
                throws CertificateException {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType)
                throws CertificateException {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }


    public void setupSelfSSLCert() {
        final LoginActivity.Trust trust = new LoginActivity.Trust();
        final TrustManager[] trustmanagers = new TrustManager[]{trust};
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustmanagers, new SecureRandom());
            Ion.getInstance(context, "rest").getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustmanagers);
            Ion.getInstance(context, "rest").getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final KeyManagementException e) {
            e.printStackTrace();
        }
    }

//    private void signIn(String email, String password) {
//        String key = "netforcekey";
//        String salt = "servernetforce";
//        byte[] iv = new byte[16];
//        Encryption encryption = Encryption.getDefault(key, salt, iv);
//        String encrypted = encryption.encryptOrNull(password);
//        String decrypted = encryption.decryptOrNull(encrypted);
//        Debugger.i("kunsang_enc_dec", password + " : \n" + encrypted + "\n" + decrypted);
//        Intent intent = new Intent(context, DashboardActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("email", email);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//
//    }
}
