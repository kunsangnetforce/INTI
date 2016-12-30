package com.netforceinfotech.inti.general;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.netforceinfotech.inti.login.LoginActivity;

/**
 * Created by Tenzin on 12/29/2016.
 */

public class UserSessionManager {

    public SharedPreferences pref;

    public SharedPreferences.Editor editor;
    Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "INTI_EXPENSES";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    private static final String KEY_USERID = "userID";
    private static final String KEY_CUSTOMERID = " customerID";

    public UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    //Create login session
    public void createUserLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

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

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    private boolean isUserLoggedIn() {

        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public static String getKeyUserid() {
        return KEY_USERID;
    }

    public static String getKeyEmail() {
        return KEY_EMAIL;
    }
    public void setKeyEmail(String email){

        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }

    public static String getKeyCustomerid() {
        return KEY_CUSTOMERID;
    }

    public void setKeyCustomerid(String customerid){

        editor.putString(KEY_CUSTOMERID,customerid);
        editor.commit();

    }

}
