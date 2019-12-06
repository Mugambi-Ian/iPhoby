package com.iCropal.iPhobia.Utility.Resources;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.iCropal.iPhobia.R;


public class Drawables {
    public Drawable btnOn, btnOff;
    public Drawable bgFtR, bgFtL, bgE;
    public Drawable mI, fI, nI;

    public Drawables(Context context) {
        btnOn = ContextCompat.getDrawable(context, R.drawable.btn_on);
        btnOff = ContextCompat.getDrawable(context, R.drawable.btn_off);
        bgFtL = ContextCompat.getDrawable(context, R.drawable.ft_bgl);
        bgFtR = ContextCompat.getDrawable(context, R.drawable.ft_bgr);
        bgE = ContextCompat.getDrawable(context, R.drawable.bp_);
        mI = ContextCompat.getDrawable(context, R.drawable.male);
        fI = ContextCompat.getDrawable(context, R.drawable.female);
        nI = ContextCompat.getDrawable(context, R.drawable.transgender);
    }

    public Drawable getGender(String userGender) {
        switch (userGender) {
            case "Male":
                return mI;
            case "Female":
                return fI;
            case "Non-Binary":
                return nI;
            default:
                return null;
        }
    }
}
