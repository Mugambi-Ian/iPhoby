package com.iCropal.iPhobia.Utility.Transmittors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iCropal.iPhobia.DataModel.AppUser;
import com.iCropal.iPhobia.DataModel.DataBase;
import com.iCropal.iPhobia.DataModel.User;
import com.iCropal.iPhobia.Utility.Resources.Constants;

import java.io.IOException;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class PreferenceManger {
    private SharedPreferences databaseReference;
    private SharedPreferences userIdPreferences;

    public void saveUserDetails(AppUser appUser) {
        userIdPreferences.edit().putString(Constants.UserName, appUser.getUserName()).apply();
        userIdPreferences.edit().putString(Constants.UserPhoneNumber, appUser.getPhoneNumber()).apply();
        userIdPreferences.edit().putString(Constants.AcDp, appUser.getAcDp()).apply();
        userIdPreferences.edit().putString(Constants.TnDP, appUser.getTnDp()).apply();
        userIdPreferences.edit().putString(Constants.DateOfBirth, appUser.getDateOfBirth()).apply();
        userIdPreferences.edit().putString(Constants.UserId, appUser.getUserId()).apply();
    }


    public PreferenceManger(Context context) {
        databaseReference = context.getSharedPreferences(Constants.User_Database, MODE_PRIVATE);
        userIdPreferences = context.getSharedPreferences(Constants.UserPhoneNumber, MODE_PRIVATE);
        if (getPhoneNumber() == null) {
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    RuntimeData.pDetails = dataSnapshot;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            FirebaseDatabase.getInstance().getReference().child(getPhoneNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    RuntimeData.pDetails = dataSnapshot;
                    RuntimeData.appUser = DataBase.getAppUserID(dataSnapshot);
                    saveUserDetails(DataBase.getAppUserID(dataSnapshot));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public String getUserDatabase() {
        return databaseReference.getString(Constants.User_Database, null);
    }

    public String getPhoneNumber() {
        return userIdPreferences.getString(Constants.UserPhoneNumber, null);
    }

    public void setUserDatabase(User appUser) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(appUser);
            Log.i(TAG, "setUserDatabase: " + json);
            databaseReference.edit().putString(Constants.User_Database, json).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return userIdPreferences.getString(Constants.UserId, null);
    }

    public void saveUserIds(String phoneNumber) {
        userIdPreferences.edit().putString(Constants.UserPhoneNumber, phoneNumber).apply();
    }

    public void logOut() {
        userIdPreferences.edit().putString(Constants.UserName, null).apply();
        userIdPreferences.edit().putString(Constants.UserPhoneNumber, null).apply();
        userIdPreferences.edit().putString(Constants.AcDp, null).apply();
        userIdPreferences.edit().putString(Constants.TnDP, null).apply();
        userIdPreferences.edit().putString(Constants.DateOfBirth, null).apply();
        userIdPreferences.edit().putString(Constants.UserId, null).apply();
        databaseReference.edit().putString(Constants.User_Database, null).apply();
    }
}
