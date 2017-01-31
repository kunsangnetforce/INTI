package com.netforceinfotech.inti.general;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by Tenzin on 12/29/2016.
 */

public class ConnectivityCheck {


    Context context;

    public ConnectivityCheck(Context context) {
        this.context = context;
    }

    public Boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()==true){

           return true;

        }

        showMessage("No internet Connection");

        return false;

    }


    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
