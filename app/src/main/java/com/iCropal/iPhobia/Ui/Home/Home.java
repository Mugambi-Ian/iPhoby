package com.iCropal.iPhobia.Ui.Home;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.agrawalsuneet.squareloaderspack.loaders.WaveLoader;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.iCropal.iPhobia.DataModel.DataBase;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Ui.RecordsUi.RecordHeartBeat;
import com.iCropal.iPhobia.Ui.Settings.Settings;
import com.iCropal.iPhobia.Ui.Store.Store;
import com.iCropal.iPhobia.Ui.User.UserAuthorization;
import com.iCropal.iPhobia.Ui.User.UserProfile;
import com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapter.PhobiaAnalysisAdapter;
import com.iCropal.iPhobia.Utility.ApiManager.DataFetcher;
import com.iCropal.iPhobia.Utility.Resources.Animations;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Transmittors.PreferenceManger;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.tombayley.activitycircularreveal.CircularReveal;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;


public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private DataFetcher fetcher;
    private Animations animations;
    private PhobiaAnalysisAdapter analysisAdapter;
    private ViewPager phobiaOptions;
    private View logoImageView;
    private PreferenceManger preferenceManger;

    @Override
    public void onBackPressed() {
        View v = findViewById(R.id.AH_fContainer);
        View h = findViewById(R.id.AH_layoutHome);
        if (v.getVisibility() == View.VISIBLE) {
            h.setVisibility(View.VISIBLE);
            h.startAnimation(animations.fadeIn);
            v.setVisibility(View.GONE);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        RuntimeData.home = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDatabase();
    }

    public void updateDatabase() {
        fetcher.getData(Constants.API_Url + RuntimeData.referenceManger.getPhoneNumber(), DataFetcher.RC_GetAllRecords);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fetcher = new DataFetcher();
        animations = new Animations(this);
        logoImageView = findViewById(R.id.AH_logo);
        phobiaOptions = findViewById(R.id.LHA_cardPager);
        RuntimeData.cleanse();
        RuntimeData.home = this;
        preferenceManger = new PreferenceManger(Home.this);
        RuntimeData.referenceManger = preferenceManger;
        WaveLoader waveLoader = findViewById(R.id.AH_loader);
        waveLoader.setInterpolator(new LinearInterpolator());
        waveLoader.setAnimDuration(1000);
        waveLoader.setDelayDuration(100);
        if (preferenceManger.getPhoneNumber() == null) {
            startRegistration();
        } else {
            findViewById(R.id.AH_btnSession).setOnClickListener(v -> {
                if (v.getTag() == null) {
                    updateDatabase();
                }
            });
            if (isOnline()) {
                fetcher.setResultListener(new DataFetcher.DataListener() {
                    @Override
                    public void onSuccess(String data, int r) {
                        try {
                            if (r == DataFetcher.RC_GetAllRecords) {
                                RuntimeData.dataBase = new DataBase(DataBase.getRecords(new JSONArray(data)));
                                RuntimeData.dataBase.userRecords = DataBase.getRecords(new JSONArray(data));
                                preferenceManger.setUserDatabase(RuntimeData.dataBase.appUser);
                            }
                            if (r == DataFetcher.Phobia_Records) {
                                //   Log.i(TAG, data);
                            }
                            Home.this.initApp();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int r) {
                        String userDataBase = preferenceManger.getUserDatabase();
                        Log.i(TAG, "onApiF: " + userDataBase);
                        //RuntimeData.dataBase =new DataBase(DataBase.getRecords(DataBase.createJsonArray(userDataBase)));
                        //initApp();
                    }
                });
                fetcher.getData(Constants.API_Url + preferenceManger.getPhoneNumber(), DataFetcher.RC_GetAllRecords);
                fetcher.getData(Constants.Phobia_Url, DataFetcher.Phobia_Records);

            } else {
                String userDataBase = preferenceManger.getUserDatabase();
                Log.i(TAG, "onIntF: " + userDataBase);
            }
        }

    }

    private void startRegistration() {
        View x = findViewById(R.id.AH_logoS);
        logoImageView.setVisibility(View.GONE);
        findViewById(R.id.AH_loaderHeader).setVisibility(View.GONE);
        new Handler().postDelayed(() -> {
            x.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.Pulse)
                    .duration(500)
                    .repeat(1)
                    .playOn(findViewById(R.id.AH_logoS));

            new Handler().postDelayed(() -> initRegistration(), 2000);
        }, 300);
        x.setVisibility(View.VISIBLE);
        x.setAnimation(animations.slideUp);
        x.startAnimation(AnimationUtils.loadAnimation(Home.this, R.anim.fade_in_extra));
    }

    private void initRegistration() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, UserAuthorization.class));
            finish();
        }, 300);
    }

    private void emptyRecords() {
        findViewById(R.id.AH_dashboard).setVisibility(View.GONE);
        findViewById(R.id.AH_emptyRecords).setVisibility(View.VISIBLE);
    }

    private void openPhobiaDetails(Phobia phobia, int position) {
        PhobiaAnalysis phobiaAnalysis = new PhobiaAnalysis();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.AH_fContainer, phobiaAnalysis);
        transaction.commitAllowingStateLoss();
        phobiaAnalysis.setAnalysisInterface(phobiaAnalysis1 -> {
            if (phobia != null && position != -1) {
                phobiaAnalysis1.openPhobia(position);
            }
            if (position == -1) {
                phobiaAnalysis1.openPhobia(phobia);
            }
        });
        findViewById(R.id.AH_layoutHome).setVisibility(View.GONE);
        findViewById(R.id.AH_fContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.AH_fContainer).startAnimation(animations.fadeIn);
    }


    private void initApp() {
        if (isOnline()) {
            View x = findViewById(R.id.AH_logoS);
            logoImageView.setVisibility(View.GONE);
            findViewById(R.id.AH_loaderHeader).setVisibility(View.GONE);
            new Handler().postDelayed(() -> {
                x.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Pulse)
                        .duration(500)
                        .repeat(1)
                        .playOn(findViewById(R.id.AH_logoS));

                new Handler().postDelayed(() -> goHome(), 2000);
            }, 300);
            x.setVisibility(View.VISIBLE);
            x.setAnimation(animations.slideUp);
            x.startAnimation(AnimationUtils.loadAnimation(Home.this, R.anim.fade_in_extra));
        }
        if (analysisAdapter != null) {
            ArrayList<Phobia> phobias = new ArrayList<>();
            for (Phobia p : RuntimeData.dataBase.appUser.getPhobias()) {
                if (p.getRecords().size() >= 3) {
                    phobias.add(p);
                }
            }
            analysisAdapter = new PhobiaAnalysisAdapter(phobias, Home.this, new PhobiaAnalysisAdapter.AdapterInterface() {
                @Override
                public void onItemClick(Phobia phobia, int position) {
                    openPhobiaDetails(phobia, position);
                }
            });
            if (phobias.size() == 0) {
                phobiaOptions.setVisibility(View.GONE);
            } else {
                phobiaOptions.setVisibility(View.VISIBLE);
            }
            if (RuntimeData.dataBase.userRecords.size() != 0) {
                loadData();
            } else {
                emptyRecords();
            }
            phobiaOptions.setAdapter(analysisAdapter);
        }
        if (RuntimeData.dataBase != null) {
            ArrayList<Phobia> phobias = new ArrayList<>();
            for (Phobia p : RuntimeData.dataBase.appUser.getPhobias()) {
                if (p.getRecords().size() >= 3) {
                    phobias.add(p);
                }
            }
            analysisAdapter = new PhobiaAnalysisAdapter(phobias, Home.this, this::openPhobiaDetails);
            if (phobias.size() == 0) {
                phobiaOptions.setVisibility(View.GONE);
            } else {
                phobiaOptions.setVisibility(View.VISIBLE);
            }
            phobiaOptions.setAdapter(analysisAdapter);
            if (RuntimeData.dataBase.userRecords.size() != 0) {
                loadData();
            } else {
                emptyRecords();
            }
        }
        findViewById(R.id.AH_btnSettings).setOnClickListener(v -> {
            v.setEnabled(false);
            CircularReveal.presentActivity(new CircularReveal.Builder(
                    Home.this,
                    v,
                    new Intent(Home.this, Settings.class),
                    500
            ));
            new Handler().postDelayed(() -> v.setEnabled(true), 600);
        });
        findViewById(R.id.AH_btnStore).setOnClickListener(v -> {
            v.setEnabled(false);
            CircularReveal.presentActivity(new CircularReveal.Builder(
                    Home.this,
                    v,
                    new Intent(Home.this, Store.class),
                    500
            ));
            new Handler().postDelayed(() -> v.setEnabled(true), 600);
        });
        findViewById(R.id.AH_btnUserProfile).setOnClickListener(v -> {
            v.setEnabled(false);
            CircularReveal.presentActivity(new CircularReveal.Builder(
                    Home.this,
                    v,
                    new Intent(Home.this, UserProfile.class),
                    500
            ));
            new Handler().postDelayed(() -> v.setEnabled(true), 600);
        });
        findViewById(R.id.AH_btnSession).setOnClickListener(v -> {
            v.setEnabled(false);
            CircularReveal.presentActivity(new CircularReveal.Builder(
                    Home.this,
                    v,
                    new Intent(Home.this, RecordHeartBeat.class),
                    500
            ));
            new Handler().postDelayed(() -> v.setEnabled(true), 600);

        });
    }

    private void goHome() {
        findViewById(R.id.AH_layoutHome).setVisibility(View.VISIBLE);
        findViewById(R.id.AH_splashScreen).setVisibility(View.GONE);
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void loadData() {
        findViewById(R.id.AH_dashboard).setVisibility(View.VISIBLE);
        findViewById(R.id.AH_emptyRecords).setVisibility(View.GONE);

        LineChartData chartM = new LineChartData();
        LineChartData chartL = new LineChartData();
        LineChartData chartH = new LineChartData();
        LineChartData chartR = new LineChartData();

        ((TextView) findViewById(R.id.LHA_bpmMost)).setText(RuntimeData.dataBase.appUser.getMostSession().getAverageBpm());
        ((TextView) findViewById(R.id.LHA_bpmLAvg)).setText(RuntimeData.dataBase.appUser.getLowestAvg().getAverageBpm());
        ((TextView) findViewById(R.id.LHA_bpmHAvg)).setText(RuntimeData.dataBase.appUser.getHighestAvg().getAverageBpm());
        ((TextView) findViewById(R.id.LHA_bpmRecent)).setText(getLastPhobia().getAverageBpm());

        ((TextView) findViewById(R.id.LHA_phobiaMost)).setText(RuntimeData.dataBase.appUser.getMostSession().getPhobiaTitle());
        ((TextView) findViewById(R.id.LHA_phobiaLAvg)).setText(RuntimeData.dataBase.appUser.getLowestAvg().getPhobiaTitle());
        ((TextView) findViewById(R.id.LHA_phobiaHAvg)).setText(RuntimeData.dataBase.appUser.getHighestAvg().getPhobiaTitle());
        ((TextView) findViewById(R.id.LHA_phobiaRecent)).setText(getLastPhobia().getPhobiaTitle());

        chartM.setLines(RuntimeData.dataBase.appUser.getMostSession().getLines(null));
        chartL.setLines(RuntimeData.dataBase.appUser.getLowestAvg().getLines("#26C6DA"));
        chartH.setLines(RuntimeData.dataBase.appUser.getHighestAvg().getLines("#B71C1C"));
        chartR.setLines(getLastPhobia().getLines("#66BB6A"));

        ((LineChartView) findViewById(R.id.LHA_chartMost)).setLineChartData(chartM);
        ((LineChartView) findViewById(R.id.LHA_chartHAvg)).setLineChartData(chartH);
        ((LineChartView) findViewById(R.id.LHA_chartLAvg)).setLineChartData(chartL);
        ((LineChartView) findViewById(R.id.LHA_chartRecent)).setLineChartData(chartR);

        findViewById(R.id.LHA_btnHAvg).setTag(RuntimeData.dataBase.appUser.getHighestAvg().getPhobiaId());
        findViewById(R.id.LHA_btnLAvg).setTag(RuntimeData.dataBase.appUser.getLowestAvg().getPhobiaId());
        findViewById(R.id.LHA_btnMost).setTag(RuntimeData.dataBase.appUser.getMostSession().getPhobiaId());
        findViewById(R.id.LHA_btnRecent).setTag(getLastPhobia().getPhobiaId());

        View.OnClickListener listener = v ->
                openPhobiaDetails(DataBase.getPhobia(String.valueOf(v.getTag()), 1), -1);
        if (RuntimeData.dataBase.appUser.getHighestAvg().getRecords().size() >= 3) {
            findViewById(R.id.LHA_btnHAvg).setOnClickListener(listener);
        }
        if (RuntimeData.dataBase.appUser.getLowestAvg().getRecords().size() >= 3) {
            findViewById(R.id.LHA_btnLAvg).setOnClickListener(listener);
        }
        if (RuntimeData.dataBase.appUser.getMostSession().getRecords().size() >= 3) {
            findViewById(R.id.LHA_btnMost).setOnClickListener(listener);
        }
        if (getLastPhobia().getRecords().size() >= 3) {
            findViewById(R.id.LHA_btnRecent).setOnClickListener(listener);
        }
    }

    private Phobia getLastPhobia() {
        return DataBase.getPhobia(DataBase.lastRecord.getPhobiaId(), 1);
    }
}
