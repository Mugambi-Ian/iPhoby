package com.iCropal.iPhobia.Utility.Adapters.PhobiaPageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.DataModel.PhobiaPage;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.CardAdapter.CardAdapter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhobiaAdapter extends PagerAdapter implements CardAdapter {
    private List<CardView> mViews;
    private ArrayList<PhobiaPage> mData;
    private float mBaseElevation;
    private Context con;

    public PhobiaAdapter(ArrayList<PhobiaPage> mData, Context con) {
        this.con = con;
        mViews = new ArrayList<>();
        this.mData = mData;
        CardView[] x = new CardView[mData.size()];
        mViews.addAll(Arrays.asList(x));
    }


    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.page_phobia_options, container, false);
        container.addView(view);
        CardView cardView = view.findViewById(R.id.PBO_layout);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        ArrayList<Phobia> notifications = mData.get(position).getPhobias();
        setView(view, notifications);
        return view;
    }


    private void setView(View view, final ArrayList<Phobia> notifications) {
        ((TextView) view.findViewById(R.id.PBO_o1Text)).setText(notifications.get(0).getPhobiaTitle());
        view.findViewById(R.id.PBO_o1Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onItemClicked(notifications.get(0));
                }
            }
        });
        /*if (notifications.get(0).getPhobiaDrawable() != null) {
            Glide.with(con).load(notifications.get(0).getPhobiaDrawable()).into(((ImageView) view.findViewById(R.id.PBO_o2Image)));
        } else {
            Glide.with(con).load(notifications.get(0).getPhobiaPng()).into(((ImageView) view.findViewById(R.id.PBO_o1Image)));
        }*/

        ((TextView) view.findViewById(R.id.PBO_o2Text)).setText(notifications.get(1).getPhobiaTitle());
        view.findViewById(R.id.PBO_o2Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onItemClicked(notifications.get(1));
                }
            }
        });
        /*if (notifications.get(1).getPhobiaDrawable() != null) {
            Glide.with(con).load(notifications.get(1).getPhobiaDrawable()).into(((ImageView) view.findViewById(R.id.PBO_o2Image)));
        } else {
            Glide.with(con).load(notifications.get(1).getPhobiaPng()).into(((ImageView) view.findViewById(R.id.PBO_o2Image)));
        }*/

        view.findViewById(R.id.PBO_o3Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onItemClicked(notifications.get(2));
                }
            }
        });
        ((TextView) view.findViewById(R.id.PBO_o3Text)).setText(notifications.get(2).getPhobiaTitle());
        /*if (notifications.get(2).getPhobiaDrawable() != null) {
            Glide.with(con).load(notifications.get(2).getPhobiaDrawable()).into(((ImageView) view.findViewById(R.id.PBO_o3Image)));
        } else {
            Glide.with(con).load(notifications.get(2).getPhobiaPng()).into(((ImageView) view.findViewById(R.id.PBO_o3Image)));
        }*/
        view.findViewById(R.id.PBO_o4Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onItemClicked(notifications.get(3));
                }
            }
        });
        ((TextView) view.findViewById(R.id.PBO_o4Text)).setText(notifications.get(3).getPhobiaTitle());
        /*if (notifications.get(3).getPhobiaDrawable() != null) {
            Glide.with(con).load(notifications.get(3).getPhobiaDrawable()).into(((ImageView) view.findViewById(R.id.PBO_o4Image)));
        } else {
            Glide.with(con).load(notifications.get(3).getPhobiaPng()).into(((ImageView) view.findViewById(R.id.PBO_o4Image)));
        }*/
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onItemClicked(Phobia phobia);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }
}