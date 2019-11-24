package com.iCropal.iPhobia.Ui;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.iCropal.iPhobia.DataModel.DataBase;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.Record;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Dialogs.NewSession;
import com.iCropal.iPhobia.Utility.Dialogs.ResultsScreen;
import com.iCropal.iPhobia.Utility.Resources.Animations;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Resources.Drawables;

import com.iCropal.iPhobia.Utility.HeartRateMeter.HeartRateOmeter;

import com.iCropal.iPhobia.Utility.ApiManager.DataFetcher;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.tombayley.activitycircularreveal.CircularReveal;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


import de.charite.balsam.utils.camera.CameraSupport;

import static android.content.ContentValues.TAG;
import static android.content.Context.VIBRATOR_SERVICE;

public class HeartBeatManager {
    private RecordHeartBeat recordHeartBeat;
    private boolean vibratorOn = false;
    private boolean startTimer = false;
    public boolean stopReading = false;
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


    public void init(RecordHeartBeat record) {
        recordHeartBeat = record;
        circularReveal = new CircularReveal(recordHeartBeat.findViewById(R.id.ARHB_rootView));
        circularReveal.onActivityCreate(recordHeartBeat.getIntent());
        animations = new Animations(recordHeartBeat);
        drawables = new Drawables(recordHeartBeat);
        connection = new DataFetcher();
        String phobiaId = record.getIntent().getExtras().getString(Constants.Phobia);
        if (phobiaId == null) {
            final Handler handler1 = new Handler();
            final int delay1 = 800;
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

    private void initData() {
        recordHeartBeat.findViewById(R.id.ARHB_layout).setVisibility(View.VISIBLE);
        recordHeartBeat.findViewById(R.id.ARHB_template).setVisibility(View.GONE);
        video = recordHeartBeat.findViewById(R.id.ARHB_videoView);
        videoCover = recordHeartBeat.findViewById(R.id.ARHB_beatCover);
        vibrator = (Vibrator) recordHeartBeat.getSystemService(VIBRATOR_SERVICE);
        final Handler handler = new Handler();
        final int delay = 1000;
        final int[] x = new int[1];
        x[0] = 0;
        handler.postDelayed(new Runnable() {
            public void run() {
                if (vibratorOn && !stopReading) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(200);
                    }
                }
                if (x[0] >= 15000) {
                    x[0] = x[0] + delay;
                } else {
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
        recordHeartBeat.findViewById(R.id.ARHB_btnCancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onFinish();
                    }
                }
        );
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });
        Uri videoPath = Uri.parse("android.resource://" + recordHeartBeat.getPackageName() + "/" + R.raw.beat);
        video.setVideoURI(videoPath);
        recordHeartBeat.startWithPermissionCheck();
        ((TextView) recordHeartBeat.findViewById(R.id.ARHB_bpmPhobia)).setText(sessionPhobia.getPhobiaTitle());
        recordHeartBeat.findViewById(R.id.ARHB_bpmPhobia).setVisibility(View.VISIBLE);
        TextView hView = recordHeartBeat.findViewById(R.id.ARHB_bpmH);
        TextView lView = recordHeartBeat.findViewById(R.id.ARHB_bpmL);
        if (sessionPhobia.getRecords() != null) {
            int h = Integer.parseInt(sessionPhobia.getHighestRecord(sessionPhobia.getRecords()).getRecordBmp());
            int l = Integer.parseInt(sessionPhobia.getLowestRecord(sessionPhobia.getRecords()).getRecordBmp());
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
        Log.i(TAG, "getSessionPhobia: ");
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
            if (!startTimer) {
                startT(15);
            }
        } else {
            Camera cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.stopPreview();
            cam.release();
        }
    }

    private void startT(int sec) {
        timer = recordHeartBeat.findViewById(R.id.ARHB_bpmTimer);
        String text = sec + " Seconds";
        timer.setText(text);
        final int delay = 1000;
        final int[] x = new int[1];
        x[0] = sec * 1000;
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            public void run() {
                if (x[0] <= 0) {
                    stopReading = true;
                    recordHeartBeat.findViewById(R.id.preview).setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(1000);
                    }
                    timer.setText("Completed");
                    View z = recordHeartBeat.findViewById(R.id.ARHB_btnDone);
                    z.startAnimation(animations.slideLeft);
                    z.setVisibility(View.VISIBLE);
                    z.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveBpm(((TextView) recordHeartBeat.findViewById(R.id.ARHB_bpmTextView)).getText().toString());
                        }
                    });
                    CameraSupport camera = heartRateOmeter.getCameraSupport();
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    videoCover.setVisibility(View.VISIBLE);
                    camera.release();
                    camera = null;
                } else {
                    if (vibratorOn) {
                        x[0] = x[0] - delay;
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(x[0]);
                        String text = checkDigit((int) seconds) + " Seconds";
                        timer.setText(text);
                    }
                    timerHandler.postDelayed(this, delay);
                }
            }

            String checkDigit(int number) {
                return number <= 9 ? "0" + number : String.valueOf(number);
            }
        };
        timerHandler.postDelayed(timerRunnable, delay);
        startTimer = true;
    }

    private void saveBpm(String text) {
        Record record = new Record(sessionPhobia.getPhobiaId());
        record.setUserPhoneNumber(RuntimeData.dataBase.appUser.getPhoneNumber());
        record.setRecordBmp(text);
        record.setPhobiaId(sessionPhobia.getPhobiaId());
        record.setRecordType("On");
        record.setRecordDecision("Ok");
        record.setRecordStatus("Reacting");
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
        connection.saveData(Constants.API_Url, DataFetcher.RC_RecordUpdate, DataBase.createJsonObject(record));
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
            String text = 15 + " Seconds";
            timer.setText(text);
            removeHandler();
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
            startT(15);
        }
        vibratorOn = fingerDetected;
    }

    private void removeHandler() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void setHeartRateOMeter(@NotNull HeartRateOmeter bpmUpdates) {
        heartRateOmeter = bpmUpdates;
    }

    public void onFinish() {
        circularReveal.unRevealActivity(recordHeartBeat);
        recordHeartBeat.finish();
        recordHeartBeat.setHeartBeatManager(null);
        vibratorOn = false;
        if (!stopReading) {
            CameraSupport camera = heartRateOmeter.getCameraSupport();
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
