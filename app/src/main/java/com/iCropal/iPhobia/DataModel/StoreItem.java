package com.iCropal.iPhobia.DataModel;

import java.util.ArrayList;

public class StoreItem {
    public String itemTitle;
    public ArrayList<Phobia> itemPhobias;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public ArrayList<Phobia> getItemPhobias() {
        return itemPhobias;
    }

    public void setItemPhobias(ArrayList<Phobia> itemPhobias) {
        this.itemPhobias = itemPhobias;
    }

    public StoreItem(String itemTitle, ArrayList<Phobia> itemPhobias) {
        this.itemTitle = itemTitle;
        this.itemPhobias = itemPhobias;
    }
}
