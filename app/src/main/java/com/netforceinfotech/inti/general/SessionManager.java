package com.netforceinfotech.inti.general;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tenzin on 1/24/2017.
 */

public class SessionManager {




    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String ISFIRSTTIME = "first_time";
    private static final String USER_ID = "user_id";
    private static final String ISLOGGEDIN = "isloggedin";
    private static final String RADIUS = "searchradius";
    private static final String LOGINMODE = "loginmode";
    private static final String PASSWORD = "password";
    private static final String KEEPMEINLOOP = "keepmeinloop";
    private static final String MESSAGENOTIFICATION = "messagenotification";
    private static final String EMAILNOTIFICATION = "emailnitification";
    private static final String INAPPVIBRATION = "inappvibration";
    private static final String PROFILE_IMAGE = "profileiamge";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String regid) {
        editor.putString(USER_ID, regid);
        editor.commit();
    }




    public void setTeamNotification(String team_name, String sound) {
        editor.putString(team_name, sound);
        editor.commit();

    }

    public boolean getIsFirstTime() {
        return pref.getBoolean(ISFIRSTTIME, true);
    }

    public void setIsFirstTime(boolean regid) {
        editor.putBoolean(ISFIRSTTIME, regid);
        editor.commit();
    }


    public boolean getTeamNotification(String team_name) {
        return pref.getBoolean(team_name, false);

    }

    public void setTeamNotification(String team_name, boolean sound) {
        editor.putBoolean(team_name, sound);
        editor.commit();
    }


    public String getEmail() {

        return pref.getString(EMAIL, "");
    }

    public void setEmail(String regid) {
        editor.putString(EMAIL, regid);
        editor.commit();
    }

    public String getName() {

        return pref.getString(NAME, "");
    }

    public void setName(String regid) {
        editor.putString(NAME, regid);
        editor.commit();
    }

    public void clearData() {
        editor.clear().commit();
    }


    public boolean getIsLogedIn() {
        return pref.getBoolean(ISLOGGEDIN, false);
    }

    public void setIsLoggedIn(boolean isloggedin) {
        editor.putBoolean(ISLOGGEDIN, isloggedin);
        editor.commit();
    }

    public int getSearchRadius() {
        return pref.getInt(RADIUS, 5);
    }

    public void setSearchRaius(int radius) {
        editor.putInt(RADIUS, radius);
        editor.commit();
    }

    public void setLoginMode(int i) {
        editor.putInt(LOGINMODE, i);
        editor.commit();
    }

    public int getLoginMode() {
        return pref.getInt(LOGINMODE, 0);
    }

    public String getpassword() {
        return pref.getString(PASSWORD, "");

    }

    public void setPassword(String pw) {
        editor.putString(PASSWORD, pw);
        editor.commit();
    }

    public boolean getKeepMeInLoop() {
        return pref.getBoolean(KEEPMEINLOOP, true);

    }

    public void setKeepMeInLoop(boolean pw) {
        editor.putBoolean(KEEPMEINLOOP, pw);
        editor.commit();
    }

    public boolean getMessageNotification() {
        return pref.getBoolean(MESSAGENOTIFICATION, true);

    }

    public void setMessageNotification(boolean pw) {
        editor.putBoolean(MESSAGENOTIFICATION, pw);
        editor.commit();
    }

    public boolean getEmailNotification() {
        return pref.getBoolean(EMAILNOTIFICATION, true);

    }

    public void setEmailNotification(boolean pw) {
        editor.putBoolean(EMAILNOTIFICATION, pw);
        editor.commit();
    }

    public boolean getInAppVibration() {
        return pref.getBoolean(INAPPVIBRATION, true);

    }

    public void setInAppVibration(boolean pw) {
        editor.putBoolean(INAPPVIBRATION, pw);
        editor.commit();
    }

    public void setProfileImage(String profile_image) {
        editor.putString(PROFILE_IMAGE, profile_image);
        editor.commit();
    }

    public String getProfileImage() {
        return pref.getString(PROFILE_IMAGE, "");

    }
}
