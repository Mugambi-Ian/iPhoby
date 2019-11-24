package com.iCropal.iPhobia.Utility.ApiManager;

import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.JsonArray;
import com.iCropal.iPhobia.Utility.ApiManager.models.RequestResponse;

import org.json.JSONObject;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.iCropal.iPhobia.Utility.Resources.Constants.API_Url;


public class DataFetcher {
    public static int RC_RecordUpdate = 0, RC_GetAllRecords = 1;

    public interface DataListener {
        void onSuccess(String data, int RequestCode);

        void onFailure();
    }

    /* URL Methods */
    public static final int GET = 1;
    public static final int PUT = 2;
    public static final int POST = 3;
    public static final int DELETE = 4;

    private DataListener listener;
    private static final MediaType JSONMedia = MediaType.parse("application/json; charset=utf-8");
    private final String TAG = this.getClass().getSimpleName();

    public DataFetcher() {

    }

    public void setResultListener(DataListener listener) {
        this.listener = listener;
    }

    public void getData(String url, int requestCode) {

        Log.i(TAG, "Request URL: " + url);
        Request.Builder requestBuilder = new Request.Builder();

        requestBuilder.url(url);
        requestBuilder.get();
        Request request = requestBuilder.build();

        new FetchDataAsyncTask(request, listener, requestCode).execute();
    }

    public void deleteData(String url, int requestCode) {
        String data_url = API_Url;
        Request.Builder requestBuilder = new Request.Builder();

        Request request = requestBuilder.url(data_url).delete().build();

        new FetchDataAsyncTask(request, listener, requestCode).execute();
    }


    public void saveData(String url, int requestCode, JSONObject data) {
        String data_url = API_Url;

        Log.i(TAG, "Request URL: " + data_url);
        Log.i(TAG, "Data: " + data.toString());

        RequestBody requestBody = RequestBody.create(JSONMedia, data.toString());
        Request.Builder requestBuilder = new Request.Builder();

        requestBuilder.url(data_url);
        requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        new FetchDataAsyncTask(request, listener, requestCode).execute();
    }

   /* public void uploadPhoto(String url, String filename, String filepath) {
        String data_url = API_Url;
        Log.i(TAG, "Request URL: " + data_url);

        RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                .addFormDataPart("photo", filename,
                        RequestBody.create(MEDIA_TYPE_PNG, new File(filepath)))
                .build();

        Request.Builder requestBuilder = new Request.Builder();

        //requestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
        Request request = requestBuilder.url(data_url).post(requestBody).build();

        new DataAsyncTask(request, listener).execute();

    }*/

    private static class FetchDataAsyncTask extends AsyncTask<Void, Void, RequestResponse> implements DataAsyncTask {

        private OkHttpClient client;
        private Request request;
        private DataListener callback;
        private final String TAG = this.getClass().getSimpleName();
        private int requestCode;

        FetchDataAsyncTask(Request request, DataListener callback, int requestCode) {
            this.requestCode = requestCode;
            this.client = new OkHttpClient();
            this.request = request;
            this.callback = callback;
        }

        @Override
        protected RequestResponse doInBackground(Void... voids) {

            try {
                Response response = client.newCall(request).execute();

                //og.i(TAG, "response received: "+ response.body().string());
                return new RequestResponse(response.isSuccessful(),
                        response.toString(), response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, "This is reached!");
            return null;
        }

        @Override
        public void onPostExecute(RequestResponse response) {
            super.onPostExecute(response);


            if (response == null || !response.isSuccessful()) {
                Log.i(TAG, "No response");
                callback.onFailure();
            } else {
                Log.i(TAG, "Got response");
                if (callback != null) {
                    callback.onSuccess(response.getBody(), requestCode);
                }
            }
        }
    }
}