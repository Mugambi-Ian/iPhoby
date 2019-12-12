package com.iCropal.iPhobia.Ui.User;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.AppUser;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Ui.Home.Home;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Resources.Drawables;
import com.iCropal.iPhobia.Utility.Resources.Time;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

public class UserProfile extends AppCompatActivity {
    private AppUser u;
    private Drawables drawables;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        u = RuntimeData.appUser;
        drawables = new Drawables(this);
        loadData(u);
        RuntimeData.referenceManger.onDataBaseLoaded(() -> {
            u = RuntimeData.appUser;
            loadData(u);
        });

    }

    private void loadData(AppUser u) {
        try {
            if (u != null) {
                findViewById(R.id.AUP_userGender).setBackground(drawables.getGender(u.getUserGender()));
                findViewById(R.id.AUP_btnLogOut).setOnClickListener(v -> {
                    RuntimeData.home.finish();
                    RuntimeData.referenceManger.logOut();
                    RuntimeData.cleanse();
                    startActivity(new Intent(UserProfile.this, Home.class));
                    finish();
                });
                findViewById(R.id.AUP_btnEditInfo).setOnClickListener(v -> {
                    startActivity(new Intent(UserProfile.this, AccountInformation.class).putExtra(Constants.UserPhoneNumber, RuntimeData.referenceManger.getPhoneNumber()));
                });
                ((TextView) findViewById(R.id.AUP_userName)).setText(u.getUserName());
                ((TextView) findViewById(R.id.AUP_dateOfBirth)).setText(u.getDateOfBirth());
                ((TextView) findViewById(R.id.AUP_phoneNumber)).setText(u.getPhoneNumber());
                ((TextView) findViewById(R.id.AUP_userAge)).setText(Time.getUserAge(u.getDateOfBirth()) + " yrs");
                Glide.with(this).load(u.getTnDp()).into((ImageView) findViewById(R.id.AUP_userDp));
            }
        } catch (Exception nexception) {
            Log.e("UserProfile", nexception.getLocalizedMessage());
        }
    }
}
