package com.iCropal.iPhobia.Utility.Resources;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.iCropal.iPhobia.R;


public class Drawables {
    public Drawable btnOn, btnOff;

    public Drawables(Context context) {
        btnOn = ContextCompat.getDrawable(context, R.drawable.btn_on);
        btnOff = ContextCompat.getDrawable(context, R.drawable.btn_off);
    }
}
