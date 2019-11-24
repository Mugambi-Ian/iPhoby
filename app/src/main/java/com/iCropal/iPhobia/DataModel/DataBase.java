package com.iCropal.iPhobia.DataModel;


import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.gson.JsonArray;
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

    public static void addUserRecord(Record record) {
        User x = RuntimeData.dataBase.appUser;
        x.addUserRecord(record);
        RuntimeData.referenceManger.setUserDatabase(x);
        RuntimeData.dataBase.appUser = x;
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

    public DataBase(ArrayList<Record> userRecords) {
        initPhobias();
        appUser = new User("0715643789");
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

        Phobia aquaPhobia = new Phobia("Aquaphobia");
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

    public static JSONObject createJsonObject(Record record) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserPhone", record.getUserPhoneNumber());
            jsonObject.put("BMP", record.getRecordBmp());
            jsonObject.put("Phobia", record.getPhobiaId());
            jsonObject.put("Type", record.getRecordType());
            jsonObject.put("Status", record.getRecordStatus());
            jsonObject.put("Decision", record.getRecordDecision());

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Record> getRecords(JSONArray jsonArray) throws JSONException {
        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Record x = new Record((String) jsonObject.get("recordTrack"));
            x.setUserPhoneNumber((String) jsonObject.get("userPhone"));
            x.setRecordBmp((String) jsonObject.get("bmp"));
            x.setPhobiaId((String) jsonObject.get("phobia"));
            x.setRecordType((String) (jsonObject.get("type")));
            x.setRecordStatus((String) jsonObject.get("status"));
            x.setRecordDecision((String) jsonObject.get("decision"));
            Log.i(TAG, "getRecords: " + x.getRecordId() + "  " + x.getRecordBmp());
            records.add(x);
        }
        return records;
    }
}
