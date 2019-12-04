package com.iCropal.iPhobia.Utility.Adapters.StoreAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.StoreItem;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.ArrayList;

public class StorePageAdapter extends PagerAdapter {

    private final ViewPager pager;
    private final StoreAdapterInterface adapterInterface;
    private ArrayList<StoreItemX> mData;
    private LayoutInflater layoutInflater;
    private Context context;

    private class PhobiaItemX {
        private Phobia phobia;
        private Drawable drawable;

        PhobiaItemX(Phobia phobia) {
            this.phobia = phobia;
        }
    }

    private static class StoreItemX {
        private ArrayList<PhobiaItemX> phobiaItemX;
        private String itemTitle;

        public StoreItemX(ArrayList<PhobiaItemX> phobiaItemX, String itemTitle) {
            this.phobiaItemX = phobiaItemX;
            this.itemTitle = itemTitle;
        }
    }

    public String getTitle(int pos) {
        if (pos > mData.size() - 1) {
            return mData.get(pos).itemTitle;
        }
        return "";
    }

    public StorePageAdapter(ArrayList<StoreItem> mData, Context context, ViewPager pager, StoreAdapterInterface adapterInterface) {
        this.pager = pager;
        this.adapterInterface = adapterInterface;
        ArrayList<StoreItemX> storeItemXES = new ArrayList<>();
        storeItemXES.add(new StoreItemX(null, "Welcome"));
        for (StoreItem x : mData) {
            storeItemXES.add(new StoreItemX(getData(x.itemPhobias), x.itemTitle));
        }
        this.mData = storeItemXES;
        this.context = context;
    }

    private ArrayList<PhobiaItemX> getData(ArrayList<Phobia> mData) {
        ArrayList<PhobiaItemX> r = new ArrayList<>();
        for (Phobia x : mData) {
            r.add(new PhobiaItemX(x));

        }
        for (PhobiaItemX x : r) {
            if (x.phobia.getPhobiaId().equals("Arachnophobia")) {
                x.drawable = ContextCompat.getDrawable(RuntimeData.home, R.drawable.spider);

            }
            if (x.phobia.getPhobiaId().equals("Hydrophobia")) {
                x.drawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.water));

            }
            if (x.phobia.getPhobiaId().equals("Acrophobia")) {
                x.drawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.height));

            }
            if (x.phobia.getPhobiaId().equals("Agoraphobia")) {
                x.drawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.crowd));
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
        StoreItemX x = mData.get(position);
        boolean w = x.phobiaItemX == null;
        View view;
        if (!w) {
            view = layoutInflater.inflate(R.layout.page_item_store, container, false);
            ListView listView = view.findViewById(R.id.PIS_listView);
            PhobiaAdapter phobiaAdapter = new PhobiaAdapter(context, R.layout.page_item_store, x.phobiaItemX);
            listView.setAdapter(phobiaAdapter);
            container.addView(view, 0);
        } else {
            view = layoutInflater.inflate(R.layout.page_item_store_home, container, false);
            view.findViewById(R.id.PIS_btnStart).setOnClickListener(v -> {
                pager.setCurrentItem(1);
            });
            container.addView(view, 0);
        }
        return view;
    }

    private class PhobiaAdapter extends ArrayAdapter<PhobiaItemX> {

        PhobiaAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PhobiaItemX> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View parentView = convertView;
            if (parentView == null) {
                parentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_store, parent, false);
            }
            PhobiaItemX x = getItem(position);
            ((TextView) parentView.findViewById(R.id.LIS_phobiaName)).setText(x.phobia.getPhobiaTitle());
            Glide.with(getContext()).load(x.drawable).into((ImageView) parentView.findViewById(R.id.LIS_phobiaDp));
            parentView.findViewById(R.id.LIS_btnPhobia).setOnClickListener(v -> {
                adapterInterface.openPhobia(x.phobia);
            });
            return parentView;
        }
    }

    public interface StoreAdapterInterface {
        void openPhobia(Phobia phobia);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
