package com.iCropal.iPhobia.DataModel;

import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class User {
    private String phoneNumber;
    private ArrayList<Phobia> phobias;

    public Phobia getHighestAvg() {
        int x = 0;
        int id = -1;
        for (Phobia phobia : phobias) {
            int z = Integer.parseInt(phobia.getAverageBpm());
            if (x < z) {
                id = phobias.indexOf(phobia);
                x = z;
            }
        }
        if (id != -1) {
            return phobias.get(id);
        }
        return null;
    }

    public Phobia getMostSession() {
        int x = 0;
        int id = -1;
        for (Phobia phobia : phobias) {
            int z = phobia.records.size();
            if (x < z) {
                id = phobias.indexOf(phobia);
                x = z;
            }
        }
        if (id != -1) {
            return phobias.get(id);
        }
        return null;
    }

    public Phobia getLowestAvg() {
        int x = 200;
        int id = -1;
        for (Phobia phobia : phobias) {
            int z = Integer.parseInt(phobia.getAverageBpm());
            if (x > z) {
                id = phobias.indexOf(phobia);
                x = z;
            }
        }
        if (id != -1) {
            return phobias.get(id);
        }
        return null;
    }

    public Phobia getLastSession() {
        ArrayList<Phobia> phobias = this.phobias;
        Collections.sort(phobias, new Comparator<Phobia>() {
            @Override
            public int compare(Phobia o1, Phobia o2) {
                return o2.getLastSession().getDate().compareTo(o1.getLastSession().getDate());
            }
        });
        if (phobias.get(0) != null) {
            return phobias.get(0);
        } else {
            return null;
        }

    }

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Phobia> getPhobias() {
        return phobias;
    }

    public void setPhobias(ArrayList<Phobia> phobias) {
        this.phobias = phobias;
    }

    public void addUserRecord(Record record) {
        boolean e = false;
        Phobia x = null;
        int id = -1;
        for (Phobia p : phobias) {
            if (record.phobiaId.equals(p)) {
                e = true;
                x = p;
                x.addRecord(record);
            }
        }

        if (!e) {
            Phobia w = RuntimeData.dataBase.getPhobia(record.phobiaId);
            if (w == null) {
                w = new Phobia(record.phobiaId);
                w.setPhobiaTitle(record.phobiaId);
            }
            phobias.add(w);
        } else {
            phobias.set(id, x);
        }
    }
}
