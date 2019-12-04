package com.iCropal.iPhobia.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.PhobiaAdapter.PhobiaDetailsAdapter;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.ArrayList;

public class PhobiaAnalysis extends Fragment {
    private ViewPager viewPager;
    private AnalysisInterface analysisInterface;
    private ArrayList<Phobia> phobias;

    public void setAnalysisInterface(AnalysisInterface analysisInterface) {
        this.analysisInterface = analysisInterface;
    }

    public void openPhobia(int p) {
        viewPager.setCurrentItem(p, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_phobia_analysis, container, false);
        viewPager = parentView.findViewById(R.id.FPA_pager);
        phobias = new ArrayList<>();
        for (Phobia p : RuntimeData.dataBase.appUser.getPhobias()) {
            if (p.getRecords().size() >= 3) {
                phobias.add(p);
            }
        }
        PhobiaDetailsAdapter adapter = new PhobiaDetailsAdapter(phobias, getContext());
        viewPager.setAdapter(adapter);
        if (analysisInterface != null) {
            analysisInterface.onLoaded(this);
        }
        return parentView;
    }

    public void openPhobia(Phobia phobia) {
        viewPager.setCurrentItem(phobias.indexOf(phobia),false);
    }

    public interface AnalysisInterface {
        void onLoaded(PhobiaAnalysis phobiaAnalysis);
    }

}
