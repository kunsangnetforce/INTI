package com.netforceinfotech.inti.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.netforceinfotech.inti.database.DatabaseOperations;
import com.netforceinfotech.inti.general.ConnectivityCheck;
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
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.concurrent.ExecutionException;

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
    String userType = "normal", eEmail, userPass;
    ProgressDialog pd;
    UserSessionManager userSessionManager;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        userSessionManager = new UserSessionManager(getApplicationContext());
        //userSessionManager.getIsLogedIn();
        checkIfAlreadyLogedIn();
        initView();


    }

    private void checkIfAlreadyLogedIn() {

            if(userSessionManager.getIsLogedIn()){
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }



    }


    private void initView() {






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

    private void SnackbarMessage(String msg) {

        Snackbar snackbar = Snackbar
                .make(coordinateLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void signIn(final String email, String password) {


        userPass = password;

        pd.setMessage("Logging In....");
        pd.show();

        // check if internet connection or not...
        ConnectivityManager cm = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {

            String additionalparameter = email + "&password=" + password;

            String BaseUrl = "http://161.202.19.38/inti_expense/api/api.php?type=login&email=" + additionalparameter;

            Log.d("LoginData",BaseUrl);

            Ion.with(context)
                    .load(BaseUrl)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error


                            Log.d("DATA", String.valueOf(result));


                            if (result != null) {

                                String status = result.get("status").getAsString();

                                if (status.equalsIgnoreCase("success")) {

                                    pd.dismiss();

                                    JsonObject json = result.getAsJsonObject("data");
                                    try {


                                        eEmail = json.get("email_id").getAsString();
                                        userType = json.get("user_type").getAsString();

                                        Log.d("SDFSDFSDAFSDF", userType);
                                        String userID = json.get("user_id").getAsString();
                                        String customerID = json.get("customer_id").getAsString();
                                        //String userType = json.get("user_type").getAsString();
                                        String username = json.get("user_name").getAsString();
                                        String usercontact = json.get("contact_no").getAsString();
                                        String profimg = json.get("image").getAsString();
                                        String basecurrency = json.get("BASE_CURRENCY_CODE").getAsString();
                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                        String userClass = json.get("USERCLASS").getAsString();
                                        String userBaseCurrencySymbol=json.get("CURRENCY_SYMBOL").getAsString();
                                        Log.d("UserCatSybmo"," "+userBaseCurrencySymbol);


                                        // String userName = json.get("user_name").getAsString();
                                        userSessionManager.createUserLoginSession(eEmail, customerID, userID,userType);

                                        userSessionManager.setKeyEmail(eEmail);
                                        userSessionManager.setKeyCustomerid(customerID);
                                        userSessionManager.setKeyUserid(userID);
                                        userSessionManager.setKeyUsertype(userType);
                                        userSessionManager.setIsLoggedIn(true);
                                        userSessionManager.setKeyUserclass(userClass);
                                        userSessionManager.setKeyCurrency(basecurrency);
                                        userSessionManager.setKeyCurrencySymbol(userBaseCurrencySymbol);

                                        DatabaseOperations databaseOperations = new DatabaseOperations(context);

                                        databaseOperations.AddUsers(databaseOperations, userID, username, eEmail, basecurrency, customerID, usercontact, profimg, date);

                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } catch (Exception ex) {
                                        ex.fillInStackTrace();
                                    }


                                } else if (status.equalsIgnoreCase("failed")) {
                                    pd.dismiss();

                                    String errorCode = result.get("Error Code").getAsString();

                                    if (errorCode.equalsIgnoreCase("103")) {


                                        showMessage(getResources().getString(R.string.inCorrectEmail));
                                        etEmail.getText().clear();
                                        etEmail.setHint(getResources().getString(R.string.invalidEmail));
                                        etEmail.setHintTextColor(ContextCompat.getColor(context, R.color.red));
                                        etEmail.requestFocus();


                                    } else {

                                        showMessage(getResources().getString(R.string.incorrectPassword));
                                        etPassword.getText().clear();
                                        etPassword.setHint(getResources().getString(R.string.errorPass));
                                        etPassword.setHintTextColor(ContextCompat.getColor(context, R.color.red));
                                        etPassword.requestFocus();
                                    }
                                }


                            } else {
                                pd.dismiss();

                                SnackbarMessage(getResources().getString(R.string.nointernetconnection));
                            }

                        }
                    });

        } else {

            pd.dismiss();
            SnackbarMessage(getResources().getString(R.string.nointernetconnection));


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


//    private void signIn(final String email, String password) {
//
//        userPass =password;
//
//        pd.setMessage("Logging In....");
//        pd.show();
//
//
//
//
//
//
//                                    eEmail ="ajay@netforce.co";
//                                    userType= "3";
//                                    String userID = "1";
//                                    String customerID = "105";
//
//                                    userSessionManager.createUserLoginSession(eEmail,customerID,userID);
//
//                                    userSessionManager.setKeyEmail(eEmail);
//                                    userSessionManager.setKeyCustomerid(customerID);
//                                    userSessionManager.setKeyUserid(userID);
//                                    userSessionManager.setKeyUsertype(userType);
//
//
//                                    Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
//                                    intent.putExtra("eEmail",eEmail);
//                                    intent.putExtra("userType",userType);
//                                    intent.putExtra("userID",userID);
//                                    intent.putExtra("customerID",customerID);
//                                    intent.putExtra("userPass",userPass);
//                                    // intent.putExtra("userName",userName);
//                                    startActivity(intent);
//                                    finish();
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    }


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


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this,getResources().getString(R.string.clickagaintoexit), Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
