package com.iCropal.iPhobia.Ui.Store;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.iCropal.iPhobia.DataModel.Phobia;
import com.iCropal.iPhobia.R;
import com.iCropal.iPhobia.Utility.Adapters.ImageAdapter.ImageAdapter;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.ArrayList;

public class PhobiaS extends Fragment {
    private PhobiaSInterface phobiaSInterface;
    private PhobiaItemX itemX;
    private View parentView;


    public void setPhobiaSInterface(PhobiaSInterface phobiaSInterface) {
        this.phobiaSInterface = phobiaSInterface;
    }

    public interface PhobiaSInterface {
        void onCreateView(PhobiaS phobiaS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_store_phobia, container, false);
        if (phobiaSInterface != null) {
            phobiaSInterface.onCreateView(this);
        }
        return parentView;
    }

    public void loadData(Phobia phobia) {
        itemX = getData(phobia);
        Glide.with(getContext()).load(itemX.drawable).into((ImageView) parentView.findViewById(R.id.FSB_phobiaDp));
        ((TextView) parentView.findViewById(R.id.FSB_phobiaName)).setText(phobia.getPhobiaTitle());
        parentView.findViewById(R.id.FSB_btnShop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parentView, "You already own this item", Snackbar.LENGTH_LONG).show();
            }
        });
        ((TextView) parentView.findViewById(R.id.FSB_txtDescription)).setText(itemX.description);
        RecyclerView recyclerView = parentView.findViewById(R.id.FSB_rcyImages);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        ImageAdapter imageAdapter = new ImageAdapter(itemX.drawables, getContext());
        LinearLayoutManager hLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(hLayoutManager);
        recyclerView.setAdapter(imageAdapter);
    }

    private PhobiaItemX getData(Phobia mData) {
        PhobiaItemX x = new PhobiaItemX(mData);
        if (x.phobia.getPhobiaId().equals("Arachnophobia")) {
            x.drawable = ContextCompat.getDrawable(RuntimeData.home, R.drawable.spider);
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.ara));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.arb));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.ard));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.arc));
            x.drawables.add(x.drawable);
            x.description = "Arachnophobia is the unreasonable fear of spiders and other arachnids such as scorpions.People with arachnophobia tend to feel uneasy in any area they believe could harbor spiders or that has visible signs of their presence, such as webs. If arachnophobes see a spider, they may not enter the general vicinity until they have overcome the panic attack that is often associated with their phobia. Some people run away, scream, cry, have emotional outbursts, experience trouble breathing, sweat, have increased heart rates, or even faint when they come in contact with an area near spiders or their webs. In some extreme cases, even a picture or a realistic drawing of a spider can trigger intense fear.";
        }
        if (x.phobia.getPhobiaId().equals("Hydrophobia")) {
            x.drawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.water));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.hya));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.hyb));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.hyd));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.hyc));
            x.drawables.add(x.drawable);
            x.description = " Hydrophobia also known as Aquaphobia(from Latin aqua, meaning 'water', and Ancient Greek φόβος (phóbos), meaning 'fear') is an irrational fear of water.Aquaphobia is considered a Specific Phobia of natural environment type in the Diagnostic and Statistical Manual of Mental Disorders. A specific phobia is an intense fear of something that poses little or no actual danger.Specific phobias are a type of anxiety disorder in which a person may feel extremely anxious or has a panic attack when exposed to the object of fear. Specific phobias are a common mental disorder.Psychologists indicate that aquaphobia manifests itself in people through a combination of experiential and genetic factors.[6] In the case of a 37 year old media professor, he noted that his fear initially presented itself as a, \"severe pain, accompanied by a tightness of his forehead,\" and a choking sensation, discrete panic attacks and a reduction in his intake of fluids.";
        }
        if (x.phobia.getPhobiaId().equals("Acrophobia")) {
            x.drawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.height));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.aca));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.acb));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.acd));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.acc));
            x.drawables.add(x.drawable);
            x.description = "Acrophobia is an extreme or irrational fear or phobia of heights, especially when one is not particularly high up. It belongs to a category of specific phobias, called space and motion discomfort, that share both similar causes and options for treatment.Traditionally, acrophobia has been attributed, like other phobias, to conditioning or a traumatic experience. Recent studies have cast doubt on this explanation; a fear of falling, along with a fear of loud noises, is one of the most commonly suggested inborn or \"non-associative\" fears. The newer non-association theory is that a fear of heights is an evolved adaptation to a world where falls posed a significant danger. The degree of fear varies and the term phobia is reserved for those at the extreme end of the spectrum. Researchers have argued that a fear of heights is an instinct found in many mammals, including domestic animals and humans. Experiments using visual cliffs have shown human infants and toddlers, as well as other animals of various ages, to be reluctant in venturing onto a glass floor with a view of a few meters of apparent fall-space below it. While an innate cautiousness around heights is helpful for survival, an extreme fear can interfere with the activities of everyday life, such as standing on a ladder or chair, or even walking up a flight of stairs.";
        }
        if (x.phobia.getPhobiaId().equals("Agoraphobia")) {
            x.drawable = (ContextCompat.getDrawable(RuntimeData.home, R.drawable.crowd));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.aga));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.agb));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.agd));
            x.drawables.add(ContextCompat.getDrawable(RuntimeData.home, R.drawable.agc));
            x.drawables.add(x.drawable);
            x.description = "Agoraphobia is an anxiety disorder characterized by symptoms of anxiety in situations where the person perceives their environment to be unsafe with no easy way to escape. These situations can include open spaces, public transit, shopping centers, or simply being outside their home.Being in these situations may result in a panic attack.Agoraphobia is a condition where sufferers become anxious in unfamiliar environments or where they perceive that they have little control. Triggers for this anxiety may include wide-open spaces, crowds (social anxiety), or traveling (even short distances). Agoraphobia is often, but not always, compounded by a fear of social embarrassment, as the agoraphobic fears the onset of a panic attack and appearing distraught in public. Most of the time they avoid these areas and stay in the comfort of their safe haven, usually their home.";
        }
        return x;
    }

    private class PhobiaItemX {
        private Phobia phobia;
        private Drawable drawable;
        private ArrayList<Drawable> drawables;
        private String description;


        PhobiaItemX(Phobia phobia) {
            this.phobia = phobia;
            this.drawables = new ArrayList<>();
        }
    }


}
