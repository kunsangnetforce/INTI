package com.netforceinfotech.inti.general;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.netforceinfotech.inti.login.LoginActivity;

import java.util.HashMap;

/**
 * Created by Tenzin on 12/29/2016.
 */

public class UserSessionManager {

    public SharedPreferences sharedPreferences;

    public SharedPreferences.Editor editor;

    Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "INTI_EXPENSES";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_ERID = "erid";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_USERID = "userID";

    public static final String KEY_CUSTOMERID = " customerID";
    public static final String KEY_USERTYPE ="userType";

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }


    //Create login session
    public void createUserLoginSession(String email, String customerID,String userID){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_CUSTOMERID, customerID);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        // storing userID in the pref...
        editor.putString(KEY_USERID, userID);


        // commit changes
        editor.commit();
    }


    public void setKeyUserid(String userid){

        editor.putString(KEY_USERID,userid);
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);

        if(Build.VERSION.SDK_INT >=11){
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        } else{

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        }

        context.startActivity(i);


    }

    private boolean isUserLoggedIn() {

        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }




    public void setKeyEmail(String email){

        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }



    public void setKeyCustomerid(String customerid){

        editor.putString(KEY_CUSTOMERID,customerid);
        editor.commit();

    }

    public void setKeyUsertype(String usertype){

        editor.putString(KEY_USERTYPE,usertype);
        editor.commit();

    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user id
        user.put(KEY_USERID, sharedPreferences.getString(KEY_USERID, null));

        // user email id
        user.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));

        //customer id
        user.put(KEY_CUSTOMERID,sharedPreferences.getString(KEY_CUSTOMERID,null));
        //
        // usertype...
        user.put(KEY_USERTYPE,sharedPreferences.getString(KEY_USERTYPE,null));

        // return user
        return user;
    }

}
