package com.netforceinfotech.inti.util;

import android.util.Log;

/**
 * Created by Netforce on 10/21/2016.
 */

public class Debugger {
    private static boolean isActive = true;

    public static void i(String tag, String msg) {
        if (isActive) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isActive) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isActive) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isActive) {
            Log.w(tag, msg);
        }
    }

}
