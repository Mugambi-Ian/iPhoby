package com.iCropal.iPhobia.Ui.Store;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.ArrayList;

public class MyPhobias extends Fragment {
    private ArrayList<PhobiaItemX> phobias;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_my_phobias, container, false);
        phobias = getData(RuntimeData.dataBase.phobias);
        ListView listView = parentView.findViewById(R.id.FMP_listView);
        PhobiaAdapter phobiaAdapter = new PhobiaAdapter(getContext(), R.layout.fragment_my_phobias, phobias);
        listView.setAdapter(phobiaAdapter);

        return parentView;
    }

    public void setMyPhobiaInterface(MyPhobiaInterface myPhobiaInterface) {
        this.myPhobiaInterface = myPhobiaInterface;
    }

    public interface MyPhobiaInterface {
        void onPhobiaSelected(Phobia phobia);

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

    private class PhobiaItemX {
        private Phobia phobia;
        private Drawable drawable;

        PhobiaItemX(Phobia phobia) {
            this.phobia = phobia;
        }
    }

    MyPhobiaInterface myPhobiaInterface;

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
            parentView.findViewById(R.id.LIS_btnPhobia).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myPhobiaInterface != null) {
                        myPhobiaInterface.onPhobiaSelected(x.phobia);
                    }
                }
            });
            return parentView;
        }
    }

}
