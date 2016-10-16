package com.blue.sky.common.http;

import android.os.AsyncTask;
import android.util.Log;
import com.blue.sky.common.utils.Strings;
import org.apache.http.params.HttpParams;

public class HttpAsyncClient extends AsyncTask<String, String, String> {

    private String url;
    private HttpParams httpParams;
    private HttpMethod httpMethod;
    private HttpResponseCallback httpResponseCallback;

    public HttpAsyncClient(String url, HttpParams params, HttpResponseCallback httpResponseCallback) {
        Log.i(">>>HttpAsyncClient url:", url);
        this.url = url;
        this.httpParams = params;
        this.httpMethod = HttpMethod.GET;
        this.httpResponseCallback = httpResponseCallback;
    }

    public HttpAsyncClient(HttpMethod method, String url, HttpParams params, HttpResponseCallback httpResponseCallback) {
        this.url = url;
        this.httpParams = params;
        this.httpMethod = method;
        this.httpResponseCallback = httpResponseCallback;
    }

    @Override
    protected String doInBackground(String... params) {
        if (HttpMethod.GET.equals(httpMethod)) {
            return HttpSyncClient.httpGet(url, httpParams);
        } else
            return HttpSyncClient.httpPost(url, httpParams);
    }


    @Override
    protected void onPostExecute(String response) {
        Log.i(">>>response:", response);
        if (Strings.EMPTY_STRING.equals(response)) {
            httpResponseCallback.onError(response);
        } else {
            httpResponseCallback.onSuccess(response);
        }
        super.onPostExecute(response);
    }
}

