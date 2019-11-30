package com.iCropal.iPhobia.Utility.ApiManager;

import android.os.AsyncTask;
import android.util.Log;

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
    public static int RC_RecordUpdate = 0, RC_GetAllRecords = 1, Phobia_Records = 2;
    public static int RC_startSession = 3;
    public static int RC_endSession =4;

    public interface DataListener {
        void onSuccess(String data, int RequestCode);

        void onFailure(int RequestCode);
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




    public void saveData(String url, int requestCode, JSONObject data) {

        Log.i(TAG, "Request URL: " + url);
        Log.i(TAG, "Data: " + data.toString());

        RequestBody requestBody = RequestBody.create(JSONMedia, data.toString());
        Request.Builder requestBuilder = new Request.Builder();

        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        new FetchDataAsyncTask(request, listener, requestCode).execute();
    }

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
                if (callback != null) {
                    callback.onFailure(requestCode);
                }
            } else {
                Log.i(TAG, "Got response");
                if (callback != null) {
                    callback.onSuccess(response.getBody(), requestCode);
                }
            }
        }
    }
}