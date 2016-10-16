package com.blue.sky.mobile.manager.search;

import android.os.AsyncTask;
import com.blue.sky.mobile.manager.file.common.FileInfo;

import java.util.List;

/**
 * Created by Administrator on 2014/11/2.
 */

public class SearchAsyncTask extends AsyncTask<String, String, List<FileInfo>> {

    private CallbackResult callbackResult;

    public SearchAsyncTask(CallbackResult callbackResult){
        this.callbackResult = callbackResult;
    }

    @Override
    protected List<FileInfo> doInBackground(String... params) {
        return callbackResult.request(params);
    }

    @Override
    protected void onPostExecute(List<FileInfo> list) {
        callbackResult.response(list);
        super.onPostExecute(list);
    }

    public interface CallbackResult {
        List<FileInfo> request(String... params);

        void response(List<FileInfo> list);
    }
}

