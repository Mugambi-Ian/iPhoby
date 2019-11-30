package com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.Record;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Resources.Time;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

public class PhobiaDetailsAdapter extends PagerAdapter {

    private ArrayList<PhobiaProcessed> mData;
    private LayoutInflater layoutInflater;
    private Context context;

    private class PhobiaProcessed {
        Phobia phobia;
        Drawable backgroundDrawable;

        PhobiaProcessed(Phobia phobia) {
            this.phobia = phobia;
        }
    }

    public PhobiaDetailsAdapter(ArrayList<Phobia> mData, Context context) {
        this.mData = getData(mData);
        this.context = context;
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
        View view = layoutInflater.inflate(R.layout.page_phobia_details, container, false);
        PhobiaProcessed x = mData.get(position);
        Glide.with(context).load(x.backgroundDrawable).into((BlurImageView) view.findViewById(R.id.PPD_phobiaImageView));
        ((TextView) view.findViewById(R.id.PPD_phobia)).setText(x.phobia.getPhobiaTitle());
        ((TextView) view.findViewById(R.id.PPD_avg)).setText(x.phobia.getAverageBpm());
        ((TextView) view.findViewById(R.id.PPD_low)).setText(x.phobia.getLowestRecord().getRecordBmp());
        ((TextView) view.findViewById(R.id.PPD_h)).setText(x.phobia.getHighestRecord().getRecordBmp());
        RecordsAdapter adapter = new RecordsAdapter(context, R.layout.page_phobia_details, x.phobia.getRecords());
        ListView recordsListView = view.findViewById(R.id.PPD_listView);
        recordsListView.setAdapter(adapter);
        LineChartView chartView = view.findViewById(R.id.PPD_pChart);
        LineChartData chartData = new LineChartData();
        chartData.setLines(x.phobia.getLines("#ff33b5e5"));
        chartView.setLineChartData(chartData);

        container.addView(view, 0);
        return view;
    }

    private class RecordsAdapter extends ArrayAdapter<Record> {

        RecordsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Record> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View parentView = convertView;
            if (parentView == null) {
                parentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_record, parent, false);
            }
            Record x = getItem(position);
            ((TextView) parentView.findViewById(R.id.LIR_bpm)).setText(x.getRecordBmp());
            ((TextView) parentView.findViewById(R.id.LIR_sLength)).setText(x.getRecordLength());
            ((TextView) parentView.findViewById(R.id.LIR_dateYear)).setText(Time.getLongDate(x.getRecordDate()));
            ((TextView) parentView.findViewById(R.id.LIR_timeDay)).setText(Time.myTime(x.getRecordDate(),x.getRecordTime()));
            return parentView;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
