package com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.ArrayList;
import java.util.Collections;

public class PhobiaCardAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PhobiaProcessed> mData;
    private AdapterInterface adapterInterface;

    private class PhobiaProcessed {
        Phobia phobia;
        Drawable backgroundDrawable;
        private boolean status = true;

        public PhobiaProcessed(Phobia phobia) {
            this.phobia = phobia;
        }
    }

    private ArrayList<PhobiaProcessed> getData(ArrayList<Phobia> mData) {
        ArrayList<PhobiaProcessed> r = new ArrayList<>();
        for (Phobia x : mData) {
            r.add(new PhobiaProcessed(x));

        }
        for (PhobiaProcessed x : r) {
            if (x.phobia.getPhobiaId().equals("Arachnophobia")) {
                x.backgroundDrawable = ContextCompat.getDrawable(RuntimeData.home, R.drawable.spider);
            }
            if (x.phobia.getPhobiaId().equals("Hydrophobia")) {
                x.backgroundDrawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.water));
            }
            if (x.phobia.getPhobiaId().equals("Acrophobia")) {
                x.backgroundDrawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.height));

            }
            if (x.phobia.getPhobiaId().equals("Agoraphobia")) {
                x.backgroundDrawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.crowd));
            }
            if (x.phobia.getPhobiaId().equals("Claustrophobia")) {
                x.backgroundDrawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.cla));
                x.status = false;
            }
            if (x.phobia.getPhobiaId().equals("NyctoPhobia")) {
                x.backgroundDrawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.nca));
                x.status = false;
            }
            if (x.phobia.getPhobiaId().equals("Ophidiophobia")) {
                x.backgroundDrawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.sna));
                x.status = false;
            }
            if (x.phobia.getPhobiaId().equals("Entomophobia")) {
                x.backgroundDrawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.bge));
                x.status = false;
            }
        }
        return r;
    }

    public PhobiaCardAdapter(Context context, AdapterInterface adapterInterface, ArrayList<Phobia> phobias) {
        mData = getData(phobias);
        Collections.shuffle(mData);
        this.context = context;
        this.adapterInterface = adapterInterface;
    }

    public interface AdapterInterface {
        void onItemClick(Phobia phobia);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View parentView = convertView;
        if (parentView == null) {
            parentView = LayoutInflater.from(context).inflate(R.layout.list_item_store, parent, false);
        }
        PhobiaProcessed x = mData.get(position);
        ((TextView) parentView.findViewById(R.id.LIS_phobiaName)).setText(x.phobia.getPhobiaTitle());
        Glide.with(context).load(x.backgroundDrawable).into((ImageView) parentView.findViewById(R.id.LIS_phobiaDp));
        parentView.findViewById(R.id.LIS_btnPhobia).setOnClickListener(v -> {
            adapterInterface.onItemClick(x.phobia);
        });
        if (!x.status) {
            ((TextView) parentView.findViewById(R.id.LIS_status)).setText("Coming Soon");
            ((TextView) parentView.findViewById(R.id.LIS_status)).setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }
        return parentView;
    }
}
