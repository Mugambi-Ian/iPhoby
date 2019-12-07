package com.iCropal.iPhobia.Utility.Adapters.StoreAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.StoreItem;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapter.PhobiaCardAdapter;
import com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapter.PhobiaSaleAdapter;
import com.iCropal.iPhobia.Utility.Resources.Animations;
import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.iCropal.iPhobia.Utility.Widget.NeneGrid;

import java.util.ArrayList;
import java.util.Collections;

public class StorePageAdapter extends PagerAdapter {
    private final ViewPager pager;
    private StoreAdapterInterface adapterInterface;
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

    private class StoreItemX {
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

    public ArrayList<Phobia> getExtraPhobias() {
        ArrayList<Phobia> phobias = new ArrayList<>();
        Phobia arachnoPhobia = new Phobia("Arachnophobia");
        arachnoPhobia.setPhobiaTitle(Constants.titleCaseConversion(arachnoPhobia.phobiaId));
        Phobia aquaPhobia = new Phobia("Hydrophobia");
        aquaPhobia.setPhobiaTitle(Constants.titleCaseConversion(aquaPhobia.phobiaId));
        Phobia acroPhobia = new Phobia("Acrophobia");
        acroPhobia.setPhobiaTitle(Constants.titleCaseConversion(acroPhobia.phobiaId));
        Phobia agoraphobia = new Phobia("Agoraphobia");
        agoraphobia.setPhobiaTitle(Constants.titleCaseConversion(agoraphobia.phobiaId));
        Phobia nyctophobia = new Phobia("NyctoPhobia");
        nyctophobia.setPhobiaTitle(Constants.titleCaseConversion(nyctophobia.phobiaId));
        Phobia coulrophobia = new Phobia("Claustrophobia");
        coulrophobia.setPhobiaTitle(Constants.titleCaseConversion(coulrophobia.phobiaId));
        Phobia ophidiophobia = new Phobia("Ophidiophobia");
        ophidiophobia.setPhobiaTitle(Constants.titleCaseConversion(ophidiophobia.phobiaId));
        Phobia trypophobia = new Phobia("Entomophobia");
        trypophobia.setPhobiaTitle(Constants.titleCaseConversion(trypophobia.phobiaId));

        phobias.add(nyctophobia);
        phobias.add(trypophobia);
        phobias.add(ophidiophobia);
        phobias.add(coulrophobia);
        phobias.add(arachnoPhobia);
        phobias.add(aquaPhobia);
        phobias.add(acroPhobia);
        phobias.add(agoraphobia);

        return phobias;
    }

    public StorePageAdapter(ArrayList<StoreItem> mData, Context context, ViewPager pager, StoreAdapterInterface adapterInterface) {
        this.pager = pager;
        this.adapterInterface = adapterInterface;
        ArrayList<StoreItemX> storeItemXES = new ArrayList<>();
        for (StoreItem x : mData) {
            storeItemXES.add(new StoreItemX(getData(x.itemPhobias), x.itemTitle));
        }
        this.mData = storeItemXES;
        Collections.shuffle(this.mData);
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
            ArrayList<Integer> integers = new ArrayList<>();
            integers.add(0);
            integers.add(1);
            PhobiaAdapter phobiaAdapter = new PhobiaAdapter(context, R.layout.page_item_store, integers);
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

    private ArrayList<Phobia> getPhobiaXX() {
        ArrayList<Phobia> phobias = new ArrayList<>();
        phobias.add(null);
        phobias.addAll(RuntimeData.dataBase.phobias);
        return phobias;
    }


    private class PhobiaAdapter extends ArrayAdapter<Integer> {

        PhobiaAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Integer> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View parentView = convertView;
            int x = getItem(position);
            if (x == 0) {
                if (parentView == null) {
                    parentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_store_dash, parent, false);
                }
                ViewPager viewPager = parentView.findViewById(R.id.LISD_pager);
                PhobiaSaleAdapter pageAdapter = new PhobiaSaleAdapter(getExtraPhobias(), context, new PhobiaSaleAdapter.AdapterInterface() {
                    @Override
                    public void onItemClick(Phobia phobia) {
                        adapterInterface.openPhobia(phobia);
                    }
                });
                viewPager.setAdapter(pageAdapter);
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        int c = viewPager.getCurrentItem();
                        if (c + 1 > getExtraPhobias().size() - 1) {
                            viewPager.setCurrentItem(0,false);
                            viewPager.startAnimation(new Animations(getContext()).fadeIn);

                        } else {
                            viewPager.setCurrentItem(c + 1);
                        }
                        handler.postDelayed(this, 2500);
                    }
                };
                handler.postDelayed(runnable, 2500);
                return parentView;
            } else {
                if (parentView == null) {
                    parentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_store_cards, parent, false);
                }
                NeneGrid gridView = parentView.findViewById(R.id.LISC_gridView);
                PhobiaCardAdapter phobiaCardAdapter = new PhobiaCardAdapter(getContext(), phobia -> adapterInterface.openPhobia(phobia), getExtraPhobias());
                gridView.setAdapter(phobiaCardAdapter);
                gridView.setExpanded(true);
                return parentView;
            }

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
