package com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.ArrayList;

public class PhobiaAnalysisAdapter extends PagerAdapter {

    private ArrayList<PhobiaProcessed> mData;
    private LayoutInflater layoutInflater;
    private Context context;
    private AdapterInterface adapterInterface;

    public interface AdapterInterface {
        void onItemClick(Phobia phobia, int position);
    }

    private class PhobiaProcessed {
        Phobia phobia;
        Drawable backgroundDrawable;

        public PhobiaProcessed(Phobia phobia) {
            this.phobia = phobia;
        }
    }

    public PhobiaAnalysisAdapter(ArrayList<Phobia> mData, Context context, AdapterInterface adapterInterface) {
        this.mData = getData(mData);
        this.context = context;
        this.adapterInterface = adapterInterface;
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
        }
        return r;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.page_phobia_analysis, container, false);
        PhobiaProcessed x = mData.get(position);
        Glide.with(context).load(x.backgroundDrawable).into((ImageView) view.findViewById(R.id.PPA_Image));
        ((TextView) view.findViewById(R.id.PPA_phobia)).setText(x.phobia.getPhobiaTitle());
        ((TextView) view.findViewById(R.id.PPA_avg)).setText(x.phobia.getAverageBpm());
        ((TextView) view.findViewById(R.id.PPA_low)).setText(x.phobia.getLowestRecord().getRecordBmp());
        ((TextView) view.findViewById(R.id.PPA_h)).setText(x.phobia.getHighestRecord().getRecordBmp());
        view.findViewById(R.id.PPA_btn).setOnClickListener(v -> adapterInterface.onItemClick(x.phobia, position));
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
