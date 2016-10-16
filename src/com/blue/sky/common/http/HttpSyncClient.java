package com.blue.sky.common.http;

import android.util.Log;
import com.blue.sky.common.utils.Strings;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2014/11/5.
 */
public class HttpSyncClient {

    public static String httpGet(String url, HttpParams params) {
        HttpGet httpRequest = new HttpGet(url);
        if(params!=null){
            httpRequest.setParams(params);
        }
        try {
            // 取得HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();

            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                return EntityUtils.toString(httpResponse.getEntity());
            } else {
                Log.e(">>>httpGet httpResponse", httpResponse.getStatusLine().getStatusCode() + "");
                return Strings.EMPTY_STRING;
            }
        } catch (ClientProtocolException e) {
            Log.e(">>>httpGet ClientProtocolException", e.toString());
        } catch (IOException e) {
            Log.e(">>>httpGet IOException", e.toString());
        }
        return Strings.EMPTY_STRING;
    }

    public static String httpPost(String url, HttpParams params) {
        HttpPost httpRequest = new HttpPost(url);
        if(params!=null){
            httpRequest.setParams(params);
        }
        try {
            // 取得HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();

            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                return EntityUtils.toString(httpResponse.getEntity());
            } else {
                Log.e(">>>httpPost httpResponse", httpResponse.getStatusLine().getStatusCode() + "");
                return Strings.EMPTY_STRING;
            }
        } catch (ClientProtocolException e) {
            Log.e(">>>httpPost ClientProtocolException", e.toString());
        } catch (IOException e) {
            Log.e(">>>httpPost IOException", e.toString());
        }
        return Strings.EMPTY_STRING;
    }
}
