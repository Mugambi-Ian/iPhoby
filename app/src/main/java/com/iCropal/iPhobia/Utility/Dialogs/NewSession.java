package com.iCropal.iPhobia.Utility.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.PhobiaPage;
import com.iCropal.iPhobia.DataModel.Record;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.PhobiaPageAdapter.PhobiaAdapter;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;


import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class NewSession extends Dialog {
    private ResultListener resultListener;
    Record record;


    public interface ResultListener {
        void onDismiss();

        void onCancel();

        void result(Phobia phobia);
    }

    public NewSession(@NonNull Context context, int themeResId, ArrayList<Phobia> phobias, ResultListener resultListener) {
        super(context, themeResId);
        setCancelable(true);
        this.resultListener = resultListener;
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (resultListener != null) {
                    resultListener.onCancel();
                    resultListener.onDismiss();
                }
                dismiss();
            }
        });
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_new_session);
        init(getPhobiaPages(phobias));
    }

    private ArrayList<PhobiaPage> getPhobiaPages(ArrayList<Phobia> phobias) {
        ArrayList<PhobiaPage> phobiaPages = new ArrayList<>();
        PhobiaPage phobiaPage = new PhobiaPage();
        for (Phobia phobia : phobias) {
            if (phobiaPage.getPhobias().size() >= 4) {
                phobiaPages.add(phobiaPage);
                phobiaPage = new PhobiaPage();
                phobiaPage.addPhobia(phobia);
            } else {
                phobiaPage.addPhobia(phobia);
            }

        }
        return phobiaPages;
    }

    private void setView(final ArrayList<Phobia> notifications) {
        ((TextView) findViewById(R.id.PBO_o1Text)).setText(notifications.get(0).getPhobiaTitle());
        findViewById(R.id.PBO_o1Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultListener != null) {
                    resultListener.result(notifications.get(0));
                    resultListener.onDismiss();
                }
                dismiss();
            }
        });
       /* if (notifications.get(0).getPhobiaDrawable() != null) {
            Glide.with(getContext()).load(notifications.get(0).getPhobiaDrawable()).into(((ImageView) findViewById(R.id.PBO_o2Image)));
        } else {
            Glide.with(getContext()).load(notifications.get(0).getPhobiaPng()).into(((ImageView) findViewById(R.id.PBO_o1Image)));
        }*/

        ((TextView) findViewById(R.id.PBO_o2Text)).setText(notifications.get(1).getPhobiaTitle());
        findViewById(R.id.PBO_o2Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultListener != null) {
                    resultListener.result(notifications.get(1));
                    resultListener.onDismiss();
                }
                dismiss();
            }
        });
        /*if (notifications.get(1).getPhobiaDrawable() != null) {
            Glide.with(getContext()).load(notifications.get(1).getPhobiaDrawable()).into(((ImageView) findViewById(R.id.PBO_o2Image)));
        } else {
            Glide.with(getContext()).load(notifications.get(1).getPhobiaPng()).into(((ImageView) findViewById(R.id.PBO_o2Image)));
        }
*/
        findViewById(R.id.PBO_o3Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultListener != null) {
                    resultListener.result(notifications.get(2));
                    resultListener.onDismiss();
                }
                dismiss();
            }
        });
        ((TextView) findViewById(R.id.PBO_o3Text)).setText(notifications.get(2).getPhobiaTitle());
        /*if (notifications.get(2).getPhobiaDrawable() != null) {
            Glide.with(getContext()).load(notifications.get(2).getPhobiaDrawable()).into(((ImageView) findViewById(R.id.PBO_o3Image)));
        } else {
            Glide.with(getContext()).load(notifications.get(2).getPhobiaPng()).into(((ImageView) findViewById(R.id.PBO_o3Image)));
        }*/
        findViewById(R.id.PBO_o4Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultListener != null) {
                    resultListener.result(notifications.get(3));
                    resultListener.onDismiss();
                }
                dismiss();
            }
        });
        ((TextView) findViewById(R.id.PBO_o4Text)).setText(notifications.get(3).getPhobiaTitle());
       /* if (notifications.get(3).getPhobiaDrawable() != null) {
            Glide.with(getContext()).load(notifications.get(3).getPhobiaDrawable()).into(((ImageView) findViewById(R.id.PBO_o4Image)));
        } else {
            Glide.with(getContext()).load(notifications.get(3).getPhobiaPng()).into(((ImageView) findViewById(R.id.PBO_o4Image)));
        }*/
    }

    private void init(ArrayList<PhobiaPage> phobiaPages) {
        setView(RuntimeData.dataBase.phobias);
        show();
    }
}
