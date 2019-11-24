package com.iCropal.iPhobia.DataModel;

public class Record {
    String recordId;
    private String recordBmp;
    String recordDecision;
    String recordStatus;
    String recordType;
    String phobiaId;
    String userPhoneNumber;

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

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Record(String recordId) {
        this.recordId = recordId;
    }
}
