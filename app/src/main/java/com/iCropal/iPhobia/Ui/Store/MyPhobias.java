package com.iCropal.iPhobia.Ui.Store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapter.PhobiaCardAdapter;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

public class MyPhobias extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_my_phobias, container, false);
        GridView gridView = parentView.findViewById(R.id.FMP_gridView);
        PhobiaCardAdapter phobiaCardAdapter = new PhobiaCardAdapter(getContext(), phobia -> {
            if (myPhobiaInterface != null) {
                myPhobiaInterface.onPhobiaSelected(phobia);
            }
        }, RuntimeData.dataBase.phobias);
        gridView.setAdapter(phobiaCardAdapter);

        return parentView;
    }

    public void setMyPhobiaInterface(MyPhobiaInterface myPhobiaInterface) {
        this.myPhobiaInterface = myPhobiaInterface;
    }

    public interface MyPhobiaInterface {
        void onPhobiaSelected(Phobia phobia);

    }

    MyPhobiaInterface myPhobiaInterface;

}
