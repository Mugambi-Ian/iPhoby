package com.iCropal.iPhobia.DataModel;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;

public class Phobia {
    public String phobiaId;
    String phobiaTitle;
    String phobiaPng;

    public void addRecord(Record record) {
        if (records == null) {
            records = new ArrayList<>();
        }
        records.add(record);
    }

    public List getYAxisData() {
        ArrayList<Integer> x = new ArrayList<>();
        for (Record r : records) {
            x.add(Integer.valueOf(r.getRecordBmp()));
        }
        return x;
    }

    public List getXAxisData() {
        ArrayList<String> x = new ArrayList<>();
        for (Record z : records) {
            x.add("");
        }
        return x;
    }

    public ArrayList<Line> getLines(String c) {
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();
        for (int i = 0; i < getXAxisData().size(); i++) {
            axisValues.add(i, new AxisValue(i).setLabel(String.valueOf(getXAxisData().get(i))));
        }
        for (int i = 0; i < getYAxisData().size(); i++) {
            yAxisValues.add(new PointValue(i, Float.parseFloat(String.valueOf(getYAxisData().get(i)))));
        }
        Line y = new Line(yAxisValues);
        ArrayList<Line> lines = new ArrayList<>();
        if (c != null) {
            y.setColor(Color.parseColor(c));
        } else {
            y.setColor(Color.parseColor("#9C27B0"));
        }
        lines.add(y);
        return lines;
    }

    public String getPhobiaId() {
        return phobiaId;
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

    public Record getHighestRecord() {
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

    public Record getLowestRecord() {
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


    public void setPhobiaTitle(String phobiaTitle) {
        this.phobiaTitle = phobiaTitle;
    }

    public void setPhobiaPng(String phobiaPng) {
        this.phobiaPng = phobiaPng;
    }

    public Phobia(String phobiaId) {
        this.phobiaId = phobiaId;
    }

    public String getAverageBpm() {
        int x = 0;
        if (records != null) {
            int t = 0;
            for (Record r : records) {
                if (isInteger(r.getRecordBmp())) {
                    x = x + (Integer.valueOf(r.getRecordBmp()));
                    t++;
                }
            }
            x = x / t;
        }
        return "" + x;
    }
}
