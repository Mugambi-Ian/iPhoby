package com.iCropal.iPhobia.Ui.Settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.iCropal.iPhobia.R;
import com.tombayley.activitycircularreveal.CircularReveal;

public class Settings extends AppCompatActivity {

    private CircularReveal circularReveal;

    @Override
    public void onBackPressed() {
        circularReveal.unRevealActivity(this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        circularReveal = new CircularReveal(findViewById(R.id.AS_rootView));
        circularReveal.onActivityCreate(getIntent());

    }
}
