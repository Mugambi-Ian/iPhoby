package com.iCropal.iPhobia.DataModel;


import android.graphics.drawable.Drawable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Phobia {
    String phobiaId;
    String phobiaPrice;
    String phobiaTitle;
    String phobiaPng;

    public void addRecord(Record record) {
        if (records == null) {
            records = new ArrayList<>();
        }
        records.add(record);
    }

    public String getPhobiaId() {
        return phobiaId;
    }

    public String getPhobiaPrice() {
        return phobiaPrice;
    }

    public String getPhobiaTitle() {
        return phobiaTitle;
    }

    public String getPhobiaPng() {
        return phobiaPng;
    }

    ArrayList<Record> records;

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public Record getHighestRecord(ArrayList<Record> records) {
        if (records != null) {
            ArrayList<Record> recordsV = new ArrayList<>();
            for (Record r : records) {
                if (isInteger(r.getRecordBmp())) {
                    recordsV.add(r);
                }
            }
            return orderDesc(recordsV).get(0);
        }
        return null;
    }

    public Record getLowestRecord(ArrayList<Record> records) {
        if (records != null) {
            ArrayList<Record> recordsV = new ArrayList<>();
            for (Record r : records) {
                if (isInteger(r.getRecordBmp())) {
                    recordsV.add(r);
                }
            }
            return orderAsc(recordsV).get(0);
        }
        return null;
    }

    private ArrayList<Record> orderAsc(ArrayList<Record> list) {
        Collections.sort(list, new Comparator<Record>() {
            public int compare(Record Val1, Record Val2) {
                // avoiding NullPointerException in case name is null
                Long idea1 = Long.valueOf(Val1.getRecordBmp());
                Long idea2 = Long.valueOf(Val2.getRecordBmp());
                return idea1.compareTo(idea2);
            }
        });
        return list;
    }

    private ArrayList<Record> orderDesc(ArrayList<Record> list) {
        Collections.sort(list, new Comparator<Record>() {
            public int compare(Record Val1, Record Val2) {
                // avoiding NullPointerException in case name is null
                Long idea1 = Long.valueOf(Val1.getRecordBmp());
                Long idea2 = Long.valueOf(Val2.getRecordBmp());
                return idea2.compareTo(idea1);
            }
        });
        return list;
    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public void setPhobiaId(String phobiaId) {
        this.phobiaId = phobiaId;
    }

    public void setPhobiaPrice(String phobiaPrice) {
        this.phobiaPrice = phobiaPrice;
    }

    public void setPhobiaTitle(String phobiaTitle) {
        this.phobiaTitle = phobiaTitle;
    }

    public void setPhobiaPng(String phobiaPng) {
        this.phobiaPng = phobiaPng;
    }

    public Phobia(String phobiaId) {
        this.phobiaId = phobiaId;
    }

}
