package com.iCropal.iPhobia.Utility.Dialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iCropal.iPhobia.R;

public class SelectImage extends Dialog {
    private ImageInterface resultListener;


    public SelectImage(@NonNull Context context, int themeResId, ImageInterface resultListener) {
        super(context, themeResId);
        setCancelable(true);
        this.resultListener = resultListener;
        setOnCancelListener(dialog -> dismiss());
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_select_image);
        init();
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
        dismiss();
    }

    private void init() {
        findViewById(R.id.DSI_btnC).setOnClickListener(v -> {
            if (resultListener != null) {
                resultListener.result(true);
            }
            dismiss();
        });
        findViewById(R.id.DSI_btnG).setOnClickListener(v -> {
            if (resultListener != null) {
                resultListener.result(false);
            }
            dismiss();
        });
        show();
    }

    public interface ImageInterface {
        void result(boolean c);
    }
}
