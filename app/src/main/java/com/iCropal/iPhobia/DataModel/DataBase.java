package com.iCropal.iPhobia.DataModel;


import android.util.Log;

import com.iCropal.iPhobia.Utility.Resources.Constants;
import com.iCropal.iPhobia.Utility.Transmittors.RuntimeData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DataBase {
    public ArrayList<Phobia> phobias;
    public User appUser;
    public static Record lastRecord;
    public ArrayList<Record> userRecords;

    public static void addUserRecord(Record record) {
        User x = RuntimeData.dataBase.appUser;
        x.addUserRecord(record);
        RuntimeData.referenceManger.setUserDatabase(x);
        RuntimeData.dataBase.appUser = x;
    }

    public int getPhobiaId(Phobia p) {
        if (appUser.getPhobias() != null) {
            for (Phobia x : appUser.getPhobias()) {
                if (p.getPhobiaId().equals(x.phobiaId)) {
                    return phobias.indexOf(p);
                }

            }
        }
        return -1;
    }


    public Phobia getPhobia(String phobiaId) {
        if (phobias != null) {
            for (Phobia x : phobias) {
                if (phobiaId.equals(x.phobiaId)) {
                    return x;
                }

            }
        }
        return null;
    }

    public static Phobia getPhobia(String phobiaId, int o) {
        for (Phobia x : RuntimeData.dataBase.appUser.getPhobias()) {
            if (phobiaId.equals(x.phobiaId)) {
                return x;
            }

        }
        return null;
    }

    public DataBase(ArrayList<Record> userRecords) {
        initPhobias();
        appUser = new User("0715351482");
        if (userRecords.size() != 0) {
            lastRecord = userRecords.get(0);
        }
        ArrayList<Phobia> phobias = new ArrayList<>();
        for (Record record : userRecords) {
            boolean e = false;
            for (Phobia phobia : phobias) {
                if (phobia.getPhobiaId().equals(record.phobiaId)) {
                    e = true;
                }
            }
            if (!e) {
                Phobia x = getPhobia(record.phobiaId);
                if (x == null) {
                    x = new Phobia(record.phobiaId);
                    x.setPhobiaTitle(record.phobiaId);
                }
                phobias.add(x);
            }
        }
        ArrayList<Phobia> result = new ArrayList<>();
        for (Phobia p : phobias) {
            ArrayList<Record> records = new ArrayList<>();
            for (Record r : userRecords) {
                if (r.phobiaId.equals(p.phobiaId)) {
                    records.add(r);
                }
            }
            p.setRecords(records);
            result.add(p);
        }
        appUser.setPhobias(result);
    }

    private void initPhobias() {
        phobias = new ArrayList<>();
        Phobia arachnoPhobia = new Phobia("Arachnophobia");
        arachnoPhobia.setPhobiaTitle(Constants.titleCaseConversion(arachnoPhobia.phobiaId));

        Phobia aquaPhobia = new Phobia("Hydrophobia");
        aquaPhobia.setPhobiaTitle(Constants.titleCaseConversion(aquaPhobia.phobiaId));

        Phobia acroPhobia = new Phobia("Acrophobia");
        acroPhobia.setPhobiaTitle(Constants.titleCaseConversion(acroPhobia.phobiaId));

        Phobia agoraphobia = new Phobia("Agoraphobia");
        agoraphobia.setPhobiaTitle(Constants.titleCaseConversion(agoraphobia.phobiaId));

        phobias.add(arachnoPhobia);
        phobias.add(aquaPhobia);
        phobias.add(acroPhobia);
        phobias.add(agoraphobia);
    }

    public static JSONObject createRecordObject(Record record) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userPhone", record.getUserPhoneNumber());
            jsonObject.put("bmp", record.getRecordBmp());
            jsonObject.put("phobia", record.getPhobiaName());
            jsonObject.put("e_Date", record.getRecordDate());
            jsonObject.put("status", record.getRecordStatus());
            jsonObject.put("decision", record.getRecordDecision());
            jsonObject.put("e_Time", record.getRecordTime());
            jsonObject.put("phobiaId", record.getPhobiaId());
            jsonObject.put("s_Time", record.getRecordLength());

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject createSessionObject(Session s) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Code", s.getSessionCode());
            jsonObject.put("Phone", s.getPhoneNumber());
            jsonObject.put("SessionStart", s.getSessionStart());
            jsonObject.put("SessionEnd", s.getSessionEnd());
            jsonObject.put("isSessionRunning", s.getIsSessionRunning());
            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Session> getSessions(JSONArray jsonArray) throws JSONException {
        ArrayList<Session> records = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Session x = new Session((String) jsonObject.get("code"));
            x.setSessionId(String.valueOf(jsonObject.get("id")));
            x.setPhoneNumber((String) jsonObject.get("phone"));
            x.setSessionEnd((String) jsonObject.get("sessionEnded"));
            records.add(x);
        }
        return records;
    }

    public static ArrayList<Record> getRecords(JSONArray jsonArray) throws JSONException {
        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Record x = new Record((String) jsonObject.get("recordTrack"));
            x.setUserPhoneNumber((String) jsonObject.get("userPhone"));
            x.setRecordBmp((String) jsonObject.get("bmp"));
            x.setPhobiaId((String) jsonObject.get("phobia"));
            x.setRecordDate(jsonObject.get("e_Date"));
            x.setRecordStatus((String) jsonObject.get("status"));
            x.setRecordDecision((String) jsonObject.get("decision"));
            x.setRecordTime((String) jsonObject.get("e_Time"));
            x.setPhobiaId((String) jsonObject.get("phobiaId"));
            x.setRecordLength((String) jsonObject.get("s_Time"));
            Log.i(TAG, "getRecords: " + x.getRecordId() + "s_Time" + x.getRecordBmp());
            records.add(x);
        }
        return records;
    }
}
