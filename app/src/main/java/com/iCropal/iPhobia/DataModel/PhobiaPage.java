package com.iCropal.iPhobia.DataModel;

;

import java.util.ArrayList;

public class PhobiaPage {
    private ArrayList<Phobia> phobias;

    public PhobiaPage() {
        phobias = new ArrayList<>();
    }

    public void addPhobia(Phobia phobia) {
        phobias.add(phobia);
    }

    public ArrayList<Phobia> getPhobias() {
        return phobias;
    }

}
