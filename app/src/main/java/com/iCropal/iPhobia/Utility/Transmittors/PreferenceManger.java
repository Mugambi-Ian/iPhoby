package com.iCropal.iPhobia.Utility.Transmittors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iCropal.iPhobia.DataModel.User;
import com.iCropal.iPhobia.Utility.Resources.Constants;

import java.io.IOException;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class PreferenceManger {
    SharedPreferences databaseReference;
    SharedPreferences userIdPreferences;

    public PreferenceManger(Context context) {
        databaseReference = context.getSharedPreferences(Constants.User_Database, MODE_PRIVATE);
        userIdPreferences = context.getSharedPreferences(Constants.User_PhoneNumber, MODE_PRIVATE);
    }

    public String getUserDatabase() {
        return databaseReference.getString(Constants.User_Database, null);
    }

    public String getPhoneNumber() {
        return userIdPreferences.getString(Constants.User_PhoneNumber,null);
    }

    public void setUserDatabase(User appUser) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(appUser);
            Log.i(TAG, "setUserDatabase: " + json);
            databaseReference.edit().putString(Constants.User_Database, json).apply();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
