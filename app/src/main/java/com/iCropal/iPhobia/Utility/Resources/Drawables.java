package com.iCropal.iPhobia.Utility.Resources;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.iCropal.iPhobia.R;


public class Drawables {
    public Drawable btnOn, btnOff;
    public Drawable bgFtR, bgFtL, bgE;

    public Drawables(Context context) {
        btnOn = ContextCompat.getDrawable(context, R.drawable.btn_on);
        btnOff = ContextCompat.getDrawable(context, R.drawable.btn_off);
        bgFtL = ContextCompat.getDrawable(context, R.drawable.ft_bgl);
        bgFtR = ContextCompat.getDrawable(context, R.drawable.ft_bgr);
        bgE = ContextCompat.getDrawable(context, R.drawable.bp_);
    }
}
