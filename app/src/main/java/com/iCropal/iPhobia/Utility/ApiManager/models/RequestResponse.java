package com.iCropal.iPhobia.Utility.ApiManager.models;


import com.iCropal.iPhobia.DataModel.Record;

public class RequestResponse extends Record {
    private boolean successful;
    private String response;
    private String body;
    private int status;

    public RequestResponse(boolean successful, String response, String body) {
        super(response);
        this.successful = successful;
        this.response = response;
        this.body = body;
        this.status = status;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    public String getResponse() {
        return response;
    }
}
