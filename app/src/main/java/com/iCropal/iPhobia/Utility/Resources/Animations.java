package com.iCropal.iPhobia.Utility.Resources;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.iCropal.iPhobia.R;


public class Animations {
    public Animation fadeIn, shake, slideLeft, slideBackLeft;
    public Animation fadeInExtra, fadeOutExtra;
    public Animation fadeOutFast, slideUp;
    public Animation fadeOut;
    public Animation slideRight;
    public Animation slideFromRight;


    public Animations(Context context) {
        slideRight = AnimationUtils.loadAnimation(context, R.anim.slide_right);
        slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        slideBackLeft = AnimationUtils.loadAnimation(context, R.anim.slide_back_left);
        fadeInExtra = AnimationUtils.loadAnimation(context, R.anim.fade_in_extra);
        fadeOutExtra = AnimationUtils.loadAnimation(context, R.anim.fade_out_extra);
        fadeOutFast = AnimationUtils.loadAnimation(context, R.anim.fade_out_fast);
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        slideFromRight = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
    }

}
