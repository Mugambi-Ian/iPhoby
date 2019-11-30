package com.iCropal.iPhobia.Utility.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.Record;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;


public class ResultsScreen extends Dialog {
    private ResultListener resultListener;
    private Record record;

    public interface ResultListener {
        void goHome();

        void restartTest();

        void onDismiss();
    }

    public ResultsScreen(@NonNull Context context, int themeResId, Phobia phobia, String bpm, ResultListener resultListener) {
        super(context, themeResId);
        this.resultListener = resultListener;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_results_screen);
        init(phobia, bpm);
    }

    private void init(Phobia phobia, String text) {
        record = new Record("");
        record.setUserPhoneNumber(RuntimeData.dataBase.appUser.getPhoneNumber());
        record.setRecordBmp(text);
        record.setPhobiaId(phobia.getPhobiaId());
        record.setRecordDecision("Ok");
        record.setRecordStatus("Reacting");
        findViewById(R.id.DRS_btnRestart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultListener != null) {
                    resultListener.onDismiss();
                    resultListener.restartTest();
                }
                dismiss();
            }
        });
        findViewById(R.id.DRS_btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultListener != null) {
                    resultListener.onDismiss();
                    resultListener.goHome();
                }
                dismiss();

            }
        });
        ((TextView) findViewById(R.id.DRS_phobiaTXT)).setText(phobia.getPhobiaTitle());
        ((TextView) findViewById(R.id.DRS_bpmTextView)).setText(text);
        show();
    }
}
