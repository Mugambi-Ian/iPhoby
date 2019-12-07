package com.iCropal.iPhobia.Ui.Store;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.StoreItem;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.StoreAdapters.StorePageAdapter;
import com.iCropal.iPhobia.Utility.Resources.Animations;
import com.iCropal.iPhobia.Utility.Resources.Drawables;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;
import com.tombayley.activitycircularreveal.CircularReveal;

import java.util.ArrayList;

public class Store extends AppCompatActivity {
    private CircularReveal circularReveal;
    private View storeView, phobiaView, myPhobiasView, layoutFt;
    private TextView txtTitle;
    private Animations animations;
    private Drawables drawables;
    private PhobiaS phobiaS;
    private MyPhobias myPhobias;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ViewPager storePager;
    private StorePageAdapter pageAdapter;
    private ArrayList<StoreItem> storeItems;

    @Override
    public void onBackPressed() {
        boolean p = phobiaView.getVisibility() == View.VISIBLE,
                ps = myPhobiasView.getVisibility() == View.VISIBLE;
        if (p || ps) {
            goHome(true);
        } else {
            circularReveal.unRevealActivity(this);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        circularReveal = new CircularReveal(findViewById(R.id.AIS_rootView));
        animations = new Animations(this);
        storeView = findViewById(R.id.AIS_storeMenu);
        txtTitle = findViewById(R.id.AIS_txtTitle);
        phobiaView = findViewById(R.id.AIS_phobiaContainer);
        myPhobiasView = findViewById(R.id.AIS_myPhobiaContainer);
        layoutFt = findViewById(R.id.AIS_layoutFt);
        circularReveal.onActivityCreate(getIntent());
        drawables = new Drawables(this);
        storePager = findViewById(R.id.AIS_storePager);
        storeItems = new ArrayList<>();
        storeItems.add(new StoreItem("Trending", RuntimeData.dataBase.phobias));
        pageAdapter = new StorePageAdapter(storeItems, this, storePager, this::openPhobia);
        storePager.setAdapter(pageAdapter);
        View.OnClickListener ftListener = v -> {
            switch (v.getId()) {
                case R.id.AIS_ftStore:
                    goHome(true);
                    break;
                case R.id.AIS_ftPhobias:
                    openMyPhobias();
                    break;
            }
        };
        findViewById(R.id.AIS_ftPhobias).setOnClickListener(ftListener);
        findViewById(R.id.AIS_ftStore).setOnClickListener(ftListener);
        fragmentManager = getSupportFragmentManager();
    }

    private void openPhobia(Phobia phobia) {
        phobiaS = new PhobiaS();
        fragmentTransaction = fragmentManager.beginTransaction();
        phobiaS.setPhobiaSInterface(phobiaS1 -> phobiaS1.loadData(phobia));
        fragmentTransaction.add(R.id.AIS_phobiaContainer, phobiaS);
        fragmentTransaction.commitAllowingStateLoss();
        phobiaView.setVisibility(View.VISIBLE);
        phobiaView.startAnimation(animations.fadeIn);
        myPhobiasView.setVisibility(View.GONE);
        storeView.setVisibility(View.GONE);
        layoutFt.setVisibility(View.GONE);
        txtTitle.setText("Add To Cart");
    }


    private void goHome(boolean anim) {
        TextView x = findViewById(R.id.AIS_ftStore);
        TextView p = findViewById(R.id.AIS_ftPhobias);
        p.setEnabled(true);
        p.setTextColor(Color.BLACK);
        p.setBackground(drawables.bgFtR);
        x.setTextColor(Color.WHITE);
        x.setEnabled(false);
        x.setBackground(drawables.bgE);
        if (!anim) {
            storePager.setCurrentItem(0, false);
        }
        pageAdapter = new StorePageAdapter(storeItems, this, storePager, this::openPhobia);
        storePager.setAdapter(pageAdapter);
        txtTitle.setText("iPhoby Store");
        layoutFt.setVisibility(View.VISIBLE);
        phobiaView.setVisibility(View.GONE);
        myPhobiasView.setVisibility(View.GONE);
        storeView.setVisibility(View.VISIBLE);
        if (anim) {
            storeView.startAnimation(animations.fadeIn);
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        if (phobiaS != null) {
            fragmentTransaction.remove(phobiaS).commitAllowingStateLoss();
            phobiaS = null;
        }
        if (myPhobias != null) {
            fragmentTransaction.remove(myPhobias).commitAllowingStateLoss();
            myPhobias = null;
        }
    }

    private void openMyPhobias() {
        TextView s = findViewById(R.id.AIS_ftStore);
        TextView p = findViewById(R.id.AIS_ftPhobias);
        s.setEnabled(true);
        s.setTextColor(Color.BLACK);
        s.setBackground(drawables.bgFtL);
        p.setTextColor(Color.WHITE);
        p.setEnabled(false);
        p.setBackground(drawables.bgE);
        txtTitle.setText("My Phobias");
        myPhobias = new MyPhobias();
        storePager.setCurrentItem(0);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.AIS_myPhobiaContainer, myPhobias);
        fragmentTransaction.commitAllowingStateLoss();
        phobiaView.setVisibility(View.GONE);
        myPhobiasView.startAnimation(animations.fadeIn);
        myPhobiasView.setVisibility(View.VISIBLE);
        storeView.setVisibility(View.GONE);
        myPhobias.setMyPhobiaInterface(new MyPhobias.MyPhobiaInterface() {
            @Override
            public void onPhobiaSelected(Phobia phobia) {
                goHome(false);
                openPhobia(phobia);
            }

        });
    }
}
