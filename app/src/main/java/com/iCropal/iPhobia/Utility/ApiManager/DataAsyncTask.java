package com.iCropal.iPhobia.Utility.ApiManager;


import com.iCropal.iPhobia.Utility.ApiManager.models.RequestResponse;

public interface DataAsyncTask {
    void onPostExecute(RequestResponse response);
}
