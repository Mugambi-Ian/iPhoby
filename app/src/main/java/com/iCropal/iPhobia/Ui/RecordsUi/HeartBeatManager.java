package com.iCropal.iPhobia.Ui.RecordsUi;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.iCropal.iPhobia.DataModel.DataBase;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.Record;
import com.iCropal.iPhobia.DataModel.Session;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.ApiManager.DataFetcher;
import com.iCropal.iPhobia.Utility.Dialogs.NewSession;
import com.iCropal.iPhobia.Utility.Dialogs.ResultsScreen;
import com.iCropal.iPhobia.Utility.HeartRateMeter.HeartRateOmeter;
import com.iCropal.iPhobia.Utility.Resources.Animations;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Resources.Drawables;
import com.iCropal.iPhobia.Utility.Resources.Time;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.tombayley.activitycircularreveal.CircularReveal;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.charite.balsam.utils.camera.CameraSupport;

import static android.content.ContentValues.TAG;
import static android.content.Context.VIBRATOR_SERVICE;

public class HeartBeatManager {
    public boolean stopReading = false;
    private RecordHeartBeat recordHeartBeat;
    private boolean vibratorOn = false;
    private Vibrator vibrator;
    private TextView timer;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private HeartRateOmeter heartRateOmeter;
    private Animations animations;
    private Drawables drawables;
    private DataFetcher connection;
    private VideoView video;
    private Phobia sessionPhobia;
    private CircularReveal circularReveal;
    private View videoCover;
    private long MillisecondTime;
    private int Seconds;
    private int Minutes;
    private boolean startSession = false;
    private ArrayList<Session> sessions;
    private String sessionLength;
    private Handler sessionHandler;
    private Runnable sessionRunnable;


    public void init(RecordHeartBeat record) {
        recordHeartBeat = record;
        circularReveal = new CircularReveal(recordHeartBeat.findViewById(R.id.ARHB_rootView));
        circularReveal.onActivityCreate(recordHeartBeat.getIntent());
        animations = new Animations(recordHeartBeat);
        drawables = new Drawables(recordHeartBeat);
        connection = new DataFetcher();
        connection.setResultListener(new DataFetcher.DataListener() {
            @Override
            public void onSuccess(String data, int RequestCode) {
                if (RequestCode == DataFetcher.RC_startSession) {
                    startSession = true;
                    Toast.makeText(recordHeartBeat, "Sync is successful", Toast.LENGTH_SHORT).show();
                    startEndSListener();
                }
            }

            @Override
            public void onFailure(int RequestCode) {

            }
        });
        String phobiaId = record.getIntent().getExtras().getString(Constants.Phobia);
        if (phobiaId == null) {
            final Handler handler1 = new Handler();
            final int delay1 = 500;
            handler1.postDelayed(new Runnable() {
                public void run() {
                    getSessionPhobia();
                }
            }, delay1);
        } else {
            sessionPhobia = RuntimeData.dataBase.getPhobia(phobiaId);
            initData();
        }
    }

    private void endEndSListener() {
        if (sessionHandler != null & sessionRunnable != null) {
            sessionHandler.removeCallbacks(sessionRunnable);
        }
    }

    private void startEndSListener() {
        DataFetcher r = new DataFetcher();
        final boolean[] i = {false};
        final boolean[] d = {false};
        sessionHandler = new Handler();
        final int delay = 200;
        r.setResultListener(new DataFetcher.DataListener() {
            @Override
            public void onSuccess(String data, int RequestCode) {
                Log.i(TAG, "onSuccess: " + data);
                if (RequestCode == DataFetcher.RC_endSession) {
                    i[0] = false;
                    int x = -1;
                    if (sessions != null) {
                        x = sessions.size();
                    }
                    try {
                        sessions = DataBase.getSessions(new JSONArray(data));
                        if (sessions.size() > x && x != -1) {
                            d[0] = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int RequestCode) {
                if (RequestCode == DataFetcher.RC_endSession) {
                    i[0] = false;
                }
            }
        });
        sessionRunnable = new Runnable() {
            public void run() {
                if (!i[0]) {
                    r.getData(Constants.EndS_Url + RuntimeData.dataBase.appUser.getPhoneNumber(), DataFetcher.RC_endSession);
                    i[0] = true;
                }
                if (!d[0]) {
                    sessionHandler.postDelayed(this, delay);
                } else {
                    endSession();
                    sessionHandler.removeCallbacks(this);
                }
            }
        };
        sessionHandler.postDelayed(sessionRunnable, delay);
    }

    public void initData() {
        recordHeartBeat.startWithPermissionCheck();
        recordHeartBeat.findViewById(R.id.ARHB_layout).setVisibility(View.VISIBLE);
        recordHeartBeat.findViewById(R.id.ARHB_template).setVisibility(View.GONE);
        recordHeartBeat.findViewById(R.id.ARHB_bpmPhobia).setVisibility(View.VISIBLE);
        recordHeartBeat.findViewById(R.id.ARHB_btnCancel).setOnClickListener(
                v -> onFinish()
        );

        Toast.makeText(recordHeartBeat, "Syncing...", Toast.LENGTH_SHORT).show();
        Session session = new Session(RuntimeData.dataBase.appUser.getPhoneNumber(), Time.getTime(), "", true, sessionPhobia.getPhobiaTitle());
        connection.saveData(Constants.Sync_Url, DataFetcher.RC_startSession, DataBase.createSessionObject(session));
        vibrator = (Vibrator) recordHeartBeat.getSystemService(VIBRATOR_SERVICE);
        ((TextView) recordHeartBeat.findViewById(R.id.ARHB_bpmPhobia)).setText(sessionPhobia.getPhobiaTitle());
        vibratorListener();

        Uri videoPath = Uri.parse("android.resource://" + recordHeartBeat.getPackageName() + "/" + R.raw.beat);
        video = recordHeartBeat.findViewById(R.id.ARHB_videoView);
        videoCover = recordHeartBeat.findViewById(R.id.ARHB_beatCover);
        video.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.start();
        });
        video.setVideoURI(videoPath);

        TextView hView = recordHeartBeat.findViewById(R.id.ARHB_bpmH);
        TextView lView = recordHeartBeat.findViewById(R.id.ARHB_bpmL);
        if (sessionPhobia.getRecords() != null) {
            int h = Integer.parseInt(sessionPhobia.getHighestRecord().getRecordBmp());
            int l = Integer.parseInt(sessionPhobia.getLowestRecord().getRecordBmp());
            int pbm = RuntimeData.previousValue;
            boolean v = pbm != -1;
            if (v && pbm > h) {
                hView.setText("" + pbm);
            } else {
                hView.setText("" + h);
            }
            if (v && pbm < l) {
                lView.setText("" + pbm);
            } else {
                lView.setText("" + l);
            }
            if (!v) {
                hView.setText("" + h);
                lView.setText("" + l);
            }
            RuntimeData.previousValue = -1;
        }
        int pbm = RuntimeData.previousValue;
        boolean v = pbm != -1;
        if (v) {
            hView.setText("" + pbm);
            lView.setText("" + pbm);
        }
        RuntimeData.previousValue = -1;
    }

    private void getSessionPhobia() {
        if (RuntimeData.dataBase != null) {
            new NewSession(recordHeartBeat, R.style.DialogBox, RuntimeData.dataBase.phobias, new NewSession.ResultListener() {
                @Override
                public void onDismiss() {
                }

                @Override
                public void onCancel() {
                    circularReveal.unRevealActivity(recordHeartBeat);
                    recordHeartBeat.finish();
                    recordHeartBeat.setHeartBeatManager(null);
                }

                @Override
                public void result(Phobia phobia) {
                    sessionPhobia = phobia;
                    initData();
                }
            });
        }
    }

    public void setBpm(HeartRateOmeter.Bpm bpm) {
        if (!stopReading) {
            ((TextView) recordHeartBeat.findViewById(R.id.ARHB_bpmTextView)).setText("" + bpm.getValue());
        } else {
            Camera cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.stopPreview();
            cam.release();
        }
    }

    private void endSession() {
        stopReading = true;
        timerHandler.removeCallbacks(timerRunnable);
        recordHeartBeat.findViewById(R.id.preview).setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(1000);
        }
        sessionLength = timer.getText().toString();
        timer.setText("Completed");
        View z = recordHeartBeat.findViewById(R.id.ARHB_btnDone);
        z.startAnimation(animations.slideLeft);
        z.setVisibility(View.VISIBLE);
        z.setOnClickListener(view -> saveBpm(((TextView) recordHeartBeat.findViewById(R.id.ARHB_bpmTextView)).getText().toString()));
        CameraSupport camera = heartRateOmeter.getCameraSupport();
        camera.stopPreview();
        camera.setPreviewCallback(null);
        videoCover.setVisibility(View.VISIBLE);
        camera.release();
    }

    private void vibratorListener() {
        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            public void run() {
                if (vibratorOn && !stopReading) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(200);
                    }
                    if (startSession) {
                        startSession();
                        startSession = false;
                    }
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void startSession() {
        timer = recordHeartBeat.findViewById(R.id.ARHB_bpmTimer);
        String text = "0 Seconds";
        timer.setText(text);
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (vibratorOn) {
                    MillisecondTime = MillisecondTime + 1000;
                    Seconds = (int) (MillisecondTime / 1000);
                    Minutes = Seconds / 60;
                    Seconds = Seconds % 60;
                    String text;
                    if (Minutes < 1) {
                        text = Seconds + " Seconds";
                    } else {
                        text = Minutes + " Min " + Seconds + " Seconds";
                    }
                    timer.setText(text);
                }
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void saveBpm(String text) {
        Record record = new Record(sessionPhobia.getPhobiaId());
        record.setUserPhoneNumber(RuntimeData.dataBase.appUser.getPhoneNumber());
        record.setRecordBmp(text);
        record.setPhobiaId(sessionPhobia.getPhobiaId());
        record.setPhobiaName(sessionPhobia.getPhobiaTitle());
        record.setRecordDate(Time.getDate());
        record.setRecordTime(Time.getTime());
        record.setRecordDecision("Ok");
        record.setRecordStatus("Reacting");
        record.setRecordLength(sessionLength);
        new ResultsScreen(recordHeartBeat, R.style.DialogBox, sessionPhobia, text, new ResultsScreen.ResultListener() {
            @Override
            public void goHome() {

            }

            @Override
            public void restartTest() {
                RuntimeData.previousValue = Integer.parseInt(record.getRecordBmp());
                newTest();
            }

            @Override
            public void onDismiss() {
                circularReveal.unRevealActivity(recordHeartBeat);
                recordHeartBeat.finish();
                recordHeartBeat.setHeartBeatManager(null);
            }


        });
        connection.saveData(Constants.API_Url, DataFetcher.RC_RecordUpdate, DataBase.createRecordObject(record));
        RuntimeData.home.updateDatabase();
    }

    private void newTest() {
        recordHeartBeat.finish();
        recordHeartBeat.setHeartBeatManager(null);
        recordHeartBeat.overridePendingTransition(0, 0);
        recordHeartBeat.startActivity(new Intent(recordHeartBeat, RecordHeartBeat.class).putExtra(Constants.Phobia, sessionPhobia.getPhobiaId()));
        recordHeartBeat.overridePendingTransition(0, 0);
    }

    public void updateVibration(boolean fingerDetected) {
        View x = recordHeartBeat.findViewById(R.id.ARHB_fingerIndicator);
        View y = recordHeartBeat.findViewById(R.id.ARHB_notificationTxt);
        if (!fingerDetected) {
            if (timerHandler != null) {
                timerHandler.removeCallbacks(timerRunnable);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                x.setBackground(drawables.btnOff);
            }
            x.startAnimation(animations.fadeIn);
            if (videoCover.getVisibility() != View.VISIBLE) {
                videoCover.setVisibility(View.VISIBLE);
            }
            if (y.getVisibility() != View.VISIBLE) {
                y.startAnimation(animations.fadeIn);
                y.startAnimation(animations.shake);
                y.setVisibility(View.VISIBLE);
            }
        } else {
            if (videoCover.getVisibility() == View.VISIBLE) {
                videoCover.setVisibility(View.GONE);
                video.seekTo(0);
            }
            if (video.getVisibility() != View.VISIBLE) {
                video.setVisibility(View.VISIBLE);
                video.startAnimation(animations.fadeInExtra);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                x.setBackground(drawables.btnOn);
            }
            y.setVisibility(View.INVISIBLE);
            x.startAnimation(animations.fadeIn);
        }
        if (vibratorOn != fingerDetected && fingerDetected) {
            if (timerHandler != null) {
                timerHandler.postDelayed(timerRunnable, 1000);
            }
        }
        vibratorOn = fingerDetected;
    }

    public void setHeartRateOMeter(@NotNull HeartRateOmeter bpmUpdates) {
        heartRateOmeter = bpmUpdates;
    }

    public void onFinish() {
        circularReveal.unRevealActivity(recordHeartBeat);
        recordHeartBeat.finish();
        recordHeartBeat.setHeartBeatManager(null);
        vibratorOn = false;
        endEndSListener();
        if (!stopReading && heartRateOmeter.getCameraSupport()!= null) {
            CameraSupport camera = heartRateOmeter.getCameraSupport();
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                Camera cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.stopPreview();
                cam.release();
            }
        }
    }
}
