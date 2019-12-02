package com.iCropal.iPhobia.Ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.iCropal.iPhobia.DataModel.StoreItem;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.StoreAdapters.StorePageAdapter;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.tombayley.activitycircularreveal.CircularReveal;

import java.util.ArrayList;

public class Store extends AppCompatActivity {
    private CircularReveal circularReveal;

    @Override
    public void onBackPressed() {
        circularReveal.unRevealActivity(this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        circularReveal = new CircularReveal(findViewById(R.id.AIS_rootView));
        circularReveal.onActivityCreate(getIntent());
        ViewPager pager = findViewById(R.id.AIS_viewPager);
        ArrayList<StoreItem> storeItems = new ArrayList<>();
        storeItems.add(new StoreItem("Trending", RuntimeData.dataBase.phobias));
        storeItems.add(new StoreItem("Free", RuntimeData.dataBase.phobias));
        storeItems.add(new StoreItem("Recommended", RuntimeData.dataBase.phobias));
        StorePageAdapter pageAdapter = new StorePageAdapter(storeItems, this, pager);
        pager.setAdapter(pageAdapter);
    }
}
