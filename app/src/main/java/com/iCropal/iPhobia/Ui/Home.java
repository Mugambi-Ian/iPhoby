package com.iCropal.iPhobia.Ui;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iCropal.iPhobia.DataModel.DataBase;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.ApiManager.DataFetcher;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Transmittors.PreferenceManger;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.tombayley.activitycircularreveal.CircularReveal;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Home extends AppCompatActivity {
    private DataFetcher fetcher;

    @Override
    protected void onResume() {
        super.onResume();
        updateDatabase();
    }

    public void updateDatabase() {
        fetcher.getData(Constants.API_Url + "?phoneNumber=0715643789", DataFetcher.RC_GetAllRecords);

    }

    private static final String TAG = "Home";
    private PreferenceManger preferenceManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fetcher = new DataFetcher();
        RuntimeData.home = this;
        preferenceManger = new PreferenceManger(Home.this);
        RuntimeData.referenceManger = preferenceManger;
        fetcher.setResultListener(new DataFetcher.DataListener() {
            @Override
            public void onSuccess(String data, int r) {
                try {
                    if (r == DataFetcher.RC_GetAllRecords) {
                        RuntimeData.dataBase = new DataBase(DataBase.getRecords(new JSONArray(data)));
                        preferenceManger.setUserDatabase(RuntimeData.dataBase.appUser);
                        Log.i(TAG, "onApiT: " + preferenceManger.getUserDatabase());
                    }
                    Home.this.initApp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                String userDataBase = preferenceManger.getUserDatabase();
                Log.i(TAG, "onApiF: " + userDataBase);
                //RuntimeData.dataBase =new DataBase(DataBase.getRecords(DataBase.createJsonArray(userDataBase)));
                //initApp();
            }
        });
        fetcher.getData(Constants.API_Url + "?phoneNumber=0715643789", DataFetcher.RC_GetAllRecords);
        findViewById(R.id.AH_btnSession).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    updateDatabase();
                    Toast.makeText(Home.this, "Initializing", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!isOnline()) {
            String userDataBase = preferenceManger.getUserDatabase();
            Log.i(TAG, "onIntF: " + userDataBase);
        }
    }

    private void initApp() {
        View btnSession = findViewById(R.id.AH_btnSession);
        btnSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(true);
                if (isOnline()) {
                    CircularReveal.presentActivity(new CircularReveal.Builder(
                            Home.this,
                            btnSession,
                            new Intent(Home.this, RecordHeartBeat.class),
                            500
                    ));
                } else {
                    Toast.makeText(Home.this, "You'll need an active internet connection for this.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
