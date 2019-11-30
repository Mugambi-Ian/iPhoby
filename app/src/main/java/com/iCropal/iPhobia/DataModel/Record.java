package com.iCropal.iPhobia.DataModel;

public class Record {
    String recordId;
    private String recordBmp;
    String recordDecision;
    String recordStatus;
    String phobiaId;
    String userPhoneNumber;
    String recordDate;
    String recordTime;
    String phobiaName;
    String recordLength;

    public String getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(String recordLength) {
        this.recordLength = recordLength;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getPhobiaName() {
        return phobiaName;
    }

    public void setPhobiaName(String phobiaName) {
        this.phobiaName = phobiaName;
    }

    public String getPhobiaId() {
        return phobiaId;
    }

    public void setPhobiaId(String phobiaId) {
        this.phobiaId = phobiaId;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordBmp() {
        return recordBmp;
    }

    public void setRecordBmp(String recordBmp) {
        this.recordBmp = recordBmp;
    }

    public String getRecordDecision() {
        return recordDecision;
    }

    public void setRecordDecision(String recordDecision) {
        this.recordDecision = recordDecision;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }


    public Record(String recordId) {
        this.recordId = recordId;
    }

    public void setRecordDate(Object e_date) {
        if (e_date != null) {
            recordDate = e_date.toString();
        }
    }

    public void setRecordTime(Object e_time) {
        if (e_time != null) {
            recordTime = e_time.toString();
        }
    }
}
