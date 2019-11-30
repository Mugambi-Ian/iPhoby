package com.iCropal.iPhobia.DataModel;

public class Session {
    String sessionId;
    String phoneNumber;
    String sessionStart;
    String sessionEnd;
    boolean isSessionRunning;
    String sessionCode;

    public Session(String phoneNumber, String sessionStart, String sessionEnd, boolean isSessionRunning, String sessionCode) {
        this.phoneNumber = phoneNumber;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.isSessionRunning = isSessionRunning;
        this.sessionCode = sessionCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(String sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public boolean getIsSessionRunning() {
        return isSessionRunning;
    }

    public void setIsSessionRunning(boolean isSessionRunning) {
        this.isSessionRunning = isSessionRunning;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public Session(String sessionCode) {
        this.sessionCode = sessionCode;
    }
}
