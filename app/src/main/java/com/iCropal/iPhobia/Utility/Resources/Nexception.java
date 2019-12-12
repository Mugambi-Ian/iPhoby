package com.iCropal.iPhobia.Utility.Resources;

import android.util.Log;

public class Nexception extends Exception {
    private static final String TAG = "Nexception";

    public void logError() {
        Log.i(TAG, "Error: " + this.getLocalizedMessage());
    }
}
