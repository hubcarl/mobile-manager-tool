package com.blue.sky.mobile.manager.video.http;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import com.blue.sky.mobile.manager.video.common.VideoInfo;

import java.util.List;

/**
 * Created by Administrator on 2014/11/2.
 */

public class VideoHttpAsyncTask extends AsyncTask<String, String, List<VideoInfo>> {

    private VideoCallbackResult callbackResult;

    public VideoHttpAsyncTask(VideoCallbackResult callbackResult){
        this.callbackResult = callbackResult;
    }

    @Override
    protected List<VideoInfo> doInBackground(String... params) {
        return callbackResult.request(params);
    }

    @Override
    protected void onPostExecute(List<VideoInfo> list) {
        callbackResult.response(list);
        super.onPostExecute(list);
    }
}

